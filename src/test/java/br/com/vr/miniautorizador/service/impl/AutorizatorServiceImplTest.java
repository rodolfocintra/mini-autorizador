package br.com.vr.miniautorizador.service.impl;

import br.com.vr.miniautorizador.domain.AccountMapperImpl;
import br.com.vr.miniautorizador.domain.CardRequest;
import br.com.vr.miniautorizador.domain.CardResponse;
import br.com.vr.miniautorizador.domain.CardTransationRequest;
import br.com.vr.miniautorizador.exception.ExistsException;
import br.com.vr.miniautorizador.exception.NotFoundException;
import br.com.vr.miniautorizador.exception.WithoutBalanceException;
import br.com.vr.miniautorizador.exception.WrongPasswordException;
import br.com.vr.miniautorizador.model.AccountEntity;
import br.com.vr.miniautorizador.repository.AccountRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class AutorizatorServiceImplTest {

    private AutorizatorServiceImpl service;


    private AccountRepository repository;

    private AccountMapperImpl mapper;


    private CardRequest request;

    private CardResponse response;

    private AccountEntity entity;

    private CardTransationRequest cardTransationRequest;

    @BeforeEach
    public void setUp(){
        repository = mock(AccountRepository.class);

        mapper = new AccountMapperImpl();

        request = new CardRequest();
        request.setCard("123456789");
        request.setPassword("1234");

        response = new CardResponse();
        response.setCard("123456789");
        response.setPassword("1234");
        response.setBalance(new BigDecimal("500"));

        cardTransationRequest = new CardTransationRequest();
        cardTransationRequest.setCard("123456789");
        cardTransationRequest.setPassword("1234");
        cardTransationRequest.setCardTransactionValue(new BigDecimal("100"));



        entity = new AccountEntity();
        entity.setCard("123456789");
        entity.setPassword("1234");
        entity.setBalance(new BigDecimal("500"));
        service = new AutorizatorServiceImpl(repository,mapper);

    }

    @Test
    public void tryToSaveCard(){
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setCard("123456789");
        accountEntity.setPassword("1234");
        accountEntity.setBalance(new BigDecimal("500"));
        when(repository.save(any(AccountEntity.class))).thenReturn(accountEntity);

        CardResponse retorno  = service.createCard(request);
        Assertions.assertThat(retorno.equals(response));
    }

    @Test
    public void existEceptionWhenTrySaveCard(){

        when(repository.save(any(AccountEntity.class))).thenThrow(DataIntegrityViolationException.class);

        Assertions.assertThatThrownBy(() ->service.createCard(request)).isInstanceOf(ExistsException.class);
    }

    @Test
    public void tryCallBalance(){

        when(repository.findAccountEntityByCard(request.getCard())).thenReturn(Optional.of(entity));

        BigDecimal retorno  = service.getBalance(request.getCard());
        Assertions.assertThat(retorno.equals(new BigDecimal("500")));
    }

    @Test
    public void notFoundExceptionWhenTryCallGetBalance(){

        when(repository.findAccountEntityByCard("9876543")).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() ->service.getBalance("9876543")).isInstanceOf(NotFoundException.class);
    }

    @Test
    public void tryCardTransaction(){

        when(repository.findAccountEntityByCard(cardTransationRequest.getCard())).thenReturn(Optional.of(entity));
        service.cardTransaction(cardTransationRequest);

    }

    @Test
    public void withoutBalanceExceptionWhenTryCallCardTransaction(){
        when(repository.findAccountEntityByCard(cardTransationRequest.getCard())).thenReturn(Optional.of(entity));
        cardTransationRequest.setCardTransactionValue(new BigDecimal("600"));
        Assertions.assertThatThrownBy(() -> service.cardTransaction(cardTransationRequest)).isInstanceOf(WithoutBalanceException.class);
    }

    @Test
    public void notFoundExceptionExceptionWhenTryCallCardTransaction(){

        when(repository.findAccountEntityByCard(cardTransationRequest.getCard())).thenReturn(Optional.of(entity));
        cardTransationRequest.setCard("1232354354354");
        Assertions.assertThatThrownBy(() -> service.cardTransaction(cardTransationRequest)).isInstanceOf(NotFoundException.class);
    }

    @Test
    public void wrongPasswordExceptionWhenTryCallCardTransaction(){
        when(repository.findAccountEntityByCard(cardTransationRequest.getCard())).thenReturn(Optional.of(entity));
        cardTransationRequest.setPassword("123");
        Assertions.assertThatThrownBy(() -> service.cardTransaction(cardTransationRequest)).isInstanceOf(WrongPasswordException.class);
    }

}
