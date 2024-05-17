package com.mockito.conta.serviceimpl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mockito.conta.entity.Banco;
import com.mockito.conta.repository.BancoRepository;
import com.mockito.conta.service.BancoService;
import com.mockito.conta.serviceimpl.BancoServiceImpl;

@ExtendWith(MockitoExtension.class)
public class BancoServiceImplTest {
	
	@InjectMocks
	BancoServiceImpl bancoServiceImpl;
	
	@Mock
	BancoRepository bancoRepository;
	
	@Mock
	BancoService bancoService;
	
	private static final Banco BANCO = new Banco(1L, "Banco do Povo", 0);
	
	@Test
	void deveriaSalvarBanco() {
		
		when(bancoRepository.save(BANCO)).thenReturn(BANCO);
		
		Banco banco = bancoServiceImpl.salvarBanco(BANCO);
		
		assertNotNull(banco);
		assertEquals(1L,  banco.getId());
		assertEquals("Banco do Povo", banco.getNome());
		assertEquals(0, banco.getTotalTransferencia());
	}
	
	
	


}
