package com.isi.desafio_fullstack.repository;

import com.isi.desafio_fullstack.model.entities.Products;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductsRepository extends JpaRepository<Products, Integer> {

    boolean existsByName(String name);

}
