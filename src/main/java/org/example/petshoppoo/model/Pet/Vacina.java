package org.example.petshoppoo.model.Pet;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public class Vacina implements Serializable {
    private UUID id;
    private  String nome;
    private LocalDate dataAplicacao;
    private String lote;
    private String veterinario;

    public Vacina(){
        this.id = UUID.randomUUID();
    }

    public Vacina(String nome,LocalDate dataAplicacao){
        this.id = id == null ? UUID.randomUUID() : id;
        this.nome = nome;
        this.dataAplicacao = dataAplicacao;
        this.lote = lote;
        this.veterinario = veterinario;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataAplicacao() {
        return dataAplicacao;
    }

    public void setDataAplicacao(LocalDate dataAplicacao) {
        this.dataAplicacao = dataAplicacao;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(String veterinario) {
        this.veterinario = veterinario;
    }

    @Override
    public String toString() {
        return nome + "-" + dataAplicacao;
    }
}
