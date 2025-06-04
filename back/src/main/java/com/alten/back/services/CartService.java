package com.alten.back.services;

import com.alten.back.dtos.CartDTO;
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
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Value("${config.optimistic_locking_failure_max_attemps:3}")
    public Integer OPTIMISTIC_LOCKING_FAILURE_MAX_ATTEMPS;

    private final AccountRepository accountRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    public CartService(AccountRepository accountRepository, ProductRepository productRepository,
                       CartRepository cartRepository, CartMapper cartMapper) {
        this.accountRepository = accountRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
    }

    @Transactional
    public CartDTO addToCart(CartDTO cartDTO) {
        Account account = accountRepository.findById(cartDTO.getAccount().getId())
                .orElseThrow(() -> new NotFoundException("Product not found"));
        Product product = productRepository.findById(cartDTO.getProduct().getId())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        // Check if the product is already in the cart
        if(cartRepository.existsByAccountIdAndProductId(account.getId(), product.getId())){
            throw new DuplicatedProductInCartException("Product already exists in Cart");
        }
        // Validate stock availability
        if (product.getQuantity() < cartDTO.getQuantity()) {
            throw new InsufficientQtyException("Insufficient stock");
        }
        Cart cart = Cart.builder().
                account(account).
                product(product).
                quantity(cartDTO.getQuantity()).
                build();
        cart = cartRepository.save(cart);

        // Update product qte
        updateProductStock(product, cart.getQuantity());

        return this.cartMapper.toCartDTO(cart);
    }

    public void removeFromCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }

    public List<CartDTO> getAllAccountCarts(Long accountId) {
        List<Cart> carts = cartRepository.findByAccount_Id(accountId);
        return cartMapper.toCartDTOs(carts);
    }

    private void updateProductStock(Product product, int quantity) {
        int attempts = OPTIMISTIC_LOCKING_FAILURE_MAX_ATTEMPS;
        while(attempts > 0) {
            try {
                product.setQuantity(product.getQuantity() - quantity);
                productRepository.save(product);
                return; // success
            } catch (OptimisticLockingFailureException e) {
                attempts--;
                if (attempts == 0) {
                    // product quantity being updated at the same time of the save, block the user current add to cart operation
                    throw new ProductQtyUpdatedException("Stock has been updated. Please refresh and try again.");
                }

            }
        }

    }
}
