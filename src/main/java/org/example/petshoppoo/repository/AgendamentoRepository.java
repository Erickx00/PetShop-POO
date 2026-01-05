package org.example.petshoppoo.repository;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Servico.Agendamento;
import org.example.petshoppoo.utils.FilePaths;
import org.example.petshoppoo.repository.JsonFileManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class AgendamentoRepository {
    private List<Agendamento> agendamentos;

    public AgendamentoRepository() throws PersistenciaException {
        this.agendamentos = new ArrayList<>();
        carregarAgendamentos();
    }

    private void carregarAgendamentos() throws PersistenciaException {
        this.agendamentos = JsonFileManager.carregar(FilePaths.AGENDAMENTOS_JSON, Agendamento.class);
    }

    public List<Agendamento> getAgendamentos() {
        return new ArrayList<>(agendamentos);
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
        throw new PersistenciaException("Agendamento não encontrado: " + agendamento.getId());
    }

    public void remover(UUID id) throws PersistenciaException {
        agendamentos.removeIf(a -> a.getId().equals(id));
        salvarAgendamentos();
    }

    public Agendamento buscarPorId(UUID id) {
        return agendamentos.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Agendamento> buscarPorUsuario(UUID idUsuario) {
        return agendamentos.stream()
                .filter(a -> a.getIdUsuario().equals(idUsuario))
                .sorted((a1, a2) -> a2.getDataHora().compareTo(a1.getDataHora()))
                .collect(Collectors.toList());
    }

    public List<Agendamento> buscarPorPet(UUID idPet) {
        return agendamentos.stream()
                .filter(a -> a.getIdPet().equals(idPet))
                .sorted((a1, a2) -> a2.getDataHora().compareTo(a1.getDataHora()))
                .collect(Collectors.toList());
    }

    public List<Agendamento> buscarPorServico(UUID idServico) {
        return agendamentos.stream()
                .filter(a -> a.getIdServico().equals(idServico))
                .sorted((a1, a2) -> a2.getDataHora().compareTo(a1.getDataHora()))
                .collect(Collectors.toList());
    }

    public List<Agendamento> buscarPorData(LocalDate data) {
        return agendamentos.stream()
                .filter(a -> a.getDataHora().toLocalDate().equals(data))
                .sorted((a1, a2) -> a1.getDataHora().compareTo(a2.getDataHora()))
                .collect(Collectors.toList());
    }

    public List<Agendamento> buscarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        return agendamentos.stream()
                .filter(a -> {
                    LocalDate dataAgendamento = a.getDataHora().toLocalDate();
                    return !dataAgendamento.isBefore(dataInicio) && !dataAgendamento.isAfter(dataFim);
                })
                .sorted((a1, a2) -> a1.getDataHora().compareTo(a2.getDataHora()))
                .collect(Collectors.toList());
    }

    public List<Agendamento> buscarPorStatus(Agendamento.StatusAgendamento status) {
        return agendamentos.stream()
                .filter(a -> a.getStatus() == status)
                .sorted((a1, a2) -> a1.getDataHora().compareTo(a2.getDataHora()))
                .collect(Collectors.toList());
    }

    public List<Agendamento> buscarAtivos() {
        return agendamentos.stream()
                .filter(Agendamento::isAtivo)
                .sorted((a1, a2) -> a1.getDataHora().compareTo(a2.getDataHora()))
                .collect(Collectors.toList());
    }

    public List<Agendamento> buscarConcluidos() {
        return agendamentos.stream()
                .filter(Agendamento::isConcluido)
                .sorted((a1, a2) -> a2.getDataHora().compareTo(a1.getDataHora()))
                .collect(Collectors.toList());
    }

    public List<Agendamento> buscarAgendadosFuturos() {
        LocalDateTime agora = LocalDateTime.now();
        return agendamentos.stream()
                .filter(a -> a.getDataHora().isAfter(agora) && a.isAtivo())
                .sorted((a1, a2) -> a1.getDataHora().compareTo(a2.getDataHora()))
                .collect(Collectors.toList());
    }

    public boolean existeConflitoHorario(LocalDateTime dataHora, int duracaoMinutos, UUID ignorarAgendamentoId) {
        LocalDateTime inicio = dataHora;
        LocalDateTime fim = dataHora.plusMinutes(duracaoMinutos);

        return agendamentos.stream()
                .filter(a -> a.isAtivo())
                .filter(a -> ignorarAgendamentoId == null || !a.getId().equals(ignorarAgendamentoId))
                .anyMatch(a -> {
                    LocalDateTime inicioExistente = a.getDataHora();
                    LocalDateTime fimExistente = a.getDataHoraFim();
                    return (inicio.isBefore(fimExistente) && fim.isAfter(inicioExistente));
                });
    }

    public boolean existeConflitoHorario(LocalDateTime dataHora, int duracaoMinutos) {
        return existeConflitoHorario(dataHora, duracaoMinutos, null);
    }

    public List<LocalDateTime> getHorariosDisponiveis(LocalDate data, int duracaoMinutos) {
        List<LocalDateTime> horariosDisponiveis = new ArrayList<>();

        // Horário de funcionamento: 8h às 18h
        LocalTime horaInicial = LocalTime.of(8, 0);
        LocalTime horaFinal = LocalTime.of(18, 0);

        // Agendamentos do dia
        List<Agendamento> agendamentosDoDia = buscarPorData(data).stream()
                .filter(a -> a.isAtivo())
                .collect(Collectors.toList());

        LocalTime horaAtual = horaInicial;
        while (horaAtual.plusMinutes(duracaoMinutos).isBefore(horaFinal) ||
                horaAtual.plusMinutes(duracaoMinutos).equals(horaFinal)) {

            LocalDateTime horarioCandidato = LocalDateTime.of(data, horaAtual);

            // Verificar se não conflita com nenhum agendamento
            boolean conflito = agendamentosDoDia.stream()
                    .anyMatch(a -> {
                        LocalDateTime fimCandidato = horarioCandidato.plusMinutes(duracaoMinutos);
                        LocalDateTime fimExistente = a.getDataHoraFim();
                        return (horarioCandidato.isBefore(fimExistente) && fimCandidato.isAfter(a.getDataHora()));
                    });

            if (!conflito) {
                horariosDisponiveis.add(horarioCandidato);
            }

            horaAtual = horaAtual.plusMinutes(30); // Intervalo de 30 minutos
        }

        return horariosDisponiveis;
    }

    public Map<LocalDate, List<Agendamento>> agruparPorData() {
        return agendamentos.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getDataHora().toLocalDate(),
                        TreeMap::new,
                        Collectors.toList()
                ));
    }

    public long contarPorUsuario(UUID idUsuario) {
        return agendamentos.stream()
                .filter(a -> a.getIdUsuario().equals(idUsuario))
                .count();
    }

    public double calcularReceitaTotal() {
        return agendamentos.stream()
                .filter(a -> a.isConcluido())
                .mapToDouble(Agendamento::getValorCobrado)
                .sum();
    }

    public double calcularReceitaPeriodo(LocalDate inicio, LocalDate fim) {
        return agendamentos.stream()
                .filter(a -> a.isConcluido())
                .filter(a -> {
                    LocalDate data = a.getDataHora().toLocalDate();
                    return !data.isBefore(inicio) && !data.isAfter(fim);
                })
                .mapToDouble(Agendamento::getValorCobrado)
                .sum();
    }

    public void concluirAgendamento(UUID idAgendamento) throws PersistenciaException {
        Agendamento agendamento = buscarPorId(idAgendamento);
        if (agendamento != null) {
            agendamento.setStatus(Agendamento.StatusAgendamento.CONCLUIDO);
            agendamento.setDataConclusao(LocalDateTime.now());
            atualizar(agendamento);
        } else {
            throw new PersistenciaException("Agendamento não encontrado: " + idAgendamento);
        }
    }

    public void cancelarAgendamento(UUID idAgendamento) throws PersistenciaException {
        Agendamento agendamento = buscarPorId(idAgendamento);
        if (agendamento != null) {
            agendamento.setStatus(Agendamento.StatusAgendamento.CANCELADO);
            atualizar(agendamento);
        } else {
            throw new PersistenciaException("Agendamento não encontrado: " + idAgendamento);
        }
    }

    public void confirmarAgendamento(UUID idAgendamento) throws PersistenciaException {
        Agendamento agendamento = buscarPorId(idAgendamento);
        if (agendamento != null) {
            agendamento.setStatus(Agendamento.StatusAgendamento.CONFIRMADO);
            atualizar(agendamento);
        } else {
            throw new PersistenciaException("Agendamento não encontrado: " + idAgendamento);
        }
    }

    public void iniciarAgendamento(UUID idAgendamento) throws PersistenciaException {
        Agendamento agendamento = buscarPorId(idAgendamento);
        if (agendamento != null) {
            agendamento.setStatus(Agendamento.StatusAgendamento.EM_ANDAMENTO);
            atualizar(agendamento);
        } else {
            throw new PersistenciaException("Agendamento não encontrado: " + idAgendamento);
        }
    }

    private void salvarAgendamentos() throws PersistenciaException {
        JsonFileManager.salvar(FilePaths.AGENDAMENTOS_JSON, agendamentos);
    }
}