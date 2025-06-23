package com.isi.desafio_fullstack.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.isi.desafio_fullstack.controller.dto.request.ProductRequest;
import com.isi.desafio_fullstack.controller.query.ListQueryParameter;
import com.isi.desafio_fullstack.model.entities.Products;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;


@Service
public class ProductService {

    private ProductsRepository productsRepository;
    private ObjectMapper objectMapper;


    public ProductService(ProductsRepository productsRepository, ObjectMapper objectMapper) {

        this.productsRepository = productsRepository;
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


       var existsNameProduct = productsRepository.existsByName(UtilsCodes.formatName(productPatch.getName()));

        if (!existsNameProduct)
            return productsRepository.save(new Products(productPatch.getName(), productPatch.getDescription(),
                    productPatch.getPrice(), productPatch.getStock()));

        else
            throw new EntityExistsException("Nome de produto já existe");

    }


    public Page<Products> findAll(ListQueryParameter listQueryParameter, Pageable pageable) {

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
                predicates.add(builder.isNotNull(root.get("finalPrice")));
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
                predicates.add(builder.equal(root.get("isActive"), false));
            }

            if (listQueryParameter.getOnlyOutOfStock()){
                predicates.add(builder.isTrue(root.get("isOutOfStock")));
            }

//            var dataTruncExpression = builder.function(
//                    "date_trunc",
//                    LocalDate.class,
//                    builder.literal("day"),
//                    root.get("transactionDate")

            return builder.and(predicates.toArray(new Predicate[]{}));

        });

    }
}