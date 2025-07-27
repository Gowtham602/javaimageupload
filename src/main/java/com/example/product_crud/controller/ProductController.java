package com.example.product_crud.controller;

import com.example.product_crud.dto.ProductRequestDto;
import com.example.product_crud.dto.ProductResponseDto;
import com.example.product_crud.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product API", description = "CRUD operations for product BY GN ")
public class ProductController {

    @Autowired
    private ProductService productService;
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ProductResponseDto> create(@ModelAttribute @Valid ProductRequestDto dto) {
        return ResponseEntity.ok(productService.createProduct(dto));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProduct(id));
    }
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ProductResponseDto> update(
            @PathVariable Long id,
            @ModelAttribute @Valid ProductRequestDto dto
    ) {
        return ResponseEntity.ok(productService.updateProduct(id, dto));
    }
    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProduct(){
        return  ResponseEntity.ok(productService.getAllProduct());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String>deleted(@PathVariable Long id){
        productService.deleted(id);
        return ResponseEntity.ok("Product Deleted successfully");
    }

}
