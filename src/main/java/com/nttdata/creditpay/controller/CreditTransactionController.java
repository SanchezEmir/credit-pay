package com.nttdata.creditpay.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.creditpay.entity.CreditTransaction;
import com.nttdata.creditpay.service.ICreditTransactionService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/creditPaid")
public class CreditTransactionController {

  @Autowired
  ICreditTransactionService service;

  @GetMapping("list")
  public Flux<CreditTransaction> findAll() {
    return service.findAll();
  }

  @GetMapping("/find/{id}")
  public Mono<CreditTransaction> findById(@PathVariable String id) {
    return service.findById(id);
  }

  @PostMapping("/create")
  public Mono<ResponseEntity<CreditTransaction>> create(
      @RequestBody CreditTransaction creditTransaction) {
    // BUSCO EL CREDITO QUE SE PRETENDE HACER EL PAGO
    return service.findCredit(creditTransaction.getCredit().getId())
        .flatMap(credit -> service.findCreditsPaid(credit.getId()) // TODAS
                                                                   // PAGOS DE
                                                                   // ESTE
                                                                   // CREDITO
            .collectList()
            .filter(listCt -> credit.getAmount() >= listCt.stream()
                .mapToDouble(ct -> ct.getTransactionAmount()).sum() + creditTransaction.getTransactionAmount())
            .flatMap(listCt -> {
              creditTransaction.setCredit(credit);
              creditTransaction.setTransactionDate(LocalDateTime.now());
              return service.create(creditTransaction);
            }))
        .map(ct -> new ResponseEntity<>(ct, HttpStatus.CREATED))
        .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
  }

  @PutMapping("/update")
  public Mono<ResponseEntity<CreditTransaction>> update(
      @RequestBody CreditTransaction creditTransaction) {
    return service.findById(creditTransaction.getId()).flatMap(ctDB -> service
        .findCredit(creditTransaction.getCredit().getId())
        .flatMap(credit -> service.findCreditsPaid(credit.getId()).collectList()
            .filter(listCt -> credit.getAmount() >= listCt.stream() 
                .mapToDouble(ct -> ct.getTransactionAmount()).sum() - ctDB.getTransactionAmount() + creditTransaction.getTransactionAmount())
            .flatMap(listCt -> {
              creditTransaction.setCredit(credit);
              creditTransaction.setTransactionDate(LocalDateTime.now());
              return service.create(creditTransaction);
            })))
        .map(ct -> new ResponseEntity<>(ct, HttpStatus.CREATED))
        .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
  }

  @DeleteMapping("/delete/{id}")
  public Mono<ResponseEntity<String>> delete(@PathVariable String id) {
    return service.delete(id).filter(deleteCustomer -> deleteCustomer)
        .map(deleteCustomer -> new ResponseEntity<>("Credit pay Deleted",
            HttpStatus.ACCEPTED))
        .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

}
