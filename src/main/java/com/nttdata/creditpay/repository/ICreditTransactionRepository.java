package com.nttdata.creditpay.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.nttdata.creditpay.entity.CreditTransaction;

import reactor.core.publisher.Flux;

public interface ICreditTransactionRepository extends ReactiveMongoRepository<CreditTransaction, String> {
  
  Flux<CreditTransaction> findByCreditId(String id);

}
