package com.alten.back.services;

import com.alten.back.dtos.AccountWishListDTO;
import com.alten.back.entities.Account;
import com.alten.back.entities.AccountWishList;
import com.alten.back.entities.Product;
import com.alten.back.mappers.AccountWishListMapper;
import com.alten.back.repos.AccountRepository;
import com.alten.back.repos.AccountWishListRepository;
import com.alten.back.repos.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountWishListService {

    private final AccountRepository accountRepository;
    private final ProductRepository productRepository;
    private final AccountWishListRepository accountWishListRepository;
    private final AccountWishListMapper accountWishListMapper;

    public AccountWishListService(AccountRepository accountRepository, ProductRepository productRepository,
                                  AccountWishListRepository accountWishListRepository, AccountWishListMapper accountWishListMapper) {
        this.accountRepository = accountRepository;
        this.productRepository = productRepository;
        this.accountWishListRepository = accountWishListRepository;
        this.accountWishListMapper = accountWishListMapper;
    }

    public AccountWishListDTO addToWishList(AccountWishListDTO accountWishListDTO) {
        Account account = accountRepository.findById(accountWishListDTO.getAccount().getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        Product product = productRepository.findById(accountWishListDTO.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        AccountWishList accountWishList = AccountWishList.builder()
                .account(account)
                .product(product)
                .build();
        accountWishList = accountWishListRepository.save(accountWishList);
        return accountWishListMapper.toAccountWishListDTO(accountWishList);
    }

    public void removeFromWishList(Long wishListId){
        accountWishListRepository.deleteById(wishListId);
    }

    public List<AccountWishListDTO> getAccountWishLists(Long accountId) {
        List<AccountWishList> accountWishLists = accountWishListRepository.findByAccountId(accountId);
        return accountWishListMapper.toAccountWishListDTOs(accountWishLists);
    }
}
