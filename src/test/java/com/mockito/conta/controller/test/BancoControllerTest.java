package com.mockito.conta.controller.test;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.script.Invocable;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockito.conta.entity.Banco;
import com.mockito.conta.service.BancoService;
import com.mockito.conta.service.ContaService;

@WebMvcTest
public class BancoControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	BancoService bancoService;
	
	@MockBean
	ContaService contaService;
	
	@Autowired
	ObjectMapper objectMapper;
	
    
	@Test
	void deveriaSalvarBanco() throws JsonProcessingException, Exception {
		
		Banco banco = new Banco(null, "Banco do Dinheiro", 0);
		
		when(bancoService.salvarBanco(any())).then(invocation -> {Banco b = invocation.getArgument(0); b.setId(1L); return b;});
		
		mockMvc.perform(post("/banco")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(banco)))
		
		.andExpect(status().isCreated())
		.andDo(print())
		.andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.nome",is("Banco do Dinheiro")))
        .andExpect(jsonPath("$.totalTransferencia", is(0)));
		
		verify(bancoService).salvarBanco(any());
		
	}

}
