package org.example.petshoppoo.model.Servico;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Servico {
    private UUID id;
    private TipoServico tipo;
    private double preco;
    private int duracaoMinutos;
    private String descricaoAdicional;
    private boolean ativo;

    public Servico() {
        this.id = UUID.randomUUID();
        this.ativo = true;
    }

    public Servico(TipoServico tipo) {
        this();
        this.tipo = tipo;
        this.preco = tipo.getPrecoBase();
        this.duracaoMinutos = tipo.getDuracaoMinutos();
        this.descricaoAdicional = tipo.getDetalhes();
    }

    // Getters e Setters
    public UUID getId() { return id; }
    public TipoServico getTipo() { return tipo; }
    public void setTipo(TipoServico tipo) { this.tipo = tipo; }
    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }
    public int getDuracaoMinutos() { return duracaoMinutos; }
    public void setDuracaoMinutos(int duracaoMinutos) { this.duracaoMinutos = duracaoMinutos; }
    public String getDescricaoAdicional() { return descricaoAdicional; }
    public void setDescricaoAdicional(String descricaoAdicional) { this.descricaoAdicional = descricaoAdicional; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}
