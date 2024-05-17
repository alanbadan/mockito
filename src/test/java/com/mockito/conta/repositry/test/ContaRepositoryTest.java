package com.mockito.conta.repositry.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.mockito.conta.builder.DadosTest;
import com.mockito.conta.entity.Conta;
import com.mockito.conta.repository.ContaRepository;

@DataJpaTest
public class ContaRepositoryTest {
	
	@Autowired
	ContaRepository contaRepository;
	
	
	
	@Test
	void testBuscaId() {
		
		var conta = DadosTest.saveConta1();
		contaRepository.save(conta);
		
		Optional<Conta> optionalConta = contaRepository.findById(conta.getId());
		
		assertTrue(optionalConta.isPresent());
		assertEquals(4L, optionalConta.orElseThrow().getId());
	}
	
	@Test
	void testExceptionBuscaId() {
		contaRepository.save(DadosTest.saveConta1());
		
		Optional<Conta> optionalConta = contaRepository.findById(3L);
		
		assertThrows(NoSuchElementException.class, optionalConta::orElseThrow);
	}
 
	@Test
	void testBuscaPorNome() {

		contaRepository.save(DadosTest.saveConta1());
		Optional<Conta> optionalConta = contaRepository.findByPessoa("Jose");
		
		assertTrue(optionalConta.isPresent());
		assertEquals("Jose", optionalConta.orElseThrow().getPessoa());
	}
	
	@Test
	void testExceptionBuscaPorNome() {
		
		contaRepository.save(DadosTest.saveConta1());
		Optional<Conta> optionalConta = contaRepository.findByPessoa("Alberto");
		
		assertThrows(NoSuchElementException.class, optionalConta::orElseThrow);
		assertFalse(optionalConta.isPresent());
	}
   
	@Test
	void testListaConta() {
		contaRepository.save(DadosTest.saveConta1());
		contaRepository.save(DadosTest.saveConta2());
		
		List<Conta> list = contaRepository.findAll();
		assertEquals(2, list.size());
	}
   
	@Test
	void testUpdateConta() {
		
		Conta conta = contaRepository.save(DadosTest.saveConta1());
		assertEquals("Jose",conta.getPessoa());
		assertEquals("1000", conta.getSaldo().toPlainString());
		
		conta.setSaldo(new BigDecimal("1200"));
		
		Conta contaUpdate = contaRepository.save(conta);
		assertEquals("Jose", contaUpdate.getPessoa());
		assertEquals("1200", contaUpdate.getSaldo().toPlainString());
		
	}
	
	@Test
	void testDelete() {
		
		Conta conta = contaRepository.save(DadosTest.saveConta2());
		contaRepository.save(conta);	
		assertEquals("Carlos", conta.getPessoa());
		
		contaRepository.deleteById(conta.getId());
		
		Optional<Conta> contaDelete = contaRepository.findById(conta.getId());
		
		assertThat(contaDelete).isEmpty();
	}
	
	@Test
	void testSave() {
		Conta conta = new Conta(null, "Maria", new BigDecimal("2000"));
		Conta contaSalva = contaRepository.save(conta);
		
		assertThat(contaSalva.getId()).isNotNull();
		assertEquals("Maria", contaSalva.getPessoa());
		assertEquals("2000", contaSalva.getSaldo().toPlainString());
		
	}
	
}
