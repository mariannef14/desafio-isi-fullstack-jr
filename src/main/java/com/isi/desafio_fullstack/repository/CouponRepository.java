package com.isi.desafio_fullstack.repository;

import com.isi.desafio_fullstack.model.entities.Coupons;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;


public interface CouponRepository extends JpaRepository<Coupons, Integer>, JpaSpecificationExecutor<Coupons> {

    Optional<Coupons> findByCode(String couponId);

    Boolean existsByCode(String couponCode);

}