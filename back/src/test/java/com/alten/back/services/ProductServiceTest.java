package com.alten.back.services;

import com.alten.back.dtos.ProductDTO;
import com.alten.back.entities.Product;
import com.alten.back.exceptions.NotFoundException;
import com.alten.back.mappers.ProductMapper;
import com.alten.back.repos.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private ProductDTO productDTO;
    private Product product;

    @BeforeEach
    void setUp() {
        productDTO = new ProductDTO(
                1L, "12345", "Product Name", "Product Description", "product.jpg", "Category", 99.99, 10,
                "REF123", null, null, 4.5, null, null
        );
        product = new Product(1L, "12345", "Product Name", "Product Description", "product.jpg", "Category",
                99.99, 10, "REF123", null, null, 4.5, null, null, null);
    }

    @Test
    void createProduct_ShouldReturnProductDTO() {
        when(productMapper.toProduct(any(ProductDTO.class))).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toProductDTO(any(Product.class))).thenReturn(productDTO);
        ProductDTO result = productService.createProduct(productDTO);
        assertNotNull(result);
        assertEquals(productDTO.getId(), result.getId());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void getAllProducts_ShouldReturnProductDTOPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(Collections.singletonList(product));
        when(productRepository.findAll(pageable)).thenReturn(productPage);
        when(productMapper.toProductDTO(product)).thenReturn(productDTO);
        Page<ProductDTO> result = productService.getAllProducts(pageable);
        assertNotNull(result);
        verify(productRepository, times(1)).findAll(pageable);
    }

    @Test
    void getProduct_ShouldReturnProductDTO_WhenProductExists() {
        when(productRepository.findById(anyLong())).thenReturn(java.util.Optional.of(product));
        when(productMapper.toProductDTO(any(Product.class))).thenReturn(productDTO);
        ProductDTO result = productService.getProduct(1L);
        assertNotNull(result);
        assertEquals(productDTO.getId(), result.getId());
        verify(productRepository, times(1)).findById(anyLong());
    }

    @Test
    void getProduct_ShouldThrowNotFoundException_WhenProductNotFound() {
        when(productRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());
        assertThrows(NotFoundException.class, () -> productService.getProduct(1L));
    }

    @Test
    void patchProduct_ShouldReturnUpdatedProductDTO() {
        when(productRepository.findById(anyLong())).thenReturn(java.util.Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toProductDTO(any(Product.class))).thenReturn(productDTO);
        ProductDTO result = productService.patchProduct(productDTO);
        assertNotNull(result);
        assertEquals(productDTO.getId(), result.getId());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void deleteProduct_ShouldCallDeleteById() {
        productService.deleteProduct(1L);
        verify(productRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void updateProduct_ShouldReturnUpdatedProductDTO() {
        when(productRepository.findById(anyLong())).thenReturn(java.util.Optional.of(product));
        when(productMapper.toProductDTO(any(Product.class))).thenReturn(productDTO);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        ProductDTO result = productService.updateProduct(productDTO);
        assertNotNull(result);
        assertEquals(productDTO.getId(), result.getId());
        verify(productRepository, times(1)).save(any(Product.class));
    }
}