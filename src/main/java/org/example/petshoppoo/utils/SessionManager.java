package org.example.petshoppoo.utils;

import org.example.petshoppoo.model.Login.Usuario;

public class SessionManager {

    private static SessionManager instance;
    private Usuario usuarioLogado;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }

    public boolean isLogado() {
        return usuarioLogado != null;
    }

    public void logout() {
        usuarioLogado = null;
    }
}
