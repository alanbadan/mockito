package com.mockito.conta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mockito.conta.entity.Banco;
import com.mockito.conta.service.BancoService;

@RestController
@RequestMapping("/banco")
public class BancoController {
	
	@Autowired
	BancoService bancoService;
	
	
	@PostMapping
	public ResponseEntity<Banco> salvarBanco(@RequestBody Banco banco) {
		return new ResponseEntity<>(bancoService.salvarBanco(banco), HttpStatus.CREATED); 
	}

}
