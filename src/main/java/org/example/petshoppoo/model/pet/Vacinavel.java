package org.example.petshoppoo.model.pet;

import java.time.LocalDate;
import java.util.List;

public interface Vacinavel {
    void adicionarVacina(String nomeVacina, LocalDate dataAplicacao);
    void adicionarVacina(Vacina vacina);
    List<Vacina> getVacinas();
    boolean isVacinado(String string);
    LocalDate getDataUltimaVacina();
}

