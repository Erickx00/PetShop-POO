package org.example.petshoppoo.model.Pet;

import java.time.LocalDate;
import java.util.UUID;

public class Cachorro extends Pet {
    private boolean adestrado;

    public Cachorro() {
        super();
        this.tipo = "Cachorro";
    }

    public Cachorro(UUID id, String nome, LocalDate dataNascimento, String raca,
                    double peso, UUID idUsuario, boolean adestrado) {
        super(id, nome, dataNascimento, raca, peso, idUsuario, "Cachorro");
        this.adestrado = adestrado;
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
}