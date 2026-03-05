package com.exemplo.seminario.controller;

import com.exemplo.seminario.dto.MensagemDto;
import com.exemplo.seminario.entity.Mensagem;
import com.exemplo.seminario.repository.MensagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mensagens")
public class MensagemController {

    private final MensagemRepository repository;

    @Autowired
    public MensagemController(MensagemRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Mensagem> listarTodas() {
        return repository.findAll();
    }

    @PostMapping
    public ResponseEntity<Mensagem> criar(@RequestBody MensagemDto dto) {
        Mensagem novaMensagem = new Mensagem(dto.conteudo());
        Mensagem salva = repository.save(novaMensagem);
        return new ResponseEntity<>(salva, HttpStatus.CREATED);
    }
}
