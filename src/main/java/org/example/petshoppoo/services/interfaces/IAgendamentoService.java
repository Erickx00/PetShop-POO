package org.example.petshoppoo.services.interfaces;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Servico.Agendamento;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface IAgendamentoService {
    void criarAgendamento(UUID idUsuario, UUID idPet, UUID idServico,
                          LocalDateTime dataHora, String observacoes) throws Exception;
    List<Agendamento> listarAgendamentosPorUsuario(UUID idUsuario) throws PersistenciaException;
    List<Agendamento> listarAgendamentosAtivos() throws PersistenciaException;
    List<Agendamento> getCancelados();

    List<LocalDateTime> listarHorariosDisponiveis(LocalDate data, int duracaoMinutos) throws PersistenciaException;
    boolean existeConflitoHorario(LocalDateTime dataHora, int duracaoMinutos) throws PersistenciaException;

    void cancelarAgendamento(Agendamento idAgendamento) throws PersistenciaException;

    void excluirAgendamento(UUID id) throws PersistenciaException;
}