package org.example.petshoppoo.model.Pet;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.UUID;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipo" // O Jackson vai procurar/criar esse campo no JSON
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Cachorro.class, name = "Cachorro"),
        @JsonSubTypes.Type(value = Gato.class, name = "Gato")
})
public abstract class Pet {
    protected UUID idPet;
    protected String nome;
    protected String raca;
    protected int idadePet;
    protected double peso;
    protected UUID idUsuario;
    protected String tipo;

    public Pet() {
        this.idPet = UUID.randomUUID();
    }

    public Pet(UUID idPet, String nome, int idadePet, String raca,
               double peso, UUID idUsuario, String tipo) {
        this.idPet = idPet == null ? UUID.randomUUID() : idPet;
        this.nome = nome;
        this.idadePet = idadePet;
        this.raca = raca;
        this.peso = peso;
        this.idUsuario = idUsuario;
        this.tipo = tipo;
    }

    public String idadeFormatada() {
        return idadePet + " Anos";
    }

    public UUID getIdPet() { return idPet; }
    public void setIdPet(UUID idPet) { this.idPet = idPet; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getRaca() { return raca; }
    public void setRaca(String raca) { this.raca = raca; }

    public void setIdadePet(int idadePet) { this.idadePet = idadePet; }

    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }

    public UUID getIdUsuario() { return idUsuario; }
    public void setIdUsuario(UUID idUsuario) { this.idUsuario = idUsuario; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

}