package com.mockito.conta.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import static org.springframework.http.HttpStatus.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mockito.conta.dto.TransacaoDto;
import com.mockito.conta.entity.Conta;
import com.mockito.conta.service.ContaService;

@RestController
@RequestMapping("/conta")
public class ContaController {
	
	
	@Autowired
	private ContaService contaService;
	
	
	@PostMapping
	public ResponseEntity<?> salvarConta(@RequestBody Conta conta) {
		return new ResponseEntity<>(contaService.salvarConta(conta), HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Conta> buscarConta(@PathVariable Long id){
		return contaService.getContafindById(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
				
	}
	
	@GetMapping("/listar")
	public ResponseEntity<List<Conta>> buscarListaConta(){
		return new ResponseEntity<>(contaService.listarContas(), HttpStatus.OK);
	}
	
	@PostMapping("/transferir")
	public ResponseEntity<?> transferir(@RequestBody TransacaoDto dto){
		contaService.transferir(dto.getContaOrigenId(), dto.getContaDestinoId() , dto.getValor(), dto.getBancoId());
	    
		Map<String, Object> response = new HashMap<>();
		response.put("date", LocalDate.now());
		response.put("status", "Ok");
		response.put("menssagem", "Transferencia Realizada com Sucesso");
		response.put("transacao", dto);
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletarConta(@PathVariable Long id) {
	       contaService.deleteById(id);
		return ResponseEntity.noContent().build();
		
	}

}
