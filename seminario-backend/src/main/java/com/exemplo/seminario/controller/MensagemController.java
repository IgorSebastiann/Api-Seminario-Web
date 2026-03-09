package com.exemplo.seminario.controller;

import com.exemplo.seminario.dto.MensagemDto;
import com.exemplo.seminario.entity.Mensagem;
import com.exemplo.seminario.repository.MensagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PutMapping("/{id}")
    public ResponseEntity<Mensagem> atualizar(@PathVariable Long id, @RequestBody MensagemDto dto) {
        return repository.findById(id)
                .map(mensagemExistente -> {
                    mensagemExistente.setConteudo(dto.conteudo());
                    Mensagem atualizada = repository.save(mensagemExistente);
                    return new ResponseEntity<>(atualizada, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
