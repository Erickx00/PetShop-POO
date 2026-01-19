package org.example.petshoppoo.services.interfaces;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Servico.Agendamento;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IAgendamentoService {
    void criarAgendamento(UUID idUsuario, UUID idPet, UUID idServico,
                          LocalDateTime dataHora, String observacoes) throws Exception;
    List<Agendamento> listarAgendamentosPorUsuario(UUID idUsuario) throws PersistenciaException;
    List<Agendamento> listarAgendamentosAtivos() throws PersistenciaException;
    List<Agendamento> listarAgendamentosPorData(LocalDate data) throws PersistenciaException;
    List<LocalDateTime> listarHorariosDisponiveis(LocalDate data, int duracaoMinutos) throws PersistenciaException;
    boolean existeConflitoHorario(LocalDateTime dataHora, int duracaoMinutos) throws PersistenciaException;
    Optional<Agendamento> buscarAgendamentoPorId(UUID id) throws PersistenciaException;
    void concluirAgendamento(UUID idAgendamento) throws PersistenciaException;
    void cancelarAgendamento(Agendamento idAgendamento) throws PersistenciaException;
    void confirmarAgendamento(UUID idAgendamento) throws PersistenciaException;
    //void iniciarAgendamento(UUID idAgendamento) throws PersistenciaException;
    List<Agendamento> buscarAgendamentosPorPet(UUID idPet) throws PersistenciaException;
    List<Agendamento> buscarAgendamentosPorServico(UUID idServico) throws PersistenciaException;
    List<Agendamento> buscarAgendamentosPorStatus(Agendamento.StatusAgendamento status) throws PersistenciaException;
    double calcularReceitaTotal() throws PersistenciaException;
    long contarAgendamentosUsuario(UUID idUsuario) throws PersistenciaException;
    void removerAgendamento(UUID idAgendamento) throws PersistenciaException;
    List<Agendamento> listarTodosAgendamentos() throws PersistenciaException;

    void excluirAgendamento(UUID id) throws PersistenciaException;
}