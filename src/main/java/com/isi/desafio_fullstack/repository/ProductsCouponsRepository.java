package com.isi.desafio_fullstack.repository;

import com.isi.desafio_fullstack.model.entities.ProductsCoupons;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ProductsCouponsRepository extends JpaRepository<ProductsCoupons, Integer> {

    @Query(value = """ 
            select exists (
                            select 1
                            from products_coupons pc
                            join products p on pc.product_id = p.id
                            join coupons c on pc.coupon_id = c.id
                            where pc.removed_at is null
                            and c.id = :couponId
                           )""", nativeQuery = true)
    Boolean existsByCouponIdAndRemovedAtIsNull(Integer couponId);

}