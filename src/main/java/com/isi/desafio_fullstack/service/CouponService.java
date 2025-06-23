package com.isi.desafio_fullstack.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.isi.desafio_fullstack.controller.dto.CouponRequest;
import com.isi.desafio_fullstack.model.entities.Coupons;
import com.isi.desafio_fullstack.repository.CouponRepository;
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
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;


@Service
public class CouponService {

    private CouponRepository couponRepository;
    private ObjectMapper objectMapper;


    public CouponService(CouponRepository couponRepository, ObjectMapper objectMapper) {
        this.couponRepository = couponRepository;
        this.objectMapper = objectMapper;
    }


    @Transactional
    public Coupons saveCoupon(CouponRequest couponRequest){

        var existCoupon = couponRepository.existsByCode(UtilsCodes.formatName(couponRequest.code()));

        if (!existCoupon){

            validPrice(couponRequest.type(), couponRequest.price());
            validDate(couponRequest.validFrom(), couponRequest.validUntil());

            return couponRepository.save(new Coupons(UtilsCodes.formatName(couponRequest.code()), couponRequest.type(),
                    couponRequest.price(), couponRequest.oneShot(), couponRequest.maxUses(), couponRequest.validFrom(),
                    couponRequest.validUntil()));

        } else {
            throw new EntityExistsException("Esse nome de código já existe");
        }

    }

    private static void validPrice(String type, BigDecimal price) {

        if (type.equalsIgnoreCase("PERCENT")){

            if (price.compareTo(new BigDecimal(80)) > 0 ||
                price.compareTo(new BigDecimal(BigInteger.ONE)) < 0)
                throw new IllegalArgumentException("O valor do desconto deve ser entre 1%-80%");

        } else if (type.equalsIgnoreCase("fixed")) {
            if (price.compareTo(new BigDecimal(BigInteger.ONE)) < 0)
                throw new IllegalArgumentException("O valor do desconto deve ser positivo");
        } else {
            throw new IllegalArgumentException("Tipo de cupom inválido");
        }

    }


    private static void validDate(LocalDateTime validFrom, LocalDateTime validUntil){

        if (validUntil.isBefore(validFrom))
            throw new IllegalArgumentException("A data de validação de expiração do cupom deve ser maior " +
                                                "que a data de validação da ativação");
        else if (validUntil.isAfter(validFrom.plusYears(5)))
            throw new IllegalArgumentException("A validade máxima permitida para cupons é de 5 anos");



    }


    public Coupons findOneCoupon(String couponCode) {

        var coupon = couponRepository.findByCode(couponCode);

        if (coupon.isPresent())
            return coupon.get();
        else
            throw new EntityNotFoundException("Cupom não encontrado");
    }


    @Transactional
    public Coupons updatePatchCoupon(String couponCode, JsonPatch patch) throws JsonPatchException, JsonProcessingException {

        var couponEntity = findOneCoupon(couponCode);
        var couponJsonNode = patch.apply(objectMapper.convertValue(couponEntity, JsonNode.class));
        var couponPatch = objectMapper.treeToValue(couponJsonNode, Coupons.class);

        validPrice(couponPatch.getType().toString(), couponPatch.getPrice());
        validDate(couponPatch.getValidFrom(), couponPatch.getValidUntil());
        couponPatch.setUpdatedAt(LocalDateTime.now(TimeZone.getTimeZone("America/Sao_Paulo").toZoneId()));

        return couponRepository.save(couponPatch);

    }


    public void changeStatus(String couponCode) {

        var coupon = findOneCoupon(couponCode);

        coupon.setIsActive(false);
        coupon.setDeletedAt(LocalDateTime.now(TimeZone.getTimeZone("America/Sao_Paulo").toZoneId()));

        couponRepository.save(coupon);

    }


    public Page<Coupons> findAllCoupons(String code, String typeCoupon, Pageable pageable){

        return couponRepository.findAll(getSpecification(code, typeCoupon),pageable);

    }


    private Specification<Coupons> getSpecification(String code, String typeCoupon) {

        return ((root, query, builder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(code) && !code.isBlank())
                predicates.add(builder.like(root.get("code"), "%" + UtilsCodes.formatName(code) + "%"));

            if (Objects.nonNull(typeCoupon) && !typeCoupon.isBlank())
                predicates.add(builder.equal(root.get("type"), typeCoupon.trim().toUpperCase()));

            return builder.and(predicates.toArray(new Predicate[]{}));

        });

    }

}