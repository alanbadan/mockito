package com.mockito.conta.controller.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockito.conta.dto.TransacaoDto;
import com.mockito.conta.entity.Banco;
import com.mockito.conta.entity.Conta;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ContaControllerWebTestClientTests {
	
	@Autowired
	WebTestClient webTestClient;
	
	private ObjectMapper objectMapper;
	
	private String URI = "http://localhost:8080/conta";
	
	 @BeforeEach
	    void setUp() {
	        objectMapper = new ObjectMapper();
	    }
	
	
	@Test
	@Order(1)
	void salvarConta() {
		 
		Conta conta = new Conta(null, "Jose", new BigDecimal("2000"));
		
		webTestClient.post().uri(URI)
		             .contentType(MediaType.APPLICATION_JSON)
		             .bodyValue(conta)
		             .exchange()
		             //then
		             .expectStatus().isCreated()
		             .expectHeader().contentType(MediaType.APPLICATION_JSON)
		             .expectBody()
		             .jsonPath("$.id").isEqualTo(1L)
		             .jsonPath("$.pessoa").isEqualTo(conta.getPessoa())
		             .jsonPath("$.saldo").isEqualTo(conta.getSaldo().toPlainString());
		             
	}

	@Test
	@Order(2)
	void salvarConta2() {
		Conta conta = new Conta(null, "Antonio", new BigDecimal("1000"));
		
		webTestClient.post().uri(URI)
                     .contentType(MediaType.APPLICATION_JSON)
                     .bodyValue(conta)
                     .exchange()
        //then
                     .expectStatus().isCreated()
		             .expectHeader().contentType(MediaType.APPLICATION_JSON)
		             .expectBody(Conta.class)
		.consumeWith(response -> {
			Conta c = response.getResponseBody();
			assertNotNull(c);
			assertEquals(2L, c.getId());
			assertEquals("Antonio", c.getPessoa());
			assertEquals("1000", c.getSaldo().toPlainString());
		});             
		
	}
	
	@Test
	@Order(3)
	void buscarPorContaId() {
		webTestClient.get().uri(URI + "/1")
		             .exchange()
		             .expectStatus().isOk()
		             .expectHeader().contentType(MediaType.APPLICATION_JSON)
		             .expectBody()
		             .jsonPath("$.id").isEqualTo(1L)
		             .jsonPath("$.pessoa").isEqualTo("Jose")
		             .jsonPath("$.saldo").isEqualTo("2000.0");
       /*     
         .expectBody(Conta.class)
                .consumeWith(response -> {
                    Conta conta = response.getResponseBody();
                    assertNotNull(conta);
                    assertEquals("Jose", conta.getPersona());
                    assertEquals("2000", conta.getSaldo().toPlainString());
                });             
        */
	}
	
	@Test
	@Order(4)
	void ListaConta() {
		webTestClient.get().uri(URI + "/listar").exchange()
		              .expectStatus().isOk()
		              .expectHeader().contentType(MediaType.APPLICATION_JSON)
		              .expectBody()
		              .jsonPath("$[0].id").isEqualTo(1L)
		              .jsonPath("$[0].pessoa").isEqualTo("Jose")
		              .jsonPath("$[0].saldo").isEqualTo("2000.0")
		              .jsonPath("$[1].id").isEqualTo(2L)
		              .jsonPath("$[1].pessoa").isEqualTo("Antonio")
		              .jsonPath("$[1].saldo").isEqualTo("1000.0")
		              .jsonPath("$").isArray()
		              .jsonPath("$").value(hasSize(2));
		              
	}
	@Test
	@Order(5)
	void retornoListaContas() {
		webTestClient.get().uri(URI + "/listar").exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBodyList(Conta.class)
        .consumeWith(response -> {
        	List<Conta> lista = response.getResponseBody();
        	assertNotNull(lista);
        	assertEquals(2, lista.size());
        	assertEquals(1L, lista.get(0).getId());
        	assertEquals("Jose", lista.get(0).getPessoa());
        	assertEquals("2000.00", lista.get(0).getSaldo().toPlainString());
        	assertEquals(2L, lista.get(1).getId());
        	assertEquals("Antonio", lista.get(1).getPessoa());
        	assertEquals("1000.00", lista.get(1).getSaldo().toPlainString());
        	      			
        })
        .hasSize(2).value(hasSize(2));
        
		
	}
	@Test
	@Order(6)
	//cirnado banco se não a transferencia quebra sem id do banco
	void salvarBanco() {
		
		Banco banco = new Banco(null, "Dinheiro Facil", 0);
		
		webTestClient.post().uri("http://localhost:8080/banco")
		             .contentType(MediaType.APPLICATION_JSON)
		             .bodyValue(banco)
		             .exchange()
		             .expectStatus().isCreated()
		             .expectHeader().contentType(MediaType.APPLICATION_JSON)
		             .expectBody()
		             .jsonPath("$.id").isEqualTo(2L)
		             .jsonPath("$.nome").isEqualTo(banco.getNome())
		             .jsonPath("$.totalTransferencia").isEqualTo(0);
	}
	@Test
	@Order(7)
	void transferir() {
		
		TransacaoDto dto = new TransacaoDto();
		dto.setContaOrigenId(1L);
		dto.setContaDestinoId(2L);
		dto.setValor(new BigDecimal("100"));
		dto.setBancoId(1L);
		
		Map<String, Object> response = new HashMap<>();
		response.put("date", LocalDate.now());
		response.put("status", "Ok");
		response.put("mensagem", "Transferencia Realizada com Sucesso");
		response.put("transacao", dto);
		
		webTestClient.post().uri(URI + "/transferir")
		              .contentType(MediaType.APPLICATION_JSON)
		              .bodyValue(dto)
		              .exchange()
		              .expectStatus().isOk()
		              .expectHeader().contentType(MediaType.APPLICATION_JSON)
		              .expectBody()
		              
		              .consumeWith(retorno -> {
			            	 
							try {
								JsonNode json = objectMapper.readTree(retorno.getResponseBody());
								assertEquals("Transferencia Realizada com Sucesso", json.path("menssagem").asText());
								assertEquals(LocalDate.now().toString(), json.path("date").asText());
								assertEquals(1L, json.path("transacao").path("contaOrigenId").asLong());
								assertEquals("100", json.path("transacao").path("valor").asText());
							} catch (IOException e) {
								e.printStackTrace();
							} 	 
			           })
			          
		              .jsonPath("$.menssagem").isNotEmpty()
		              .jsonPath("$.menssagem").value(is("Transferencia Realizada com Sucesso"))
		              .jsonPath("$.transacao.contaOrigenId").isEqualTo(dto.getContaOrigenId())
		              .jsonPath("$.transacao.contaDestinoId").isEqualTo(dto.getContaDestinoId())
		              .jsonPath("$.date").isEqualTo(LocalDate.now().toString());
		             // .json(objectMapper.writeValueAsString(response));
		              
	}
	
	@Test
	@Order(8)
	void deletandoConta() {
		webTestClient.get().uri(URI +"/listar").exchange()
		             .expectStatus().isOk()
		             .expectHeader()
		             .contentType(MediaType.APPLICATION_JSON)
		             .expectBodyList(Conta.class)
		             .hasSize(2);
		
		webTestClient.delete().uri(URI + "/2").exchange()
		             .expectStatus().isNoContent()
		             .expectBody().isEmpty();
		
		webTestClient.get().uri(URI +"/listar").exchange()
                     .expectStatus().isOk()
                     .expectHeader()
                     .contentType(MediaType.APPLICATION_JSON)
                     .expectBodyList(Conta.class)
                     .hasSize(1);
		
		webTestClient.get().uri(URI + "/2").exchange()
		             .expectStatus().isNotFound()
//		             .expectStatus().isNoContent()
		             .expectBody().isEmpty();
		             
		             
	}
	

}
