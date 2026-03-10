package com.exemplo.seminario.entity;

public class Mensagem {

    private Long id;

    private String conteudo;

    public Mensagem() { //
    }

    public Mensagem(String conteudo) {
        this.conteudo = conteudo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }
}
