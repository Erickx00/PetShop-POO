package org.example.petshoppoo.repository;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.repository.implementations.AgendamentoRepository;
import org.example.petshoppoo.repository.implementations.PetRepository;
import org.example.petshoppoo.repository.implementations.ServicoRepository;
import org.example.petshoppoo.repository.implementations.UsuarioRepository;
import org.example.petshoppoo.repository.interfaces.IAgendamentoRepository;
import org.example.petshoppoo.repository.interfaces.IPetRepository;
import org.example.petshoppoo.repository.interfaces.IServicoRepository;
import org.example.petshoppoo.repository.interfaces.IUsuarioRepository;

public class RepositoryFactory {

    private static IUsuarioRepository usuarioRepository;
    private static IPetRepository petRepository;
    private static IServicoRepository servicoRepository;
    private static IAgendamentoRepository agendamentoRepository;

    public static IUsuarioRepository getUsuarioRepository() throws PersistenciaException {
        if (usuarioRepository == null) {
            usuarioRepository = new UsuarioRepository();
        }
        return usuarioRepository;
    }

    public static IPetRepository getPetRepository() {
        if (petRepository == null) {
            petRepository = new PetRepository();
        }
        return petRepository;
    }

    public static IServicoRepository getServicoRepository() throws PersistenciaException {
        if (servicoRepository == null) {
            servicoRepository = new ServicoRepository();
        }
        return servicoRepository;
    }

    public static IAgendamentoRepository getAgendamentoRepository() {
        if (agendamentoRepository == null) {
            agendamentoRepository = new AgendamentoRepository();
        }
        return agendamentoRepository;
    }
}