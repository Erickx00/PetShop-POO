package org.example.petshoppoo.controllers;

import org.example.petshoppoo.utils.SessionManager;

public abstract class BaseController {
    protected SessionManager session = SessionManager.getInstance();

    protected void validarSessao(){
        if(!SessionManager.getInstance().isLogado()){
            throw new RuntimeException("Sessao Invalida");
        }
    }
}
