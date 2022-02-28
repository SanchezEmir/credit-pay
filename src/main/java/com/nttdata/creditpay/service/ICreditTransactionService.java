package com.nttdata.creditpay.service;

import com.nttdata.creditpay.entity.CreditTransaction;
import com.nttdata.creditpay.entity.dto.Credit;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICreditTransactionService {
  
  Mono<CreditTransaction> findById(String id);
  Flux<CreditTransaction> findAll();
  Mono<CreditTransaction> create(CreditTransaction t);
  Mono<CreditTransaction> update(CreditTransaction t);
  Mono<Boolean> delete(String t);
  Flux<CreditTransaction> findCreditsPaid(String id);
  Mono<Credit> findCredit(String id);

}
