package org.example.petshoppoo.model.Pet;

import com.fasterxml.jackson.annotation.JsonTypeName;

import java.time.LocalDate;
import java.util.UUID;

@JsonTypeName("Cachorro")
public class Cachorro extends Pet {
    private boolean adestrado;
    private boolean castrado;

    public Cachorro() {
        super();
        this.tipo = "Cachorro";
    }

    public Cachorro(UUID id, String nome, int dataNascimento, String raca,
                    double peso, UUID idUsuario, boolean adestrado,boolean castrado) {
        super(id, nome, dataNascimento, raca, peso, idUsuario, "Cachorro");
        this.adestrado = adestrado;
        this.castrado = castrado;
    }

    @Override
    public String getTipo() {
        return "Cachorro";
    }

    public boolean isAdestrado() {
        return adestrado;
    }

    public void setAdestrado(boolean adestrado) {
        this.adestrado = adestrado;
    }

    public boolean isCastrado() {
        return castrado;
    }

    public void setCastrado(boolean castrado) {
        this.castrado = castrado;
    }
}