package org.example.petshoppoo.services;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Servico.Agendamento;
import org.example.petshoppoo.model.Servico.Servico;
import org.example.petshoppoo.repository.AgendamentoRepository;
import org.example.petshoppoo.repository.ServicoRepository;
import org.example.petshoppoo.repository.PetRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AgendamentoService {
    private final AgendamentoRepository agendamentoRepository;
    private final ServicoRepository servicoRepository;
    private final PetRepository petRepository;

    public AgendamentoService() throws PersistenciaException {
        this.agendamentoRepository = new AgendamentoRepository();
        this.servicoRepository = new ServicoRepository();
        this.petRepository = new PetRepository();
    }

    /**
     * Agendar um novo serviço para um pet
     */
    public Agendamento agendar(UUID idPet, UUID idServico, UUID idUsuario,
                               LocalDateTime dataHora, String observacoes) throws PersistenciaException {

        // Validar se pet existe
        if (petRepository.buscarPorId(idPet).isEmpty()) {
            throw new PersistenciaException("Pet não encontrado");
        }

        // Validar serviço
        Servico servico = servicoRepository.buscarPorId(idServico);
        if (servico == null) {
            throw new PersistenciaException("Serviço não encontrado");
        }

        // Validar data/hora
        if (dataHora.isBefore(LocalDateTime.now())) {
            throw new PersistenciaException("Data/hora deve ser futura");
        }

        // Verificar conflito de horário
        if (agendamentoRepository.existeConflitoHorario(dataHora, servico.getDuracaoMinutos())) {
            throw new PersistenciaException("Horário indisponível. Escolha outro horário.");
        }

        // Criar agendamento - CORRIGIDO: adicionar duraçãoMinutos
        Agendamento agendamento = new Agendamento(
                idPet,
                idServico,
                idUsuario,
                dataHora,
                observacoes,
                servico.getDuracaoMinutos() // Adicionar duração
        );

        agendamento.setValorCobrado(servico.getPreco());

        // Salvar
        agendamentoRepository.adicionar(agendamento);
        return agendamento;
    }

    /**
     * Listar todos os agendamentos de um usuário
     */
    public List<Agendamento> listarAgendamentosUsuario(UUID idUsuario) throws PersistenciaException {
        return agendamentoRepository.buscarPorUsuario(idUsuario);
    }

    /**
     * Listar agendamentos ativos (não cancelados ou concluídos)
     */
    public List<Agendamento> listarAgendamentosAtivos() throws PersistenciaException {
        return agendamentoRepository.buscarAtivos();
    }

    /**
     * Listar agendamentos futuros
     */
    public List<Agendamento> listarAgendamentosFuturos() throws PersistenciaException {
        return agendamentoRepository.buscarAgendadosFuturos();
    }

    /**
     * Listar agendamentos por data específica
     */
    public List<Agendamento> listarAgendamentosPorData(LocalDate data) throws PersistenciaException {
        return agendamentoRepository.buscarPorData(data);
    }

    /**
     * Listar horários disponíveis para uma data e duração
     */
    public List<LocalDateTime> listarHorariosDisponiveis(LocalDate data, int duracaoMinutos) throws PersistenciaException {
        return agendamentoRepository.getHorariosDisponiveis(data, duracaoMinutos);
    }

    /**
     * Verificar se há conflito de horário
     */
    public boolean existeConflitoHorario(LocalDateTime dataHora, int duracaoMinutos) throws PersistenciaException {
        return agendamentoRepository.existeConflitoHorario(dataHora, duracaoMinutos);
    }

    /**
     * Buscar agendamento por ID
     */
    public Agendamento buscarAgendamentoPorId(UUID id) throws PersistenciaException {
        return agendamentoRepository.buscarPorId(id);
    }

    /**
     * Buscar agendamentos por período
     */
    public List<Agendamento> buscarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) throws PersistenciaException {
        return agendamentoRepository.buscarPorPeriodo(dataInicio, dataFim);
    }

    /**
     * Concluir um agendamento
     */
    public void concluirAgendamento(UUID idAgendamento) throws PersistenciaException {
        agendamentoRepository.concluirAgendamento(idAgendamento);
    }

    /**
     * Cancelar um agendamento
     */
    public void cancelarAgendamento(UUID idAgendamento) throws PersistenciaException {
        agendamentoRepository.cancelarAgendamento(idAgendamento);
    }

    /**
     * Confirmar um agendamento
     */
    public void confirmarAgendamento(UUID idAgendamento) throws PersistenciaException {
        agendamentoRepository.confirmarAgendamento(idAgendamento);
    }

    /**
     * Iniciar um agendamento (em andamento)
     */
    public void iniciarAgendamento(UUID idAgendamento) throws PersistenciaException {
        agendamentoRepository.iniciarAgendamento(idAgendamento);
    }

    /**
     * Atualizar status de um agendamento
     */
    public void atualizarStatus(UUID idAgendamento, Agendamento.StatusAgendamento status) throws PersistenciaException {
        Agendamento agendamento = agendamentoRepository.buscarPorId(idAgendamento);
        if (agendamento == null) {
            throw new PersistenciaException("Agendamento não encontrado");
        }

        agendamento.setStatus(status);

        if (status == Agendamento.StatusAgendamento.CONCLUIDO) {
            agendamento.setDataConclusao(LocalDateTime.now());
        }

        agendamentoRepository.atualizar(agendamento);
    }

    /**
     * Atualizar observações de um agendamento
     */
    public void atualizarObservacoes(UUID idAgendamento, String observacoes) throws PersistenciaException {
        Agendamento agendamento = agendamentoRepository.buscarPorId(idAgendamento);
        if (agendamento != null) {
            agendamento.setObservacoes(observacoes);
            agendamentoRepository.atualizar(agendamento);
        }
    }

    /**
     * Buscar agendamentos por pet
     */
    public List<Agendamento> buscarAgendamentosPorPet(UUID idPet) throws PersistenciaException {
        return agendamentoRepository.buscarPorPet(idPet);
    }

    /**
     * Buscar agendamentos por serviço
     */
    public List<Agendamento> buscarAgendamentosPorServico(UUID idServico) throws PersistenciaException {
        return agendamentoRepository.buscarPorServico(idServico);
    }

    /**
     * Buscar agendamentos por status
     */
    public List<Agendamento> buscarAgendamentosPorStatus(Agendamento.StatusAgendamento status) throws PersistenciaException {
        return agendamentoRepository.buscarPorStatus(status);
    }

    /**
     * Agrupar agendamentos por data
     */
    public Map<LocalDate, List<Agendamento>> agruparAgendamentosPorData() throws PersistenciaException {
        return agendamentoRepository.agruparPorData();
    }

    /**
     * Calcular receita total de todos os agendamentos concluídos
     */
    public double calcularReceitaTotal() throws PersistenciaException {
        return agendamentoRepository.calcularReceitaTotal();
    }

    /**
     * Calcular receita de um período
     */
    public double calcularReceitaPeriodo(LocalDate inicio, LocalDate fim) throws PersistenciaException {
        return agendamentoRepository.calcularReceitaPeriodo(inicio, fim);
    }

    /**
     * Contar agendamentos de um usuário
     */
    public long contarAgendamentosUsuario(UUID idUsuario) throws PersistenciaException {
        return agendamentoRepository.contarPorUsuario(idUsuario);
    }

    /**
     * Verificar se um agendamento pertence a um usuário
     */
    public boolean agendamentoPertenceAoUsuario(UUID idAgendamento, UUID idUsuario) throws PersistenciaException {
        Agendamento agendamento = buscarAgendamentoPorId(idAgendamento);
        return agendamento != null && agendamento.getIdUsuario().equals(idUsuario);
    }

    /**
     * Remover um agendamento (apenas admin)
     */
    public void removerAgendamento(UUID idAgendamento) throws PersistenciaException {
        agendamentoRepository.remover(idAgendamento);
    }

    /**
     * Obter todos os agendamentos
     */
    public List<Agendamento> listarTodosAgendamentos() throws PersistenciaException {
        return agendamentoRepository.getAgendamentos();
    }

    /**
     * Método auxiliar para buscar por ID (mantido para compatibilidade)
     */
    private Agendamento buscarPorId(UUID idAgendamento) throws PersistenciaException {
        return agendamentoRepository.buscarPorId(idAgendamento);
    }
}