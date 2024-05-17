package com.mockito.conta.builder;

import java.math.BigDecimal;
import java.util.Optional;

import com.mockito.conta.entity.Banco;
import com.mockito.conta.entity.Conta;

public class DadosTest {
	
	public static Optional<Conta> conta1 () {
		return Optional.of(new Conta(1L, "Jose", new BigDecimal("1000")));
	}
	public static Optional<Conta> conta2 () { 
	    return Optional.of( new Conta(2L, "Antonio", new BigDecimal("1000")));
	}
	
	public static Optional<Banco> banco(){
		return Optional.of(new Banco(1L, "Banco DinheiroFacil", 0));
	}
	
	public static Banco saveBanco() {
		return new Banco(1L, "Banco DinheiroFacil", 0);
	}
	
	 
	public static Conta saveConta1() {
		return new Conta (null, "Jose", new BigDecimal("1000"));
	}
	
	public static Conta saveConta2() {
		return new Conta (null, "Carlos", new BigDecimal("1000"));
	}
	
}
