package com.bridgingcode.bankaccountscqrsdemo.query.service;

import com.bridgingcode.bankaccountscqrsdemo.common.event.AccountActivatedEvent;
import com.bridgingcode.bankaccountscqrsdemo.common.event.AccountCreatedEvent;
import com.bridgingcode.bankaccountscqrsdemo.common.event.AccountCreditedEvent;
import com.bridgingcode.bankaccountscqrsdemo.common.event.AccountDebitedEvent;
import com.bridgingcode.bankaccountscqrsdemo.query.entity.Account;
import com.bridgingcode.bankaccountscqrsdemo.query.entity.AccountHistory;
import com.bridgingcode.bankaccountscqrsdemo.query.query.FindAccountByIdQuery;
import com.bridgingcode.bankaccountscqrsdemo.query.repository.AccountHistoryRepository;
import com.bridgingcode.bankaccountscqrsdemo.query.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ReplayStatus;
import org.axonframework.eventsourcing.eventstore.DomainEventStream;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author BridgingCode - AJ Catambay
 **/
@Service
@Slf4j
public class ManageAccountService {

    private final AccountRepository accountRepository;

    private final AccountHistoryRepository accountHistoryRepository;

    private final EventStore eventStore;

    public ManageAccountService(AccountRepository accountRepository, AccountHistoryRepository accountHistoryRepository, EventStore eventStore) {
        this.accountRepository = accountRepository;
        this.accountHistoryRepository = accountHistoryRepository;
        this.eventStore = eventStore;
    }

    /**
     * 测试获取事件列表  另外如果不满足需求 可以再写一张业务表，来保存事件
     */
    public void listEventsTest(){
        DomainEventStream domainEventStream = this.eventStore.readEvents("3ab6121f-8a93-4f86-a554-bd5a0deb4891");

        domainEventStream.asStream().forEach(i ->
            {
                System.out.println(i.getMetaData());

                System.out.println(i.getPayload());

                System.out.println(i.getPayloadType());

                if(i.getPayload() instanceof AccountCreatedEvent){
                    System.out.println(((AccountCreatedEvent) i.getPayload()).getBalance());
                }
            }
        );

        System.out.println(domainEventStream.toString());
        System.out.println("listTest");
    }


    @EventHandler
    public void on(AccountCreatedEvent accountCreatedEvent, ReplayStatus replayStatus) {
        log.info("Handling AccountCreatedEvent...");
        Account account = new Account();
        account.setAccountId(accountCreatedEvent.getId());
        account.setBalance(accountCreatedEvent.getBalance());
        account.setStatus("CREATED");

        if(replayStatus.isReplay()){
            // 如果是时光机，可以写一些定制逻辑
        }

        // 转账历史
        AccountHistory accountHistory = new AccountHistory();
        accountHistory.setId(UUID.randomUUID().toString());
        accountHistory.setAccountId(accountCreatedEvent.getId());
        accountHistory.setBalance(accountCreatedEvent.getBalance());
        accountHistory.setBalanceType("+");
        accountHistory.setTotalBalance(accountCreatedEvent.getBalance());
        accountHistory.setComments("初始存入");
        accountHistory.setOtherUser("lxh");
        accountHistory.setTimeStamp((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()) );

        accountHistoryRepository.save(accountHistory);

        accountRepository.save(account);
    }

    @EventHandler
    public void on(AccountActivatedEvent accountActivatedEvent) {
        log.info("Handling AccountActivatedEvent...");
        Account account = accountRepository.findById(accountActivatedEvent.getId()).orElse(null);

        if (account != null) {
            account.setStatus(accountActivatedEvent.getStatus());
            account.setAccountUser("lxh");
            account.setBankName("中信银行花园路支行");
            accountRepository.save(account);
        }
    }
    @EventHandler
    public void on(AccountCreditedEvent accountCreditedEvent) {
        log.info("Handling AccountCreditedEvent...");
        Account account = accountRepository
                .findById(accountCreditedEvent.getId()).orElse(null);

        if (account != null) {
            account.setBalance(account.getBalance()
                    .add(accountCreditedEvent.getAmount()));

            // 转账历史
            AccountHistory accountHistory = new AccountHistory();
            accountHistory.setId(UUID.randomUUID().toString());
            accountHistory.setAccountId(accountCreditedEvent.getId());
            accountHistory.setBalance(accountCreditedEvent.getAmount());
            accountHistory.setBalanceType("+");
            accountHistory.setTotalBalance(account.getBalance());
            accountHistory.setComments("工资存入");
            accountHistory.setOtherUser("xx公司");
            accountHistory.setTimeStamp((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()) );

            accountHistoryRepository.save(accountHistory);
        }
    }
    @EventHandler
    public void on(AccountDebitedEvent accountDebitedEvent) {
        log.info("Handling AccountDebitedEvent...");
        Account account = accountRepository
                .findById(accountDebitedEvent.getId()).orElse(null);

        if (account != null) {
            account.setBalance(account.getBalance()
                    .subtract(accountDebitedEvent.getAmount()));

            // 转账历史
            AccountHistory accountHistory = new AccountHistory();
            accountHistory.setId(UUID.randomUUID().toString());
            accountHistory.setAccountId(accountDebitedEvent.getId());
            accountHistory.setBalance(accountDebitedEvent.getAmount());
            accountHistory.setBalanceType("-");
            accountHistory.setTotalBalance(account.getBalance());
            accountHistory.setComments("车贷扣除");
            accountHistory.setOtherUser("建设银行");
            accountHistory.setTimeStamp((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()) );

            accountHistoryRepository.save(accountHistory);
        }
    }

    @QueryHandler
    public Account handle(FindAccountByIdQuery query) {
        log.info("Handling FindAccountByIdQuery...");
        Account account = accountRepository
                .findById(query.getAccountId()).orElse(null);

        return account;
    }
}
