package com.isi.desafio_fullstack.repository;

import com.isi.desafio_fullstack.model.entities.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface ProductsRepository extends JpaRepository<Products, Integer>, JpaSpecificationExecutor<Products> {

    boolean existsByName(String name);

}
