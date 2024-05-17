package com.mockito.conta.serviceimpl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mockito.conta.builder.DadosTest;
import com.mockito.conta.entity.Banco;
import com.mockito.conta.entity.Conta;
import com.mockito.conta.exception.SaldoInuficienteException;
import com.mockito.conta.repository.BancoRepository;
import com.mockito.conta.repository.ContaRepository;
import com.mockito.conta.service.ContaService;
import com.mockito.conta.serviceimpl.ContaServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ContaServiceImplTest {

	@InjectMocks
	ContaServiceImpl contaServiceImpl;
	
	@Mock
	ContaRepository contaRepository;
	
	@Mock 
	BancoRepository bancoRepository;
	
	@Mock
	ContaService contaService;
	

	private static final Conta CONTA_001 = new Conta(1L, "Jose", new BigDecimal("1000"));
	private static final Conta CONTA_002 = new Conta(2L, "Antonio", new BigDecimal("2000"));
	private static final Conta CONTA_003 = new Conta(2L, "Maria", new BigDecimal("1000"));
	private static final Banco BANCO = new Banco(1L, "Banco DinheiroFacil", 0);
	
	
	@Test
	void deveriaSalvarConta() {
	//	var conta1 =  new Conta(1L, "Jose", new BigDecimal("1000"));
			
		when(contaRepository.save(CONTA_001)).thenReturn(CONTA_001);
		
		Conta conta = contaServiceImpl.salvarConta(CONTA_001);
		
		assertNotNull(conta);
		assertEquals(1L, conta.getId());
		assertEquals("Jose", conta.getPessoa());
		assertEquals("900", conta.getSaldo().toPlainString());
		
		verify(contaRepository, times(1)).save(conta);
		
	}
	
	@Test
	void deveriaRetornarRevisaoDeSaldo() {
		
		when(contaRepository.findById(1L)).thenReturn(Optional.of(CONTA_001));
		when(contaRepository.findById(2L)).thenReturn(Optional.of(CONTA_002));
		
		BigDecimal saldoOrigem = contaServiceImpl.revisarSaldo(CONTA_001.getId());
		BigDecimal saldoDestino = contaServiceImpl.revisarSaldo(CONTA_002.getId());
		
		assertEquals("900", saldoOrigem.toPlainString());
		assertEquals("2100", saldoDestino.toPlainString());
		
		verify(contaRepository, times(1)).findById(1L);
		verify(contaRepository, times(1)).findById(2L);
	}
	
    
    @Test 
    void deveriaBuscarContaPorId() {
    	when(contaRepository.findById(1L)).thenReturn(Optional.of(CONTA_001));
    	
    	Optional<Conta> conta1 = contaServiceImpl.getContafindById(1L);
    	
    	assertNotNull(conta1);
    	assertEquals(1L, conta1.get().getId());
    	assertEquals("Jose", conta1.get().getPessoa());
    	assertEquals("1000", conta1.get().getSaldo().toPlainString());
    
    }

  @Test
  void deveriaTransferirDinheiroEntreContasERealizarUpdate() {
	  
	    when(contaRepository.findById(1L)).thenReturn(Optional.of(CONTA_001));
		when(contaRepository.findById(2L)).thenReturn(Optional.of(CONTA_002));
		when(bancoRepository.findById(1L)).thenReturn(Optional.of(BANCO));
		
		//then
		contaServiceImpl.transferir(1L, 2L, new BigDecimal("100"), 1L);
		
		BigDecimal saldoOrigem = contaServiceImpl.revisarSaldo(CONTA_001.getId());
		BigDecimal saldoDestino = contaServiceImpl.revisarSaldo(CONTA_002.getId());
		//totalTransferencia
		Integer total = contaServiceImpl.revisarTotalTransferencia(1L);
		assertEquals(1, total);
		
		assertEquals("900", saldoOrigem.toPlainString());
		assertEquals("2100", saldoDestino.toPlainString());
		
		
		verify(contaRepository, times(2)).findById(1L);
		verify(contaRepository, times(2)).findById(2L);
		//update contas
		verify(contaRepository, times(2)).save(any(Conta.class));
		//banco
		verify(bancoRepository, times(2)).findById(1L);
		verify(bancoRepository, times(1)).save(any(Banco.class));
	     	
    }
  
    @Test
    void deveriaRevisarTotalTransferencia() {
    	
    	Integer transferencia = 1;
    	var banco = new Banco(1L, "Banco Dinheiro Facil", 1);
    	when(bancoRepository.findById(1L)).thenReturn(Optional.of(banco));
    	
    	Integer total = contaServiceImpl.revisarTotalTransferencia(1L);
		assertEquals(transferencia, total);
		
		verify(bancoRepository, times(1)).findById(1L);
    	
    }
    @Test
    void deveriaLancarExceptionSaldoInsuficiente() {
    	
    	 BigDecimal valor = new BigDecimal("1200"); 
    	
    	when(contaRepository.findById(1L)).thenReturn(Optional.of(CONTA_001));

 		assertThrows(SaldoInuficienteException.class, () -> contaServiceImpl.saldoSuficiente(1L, valor)); 
 	
    	
    }
    @Test
    void deveriaAdicionarCreditoEmConta() {
    	
    	BigDecimal valor = new BigDecimal("100"); 
    	
    	when(contaRepository.findById(1L)).thenReturn(Optional.of(CONTA_003));
    	
        Conta conta = contaServiceImpl.creditoConta(1L, valor);
        assertEquals("1100",conta.getSaldo().toPlainString());
        
        verify(contaRepository, times(1)).findById(1L);
        
    }

   

}