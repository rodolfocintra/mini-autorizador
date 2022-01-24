package br.com.vr.miniautorizador.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardTransationRequest {

    @JsonProperty("numeroCartao")
    private String card;

    @JsonProperty("senhaCartao")
    private String password;

    @JsonProperty("valor")
    private BigDecimal cardTransactionValue;
}
