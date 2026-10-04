package org.example.petshoppoo.services.interfaces;

import org.example.petshoppoo.exceptions.AutenticacaoException;
import org.example.petshoppoo.exceptions.PersistenciaException;

public interface IAuthService {
    void login(String email, String senha) throws AutenticacaoException, PersistenciaException;
    void logout();
    boolean temUsuarioLogado();
}
