package com.mockito.conta.serviceimpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mockito.conta.entity.Banco;
import com.mockito.conta.entity.Conta;
import com.mockito.conta.repository.BancoRepository;
import com.mockito.conta.repository.ContaRepository;
import com.mockito.conta.service.ContaService;



@Service
public class ContaServiceImpl implements ContaService{
	
	private ContaRepository contaRepository;
	private BancoRepository bancoRepository;
	

	public ContaServiceImpl(ContaRepository contaRepository, BancoRepository bancoRepository) {
		super();
		this.contaRepository = contaRepository;
		this.bancoRepository = bancoRepository;
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public Conta salvarConta(Conta conta) {
		return contaRepository.save(conta);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Conta> getContafindById(Long id) {		
		return contaRepository.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Integer revisarTotalTransferencia(Long bancoId) {
		Banco banco = bancoRepository.findById(bancoId).orElseThrow();
		return banco.getTotalTransferencia();
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal revisarSaldo(Long contaId) {
		Conta conta = contaRepository.findById(contaId).orElseThrow();
		return conta.getSaldo();
	}

	@Override
	@Transactional
	public void transferir(Long numContaOrigem, Long numContaDestino, BigDecimal valor, Long bancoId) {
		
        saldoSuficiente(numContaOrigem, valor);
        creditoConta(numContaDestino, valor);
        totalTransferencia(bancoId);
        
        		
	}
	
	@Override
	public Banco totalTransferencia(Long idBanco) {
		 
		Banco banco = bancoRepository.findById(idBanco).orElseThrow(); 
        Integer totalTransferencia = banco.getTotalTransferencia();
        banco.setTotalTransferencia(++totalTransferencia);
        bancoRepository.save(banco);
        return banco;
	}
	
	@Override
	public Conta saldoSuficiente(Long numContaOrigem, BigDecimal valor) {
		
		Conta contaOrigem = contaRepository.findById(numContaOrigem).orElseThrow();
        contaOrigem.debito(valor);
        contaRepository.save(contaOrigem);
        return contaOrigem;
	}
	
	@Override
	public Conta creditoConta(Long numContaDestino, BigDecimal valor) {
		
		Conta contaDestino = contaRepository.findById(numContaDestino).orElseThrow();
        contaDestino.credito(valor);
        contaRepository.save(contaDestino);
        return contaDestino;
	}


	@Override
	public List<Conta> listarContas() {
		return contaRepository.findAll();
	}


	@Override
	public void deleteById(Long id) {
		contaRepository.deleteById(id);
		
	}

	

}
