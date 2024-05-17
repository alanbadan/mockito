package com.mockito.conta.controller.test;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.assertj.core.error.ShouldHaveSameSizeAs;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockito.conta.dto.TransacaoDto;
import com.mockito.conta.entity.Conta;
import com.mockito.conta.service.BancoService;
import com.mockito.conta.service.ContaService;

@WebMvcTest
public class ContaControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	ContaService contaService;
	
	@MockBean
	BancoService bancoService;
	
	@Autowired
	ObjectMapper objectMapper;
	
	
	@Test
	void deveriaCriarConta() throws JsonProcessingException, Exception {
		
		Conta conta = new Conta(null, "Jose", new BigDecimal("1000"));
		
		when(contaService.salvarConta(any())).then(invocation -> { Conta c = invocation.getArgument(0); c.setId(1L); return c;});
		
		mockMvc.perform(post("/conta")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(conta)))
		
		.andExpect(status().isCreated())
		.andDo(print())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.id", is(1)))
		.andExpect(jsonPath("$.pessoa", is("Jose")))
		.andExpect(jsonPath("$.saldo", is(1000)));
		
		verify(contaService).salvarConta(any());
			
	}
	@Test
	void deveriaBuscarContaPorId() throws Exception {
		Long id = 1L;
		
		Conta conta = new Conta(1l, "Jose", new BigDecimal("1000"));
		
		when(contaService.getContafindById(id)).thenReturn(Optional.of(conta));
		
		mockMvc.perform(get("/conta/{id}", id)
		       .contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andDo(print())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.pessoa", is("Jose")))
		.andExpect(jsonPath("$.saldo", is(1000)));
		
		verify(contaService).getContafindById(id);	
		
	}
	@Test
	void deveriaRetornarFuncionarioNaoEncontradoPorId() throws Exception {
        Long id = 1L;
		
		Conta conta = new Conta(1L, "Jose", new BigDecimal("1000"));
		
		when(contaService.getContafindById(id)).thenReturn(Optional.empty());
		
		mockMvc.perform(get("/conta/{id}", conta.getId())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound())
		.andDo(print());
	}
	
	@Test
	void deveriaRetornarListaDeContas() throws JsonProcessingException, Exception {
		Optional<Conta> conta1 =  Optional.of(new Conta(1L, "Jose", new BigDecimal("1000")));
		Optional<Conta> conta2 =  Optional.of(new Conta(2L, "Maria", new BigDecimal("2000")));
		
		
		List<Conta> list =  new ArrayList<>();// Arrays.asList(conta1.orElseThrow(), conta2.orElseThrow());
		list.add(conta1.orElseThrow());
		list.add(conta2.orElseThrow());
		
		when(contaService.listarContas()).thenReturn(list);
		
		mockMvc.perform(get("/conta/listar"))
		    //   .contentType(MediaType.APPLICATION_JSON))
		
		.andExpect(status().isOk())
		.andDo(print())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$[0].pessoa").value("Jose"))
		.andExpect(jsonPath("$[0].saldo").value("1000"))
		.andExpect(jsonPath("$[1].pessoa").value("Maria"))
		.andExpect(jsonPath("$[1].saldo").value("2000"))
		.andExpect(jsonPath("$.size()" ,is(list.size())))
		.andExpect(content().json(objectMapper.writeValueAsString(list)));
		
	    verify(contaService).listarContas();
	}
	
	@Test
	void deveriaFazerTransferenciaEntreContas() throws Exception {
		
		TransacaoDto dto = new TransacaoDto();
		dto.setContaOrigenId(1L);
		dto.setContaDestinoId(2L);
		dto.setValor(new BigDecimal("100"));
		dto.setBancoId(1L);
		
//		System.out.println(objectMapper.writeValueAsString(dto));
//		doNothing().when(contaService).transferir(1L, 2L, new BigDecimal("100"), 1l);
		
		Map<String, Object> response = new HashMap<>();
		response.put("date", LocalDate.now());
		response.put("status", "Ok");
		response.put("menssagem", "Transferencia Realizada com Sucesso");
		response.put("transacao", dto);
		
		System.out.println(objectMapper.writeValueAsString(response));
		
		mockMvc.perform(post("/conta/transferir")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
		
		.andExpect(status().isOk())
		.andDo(print())
		.andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
		.andExpect(jsonPath("$.menssagem").value(is("Transferencia Realizada com Sucesso")))
		.andExpect(jsonPath("$.status").value("Ok"))
		.andExpect(jsonPath("$.transacao.contaOrigenId").value(dto.getContaOrigenId()))
	.andExpect(content().json(objectMapper.writeValueAsString(response)));
		
		
	}

}
