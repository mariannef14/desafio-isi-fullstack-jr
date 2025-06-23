package com.isi.desafio_fullstack.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.isi.desafio_fullstack.controller.dto.CouponRequest;
import com.isi.desafio_fullstack.controller.dto.CouponResponse;
import com.isi.desafio_fullstack.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@Tag(name = "Cupons")
@RestController
@RequestMapping("/api/v1/coupons")
public class CouponController {

    private CouponService couponService;


    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }


    @Operation(summary = "Cria um cupom")
    @PostMapping
    public ResponseEntity<CouponResponse> createCoupon(@RequestBody @Valid CouponRequest couponRequest){

        var coupon = couponService.saveCoupon(couponRequest);

        var location = URI.create("/coupon/" + coupon.getId());

        return ResponseEntity.created(location).body(CouponResponse.from(coupon));

    }


    @Operation(summary = "Detalhes de um cupom")
    @GetMapping("/{code}")
    public ResponseEntity<CouponResponse> searchOneCoupon(@PathVariable("code") String couponCode){

        var coupon = couponService.findOneCoupon(couponCode);

        return ResponseEntity.ok().body(CouponResponse.from(coupon));

    }


    @Operation(summary = "Edita um cupom (exceto code)")
    @PatchMapping(value = "/{code}", consumes = "application/json-patch+json")
    public ResponseEntity<CouponResponse> updatePatchCoupon(@PathVariable("code") String couponCode,
                                                            @RequestBody JsonPatch patch) throws JsonPatchException,
                                                                                                JsonProcessingException {

        var coupon = couponService.updatePatchCoupon(couponCode, patch);

        return ResponseEntity.ok().body(CouponResponse.from(coupon));

    }


    @Operation(summary = "Inativa (soft-delete) um cupom")
    @DeleteMapping("/{code}")
    public ResponseEntity<Void> inactiveCoupon(@PathVariable("code") String couponCode){

        couponService.changeStatus(couponCode);

        return ResponseEntity.noContent().build();

    }


    @Operation(summary = "Lista/busca códigos")
    @GetMapping
    public ResponseEntity<Page<CouponResponse>> searchAllCoupons(@RequestParam(required = false) String code,
                                                                 @RequestParam(required = false) String typeCoupon,
                                                                 @PageableDefault(page = 0, value = 10) Pageable pageable){

        var coupons = couponService.findAllCoupons(code, typeCoupon,pageable);

        return ResponseEntity.ok(coupons.map(CouponResponse::from));

    }

}