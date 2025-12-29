package org.example.petshoppoo.repository;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Servico.Agendamento;
import org.example.petshoppoo.utils.FilePaths;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AgendamentoRepository {
    private List<Agendamento> agendamentos;

    public AgendamentoRepository() throws PersistenciaException {
        carregarAgendamentos();
    }

    private void carregarAgendamentos() throws PersistenciaException {
        this.agendamentos = JsonFileManager.carregar(FilePaths.AGENDAMENTOS_JSON, Agendamento.class);
    }

    public void adicionar(Agendamento agendamento) throws PersistenciaException {
        agendamentos.add(agendamento);
        salvarAgendamentos();
    }

    public void atualizar(Agendamento agendamento) throws PersistenciaException {
        for (int i = 0; i < agendamentos.size(); i++) {
            if (agendamentos.get(i).getId().equals(agendamento.getId())) {
                agendamentos.set(i, agendamento);
                salvarAgendamentos();
                return;
            }
        }
    }

    public List<Agendamento> buscarPorUsuario(UUID idUsuario) {
        return agendamentos.stream()
                .filter(a -> a.getIdUsuario().equals(idUsuario))
                .sorted((a1, a2) -> a2.getDataHora().compareTo(a1.getDataHora()))
                .collect(Collectors.toList());
    }

    public List<Agendamento> buscarPorData(LocalDate data) {
        return agendamentos.stream()
                .filter(a -> a.getDataHora().toLocalDate().equals(data))
                .collect(Collectors.toList());
    }

    public boolean existeConflitoHorario(LocalDateTime dataHora, int duracaoMinutos) {
        LocalDateTime inicio = dataHora;
        LocalDateTime fim = dataHora.plusMinutes(duracaoMinutos);

        return agendamentos.stream()
                .filter(a -> a.getStatus() != Agendamento.StatusAgendamento.CANCELADO)
                .anyMatch(a -> {
                    LocalDateTime inicioExistente = a.getDataHora();
                    LocalDateTime fimExistente = a.getDataHora().plusMinutes(duracaoMinutos);
                    return (inicio.isBefore(fimExistente) && fim.isAfter(inicioExistente));
                });
    }

    private void salvarAgendamentos() throws PersistenciaException {
        JsonFileManager.salvar(FilePaths.AGENDAMENTOS_JSON, agendamentos);
    }
}
