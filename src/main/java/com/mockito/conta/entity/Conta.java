package com.mockito.conta.entity;

import java.math.BigDecimal;

import com.mockito.conta.exception.SaldoInuficienteException;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class Conta {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String pessoa;
	private BigDecimal saldo;
	
	
	//debito
	public void debito(BigDecimal valor) {
		BigDecimal novoSaldo =  this.saldo.subtract(valor);
		if(novoSaldo.compareTo(BigDecimal.ZERO) < 0 ) {
			throw new SaldoInuficienteException("Saldo insuficiente na conta");
			}
		this.saldo = novoSaldo;
	}
   //credito
	public void credito(BigDecimal valor) {
		this.saldo = this.saldo.add(valor);
	}
}
