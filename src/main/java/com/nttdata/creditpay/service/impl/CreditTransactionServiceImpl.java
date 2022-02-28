package com.nttdata.creditpay.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.nttdata.creditpay.entity.CreditTransaction;
import com.nttdata.creditpay.entity.dto.Credit;
import com.nttdata.creditpay.repository.ICreditTransactionRepository;
import com.nttdata.creditpay.service.ICreditTransactionService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class CreditTransactionServiceImpl implements ICreditTransactionService {

  private final WebClient webClient;
  private final ReactiveCircuitBreaker reactiveCircuitBreaker;

  @Value("${config.base.apigateway}")
  private String url;

  public CreditTransactionServiceImpl(
      ReactiveResilience4JCircuitBreakerFactory circuitBreakerFactory) {
    this.webClient = WebClient.builder().baseUrl(this.url).build();
    this.reactiveCircuitBreaker = circuitBreakerFactory.create("creditcharge");
  }

  @Autowired
  private ICreditTransactionRepository repo;

  @Override
  public Mono<CreditTransaction> findById(String id) {
    return repo.findById(id);
  }

  @Override
  public Flux<CreditTransaction> findAll() {
    return repo.findAll();
  }

  @Override
  public Mono<CreditTransaction> create(CreditTransaction t) {
    return repo.save(t);
  }

  @Override
  public Mono<CreditTransaction> update(CreditTransaction t) {
    return repo.save(t);
  }

  @Override
  public Mono<Boolean> delete(String t) {
    return repo.findById(t)
        .flatMap(tar -> repo.delete(tar).then(Mono.just(Boolean.TRUE)))
        .defaultIfEmpty(Boolean.FALSE);
  }

  @Override
  public Flux<CreditTransaction> findCreditsPaid(String id) {
    return repo.findByCreditId(id);
  }

  @Override
  public Mono<Credit> findCredit(String id) {
    log.info("buscando creditcharge");
    return reactiveCircuitBreaker.run(webClient.get().uri(this.url, id)
        .accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(Credit.class),
        throwable -> {
          return this.getDefaultCreditCard();
        });
  }

  public Mono<Credit> getDefaultCreditCard() {
    log.info("no encontro el servicio");
    Mono<Credit> credit = Mono.just(new Credit("0", null, null, null));
    return credit;
  }

}
