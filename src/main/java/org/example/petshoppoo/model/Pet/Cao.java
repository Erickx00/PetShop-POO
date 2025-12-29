package org.example.petshoppoo.model.Pet;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Cao extends Pet implements Vacinavel {
    private boolean adestrado;
    private List<Vacina> vacinas;

    public Cao() {
        super();
        this.tipo = "Cachorro";
        this.vacinas = new ArrayList<>();
    }

    public Cao(UUID id, String nome, LocalDate dataNascimento, String raca,
               double peso, UUID idDono, boolean adestrado) {
        super(id, nome, dataNascimento, raca, peso, idDono, "Cachorro");
        this.adestrado = adestrado;
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

    public boolean isAdestrado() { return adestrado; }
    public void setAdestrado(boolean adestrado) { this.adestrado = adestrado; }
}