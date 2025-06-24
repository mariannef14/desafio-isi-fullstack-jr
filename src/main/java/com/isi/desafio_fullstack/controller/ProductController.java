package com.isi.desafio_fullstack.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.isi.desafio_fullstack.controller.dto.request.ProductRequest;
import com.isi.desafio_fullstack.controller.dto.response.ProductDiscountResponse;
import com.isi.desafio_fullstack.controller.dto.response.ProductResponse;
import com.isi.desafio_fullstack.controller.query.ListQueryParameter;
import com.isi.desafio_fullstack.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;


@Tag(name = "Produtos")
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private ProductService productService;


    public ProductController(ProductService productService){
        this.productService = productService;
    }


    @Operation(summary = "Cria um produto")
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid ProductRequest productRequest){

        var product = productService.saveProduct(productRequest);

        var location = URI.create("/products/" + product.getId());

        return ResponseEntity.created(location).body(ProductResponse.from(product));

    }


    @Operation(summary = "Detalhes de um produto")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDiscountResponse> searchOneProduct(@PathVariable(value = "id") Integer productId){

        var product = productService.findOneProduct(productId);

        return ResponseEntity.ok().body(ProductDiscountResponse.from(product));

    }


    @Operation(summary = "Inativa (soft-delete) um produto")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inactiveProduct(@PathVariable("id") Integer productId){

        productService.changeStatus(productId);

        return ResponseEntity.noContent().build();

    }


    @Operation(summary = "Restaura um produto inativo")
    @PostMapping("/{id}/restore")
    public ResponseEntity<Void> activeProduct(@PathVariable("id") Integer productId){

        productService.changeStatus(productId);

        return ResponseEntity.ok().build();

    }


    @Operation(summary = "Atualização parcial de um produto via JSON Patch")
    @PatchMapping(value = "/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<ProductResponse> updatePatchProduct(@PathVariable("id") Integer productId,
                                                              @RequestBody JsonPatch patch) throws JsonPatchException,
                                                                                            JsonProcessingException {

        var product = productService.updatePatchProduct(productId, patch);

        return ResponseEntity.ok().body(ProductResponse.from(product));

    }


    @Operation(summary = "Lista paginada com filtros avançados")

    @GetMapping
    public ResponseEntity<Page<ProductDiscountResponse>> searchAllProduct(ListQueryParameter listQueryParameter,
                                                                         @Parameter(example = """
                                                                                 {
                                                                                   "page": 0,
                                                                                   "size": 10
                                                                                 }
                                                                                 """)
                                                                         @PageableDefault Pageable pageable){

        var products = productService.findAllProducts(listQueryParameter, pageable);

        return ResponseEntity.ok().body(products.map(ProductDiscountResponse::from));

    }


    @Operation(summary = "Aplica desconto percentual")
    @PostMapping("/{id}/discount/percent")
    public ResponseEntity<BigDecimal> applyDiscountPercent(@PathVariable("id") Integer productId){

        var value = productService.changePriceWithDiscountPercent(productId);

        return ResponseEntity.ok().body(value);

    }


    @Operation(summary = "Aplica cupom promocional")
    @PostMapping("/{id}/discount/coupon")
    public ResponseEntity<ProductDiscountResponse> applyDiscountCoupon(@PathVariable("id") Integer productId,
                                                                       @RequestBody String couponCode){

        var product = productService.changePriceWithDiscountCoupon(productId, couponCode);

        return ResponseEntity.ok().body(ProductDiscountResponse.from(product));

    }


    @Operation(summary = "Remove desconto ativo")
    @DeleteMapping("/{id}/discount")
    public ResponseEntity<Void> deleteDiscount(@PathVariable("id") Integer productId){

        productService.removeDiscount(productId);

        return ResponseEntity.noContent().build();

    }

}