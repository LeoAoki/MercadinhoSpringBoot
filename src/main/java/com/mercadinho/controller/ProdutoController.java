package com.mercadinho.controller;

import com.mercadinho.model.Categoria;
import com.mercadinho.model.Produto;
import com.mercadinho.repository.CategoriaRepository;
import com.mercadinho.repository.ProdutoRepository;
import com.mercadinho.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private VendaRepository vendaRepository;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("produtos", produtoRepository.findAll(Sort.by(Sort.Direction.ASC, "id")));
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("produto", new Produto());
        return "produtos";
    }

    @PostMapping("/salvar")
    public String salvar(Produto produto) {
        if (produto.getCategoria() != null && produto.getCategoria().getId() != null) {
            Categoria categoria = categoriaRepository.findById(produto.getCategoria().getId()).orElse(null);
            produto.setCategoria(categoria);
        }
        produtoRepository.save(produto);
        return "redirect:/produtos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("produto", produtoRepository.findById(id).orElse(new Produto()));
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "produtos";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id, Model model) {
        if (!vendaRepository.findByProdutoId(id).isEmpty()) {
            model.addAttribute("mensagem", "Não é possível deletar o produto: ele está associado a uma ou mais vendas.");
            return listar(model);
        }

        produtoRepository.deleteById(id);
        model.addAttribute("mensagem", "Produto deletado com sucesso!");
        return "redirect:/produtos";
    }
}
