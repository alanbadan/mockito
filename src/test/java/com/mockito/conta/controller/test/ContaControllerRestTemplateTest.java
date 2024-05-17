package com.mockito.conta.controller.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockito.conta.dto.TransacaoDto;
import com.mockito.conta.entity.Banco;
import com.mockito.conta.entity.Conta;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ContaControllerRestTemplateTest {
	
	@Autowired
	RestTemplate restTemplate;
	
	private ObjectMapper objectMapper;
	
	@LocalServerPort
	private int porta;
	
	
	@Test
	@Order(1)
	void salvarConta() {
		
		Conta conta = new Conta(null, "Jose", new BigDecimal("1000"));
		
		ResponseEntity<Conta> response = restTemplate.postForEntity(createUri("/conta"), conta, Conta.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
		Conta contaSalva = response.getBody();
		assertNotNull(contaSalva);
		assertEquals(1L, contaSalva.getId());
		assertEquals("Jose", contaSalva.getPessoa());
		assertEquals("1000", contaSalva.getSaldo().toPlainString());
				
	}
	
	
	@Test
	@Order(2)
	void salvarConta2() {
		
	Conta conta = new Conta(null, "Maria", new BigDecimal("2000"));
		
		ResponseEntity<Conta> response = restTemplate.postForEntity(createUri("/conta"), conta, Conta.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
		Conta contaSalva = response.getBody();
		assertNotNull(contaSalva);
		assertEquals(2L, contaSalva.getId());
		assertEquals("Maria", contaSalva.getPessoa());
		assertEquals("2000", contaSalva.getSaldo().toPlainString());
		
	}
/*	
	@Test
	@Order(3)
	void salvarBanco() {
		 Banco banco = new Banco(null, "Dinheiro Facil", 0);
		 
		 ResponseEntity<Banco> response = restTemplate.postForEntity(createUri("/banco"), banco, Banco.class);
		 assertEquals(HttpStatus.CREATED, response.getStatusCode());
		 assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
		 Banco bancoSalvo = response.getBody();
		 assertEquals(1L, bancoSalvo.getId());
		 assertEquals("Dinheiro Facil", bancoSalvo.getNome());
		 assertEquals(0, bancoSalvo.getTotalTransferencia());
	}
	*/
	@Test
	@Order(3)
	void buscarContaId() {
		
		ResponseEntity<Conta> response = restTemplate.getForEntity(createUri("/conta/1"), Conta.class);
		Conta contaId = response.getBody();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
		
		assertNotNull(contaId);
		assertEquals(1L, contaId.getId());
		assertEquals("Jose", contaId.getPessoa());
		assertEquals("1000.00", contaId.getSaldo().toPlainString());
		
	}
	
	@Test
	@Order(4)
	void ListarContas() throws JsonMappingException, JsonProcessingException {
		ResponseEntity<Conta[]> response = restTemplate.getForEntity(createUri("/conta/listar"), Conta[].class);
		List<Conta> listaConta = Arrays.asList(response.getBody());
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
		
		assertNotNull(listaConta);
		assertEquals(2, listaConta.size());
		assertEquals(1L, listaConta.get(0).getId());
		assertEquals("Jose", listaConta.get(0).getPessoa());
		assertEquals("1000.00", listaConta.get(0).getSaldo().toPlainString());
		assertNotNull(listaConta);
		assertEquals(2L, listaConta.get(1).getId());
		assertEquals("Maria", listaConta.get(1).getPessoa());
		assertEquals("2000.00", listaConta.get(1).getSaldo().toPlainString());
	/*	
		JsonNode json = objectMapper.readTree(objectMapper.writeValueAsString(listaConta));
		assertEquals(1L, json.get(0).path("id").asLong());
		assertEquals("Jose", json.get(0).path("pessoa").asText());
		assertEquals("1000.00", json.get(0).path("saldo").asText());
		assertEquals(2L, json.get(1).path("id").asLong());
		assertEquals("Maria", json.get(1).path("pessoa").asText());
		assertEquals("2000.00", json.get(1).path("saldo").asText());
		*/
	}
	
	@Test
	@Order(5)
	void salvarBanco() {
		
	   Banco banco = new Banco(null, "Dinheiro Facil", 0);
	   
	   ResponseEntity<Banco> response = restTemplate.postForEntity(createUri("/banco"), banco, Banco.class);
	   Banco bancoSalvo = response.getBody();
	   assertNotNull(bancoSalvo);
	   assertEquals(2L, bancoSalvo.getId());
	   assertEquals("Dinheiro Facil", bancoSalvo.getNome());
	   assertEquals(0, bancoSalvo.getTotalTransferencia());
	   
	}
	
	@Test
	@Order(6)
	void tranferir() throws JsonMappingException, JsonProcessingException {
		
		TransacaoDto dto = new TransacaoDto();
		dto.setContaOrigenId(1L);
		dto.setContaDestinoId(2L);
		dto.setValor(new BigDecimal("100"));
		dto.setBancoId(1L);
		
		ResponseEntity<String> response = restTemplate.postForEntity(createUri("/conta/transferir"), dto, String.class);
		String json = response.getBody();
		System.out.println(json);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
		
		assertNotNull(json);
		assertTrue(json.contains("Transferencia Realizada com Sucesso"));
		assertTrue(json.contains("{\"contaOrigenId\":1,\"contaDestinoId\":2,\"valor\":100,\"bancoId\":1}"));
/*		
		JsonNode jsonNode = objectMapper.readTree(json);
		assertEquals("Transferencia Realizada com Sucesso", jsonNode.path("menssagem").asText());
		assertEquals(LocalDate.now().toString(), jsonNode.path("date").asText());
		assertEquals("100", jsonNode.path("transacao").path("valor").asText());
		assertEquals(1L, jsonNode.path("transacao").path("contaOrigemId").asLong());
*/		
		Map<String, Object> response2 = new HashMap<>();
		response2.put("date", LocalDate.now());
		response2.put("status", "Ok");
		response2.put("mensagem", "Transferencia Realizada com Sucesso");
		response2.put("transacao", dto);
		
//		assertEquals(objectMapper.writeValueAsString(response2), json);
				
	}
	@Test
	@Order(7)
	void deletarConta() {
		
		ResponseEntity<Conta[]> response = restTemplate.getForEntity(createUri("/conta/listar"), Conta[].class);
		List<Conta> listaContas = Arrays.asList(response.getBody());
		assertEquals(2, listaContas.size());
		
	//	restTemplate.delete(createUri("conta/2"));
		Map<String, Long> pathVariable = new HashMap<>();
		pathVariable.put("id", 2L);
		ResponseEntity<Void> deleteConta = restTemplate.exchange(createUri("/conta/{id}"), HttpMethod.DELETE, null, Void.class, pathVariable);
		
		assertEquals(HttpStatus.NO_CONTENT, deleteConta.getStatusCode());
		assertFalse(deleteConta.hasBody());
		
		response = restTemplate.getForEntity(createUri("/conta/listar"), Conta[].class);
		listaContas = Arrays.asList(response.getBody());
		assertEquals(1, listaContas.size());	
			
		
	}
/*	
	@Test()
	@Order(8)
	void buscaContaIdInexistente() {
		
		ResponseEntity<Conta> contaPorId = restTemplate.getForEntity(createUri("/conta/2"), Conta.class);
		assertEquals(HttpStatus.NOT_FOUND, contaPorId.getStatusCode());
		assertTrue(contaPorId.hasBody());
	}
	
*/
    private String createUri(String uri) {
	  return "http://localhost:" + porta + uri;
  }
}
