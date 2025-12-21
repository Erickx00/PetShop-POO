package org.example.petshoppoo.model.Pet;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public abstract class Pet implements Serializable {
    protected UUID id;
    protected String nome;
    protected LocalDate dataNascimento;
    protected String raca;
    protected double peso;
    protected UUID idDono;

    public Pet() {
        this.id = UUID.randomUUID();
    }

    public Pet(UUID id, String nome, LocalDate dataNascimento, String raca, double peso, UUID idDono) {
        this.id = id == null ? UUID.randomUUID() : id;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.raca = raca;
        this.peso = peso;
        this.idDono = idDono;
    }

    // Método abstrato - polimorfismo
    public abstract String getTipo();


    public int calcularIdade() {
        if (dataNascimento == null) return 0;
        return LocalDate.now().getYear() - dataNascimento.getYear();
    }

    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public UUID getIdDono() {
        return idDono;
    }

    public void setIdDono(UUID idDono) {
        this.idDono = idDono;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pet pet = (Pet) o;
        return Objects.equals(id, pet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Pet{" +
                ", nome='" + nome + '\'' +
                ", tipo='" + getTipo() + '\'' +
                ", idade=" + calcularIdade() + " anos" +
                '}';
    }
}