package br.com.vr.miniautorizador.controler;


import br.com.vr.miniautorizador.domain.CardRequest;
import br.com.vr.miniautorizador.domain.CardResponse;
import br.com.vr.miniautorizador.domain.CardTransationRequest;
import br.com.vr.miniautorizador.exception.ExistsException;
import br.com.vr.miniautorizador.exception.NotFoundException;
import br.com.vr.miniautorizador.exception.WithoutBalanceException;
import br.com.vr.miniautorizador.exception.WrongPasswordException;
import br.com.vr.miniautorizador.service.AutorizatorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/")
public class AutorizatorControlher {

    private static final Logger log = LogManager.getLogger(AutorizatorControlher.class);

    private static final String CARTAO_INEXISTENTE = "CARTAO_INEXISTENTE";
    private static final String SENHA_INVALIDA= "SENHA_INVALIDA";
    private static final String SALDO_INSUFICIENTE = "SALDO_INSUFICIENTE";


    private final AutorizatorService service;

    public AutorizatorControlher(AutorizatorService service) {
        this.service = service;
    }


    @PostMapping(value = "/cartoes")
    public ResponseEntity<CardResponse> createCard(@RequestBody CardRequest cardRequest) {

        log.info("Inicio da Criação do cartão.");
        try {
            CardResponse response = service.createCard(cardRequest);
            log.info("cartão criado com sucesso!!!!!!");
            return new ResponseEntity<CardResponse>(response, HttpStatus.CREATED);
        } catch (ExistsException e) {
            log.info("Numero do cartão ja existe !!!");
            return new ResponseEntity(cardRequest, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping(value = "/cartoes/{numeroCartao}")
    public ResponseEntity getBalance(@PathVariable("numeroCartao") String cardNumber) {
        try {
            log.info("Inicio da consulta saldo do cartão.");
            BigDecimal response = service.getBalance(cardNumber);
            log.info("Fim da consulta saldo do cartão.");
            return new ResponseEntity(response, HttpStatus.OK);
        }catch (NotFoundException e){
            log.info("Cartão não encontrado!!!");
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/transacoes")
    public ResponseEntity cardTransaction (@RequestBody CardTransationRequest cardTransationRequest) {

        try {
            log.info("Inicio da transação do cartão.");
            service.cardTransaction(cardTransationRequest);
            log.info("Transação do cartão concluida com sucesso");
            return new ResponseEntity("OK", HttpStatus.CREATED);
        } catch (NotFoundException e) {
            log.info("Cartão não encontrado!!!");
            return new ResponseEntity(CARTAO_INEXISTENTE, HttpStatus.UNPROCESSABLE_ENTITY);
        }catch (WrongPasswordException e){
            log.info("Senha do cartão  invalida" );
            return new ResponseEntity(SENHA_INVALIDA, HttpStatus.UNPROCESSABLE_ENTITY);
        }catch (WithoutBalanceException e){
            log.info("Transação do cartão não realizada saldo insuficiente");
            return new ResponseEntity(SALDO_INSUFICIENTE, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
