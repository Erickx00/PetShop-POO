package org.example.petshoppoo.repository.interfaces;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Servico.Agendamento;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface IAgendamentoRepository extends IRepository<Agendamento> {
    List<Agendamento> buscarPorUsuario(UUID idUsuario);
    List<Agendamento> buscarPorPet(UUID idPet);
    List<Agendamento> buscarPorServico(UUID idServico);
    List<Agendamento> buscarPorData(LocalDate data);
    List<Agendamento> buscarPorStatus(Agendamento.StatusAgendamento status);
    List<Agendamento> buscarAtivos();
    List<Agendamento> buscarConcluidos();
    boolean existeConflitoHorario(LocalDateTime dataHora, int duracaoMinutos, UUID ignorarAgendamentoId);
    boolean existeConflitoHorario(LocalDateTime dataHora, int duracaoMinutos);
    List<LocalDateTime> getHorariosDisponiveis(LocalDate data, int duracaoMinutos);
    double calcularReceitaTotal();
    void concluirAgendamento(UUID idAgendamento) throws PersistenciaException;
    void cancelarAgendamento(Agendamento agendamento) throws PersistenciaException;
    void confirmarAgendamento(UUID idAgendamento) throws PersistenciaException;
    //void iniciarAgendamento(UUID idAgendamento) throws PersistenciaException;
}