package com.alten.back.repos;

import com.alten.back.entities.AccountWishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountWishListRepository extends JpaRepository<AccountWishList, Long> {
    List<AccountWishList> findByAccountId(Long accountId);
}
