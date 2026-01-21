package org.example.petshoppoo.services;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.repository.RepositoryFactory;
import org.example.petshoppoo.services.interfaces.*;

public class ServiceFactory {

    private static IPetService petService;
    private static IUsuarioService usuarioService;
    private static IAuthService authService;
    private static IServicoService servicoService;
    private static IAgendamentoService agendamentoService;

    public static IPetService getPetService() throws PersistenciaException {
        if (petService == null) {
            petService = new PetService(RepositoryFactory.getPetRepository());
        }
        return petService;
    }

    public static IUsuarioService getUsuarioService() throws PersistenciaException {
        if(usuarioService == null){
            usuarioService = new UsuarioService(RepositoryFactory.getUsuarioRepository());
        }
        return usuarioService;
    }

    public static IAuthService getAuthService() throws PersistenciaException {
        if(authService == null){
            authService = new AuthService(RepositoryFactory.getUsuarioRepository());
        }
        return authService;
    }

    public static IServicoService getServicoService() throws PersistenciaException {
        if(servicoService == null){
            servicoService = new ServicoService(RepositoryFactory.getServicoRepository());
        }
        return servicoService;
    }

    public static IAgendamentoService getAgendamentoService() throws PersistenciaException {
        if(agendamentoService == null){
            agendamentoService = new AgendamentoService(RepositoryFactory.getAgendamentoRepository());
        }
        return agendamentoService;
    }
}