package com.exemplo.seminario.entity;

public class Mensagem { // classe que representa a entidade mensagem

    private Long id; // id da mensagem

    private String conteudo; // conteudo da mensagem

    public Mensagem() { // construtor vazio
    }

    public Mensagem(String conteudo) { // construtor com conteudo
        this.conteudo = conteudo;
    }

    public Long getId() { // metodo para obter o id
        return id;
    }

    public void setId(Long id) { // metodo para definir o id
        this.id = id;
    }

    public String getConteudo() { // metodo para obter o conteudo
        return conteudo;
    }

    public void setConteudo(String conteudo) { // metodo para definir o conteudo
        this.conteudo = conteudo; // define o conteudo
    }
}
