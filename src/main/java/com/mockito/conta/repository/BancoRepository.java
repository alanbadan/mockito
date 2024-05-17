package com.mockito.conta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mockito.conta.entity.Banco;

public interface BancoRepository extends JpaRepository<Banco, Long>{
	
	

}
