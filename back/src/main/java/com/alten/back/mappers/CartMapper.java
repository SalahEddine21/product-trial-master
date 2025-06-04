package com.alten.back.mappers;

import com.alten.back.dtos.CartDTO;
import com.alten.back.entities.Cart;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartDTO toCartDTO(Cart cart);

    Cart toCart(CartDTO cartDTO);

    List<CartDTO> toCartDTOs(List<Cart> cartList);
}
