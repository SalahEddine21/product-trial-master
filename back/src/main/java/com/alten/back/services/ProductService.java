package com.alten.back.services;

import com.alten.back.entities.Product;
import com.alten.back.dtos.ProductDTO;
import com.alten.back.exceptions.NotFoundException;
import com.alten.back.mappers.ProductMapper;
import com.alten.back.repos.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        final Product productToSave = productMapper.toProduct(productDTO);
        final Product savedProduct = this.productRepository.save(productToSave);
        return this.productMapper.toProductDTO(savedProduct);
    }

    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(productMapper::toProductDTO);
    }

    public ProductDTO getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        return productMapper.toProductDTO(product);
    }

    @Transactional
    public ProductDTO patchProduct(ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(productDTO.getId())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        if (productDTO.getCode() != null) {
            existingProduct.setCode(productDTO.getCode());
        }
        if (productDTO.getName() != null) {
            existingProduct.setName(productDTO.getName());
        }
        if (productDTO.getDescription() != null) {
            existingProduct.setDescription(productDTO.getDescription());
        }
        if (productDTO.getImage() != null) {
            existingProduct.setImage(productDTO.getImage());
        }
        if (productDTO.getCategory() != null) {
            existingProduct.setCategory(productDTO.getCategory());
        }
        if (productDTO.getPrice() != null) {
            existingProduct.setPrice(productDTO.getPrice());
        }
        if (productDTO.getQuantity() != null) {
            existingProduct.setQuantity(productDTO.getQuantity());
        }
        if (productDTO.getInternalReference() != null) {
            existingProduct.setInternalReference(productDTO.getInternalReference());
        }
        if (productDTO.getShellId() != null) {
            existingProduct.setShellId(productDTO.getShellId());
        }
        if (productDTO.getInventoryStatus() != null) {
            existingProduct.setInventoryStatus(productDTO.getInventoryStatus());
        }
        if (productDTO.getRating() != null) {
            existingProduct.setRating(productDTO.getRating());
        }
        existingProduct.setUpdatedAt(LocalDateTime.now());
        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.toProductDTO(updatedProduct);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Transactional
    public ProductDTO updateProduct(ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(productDTO.getId())
                .orElseThrow(() -> new NotFoundException("Product not found"));
        productMapper.updateProductFromDTO(productDTO, existingProduct);
        existingProduct.setUpdatedAt(LocalDateTime.now());
        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.toProductDTO(updatedProduct);
    }

}
