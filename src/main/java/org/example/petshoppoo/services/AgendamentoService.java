package org.example.petshoppoo.services;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.exceptions.PetNaoEncontradoException;
import org.example.petshoppoo.exceptions.ValidacaoException;
import org.example.petshoppoo.model.Pet.Pet;
import org.example.petshoppoo.model.Servico.Agendamento;
import org.example.petshoppoo.model.Servico.Servico;
import org.example.petshoppoo.repository.interfaces.IAgendamentoRepository;
import org.example.petshoppoo.repository.interfaces.IPetRepository;
import org.example.petshoppoo.repository.interfaces.IServicoRepository;
import org.example.petshoppoo.services.interfaces.IAgendamentoService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class AgendamentoService implements IAgendamentoService {
    private final IAgendamentoRepository agendamentoRepository;
    private final IServicoRepository servicoRepository;
    private final IPetRepository petRepository;

    public AgendamentoService(IAgendamentoRepository agendamentoRepository,
                              IServicoRepository servicoRepository,
                              IPetRepository petRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.servicoRepository = servicoRepository;
        this.petRepository = petRepository;
    }

    @Override
    public void criarAgendamento(UUID idUsuario, UUID idPet, UUID idServico,
                                 LocalDateTime dataHora, String observacoes) throws PersistenciaException, PetNaoEncontradoException, ValidacaoException {


        if (idPet == null) throw new ValidacaoException("Selecione um Pet!");
        if (idServico == null) throw new ValidacaoException("Selecione um Serviço!");
        if (idUsuario == null) throw new ValidacaoException("É necessário estar autenticado para agendar.");
        if (dataHora == null) throw new ValidacaoException("Selecione data e hora!");
        if (dataHora.isBefore(LocalDateTime.now())) throw new ValidacaoException("A data deve ser futura!");


        Servico servico = servicoRepository.buscarPorId(idServico).orElse(null);
        if (servico == null) throw new ValidacaoException("Serviço não encontrado!");

        Pet pet = petRepository.buscarPorId(idPet).orElse(null);
        if (pet == null) throw new PetNaoEncontradoException("Pet não encontrado!");
        if (!idUsuario.equals(pet.getIdUsuario())) {
            throw new ValidacaoException("O pet selecionado não pertence ao usuário logado.");
        }

        if (agendamentoRepository.existeConflitoHorario(dataHora, servico.getDuracaoMinutos())) {
            throw new ValidacaoException("Horário indisponível. O intervalo escolhido se sobrepõe a outro agendamento ativo.");
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


    public List<Agendamento> getCancelados(){
        return agendamentoRepository.getCancelados();
    }

    @Override
    public List<Agendamento> listarAgendamentosAtivos() throws PersistenciaException {
       return agendamentoRepository.buscarAtivos();
    }


    @Override
    public List<LocalDateTime> listarHorariosDisponiveis(LocalDate data, int duracaoMinutos) throws PersistenciaException {
        return agendamentoRepository.getHorariosDisponiveis(data,duracaoMinutos);
    }

    @Override
    public boolean existeConflitoHorario(LocalDateTime dataHora, int duracaoMinutos) throws PersistenciaException {
        return agendamentoRepository.existeConflitoHorario(dataHora,duracaoMinutos);
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
