package com.br.sccon.gerenciador.pessoas.service.domain;

import java.time.ZonedDateTime;

public class Pessoa {

    private Long id;
    private String nome;
    private ZonedDateTime dataNascimento;
    private ZonedDateTime dataAdmissao;

    public Pessoa(Long id, String nome, ZonedDateTime dataNascimento, ZonedDateTime dataAdmissao) {
        this.id = id;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.dataAdmissao = dataAdmissao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ZonedDateTime getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(ZonedDateTime dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public ZonedDateTime getDataAdmissao() {
        return dataAdmissao;
    }

    public void setDataAdmissao(ZonedDateTime dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }
}
