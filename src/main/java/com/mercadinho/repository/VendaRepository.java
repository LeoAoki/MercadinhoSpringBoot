package com.mercadinho.repository;

import com.mercadinho.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VendaRepository extends JpaRepository<Venda, Long> {
    List<Venda> findByProdutoId(Long produtoId);
}
