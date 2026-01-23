package org.example.petshoppoo.model.Pet;

import com.fasterxml.jackson.annotation.JsonTypeName;

import java.time.LocalDate;
import java.util.UUID;

@JsonTypeName("Gato")
public class Gato extends Pet {

    public Gato() {
        super();
        this.tipo = "Gato"; // Correto
    }


    public Gato(UUID id, String nome, int dataNascimento, String raca,
                double peso, UUID idUsuario, boolean adestrado, boolean castrado) {
        super(id, nome, dataNascimento, raca, peso, idUsuario, adestrado,castrado);

        this.tipo = "Gato";
    }

    @Override
    public String getTipo() {
        return "Gato";
    }


}