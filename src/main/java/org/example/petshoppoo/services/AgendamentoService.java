package org.example.petshoppoo.services;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Pet.Pet;
import org.example.petshoppoo.model.Servico.Agendamento;
import org.example.petshoppoo.model.Servico.Servico;
import org.example.petshoppoo.repository.AgendamentoRepository;
import org.example.petshoppoo.repository.PetRepository;
import org.example.petshoppoo.repository.ServicoRepository;

import java.time.LocalDateTime;
import java.util.List;
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

    public void criarAgendamento(UUID idUsuario, UUID idPet, UUID idServico,
                                 LocalDateTime dataHora, String observacoes) throws Exception {


        if (idPet == null) throw new Exception("Selecione um Pet!");
        if (idServico == null) throw new Exception("Selecione um Serviço!");
        if (dataHora == null) throw new Exception("Selecione data e hora!");
        if (dataHora.isBefore(LocalDateTime.now())) throw new Exception("A data deve ser futura!");


        Servico servico = servicoRepository.buscarPorId(idServico);
        if (servico == null) throw new Exception("Serviço não encontrado!");

        Pet pet = petRepository.buscarPorId(idPet).orElse(null);
        if (pet == null) throw new Exception("Pet não encontrado!");


        List<Agendamento> agendamentosExistentes = agendamentoRepository.listarTodos();

        for (Agendamento ag : agendamentosExistentes) {// Verifica se é EXATAMENTE o mesmo horário

            if (ag.getDataHora().equals(dataHora)) {
                // Se o agendamento existente NÃO estiver CANCELADO, então o horário está ocupado
                if (ag.getStatus() != Agendamento.StatusAgendamento.CANCELADO) {
                    throw new Exception("Horário indisponível! Já existe um agendamento para " +
                            dataHora.getHour() + ":" + String.format("%02d", dataHora.getMinute()));
                }
            }
        }

        Agendamento novoAgendamento = new Agendamento(
                idPet,
                idServico,
                idUsuario,
                dataHora,
                observacoes,
                servico.getDuracaoMinutos()
        );

        novoAgendamento.setValorCobrado(servico.getPreco());


        agendamentoRepository.salvar(novoAgendamento);
    }

    public List<Agendamento> listarAgendamentosPorUsuario(UUID idUsuario) {
        return agendamentoRepository.buscarPorUsuario(idUsuario);
    }

    public void cancelarAgendamento(Agendamento agendamento) throws PersistenciaException {
        if (agendamento != null) {
            agendamento.setStatus(Agendamento.StatusAgendamento.CANCELADO);
            agendamentoRepository.atualizar(agendamento);
        }
    }

    public List<Agendamento> listarTodos() {
        return agendamentoRepository.listarTodos();
    }

    public void excluirAgendamento(Agendamento agendamento) throws Exception {
        if (agendamento == null) return;

        agendamentoRepository.deletar(agendamento);
    }
}