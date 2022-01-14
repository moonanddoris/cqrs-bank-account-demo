package com.bridgingcode.bankaccountscqrsdemo.query.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * 账户明细
 * @author xinhai.li
 **/
@Entity
public class AccountHistory {

    @Id
    private String id;

    private String accountId;
    private BigDecimal balance;
    private String balanceType; // '+'  or  '-'
    private BigDecimal totalBalance;
    private String otherUser;
    private String comments;
    private String timeStamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    public String getOtherUser() {
        return otherUser;
    }

    public void setOtherUser(String accountUser) {
        this.otherUser = accountUser;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getBalanceType() {
        return balanceType;
    }

    public void setBalanceType(String balanceType) {
        this.balanceType = balanceType;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
