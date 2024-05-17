package com.mockito.conta.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mockito.conta.entity.Conta;

public interface ContaRepository extends JpaRepository<Conta, Long>{

	
	Optional<Conta> findByPessoa(String pessoa);

}
