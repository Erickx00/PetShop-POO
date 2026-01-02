package org.example.petshoppoo.utils;

import org.example.petshoppoo.model.Login.Usuario;

import java.util.UUID;

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

    public static UUID getUsuarioId() {
        return SessionManager.getInstance().usuarioLogado.getId();
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
