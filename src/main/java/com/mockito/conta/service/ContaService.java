package com.mockito.conta.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.mockito.conta.entity.Banco;
import com.mockito.conta.entity.Conta;

public interface ContaService {
	
	Conta salvarConta(Conta conta);
	
	Optional<Conta> getContafindById(Long id);
	
	List<Conta> listarContas();
	
	Integer revisarTotalTransferencia(Long bancoid);
	
	BigDecimal revisarSaldo(Long contaId);
	
	Conta saldoSuficiente(Long numContaOrigem, BigDecimal valor);
	
	Conta creditoConta(Long umContaDestino, BigDecimal valor);
	
	void transferir(Long numContaOrigem, Long numContaDestino, BigDecimal valor, Long bancoId);

	Banco totalTransferencia(Long idBanco);
	
	void deleteById(Long id);

	

}
