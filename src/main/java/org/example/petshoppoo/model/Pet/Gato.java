package org.example.petshoppoo.model.Pet;

import com.fasterxml.jackson.annotation.JsonTypeName;

import java.time.LocalDate;
import java.util.UUID;

@JsonTypeName("Gato")
public class Gato extends Pet {
    private boolean castrado;
    private boolean adestrado;

    public Gato() {
        super();
        this.tipo = "Gato";
    }

    public Gato(UUID id, String nome, LocalDate dataNascimento, String raca,
                double peso, UUID idUsuario, boolean castrado, boolean adestrado) {
        super(id, nome, dataNascimento, raca, peso, idUsuario, "Gato");
        this.castrado = castrado;
        this.adestrado = adestrado;
    }

    @Override
    public String getTipo() {
        return "Gato";
    }

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