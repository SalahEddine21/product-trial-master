package com.alten.back.mappers;

import com.alten.back.dtos.AccountDTO;
import com.alten.back.entities.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountDTO toAccountDTO(Account account);

    Account toAccount(AccountDTO accountDTO);
}
