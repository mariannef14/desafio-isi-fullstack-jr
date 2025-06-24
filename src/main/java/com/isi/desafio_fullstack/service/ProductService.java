package com.isi.desafio_fullstack.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.isi.desafio_fullstack.config.exceptions.CouponNotActiveException;
import com.isi.desafio_fullstack.config.exceptions.UnprocessableEntityException;
import com.isi.desafio_fullstack.controller.dto.request.ProductRequest;
import com.isi.desafio_fullstack.controller.query.ListQueryParameter;
import com.isi.desafio_fullstack.model.entities.Coupons;
import com.isi.desafio_fullstack.model.entities.Products;
import com.isi.desafio_fullstack.model.entities.ProductsCoupons;
import com.isi.desafio_fullstack.model.enums.TypeCouponEnum;
import com.isi.desafio_fullstack.model.enums.TypeDiscount;
import com.isi.desafio_fullstack.repository.ProductsCouponsRepository;
import com.isi.desafio_fullstack.repository.ProductsRepository;
import com.isi.desafio_fullstack.utils.UtilsCodes;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class ProductService {

    private ProductsRepository productsRepository;
    private CouponService couponService;
    private ProductsCouponsRepository productsCouponsRepository;
    private ObjectMapper objectMapper;


    public ProductService(ProductsRepository productsRepository, CouponService couponService,
                          ProductsCouponsRepository productsCouponsRepository, ObjectMapper objectMapper) {

        this.productsRepository = productsRepository;
        this.couponService = couponService;
        this.productsCouponsRepository = productsCouponsRepository;
        this.objectMapper = objectMapper;

    }


    @Transactional
    public Products saveProduct(ProductRequest productRequest) {

        var existsNameProduct = productsRepository.existsByName(UtilsCodes.formatName(productRequest.name()));

        if (!existsNameProduct) {
            var product = new Products(productRequest.name(), productRequest.description(), productRequest.price(),
                                        productRequest.stock());

            return productsRepository.save(product);

        } else {
            throw new EntityExistsException("Nome de produto já existe");
        }

    }


    public Products findOneProduct(Integer productId) {

        var product = productsRepository.findById(productId);

        if (product.isPresent())
            return product.get();
        else
            throw new EntityNotFoundException("Produto não encontrado");

    }


    public void changeStatus(Integer productId) {

        var product = findOneProduct(productId);

        if (product.getIsActive()) {
            product.setIsActive(false);
            product.setDeletedAt(LocalDateTime.now(TimeZone.getTimeZone("America/Sao_Paulo").toZoneId()));
        } else {
            product.setIsActive(true);
            product.setUpdatedAt(LocalDateTime.now(TimeZone.getTimeZone("America/Sao_Paulo").toZoneId()));
        }

        productsRepository.save(product);

    }


    @Transactional
    public Products updatePatchProduct(Integer productId, JsonPatch patch) throws JsonPatchException, JsonProcessingException {

       var productEntity = findOneProduct(productId);
       var productJsonNode = patch.apply(objectMapper.convertValue(productEntity, JsonNode.class));
       var productPatch = objectMapper.treeToValue(productJsonNode, Products.class);

       productPatch.setName(UtilsCodes.formatName(productPatch.getName()));
       productPatch.setPrice(productPatch.getPrice().abs());
       productPatch.setIsOutOfStock(productPatch.getStock() == 0);
       productPatch.setUpdatedAt(UtilsCodes.dateToday());

       return productsRepository.save(productPatch);

    }


    public Page<Products> findAllProducts(ListQueryParameter listQueryParameter, Pageable pageable) {

        if (pageable.getPageSize() > 50)
            throw new IllegalArgumentException("A quantidade máxima de itens por página é 50");

       return productsRepository.findAll(getSpecification(listQueryParameter), pageable);

    }

    public Specification<Products> getSpecification(ListQueryParameter listQueryParameter){

        return ((root, query, builder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(listQueryParameter.getSearch()) && !listQueryParameter.getSearch().isEmpty()){
                predicates.add(builder.or(builder.like(root.get("name"), "%" + UtilsCodes.formatName(listQueryParameter.getSearch()) + "%"),
                            builder.like(builder.lower(root.get("description")), "%" + UtilsCodes.formatName(listQueryParameter.getSearch()) + "%")));
            }

            if ((Objects.nonNull(listQueryParameter.getMaxPrice()) && Objects.nonNull(listQueryParameter.getMinPrice()))){
                predicates.add(builder.greaterThanOrEqualTo(root.get("price"), listQueryParameter.getMinPrice().abs()));
                predicates.add(builder.lessThanOrEqualTo(root.get("price"), listQueryParameter.getMaxPrice().abs()));
            } else if (Objects.nonNull(listQueryParameter.getMaxPrice())) {
                predicates.add(builder.lessThanOrEqualTo(root.get("price"), listQueryParameter.getMaxPrice().abs()));
            } else if (Objects.nonNull(listQueryParameter.getMinPrice())) {
                    predicates.add(builder.greaterThanOrEqualTo(root.get("price"), listQueryParameter.getMinPrice().abs()));
            }


            if (Objects.nonNull(listQueryParameter.getHasDiscount()) && listQueryParameter.getHasDiscount()){
                predicates.add(builder.and(
                                builder.notEqual(root.get("finalPrice"), BigDecimal.ZERO),
                                builder.isNotNull(root.get("finalPrice")))
                                );
            } else if (Objects.nonNull(listQueryParameter.getHasDiscount())) {
                predicates.add(builder.or(
                                builder.isNull(root.get("finalPrice")),
                                builder.equal(root.get("finalPrice"), BigDecimal.ZERO))
                                );
            }

            if (Objects.nonNull(listQueryParameter.getSortBy()) && !listQueryParameter.getSortBy().isEmpty()){

                switch (listQueryParameter.getSortBy().trim()) {

                    case "name" -> query.orderBy(builder.asc(root.get("name")));
                    case "price" -> query.orderBy(builder.asc(root.get("price")));
                    case "stock" -> query.orderBy(builder.asc(root.get("stock")));
                    case "created_at" -> query.orderBy(builder.asc(root.get("createdAt")));

                }

                if (listQueryParameter.getSortOrder().equals("desc")){

                    switch (listQueryParameter.getSortBy().trim()) {

                        case "name" -> query.orderBy(builder.desc(root.get("name")));
                        case "price" -> query.orderBy(builder.desc(root.get("price")));
                        case "stock" -> query.orderBy(builder.desc(root.get("stock")));
                        case "created_at" -> query.orderBy(builder.desc(root.get("createdAt")));

                    }

                }

            }

            if (listQueryParameter.getIncludeDeleted()){
                predicates.add(builder.or(
                                builder.equal(root.get("isActive"), false),
                                builder.equal(root.get("isActive"), true))
                            );
            } else {
                predicates.add(builder.equal(root.get("isActive"), true));
            }

            if (Objects.nonNull(listQueryParameter.getOnlyOutOfStock())){
                if (listQueryParameter.getOnlyOutOfStock())
                    predicates.add(builder.equal(root.get("isOutOfStock"), true));
                else
                    predicates.add(builder.equal(root.get("isOutOfStock"), false));
            }

            return builder.and(predicates.toArray(new Predicate[]{}));

        });

    }


    public BigDecimal changePriceWithDiscountPercent(Integer productId) {

        var product = findOneProduct(productId);

        var valueDiscount = 0.0;

        for (TypeDiscount discount: TypeDiscount.values())
            if (discount.getIsActive())
                valueDiscount = discount.getDiscount();

        var priceProduct = product.getPrice();

        var priceProductWithDiscount = priceProduct.subtract(priceProduct
                                        .multiply(BigDecimal.valueOf(valueDiscount)))
                                        .setScale(2, RoundingMode.HALF_EVEN);

        if (priceProductWithDiscount.compareTo(new BigDecimal("0.01")) < 0 )
            throw new UnprocessableEntityException("Produto com preço com desconto menor que R$ 0,01");


        return priceProductWithDiscount;

    }


    @Transactional
    public Products changePriceWithDiscountCoupon(Integer productId, String couponCode) {

        var product = findOneProduct(productId);

        var coupon = couponService.findOneCoupon(couponCode);


        Optional<ProductsCoupons> productWithCoupon;

        if (Objects.isNull(coupon.getIsActive()) || coupon.getIsActive()) {

            if (!product.getDiscount().isEmpty()) {
                productWithCoupon = product.getDiscount()
                                    .stream()
                                    .filter(productsCoupons -> productsCoupons.getRemovedAt() == null)
                                    .findFirst();


                if (productWithCoupon.isPresent())
                    throw new EntityExistsException("Esse produto já tem um desconto aplicado");
                else
                    return applyDiscount(coupon, product);
            } else {
                return applyDiscount(coupon, product);
            }

        } else
            throw new CouponNotActiveException("Cupom não encontrado");

    }


    private Products applyDiscount(Coupons coupon, Products product){

        var date_today = UtilsCodes.dateToday();
        if (date_today.isBefore(coupon.getValidFrom()) || date_today.isAfter(coupon.getValidUntil()))
            throw new DateTimeException("O cupom está fora da validade");


        if (coupon.getOneShot() && productsCouponsRepository.existsByCouponIdAndRemovedAtIsNull(coupon.getId()))
            throw new EntityExistsException("Esse cupom já está sendo utilizado");


        var priceProductWithDiscount = calculateValueDiscount(coupon.getType(), coupon.getPrice(), product.getPrice());
        System.out.println(priceProductWithDiscount);

        if (priceProductWithDiscount.compareTo(new BigDecimal("0.01")) < 0 )
            throw new UnprocessableEntityException("Produto com preço com desconto menor que R$ 0,01");


        var productsCoupons = productsCouponsRepository.save(new ProductsCoupons(product, coupon));

        product.setFinalPrice(priceProductWithDiscount);

        product.getDiscount().add(productsCoupons);

        return product;

    }


    private BigDecimal calculateValueDiscount(TypeCouponEnum type, BigDecimal priceCoupon, BigDecimal priceProduct) {

        if (type.equals(TypeCouponEnum.PERCENT)){
            return priceProduct.subtract(
                    priceProduct.multiply(priceCoupon.divide(new BigDecimal(100))))
                    .setScale(2, RoundingMode.HALF_EVEN);
        }

        return priceProduct.subtract(priceCoupon);

    }


    @Transactional
    public void removeDiscount(Integer productId) {

        var product = findOneProduct(productId);

        var hasDiscount = product.getDiscount().stream()
                            .filter(productsCoupons -> productsCoupons.getRemovedAt() == null)
                            .findFirst();

        if (hasDiscount.isPresent()){

            var date_today = UtilsCodes.dateToday();

            if (date_today.isAfter(hasDiscount.get().getCouponId().getValidUntil())) {

                product.setFinalPrice(BigDecimal.ZERO);
                hasDiscount.get().setRemovedAt(Timestamp.valueOf(UtilsCodes.dateToday()));
                hasDiscount.get().getCouponId().setIsActive(false);

            } else {

                hasDiscount.get().setRemovedAt(Timestamp.valueOf(UtilsCodes.dateToday()));
                product.setFinalPrice(BigDecimal.ZERO);

            }

        } else {
            throw new EntityNotFoundException("Esse produto não tem desconto aplicado");
        }

    }

}