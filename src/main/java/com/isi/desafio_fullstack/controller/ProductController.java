package com.isi.desafio_fullstack.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.isi.desafio_fullstack.controller.dto.request.ProductRequest;
import com.isi.desafio_fullstack.controller.dto.response.ProductResponse;
import com.isi.desafio_fullstack.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;


@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private ProductService productService;


    public ProductController(ProductService productService){
        this.productService = productService;
    }


    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid ProductRequest productRequest){

        var product = productService.saveProduct(productRequest);
        var location = URI.create("/products/" + product.getId());

        return ResponseEntity.created(location).body(ProductResponse.from(product));

    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> searchProduct(@PathVariable(value = "id") Integer productId){

        var product = productService.findOneProduct(productId);

        return ResponseEntity.ok().body(ProductResponse.from(product));

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inactiveProduct(@PathVariable("id") Integer productId){

        productService.changeStatus(productId);

        return ResponseEntity.noContent().build();

    }


    @PostMapping("/{id}/restore")
    public ResponseEntity<Void> activeProduct(@PathVariable("id") Integer productId){

        productService.changeStatus(productId);

        return ResponseEntity.ok().build();

    }


    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponse> updatePatchProduct(@PathVariable("id") Integer productId,
                                                              @RequestBody JsonPatch patch) throws JsonPatchException,
                                                                                            JsonProcessingException {

        var product = productService.updatePatchProduct(productId, patch);

        return ResponseEntity.ok().body(ProductResponse.from(product));

    }

}
