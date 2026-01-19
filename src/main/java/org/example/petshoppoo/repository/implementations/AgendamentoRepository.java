package org.example.petshoppoo.repository.implementations;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Servico.Agendamento;
import org.example.petshoppoo.repository.interfaces.IAgendamentoRepository;
import org.example.petshoppoo.utils.FilePaths;
import org.example.petshoppoo.utils.JsonFileManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class AgendamentoRepository implements IAgendamentoRepository {
    private List<Agendamento> agendamentos;

    public AgendamentoRepository() {
        carregarDados();
    }

    private void carregarDados() {
        try {
            agendamentos = JsonFileManager.carregar(FilePaths.AGENDAMENTOS_JSON, Agendamento.class);
            if (agendamentos == null) {
                agendamentos = new ArrayList<>();
            }
        } catch (Exception e) {
            agendamentos = new ArrayList<>();
            // Se o arquivo não existir ou der erro, inicia lista vazia para não travar o sistema
            System.out.println("Aviso: Iniciando lista de agendamentos vazia.");
        }
    }

    public void salvar(Agendamento agendamento) throws PersistenciaException {
        agendamentos.add(agendamento);
        salvarAgendamentos();
    }

    public void atualizar(Agendamento agendamento) throws PersistenciaException {
        for (int i = 0; i < agendamentos.size(); i++) {
            if (agendamentos.get(i).getId().equals(agendamento.getId())) {
                agendamentos.set(i, agendamento);
                break;
            }
        }
        salvarAgendamentos();
    }

    @Override
    public void deletar(UUID id) throws PersistenciaException {
        agendamentos.stream()
                .filter(agendamento -> agendamento.getId().equals(id)) // Filtra pelo ID
                .findFirst() // Pega o primeiro (e único) encontrado
                .ifPresent(agendamentoParaRemover -> { // Se encontrar...
                    // 2. Remove da coleção original
                    agendamentos.remove(agendamentoParaRemover);
                });
        salvarAgendamentos();
    }

    /* --- ESSE ERA O MÉTODO QUE FALTAVA ---
    public void deletar(Agendamento agendamento) throws PersistenciaException {
        if (agendamento != null) {
            // Chama o método remover passando o ID do objeto
            remover(agendamento.getId());
        }
    }
    // -------------------------------------

    public void remover(UUID id) throws PersistenciaException {
        boolean removido = agendamentos.removeIf(a -> a.getId().equals(id));

        // Só grava no arquivo se realmente removeu alguém, pra evitar I/O desnecessário
        if (removido) {
            salvarAgendamentos();
        }
    }*/

    public void salvarAgendamentos() throws PersistenciaException {
        JsonFileManager.salvar(FilePaths.AGENDAMENTOS_JSON, agendamentos);
    }

    public List<Agendamento> listarTodos() {
        return new ArrayList<>(agendamentos);
    }

    @Override
    public Optional<Agendamento> buscarPorId(UUID id) {
        return Optional.ofNullable(agendamentos.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null));
    }

    public List<Agendamento> buscarPorUsuario(UUID idUsuario) {
        return agendamentos.stream()
                .filter(a -> a.getIdUsuario().equals(idUsuario))
                .collect(Collectors.toList());
    }

    public List<Agendamento> buscarPorPet(UUID idPet) {
        return agendamentos.stream()
                .filter(a -> a.getIdPet().equals(idPet))
                .collect(Collectors.toList());
    }

    @Override
    public List<Agendamento> buscarPorServico(UUID idServico) {
        return agendamentos.stream()
                .filter(a -> a.getIdServico().equals(idServico))
                .sorted((a1, a2) -> a2.getDataHora().compareTo(a1.getDataHora()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Agendamento> buscarPorData(LocalDate data) {
        return agendamentos.stream()
                .filter(a -> a.getDataHora().toLocalDate().equals(data))
                .sorted((a1, a2) -> a1.getDataHora().compareTo(a2.getDataHora()))
                .collect(Collectors.toList());
    }


    @Override
    public List<Agendamento> buscarPorStatus(Agendamento.StatusAgendamento status) {
        return agendamentos.stream()
                .filter(a -> a.getStatus() == status)
                .sorted((a1, a2) -> a1.getDataHora().compareTo(a2.getDataHora()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Agendamento> buscarAtivos() {
        return agendamentos.stream()
                .filter(Agendamento::isAtivo)
                .sorted((a1, a2) -> a1.getDataHora().compareTo(a2.getDataHora()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Agendamento> buscarConcluidos() {
        return agendamentos.stream()
                .filter(Agendamento::isConcluido)
                .sorted((a1, a2) -> a2.getDataHora().compareTo(a1.getDataHora()))
                .collect(Collectors.toList());
    }



    @Override
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

    @Override
    public boolean existeConflitoHorario(LocalDateTime dataHora, int duracaoMinutos) {
        return existeConflitoHorario(dataHora, duracaoMinutos, null);
    }

    @Override
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



    @Override
    public double calcularReceitaTotal() {
        return agendamentos.stream()
                .filter(a -> a.isConcluido())
                .mapToDouble(Agendamento::getValorCobrado)
                .sum();
    }


    @Override
    public void concluirAgendamento(UUID idAgendamento) throws PersistenciaException {
        Agendamento agendamento = buscarPorId(idAgendamento).orElse(null);
        if (agendamento != null) {
            agendamento.setStatus(Agendamento.StatusAgendamento.CONCLUIDO);
            agendamento.setDataConclusao(LocalDateTime.now());
            atualizar(agendamento);
        } else {
            throw new PersistenciaException("Agendamento não encontrado: " + idAgendamento);
        }
        salvarAgendamentos();
    }

    @Override
    public void cancelarAgendamento(Agendamento agendamento) throws PersistenciaException {
        if (agendamento != null) {
            agendamento.setStatus(Agendamento.StatusAgendamento.CANCELADO);
            atualizar(agendamento);
        } else {
            throw new PersistenciaException("Agendamento não encontrado: " + agendamento);
        }
        salvarAgendamentos();
    }

    @Override
    public void confirmarAgendamento(UUID idAgendamento) throws PersistenciaException {
        Agendamento agendamento = buscarPorId(idAgendamento).orElse(null);
        if (agendamento != null) {
            agendamento.setStatus(Agendamento.StatusAgendamento.AGENDADO);
            atualizar(agendamento);
        } else {
            throw new PersistenciaException("Agendamento não encontrado: " + idAgendamento);
        }
        salvarAgendamentos();
    }

    /*
    @Override
    public void iniciarAgendamento(UUID idAgendamento) throws PersistenciaException {
        Agendamento agendamento = buscarPorId(idAgendamento).orElse(null);
        if (agendamento != null) {
            agendamento.setStatus(Agendamento.StatusAgendamento.EM_ANDAMENTO);
            atualizar(agendamento);
        } else {
            throw new PersistenciaException("Agendamento não encontrado: " + idAgendamento);
        }
    }*/
}