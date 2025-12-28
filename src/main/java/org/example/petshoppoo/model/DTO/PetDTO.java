package org.example.petshoppoo.model.DTO;

import java.time.LocalDate;
import java.util.UUID;

public class PetDTO {
    private UUID id;
    private String nome;
    private String tipo;
    private String raca;
    private int idade;
    private double peso;
    private UUID idDono;
    private String nomeDono;
    private boolean adestrado;
    private boolean castrado;

    public PetDTO() {}

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getRaca() { return raca; }
    public void setRaca(String raca) { this.raca = raca; }
    public int getIdade() { return idade; }
    public void setIdade(int idade) { this.idade = idade; }
    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }
    public UUID getIdDono() { return idDono; }
    public void setIdDono(UUID idDono) { this.idDono = idDono; }
    public String getNomeDono() { return nomeDono; }
    public void setNomeDono(String nomeDono) { this.nomeDono = nomeDono; }
    public boolean isAdestrado() { return adestrado; }
    public void setAdestrado(boolean adestrado) { this.adestrado = adestrado; }
    public boolean isCastrado() { return castrado; }
    public void setCastrado(boolean castrado) { this.castrado = castrado; }
}