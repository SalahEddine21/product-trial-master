package com.alten.back.mappers;

import com.alten.back.dtos.AccountWishListDTO;
import com.alten.back.entities.AccountWishList;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountWishListMapper {

    AccountWishListDTO toAccountWishListDTO(AccountWishList accountWishList);

    AccountWishList toAccountWishList(AccountWishListDTO accountWishListDTO);

    List<AccountWishListDTO> toAccountWishListDTOs(List<AccountWishList> accountWishLists);
}
