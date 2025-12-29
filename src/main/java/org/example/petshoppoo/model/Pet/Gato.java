package org.example.petshoppoo.model.Pet;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Gato extends Pet implements Vacinavel {
    private boolean castrado;
    private List<Vacina> vacinas;

    public Gato() {
        super();
        this.tipo = "Gato";
        this.vacinas = new ArrayList<>();
    }

    public Gato(UUID id, String nome, LocalDate dataNascimento, String raca,
                double peso, UUID idDono, boolean castrado) {
        super(id, nome, dataNascimento, raca, peso, idDono, "Gato");
        this.castrado = castrado;
        this.vacinas = new ArrayList<>();
    }

    @Override
    public void adicionarVacina(String nomeVacina, LocalDate dataAplicacao) {
        vacinas.add(new Vacina(nomeVacina, dataAplicacao));
    }

    @Override
    public List<Vacina> getVacinas() {
        return new ArrayList<>(vacinas);
    }

    @Override
    public boolean isVacinado(String nomeVacina) {
        return vacinas.stream()
                .anyMatch(v -> v.getNome().equalsIgnoreCase(nomeVacina));
    }

    @Override
    public LocalDate getDataUltimaVacina() {
        return vacinas.stream()
                .map(Vacina::getDataAplicacao)
                .max(LocalDate::compareTo)
                .orElse(null);
    }

    public boolean isCastrado() { return castrado; }
    public void setCastrado(boolean castrado) { this.castrado = castrado; }
}
