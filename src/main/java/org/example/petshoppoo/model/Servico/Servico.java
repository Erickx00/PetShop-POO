package org.example.petshoppoo.model.Servico;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Servico{
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

    public Servico(UUID id, TipoServico tipo, double preco, int duracaoMinutos, String descricaoAdicional) {
        this.id = id == null ? UUID.randomUUID() : id;
        this.tipo = tipo;
        this.preco = preco;
        this.duracaoMinutos = duracaoMinutos;
        this.descricaoAdicional = descricaoAdicional;
        this.ativo = true;
    }

    public Servico(Servico servico){
        this.id = UUID.randomUUID();
        this.tipo = tipo;
        this.preco = tipo.getPrecoBase();
        this.duracaoMinutos = tipo.getDuracaoMinutos();
        this.ativo = true;
    }

    public UUID getId() {
        return id;
    }

    public TipoServico getTipo() {
        return tipo;
    }

    public void setTipo(TipoServico tipo) {
        this.tipo = tipo;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getDuracaoMinutos() {
        return duracaoMinutos;
    }

    public void setDuracaoMinutos(int duracaoMinutos) {
        this.duracaoMinutos = duracaoMinutos;
    }

    public String getDescricaoAdicional() {
        return descricaoAdicional;
    }

    public void setDescricaoAdicional(String descricaoAdicional) {
        this.descricaoAdicional = descricaoAdicional;
    }

    public boolean isAtivo() {
        return ativo; }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Servico servico = (Servico) o;
        return Objects.equals(id, servico.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Servico{" +
                "tipo=" + tipo.getDescricao() +
                ", preco=R$ " + String.format("%.2f", preco) +
                ", duracao=" + duracaoMinutos + "min" +
                '}';
    }
}
