package com.bridgingcode.bankaccountscqrsdemo.query.repository;

import com.bridgingcode.bankaccountscqrsdemo.query.entity.Account;
import com.bridgingcode.bankaccountscqrsdemo.query.entity.AccountHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author xinhai.li
 **/
@Repository
public interface AccountHistoryRepository extends JpaRepository<AccountHistory, String> {
}
