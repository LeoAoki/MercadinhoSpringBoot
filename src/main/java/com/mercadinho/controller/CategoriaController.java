package com.mercadinho.controller;

import com.mercadinho.model.Categoria;
import com.mercadinho.repository.CategoriaRepository;
import com.mercadinho.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("categoria", new Categoria());
        model.addAttribute("mensagem", model.getAttribute("mensagem"));
        return "categorias";
    }

    @PostMapping("/salvar")
    public String salvar(Categoria categoria) {
        categoriaRepository.save(categoria);
        return "redirect:/categorias";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("categoria", categoriaRepository.findById(id).orElse(new Categoria()));
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "categorias";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id, Model model) {
        if (!produtoRepository.findByCategoriaId(id).isEmpty()) {
            model.addAttribute("mensagem", "Não é possível deletar a categoria: ela está sendo usada por produtos.");
            return listar(model);
        }
        categoriaRepository.deleteById(id);
        return "redirect:/categorias";
    }
}

