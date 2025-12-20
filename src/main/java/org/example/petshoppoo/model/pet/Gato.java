package org.example.petshoppoo.model.pet;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Gato extends Pet implements Vacinavel{
    private boolean castrado;
    private List<Vacina> vacinas;

    public Gato(){
        super();
        this.vacinas = new ArrayList<>();
    }

    public Gato(UUID id, String nome, LocalDate dataNascimento, String raca, double peso, UUID idDono, boolean castrado, List<Vacina> vacinas) {
        super(id, nome, dataNascimento, raca, peso, idDono);
        this.castrado = castrado;
        this.vacinas = vacinas;
    }

    @Override
    public String getTipo() {
        return "Gato";
    }

    @Override
    public void adicionarVacina(String nomeVacina, LocalDate dataAplicacao) {
        vacinas.add(new Vacina(nomeVacina, dataAplicacao));
    }

    @Override
    public void adicionarVacina(Vacina vacina) {
        if(vacina != null){
            vacinas.add(vacina);
        }

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

    public boolean isCastrado() {
        return castrado;
    }

    public void setCastrado(boolean castrado) {
        this.castrado = castrado;
    }

    public void setVacinas(List<Vacina> vacinas) {
        this.vacinas = vacinas != null ? vacinas : new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Gato{" +
                "castrado=" + castrado +
                ", vacinas=" + vacinas +
                ", nome='" + nome + '\'' +
                ", raca='" + raca + '\'' +
                ", dataNascimento=" + calcularIdade() + "anos" +
                '}';
    }
}
