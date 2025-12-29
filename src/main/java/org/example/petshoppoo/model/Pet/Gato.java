package org.example.petshoppoo.model.Pet;

import java.time.LocalDate;
import java.util.UUID;

public class Gato extends Pet {
    private boolean castrado;

    public Gato() {
        super();
        this.tipo = "Gato";
    }

    public Gato(UUID id, String nome, LocalDate dataNascimento, String raca,
                double peso, UUID idUsuario, boolean castrado) {
        super(id, nome, dataNascimento, raca, peso, idUsuario, "Gato");
        this.castrado = castrado;
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
}