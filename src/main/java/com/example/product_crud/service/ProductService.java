package com.example.product_crud.service;

import com.example.product_crud.dto.ProductRequestDto;
import com.example.product_crud.dto.ProductResponseDto;

import java.util.List;

public interface ProductService {
    ProductResponseDto createProduct(ProductRequestDto dto);
    ProductResponseDto getProduct(Long id);
    List<ProductResponseDto> getAllProduct();
    ProductResponseDto updateProduct(Long id,ProductRequestDto dto);
    void deleted (Long id);

}
