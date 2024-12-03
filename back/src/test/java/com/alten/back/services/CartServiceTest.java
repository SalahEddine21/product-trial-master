package com.alten.back.services;

import com.alten.back.dtos.AccountDTO;
import com.alten.back.dtos.CartDTO;
import com.alten.back.dtos.ProductDTO;
import com.alten.back.entities.Account;
import com.alten.back.entities.Cart;
import com.alten.back.entities.Product;
import com.alten.back.exceptions.DuplicatedProductInCartException;
import com.alten.back.exceptions.InsufficientQtyException;
import com.alten.back.exceptions.NotFoundException;
import com.alten.back.exceptions.ProductQtyUpdatedException;
import com.alten.back.mappers.CartMapper;
import com.alten.back.repos.AccountRepository;
import com.alten.back.repos.CartRepository;
import com.alten.back.repos.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.OptimisticLockingFailureException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartMapper cartMapper;

    @InjectMocks
    private CartService cartService;

    private CartDTO cartDTO;
    private Account account;
    private Product product;
    private AccountDTO accountDTO;
    private ProductDTO productDTO;
    private Cart cart;

    @BeforeEach
    void setUp() {
        cartService.OPTIMISTIC_LOCKING_FAILURE_MAX_ATTEMPS = 3;

        account = new Account();
        account.setId(1L);

        accountDTO = new AccountDTO();
        accountDTO.setId(1L);

        product = new Product();
        product.setId(1L);
        product.setQuantity(10);

        productDTO = new ProductDTO();
        productDTO.setId(1L);

        cart = new Cart();
        cart.setAccount(account);
        cart.setProduct(product);
        cart.setQuantity(1);

        cartDTO = new CartDTO();
        cartDTO.setAccount(accountDTO);
        cartDTO.setProduct(productDTO);
        cartDTO.setQuantity(1);
    }

    @Test
    void addToCart_ShouldReturnCartDTO_WhenProductAddedSuccessfully() {
        when(accountRepository.findById(account.getId())).thenReturn(java.util.Optional.of(account));
        when(productRepository.findById(product.getId())).thenReturn(java.util.Optional.of(product));
        when(cartRepository.existsByAccountIdAndProductId(account.getId(), product.getId())).thenReturn(false);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(cartMapper.toCartDTO(cart)).thenReturn(cartDTO);

        CartDTO result = cartService.addToCart(cartDTO);

        assertNotNull(result);
        assertEquals(cartDTO.getAccount().getId(), result.getAccount().getId());
        assertEquals(cartDTO.getProduct().getId(), result.getProduct().getId());
        assertEquals(cartDTO.getQuantity(), result.getQuantity());
        verify(cartRepository, times(1)).save(any(Cart.class));
        verify(productRepository, times(1)).save(any(Product.class));  // Verifying stock update
    }

    @Test
    void addToCart_ShouldThrowDuplicatedProductInCartException_WhenProductAlreadyInCart() {
        when(accountRepository.findById(account.getId())).thenReturn(java.util.Optional.of(account));
        when(productRepository.findById(product.getId())).thenReturn(java.util.Optional.of(product));
        when(cartRepository.existsByAccountIdAndProductId(account.getId(), product.getId())).thenReturn(true);

        assertThrows(DuplicatedProductInCartException.class, () -> cartService.addToCart(cartDTO));
        verify(cartRepository, times(0)).save(any(Cart.class));  // No save attempt due to exception
    }

    @Test
    void addToCart_ShouldThrowInsufficientQtyException_WhenStockIsInsufficient() {
        cartDTO.setQuantity(20); // Setting quantity greater than available stock
        when(accountRepository.findById(account.getId())).thenReturn(java.util.Optional.of(account));
        when(productRepository.findById(product.getId())).thenReturn(java.util.Optional.of(product));

        assertThrows(InsufficientQtyException.class, () -> cartService.addToCart(cartDTO));
        verify(cartRepository, times(0)).save(any(Cart.class));  // No save attempt due to exception
    }

    @Test
    void addToCart_ShouldThrowNotFoundException_WhenAccountNotFound() {
        when(accountRepository.findById(account.getId())).thenReturn(java.util.Optional.empty());

        assertThrows(NotFoundException.class, () -> cartService.addToCart(cartDTO));
    }

    @Test
    void addToCart_ShouldThrowNotFoundException_WhenProductNotFound() {
        when(accountRepository.findById(account.getId())).thenReturn(java.util.Optional.of(account));
        when(productRepository.findById(product.getId())).thenReturn(java.util.Optional.empty());
        assertThrows(NotFoundException.class, () -> cartService.addToCart(cartDTO));
    }

    @Test
    void updateProductStock_ShouldThrowProductQtyUpdatedException_WhenOptimisticLockingFailureOccurs() {
        when(accountRepository.findById(account.getId())).thenReturn(java.util.Optional.of(account));
        when(productRepository.findById(product.getId())).thenReturn(java.util.Optional.of(product));
        when(cartRepository.existsByAccountIdAndProductId(account.getId(), product.getId())).thenReturn(false);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        doThrow(OptimisticLockingFailureException.class).when(productRepository).save(any(Product.class));  // Simulating OptimisticLockingFailureException

        assertThrows(ProductQtyUpdatedException.class, () -> cartService.addToCart(cartDTO));
    }
}

