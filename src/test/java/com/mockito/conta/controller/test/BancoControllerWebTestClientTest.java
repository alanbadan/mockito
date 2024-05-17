package com.mockito.conta.controller.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.mockito.conta.entity.Banco;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BancoControllerWebTestClientTest {
	
	@Autowired
	WebTestClient webTestClient;
	
	
	private String URI = "http://localhost:8080/banco";
	
	
	@Test
	void salvarBanco() {
		
		Banco banco = new Banco(null, "Banco do Povo", 0);
		
		webTestClient.post().uri(URI)
		             .contentType(MediaType.APPLICATION_JSON)
		             .bodyValue(banco)
		             .exchange()
		             //then
		             .expectStatus().isCreated()
		             .expectHeader().contentType(MediaType.APPLICATION_JSON)
		             .expectBody()
		             .jsonPath("$.id").isEqualTo(1L)
		             .jsonPath("$.nome").isEqualTo(banco.getNome())
		             .jsonPath("$.totalTransferencia").isEqualTo(banco.getTotalTransferencia());
		             
	}
}
