package org.example.petshoppoo.model.Pet;

import java.time.LocalDate;
import java.util.List;

public interface Vacinavel {
    void adicionarVacina(String nomeVacina, LocalDate dataAplicacao);
    List<Vacina> getVacinas();
    boolean isVacinado(String string);
    LocalDate getDataUltimaVacina();
}

