package com.mockito.conta.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mockito.conta.entity.Banco;
import com.mockito.conta.repository.BancoRepository;
import com.mockito.conta.service.BancoService;

@Service
public class BancoServiceImpl implements BancoService{
	
	@Autowired
	BancoRepository bancoRepository;

	@Override
	public Banco salvarBanco(Banco banco) {
		
		return bancoRepository.save(banco);
	}

}
