package org.example.petshoppoo.services;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Pet.Pet;
import org.example.petshoppoo.model.Servico.Agendamento;
import org.example.petshoppoo.model.Servico.Servico;
import org.example.petshoppoo.repository.implementations.AgendamentoRepository;
import org.example.petshoppoo.repository.implementations.PetRepository;
import org.example.petshoppoo.repository.implementations.ServicoRepository;
import org.example.petshoppoo.repository.interfaces.IAgendamentoRepository;
import org.example.petshoppoo.repository.interfaces.IPetRepository;
import org.example.petshoppoo.repository.interfaces.IServicoRepository;
import org.example.petshoppoo.services.interfaces.IAgendamentoService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AgendamentoService implements IAgendamentoService {
    private final IAgendamentoRepository agendamentoRepository;
    private final IServicoRepository servicoRepository;
    private final IPetRepository petRepository;

    public AgendamentoService(IAgendamentoRepository agendamentoRepository) throws PersistenciaException {
        this.agendamentoRepository = new AgendamentoRepository();
        this.servicoRepository = new ServicoRepository();
        this.petRepository = new PetRepository();
    }

    @Override
    public void criarAgendamento(UUID idUsuario, UUID idPet, UUID idServico,
                                 LocalDateTime dataHora, String observacoes) throws Exception {


        if (idPet == null) throw new Exception("Selecione um Pet!");
        if (idServico == null) throw new Exception("Selecione um Serviço!");
        if (dataHora == null) throw new Exception("Selecione data e hora!");
        if (dataHora.isBefore(LocalDateTime.now())) throw new Exception("A data deve ser futura!");


        Servico servico = servicoRepository.buscarPorId(idServico).orElse(null);
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



    @Override
    public List<Agendamento> listarAgendamentosAtivos() throws PersistenciaException {
       return agendamentoRepository.buscarAtivos();
    }


    @Override
    public List<Agendamento> listarAgendamentosPorData(LocalDate data) throws PersistenciaException {
        return agendamentoRepository.buscarPorData(data);
    }

    @Override
    public List<LocalDateTime> listarHorariosDisponiveis(LocalDate data, int duracaoMinutos) throws PersistenciaException {
        return agendamentoRepository.getHorariosDisponiveis(data,duracaoMinutos);
    }

    @Override
    public boolean existeConflitoHorario(LocalDateTime dataHora, int duracaoMinutos) throws PersistenciaException {
        return agendamentoRepository.existeConflitoHorario(dataHora,duracaoMinutos);
    }

    @Override
    public Optional<Agendamento> buscarAgendamentoPorId(UUID id) throws PersistenciaException {
        return agendamentoRepository.buscarPorId(id);
    }


    @Override
    public void concluirAgendamento(UUID idAgendamento) throws PersistenciaException {
        agendamentoRepository.concluirAgendamento(idAgendamento);
    }


    @Override
    public void confirmarAgendamento(UUID idAgendamento) throws PersistenciaException {
        agendamentoRepository.confirmarAgendamento(idAgendamento);
    }
    /*
    @Override
    public void iniciarAgendamento(UUID idAgendamento) throws PersistenciaException {
        //Parte Funcionario se for botar
    }*/


    @Override
    public List<Agendamento> buscarAgendamentosPorPet(UUID idPet) throws PersistenciaException {
        return List.of();
    }

    @Override
    public List<Agendamento> buscarAgendamentosPorServico(UUID idServico) throws PersistenciaException {
        return List.of();
    }

    @Override
    public List<Agendamento> buscarAgendamentosPorStatus(Agendamento.StatusAgendamento status) throws PersistenciaException {
        return agendamentoRepository.buscarPorStatus(status);
    }


    @Override
    public double calcularReceitaTotal() throws PersistenciaException {
        return agendamentoRepository.calcularReceitaTotal();
    }



    @Override
    public long contarAgendamentosUsuario(UUID idUsuario) throws PersistenciaException {
        return 0;
    }


    @Override
    public void removerAgendamento(UUID idAgendamento) throws PersistenciaException {
        agendamentoRepository.deletar(idAgendamento);
    }

    @Override
    public List<Agendamento> listarTodosAgendamentos() throws PersistenciaException {
        return agendamentoRepository.listarTodos();
    }

    public void cancelarAgendamento(Agendamento idAgendamento) throws PersistenciaException {
        agendamentoRepository.cancelarAgendamento(idAgendamento);
    }

    public List<Agendamento> listarTodos() {
        return agendamentoRepository.listarTodos();
    }

    public void excluirAgendamento(UUID id) throws PersistenciaException {
        agendamentoRepository.deletar(id);
    }
}