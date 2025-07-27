package com.example.product_crud.service;

import com.example.product_crud.dto.ProductRequestDto;
import com.example.product_crud.dto.ProductResponseDto;
import com.example.product_crud.entity.Product;
import com.example.product_crud.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    @Value("${upload.dir}")
    private String uploadDir;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public ProductResponseDto createProduct(ProductRequestDto dto) {
        try {
            Product product = Product.builder()
                    .productName(dto.getProductName())
                    .quantity(dto.getQuantity())
                    .description(dto.getDescription())
                    .createdAt(LocalDateTime.now())
                    .build();

            if (dto.getImage() != null && !dto.getImage().isEmpty()) {
                String imagePath = saveImage(dto.getImage());
                product.setImagePath(imagePath);
            }

            product = productRepository.save(product);
            return mapToResponse(product);

        } catch (IOException e) {
            throw new RuntimeException("Image upload failed", e);
        }
    }

    private String saveImage(MultipartFile file) throws IOException {
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadDir + filename);
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());
        return filename;
    }

    private ProductResponseDto mapToResponse(Product product) {
        ProductResponseDto dto = new ProductResponseDto();
        BeanUtils.copyProperties(product, dto);
        return dto;
    }

    @Override
    public List<ProductResponseDto> getAllProduct() {
        List<Product> ProductList = productRepository.findAll();

        return ProductList.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ProductResponseDto getProduct (Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Product not found"));
        return mapToResponse(product);
    }
    @Override
    public ProductResponseDto updateProduct(Long id, ProductRequestDto dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        product.setProductName(dto.getProductName());
        product.setQuantity(dto.getQuantity());
        product.setDescription(dto.getDescription());
        if (dto.getImage() != null && !dto.getImage().isEmpty()) {
            try {
                String imagePath = saveImage(dto.getImage());
                product.setImagePath(imagePath);
            } catch (IOException e) {
                throw new RuntimeException("Image upload failed during update", e);
            }
        }

        Product updated = productRepository.save(product);
        return mapToResponse(updated);
    }
    @Override
    public  void deleted(Long id){
        if(!productRepository.existsById(id)){
            throw  new EntityNotFoundException("Product  id"+id+"Not Found");
        }
        productRepository.deleteById(id);

    }
}



