package com.bridgingcode.bankaccountscqrsdemo.command.service;

import com.bridgingcode.bankaccountscqrsdemo.command.aggregate.AccountAggregate;
import lombok.AllArgsConstructor;
import org.axonframework.config.EventProcessingConfiguration;
import org.axonframework.eventhandling.TrackingEventProcessor;
import org.axonframework.messaging.StreamableMessageSource;
import org.axonframework.messaging.unitofwork.DefaultUnitOfWork;
import org.axonframework.messaging.unitofwork.UnitOfWork;
import org.axonframework.modelling.command.Aggregate;
import org.axonframework.modelling.command.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@AllArgsConstructor
public class Replayer{
    @Autowired
    private EventProcessingConfiguration eventProcessingConfiguration;
    @Resource
    private Repository<AccountAggregate> accountAggregateRepository;

    public void replay(){

        StreamableMessageSource streamableMessageSource;

        String name = "com.bridgingcode.bankaccountscqrsdemo.query.service";

        eventProcessingConfiguration.eventProcessor(name, TrackingEventProcessor.class).ifPresent(processor -> {
            processor.shutDown();
            processor.resetTokens();
            //processor.resetTokens(GapAwareTrackingToken.newInstance(2, Collections.emptySortedSet()));
            processor.start();
        });
    }

    /**
     * 加载Aggregate
     * @param accountId
     * @return
     */
    public void loadAggregate(String accountId){

        UnitOfWork uow = DefaultUnitOfWork.startAndGet(null);

        Aggregate<AccountAggregate> accountAggregate = accountAggregateRepository.load(accountId);

        // 只能使用以下方式调用 Aggregate的方法
        //accountAggregate.invoke(AccountAggregate::toString);

        uow.commit();
    }
}