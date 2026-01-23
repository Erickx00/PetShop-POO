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
    private boolean castrado;
    private boolean adestrado;

    public Pet() {
        this.idPet = UUID.randomUUID();
    }

    public Pet(UUID idPet, String nome, int idadeAnos, String raca, double peso,
               UUID idUsuario, boolean adestrado, boolean castrado) {
        this.idPet = (idPet != null) ? idPet : UUID.randomUUID();
        this.nome = nome;
        this.idadePet = idadeAnos;
        this.raca = raca;
        this.peso = peso;
        this.idUsuario = idUsuario;
        this.adestrado = adestrado;
        this.castrado = castrado;
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

    public int getIdadePet(){ return idadePet;}
    public void setIdadePet(int idadePet) { this.idadePet = idadePet; }

    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }

    public UUID getIdUsuario() { return idUsuario; }
    public void setIdUsuario(UUID idUsuario) { this.idUsuario = idUsuario; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }


    public boolean isCastrado() {
        return castrado;
    }

    public void setCastrado(boolean castrado) {
        this.castrado = castrado;
    }

    public boolean isAdestrado() {
        return adestrado;
    }

    public void setAdestrado(boolean adestrado) {
        this.adestrado = adestrado;
    }
}