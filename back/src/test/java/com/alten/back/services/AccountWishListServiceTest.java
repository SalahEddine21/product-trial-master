package com.alten.back.services;

import com.alten.back.dtos.AccountDTO;
import com.alten.back.dtos.AccountWishListDTO;
import com.alten.back.dtos.ProductDTO;
import com.alten.back.entities.Account;
import com.alten.back.entities.Product;
import com.alten.back.entities.AccountWishList;
import com.alten.back.mappers.AccountWishListMapper;
import com.alten.back.repos.AccountRepository;
import com.alten.back.repos.AccountWishListRepository;
import com.alten.back.repos.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountWishListServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private AccountWishListRepository accountWishListRepository;

    @Mock
    private AccountWishListMapper accountWishListMapper;

    @InjectMocks
    private AccountWishListService accountWishListService;

    private AccountWishListDTO accountWishListDTO;
    private Account account;
    private Product product;
    private AccountDTO accountDTO;
    private ProductDTO productDTO;
    private AccountWishList accountWishList;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setId(1L);

        accountDTO = new AccountDTO();
        accountDTO.setId(1L);

        product = new Product();
        product.setId(1L);

        productDTO = new ProductDTO();
        productDTO.setId(1L);

        accountWishListDTO = new AccountWishListDTO();
        accountWishListDTO.setAccount(accountDTO);
        accountWishListDTO.setProduct(productDTO);

        accountWishList = AccountWishList.builder()
                .account(account)
                .product(product)
                .build();
    }

    @Test
    void testAddToWishList() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(accountWishListRepository.save(any(AccountWishList.class))).thenReturn(accountWishList);
        when(accountWishListMapper.toAccountWishListDTO(any(AccountWishList.class))).thenReturn(accountWishListDTO);

        AccountWishListDTO result = accountWishListService.addToWishList(accountWishListDTO);

        assertNotNull(result);
        assertEquals(account.getId(), result.getAccount().getId());
        assertEquals(product.getId(), result.getProduct().getId());

        verify(accountRepository).findById(anyLong());
        verify(productRepository).findById(anyLong());
        verify(accountWishListRepository).save(any(AccountWishList.class));
        verify(accountWishListMapper).toAccountWishListDTO(any(AccountWishList.class));
    }

    @Test
    void testAddToWishList_ProductNotFound() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            accountWishListService.addToWishList(accountWishListDTO);
        });

        assertEquals("Product not found", thrown.getMessage());

        verify(accountRepository).findById(anyLong());
        verify(productRepository).findById(anyLong());
        verify(accountWishListRepository, never()).save(any(AccountWishList.class));
    }

    @Test
    void testRemoveFromWishList() {
        doNothing().when(accountWishListRepository).deleteById(anyLong());

        accountWishListService.removeFromWishList(1L);

        verify(accountWishListRepository).deleteById(anyLong());
    }

    @Test
    void testGetAccountWishLists() {
        AccountWishList accountWishList1 = AccountWishList.builder().account(account).product(product).build();
        AccountWishList accountWishList2 = AccountWishList.builder().account(account).product(product).build();
        List<AccountWishList> wishLists = Arrays.asList(accountWishList1, accountWishList2);

        when(accountWishListRepository.findByAccountId(anyLong())).thenReturn(wishLists);
        when(accountWishListMapper.toAccountWishListDTOs(anyList())).thenReturn(Arrays.asList(accountWishListDTO, accountWishListDTO));

        List<AccountWishListDTO> result = accountWishListService.getAccountWishLists(account.getId());

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(accountWishListRepository).findByAccountId(anyLong());
        verify(accountWishListMapper).toAccountWishListDTOs(anyList());
    }
}

