package org.example.petshoppoo.model.Pet;

import com.fasterxml.jackson.annotation.JsonTypeName;

import java.time.LocalDate;
import java.util.UUID;

@JsonTypeName("Cachorro")
public class Cachorro extends Pet {

    public Cachorro() {
        super();
        this.tipo = "Cachorro"; // Correto
    }


    public Cachorro(UUID idPet, String nome, int idadeAnos, String raca, double peso,
                    UUID idUsuario, boolean adestrado, boolean castrado) {
        super(idPet, nome, idadeAnos, raca, peso, idUsuario, adestrado, castrado);
        this.tipo = "Cachorro";
    }

    @Override
    public String getTipo() {
        return "Cachorro";
    }

}