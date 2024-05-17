package com.mockito.conta.controller.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.mockito.conta.entity.Banco;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BancoControllerRestTemplateTest {
	
	@Autowired
	RestTemplate restTemplate;
	
	@LocalServerPort
	private int porta;
	
	
	@Test
	@Order(1)
	void salvarBanco() {
		
		Banco banco = new Banco(null, "Banco do Povo", 0);
		
		ResponseEntity<Banco> response = restTemplate.postForEntity(createUri("/banco"), banco, Banco.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
		Banco bancoSlavo = response.getBody();
		assertNotNull(bancoSlavo);
		assertEquals(1L, bancoSlavo.getId());
		assertEquals("Banco do Povo", bancoSlavo.getNome());
		assertEquals(0, bancoSlavo.getTotalTransferencia());
	}
	
	
	 private String createUri(String uri) {
		  return "http://localhost:" + porta + uri;
	  }

}
