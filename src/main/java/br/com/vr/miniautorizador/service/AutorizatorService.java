package br.com.vr.miniautorizador.service;

import br.com.vr.miniautorizador.domain.CardRequest;
import br.com.vr.miniautorizador.domain.CardResponse;
import br.com.vr.miniautorizador.domain.CardTransationRequest;

import java.math.BigDecimal;


public interface AutorizatorService {

    CardResponse createCard(CardRequest card);

    BigDecimal getBalance(String card);

    void cardTransaction(CardTransationRequest cardTransationRequest);
}
