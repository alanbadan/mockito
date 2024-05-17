package com.mockito.conta.repositry.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.mockito.conta.entity.Banco;
import com.mockito.conta.repository.BancoRepository;

@DataJpaTest
public class BancoRepositoryTest {
	
	@Autowired
	BancoRepository bancoRepository;
	
	@Test
	void testSalvar() {
		
		Banco banco = new Banco(null, "Banco do povo", 0);
		Banco bancoSalvo = bancoRepository.save(banco);
		
		assertThat(bancoSalvo.getId()).isNotNull();
		assertEquals("Banco do povo", bancoSalvo.getNome());
		assertEquals(0, bancoSalvo.getTotalTransferencia());
	}
	

}
