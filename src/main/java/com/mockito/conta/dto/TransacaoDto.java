package com.mockito.conta.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransacaoDto {
	
	    private Long contaOrigenId;
	    private Long contaDestinoId;
	    private BigDecimal valor;
	    private Long bancoId;

}
