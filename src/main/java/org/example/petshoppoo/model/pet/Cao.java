package org.example.petshoppoo.model.pet;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Cao extends Pet implements Vacinavel{

    private boolean adestrado;
    private List<Vacina> vacinas;

    public Cao(){
        super();
        this.vacinas = new ArrayList<>();

    }

    public Cao(UUID id, String nome, LocalDate dataNascimento, String raca, double peso, UUID idDono, boolean adestrado, List<Vacina> vacinas) {
        super(id, nome, dataNascimento, raca, peso, idDono);
        this.adestrado = adestrado;
        this.vacinas = vacinas;
    }

    @Override
    public String getTipo() {
        return "Cao";
    }

    public void adicionarVacina(String nomeVacina, LocalDate dataAplicacao){
        vacinas.add(new Vacina(nomeVacina, dataAplicacao));

    }

    public void adicionarVacina(Vacina vacina){
        if(vacina != null){
            vacinas.add(vacina);
        }
    }

    public List<Vacina> getVacinas(){
        return new ArrayList<>(vacinas);
    }


    public boolean isVacinado(String nomeVacina) {
        return vacinas.stream()
                .anyMatch(v -> v.getNome().equalsIgnoreCase(nomeVacina));
    }


    public LocalDate getDataUltimaVacina() {
        return vacinas.stream()
                .map(Vacina::getDataAplicacao)
                .max(LocalDate::compareTo)
                .orElse(null);
    }


    public void setVacinas(List<Vacina> vacinas) {
        this.vacinas = vacinas != null ? vacinas : new ArrayList<>();
    }


    public boolean isAdestrado() {
        return adestrado;
    }

    public void setAdestrado(boolean adestrado) {
        this.adestrado = adestrado;
    }

    @Override
    public String toString() {
        return "Cao{" +
                "nome='" + nome + '\'' +
                ", raca='" + raca + '\'' +
                ", adestrado=" + adestrado +
                ", dataNascimento=" + calcularIdade() + "anos" +
                ", vacinas=" + vacinas.size() +
                '}';
    }
}
