package org.example.petshoppoo.services.interfaces;

import org.example.petshoppoo.exceptions.AutenticacaoException;

public interface IAuthService {
    void login(String email, String senha) throws AutenticacaoException;
    void logout();
    boolean temUsuarioLogado();
}
