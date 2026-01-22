package org.example.petshoppoo.repository.interfaces;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Servico.Agendamento;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface IAgendamentoRepository extends IRepository<Agendamento> {
    List<Agendamento> buscarPorUsuario(UUID idUsuario);
    List<Agendamento> buscarPorData(LocalDate data);
    List<Agendamento> buscarAtivos();
    boolean existeConflitoHorario(LocalDateTime dataHora, int duracaoMinutos, UUID ignorarAgendamentoId);
    boolean existeConflitoHorario(LocalDateTime dataHora, int duracaoMinutos);
    List<LocalDateTime> getHorariosDisponiveis(LocalDate data, int duracaoMinutos);
    List<Agendamento> getCancelados();
    void cancelarAgendamento(Agendamento agendamento) throws PersistenciaException;

}