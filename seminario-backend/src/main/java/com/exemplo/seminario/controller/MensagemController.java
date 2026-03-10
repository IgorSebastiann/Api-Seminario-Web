package com.exemplo.seminario.controller;

import com.exemplo.seminario.dto.MensagemDto; // Importa o DTO de mensagem
import com.exemplo.seminario.entity.Mensagem; // Importa a entidade mensagem
import org.springframework.http.HttpStatus; // Importa o HttpStatus
import org.springframework.http.ResponseEntity; // Importa o ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping; // Importa a anotação DeleteMapping
import org.springframework.web.bind.annotation.GetMapping; // Importa a anotação GetMapping
import org.springframework.web.bind.annotation.PathVariable; // Importa a anotação PathVariable
import org.springframework.web.bind.annotation.PostMapping; // Importa a anotação PostMapping
import org.springframework.web.bind.annotation.PutMapping; // Importa a anotação PutMapping
import org.springframework.web.bind.annotation.RequestBody; // Importa a anotação RequestBody
import org.springframework.web.bind.annotation.RequestMapping; // Importa a anotação RequestMapping
import org.springframework.web.bind.annotation.RestController; // Importa a anotação RestController

import java.util.ArrayList;
import java.util.List; // Importa a classe List
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@RestController //Indica que a classe é um Controller
@RequestMapping("/api/mensagens") // Define o caminho base para as requisições
public class MensagemController {//

    // Substituindo o banco de dados por uma lista na memória
    private final List<Mensagem> mensagensEmMemoria = new ArrayList<>();
    private final AtomicLong contadorId = new AtomicLong(1); // Gerador de IDs automáticos

    @GetMapping // Método GET para listar todas as mensagens
    public List<Mensagem> listarTodas() {
        return mensagensEmMemoria; // Retorna a lista da memória
    }

    @PostMapping // Método POST para criar uma nova mensagem
    public ResponseEntity<Mensagem> criar(@RequestBody MensagemDto dto) {
        Mensagem novaMensagem = new Mensagem(dto.conteudo()); // Cria uma nova mensagem
        novaMensagem.setId(contadorId.getAndIncrement()); // Gera e atribui um ID
        mensagensEmMemoria.add(novaMensagem); // Adiciona na lista
        return new ResponseEntity<>(novaMensagem, HttpStatus.CREATED); // Retorna a nova mensagem
    }

    @PutMapping("/{id}") // Método PUT para atualizar uma mensagem existente
    public ResponseEntity<Mensagem> atualizar(@PathVariable Long id, @RequestBody MensagemDto dto) {
        Optional<Mensagem> mensagemExistente = mensagensEmMemoria.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst();

        if (mensagemExistente.isPresent()) {
            Mensagem mensagem = mensagemExistente.get();
            mensagem.setConteudo(dto.conteudo()); // Atualiza o conteúdo na memória
            return new ResponseEntity<>(mensagem, HttpStatus.OK);
        }
        
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Se não achar na lista
    }

    @DeleteMapping("/{id}") // Método DELETE para deletar uma mensagem existente
    public ResponseEntity<Void> deletar(@PathVariable Long id) { // Se a mensagem existir
        boolean removido = mensagensEmMemoria.removeIf(m -> m.getId().equals(id));
        
        if (removido) { 
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Retorna status NO_CONTENT
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Se a mensagem não existir na lista
    }
}
