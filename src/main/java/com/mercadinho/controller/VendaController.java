package com.mercadinho.controller;

import com.mercadinho.model.Produto;
import com.mercadinho.model.Venda;
import com.mercadinho.repository.ProdutoRepository;
import com.mercadinho.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/vendas")
public class VendaController {

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("vendas", vendaRepository.findAll());
        model.addAttribute("produtos", produtoRepository.findAll());
        model.addAttribute("mensagem", model.getAttribute("mensagem"));
        return "vendas";
    }

    @PostMapping("/salvar")
    public String salvar(Venda venda, Model model) {
        Produto produto = produtoRepository.findById(venda.getProduto().getId()).orElse(null);

        if (produto == null) {
            model.addAttribute("mensagem", "Produto não encontrado!");
            return listar(model);
        }

        if (produto.getQuantidadeEmEstoque() < venda.getQuantidade()) {
            model.addAttribute("mensagem", "Estoque insuficiente para realizar a venda.");
            return listar(model);
        }

        produto.setQuantidadeEmEstoque(produto.getQuantidadeEmEstoque() - venda.getQuantidade());
        produtoRepository.save(produto);

        venda.setData(LocalDateTime.now());
        venda.setValorTotal(produto.getPreco() * venda.getQuantidade());
        vendaRepository.save(venda);

        model.addAttribute("mensagem", "Venda registrada com sucesso!");
        return listar(model);
    }
}
