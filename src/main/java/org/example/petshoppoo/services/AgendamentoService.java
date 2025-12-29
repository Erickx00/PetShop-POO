package org.example.petshoppoo.services;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Servico.Agendamento;
import org.example.petshoppoo.model.Servico.Servico;
import org.example.petshoppoo.repository.AgendamentoRepository;
import org.example.petshoppoo.repository.ServicoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class AgendamentoService {
    private final AgendamentoRepository agendamentoRepository;
    private final ServicoRepository servicoRepository;

    public AgendamentoService() throws PersistenciaException {
        this.agendamentoRepository = new AgendamentoRepository();
        this.servicoRepository = new ServicoRepository();
    }

    public Agendamento agendar(UUID idPet, UUID idServico, UUID idUsuario,
                               LocalDateTime dataHora, String observacoes) throws PersistenciaException {

        Servico servico = servicoRepository.buscarPorId(idServico);
        if (servico == null) {
            throw new PersistenciaException("Serviço não encontrado");
        }

        if (agendamentoRepository.existeConflitoHorario(dataHora, servico.getDuracaoMinutos())) {
            throw new PersistenciaException("Horário indisponível. Escolha outro horário.");
        }

        Agendamento agendamento = new Agendamento(idPet, idServico, idUsuario, dataHora, observacoes);
        agendamento.setValorCobrado(servico.getPreco());

        agendamentoRepository.adicionar(agendamento);
        return agendamento;
    }

    public List<Agendamento> listarAgendamentosUsuario(UUID idUsuario) throws PersistenciaException {
        return agendamentoRepository.buscarPorUsuario(idUsuario);
    }

    public void cancelarAgendamento(UUID idAgendamento) throws PersistenciaException {

    }
}
