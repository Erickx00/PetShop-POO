package org.example.petshoppoo.utils;

import org.example.petshoppoo.model.Login.Usuario;

public class SessionManager {
    private static Usuario usuarioLogado;
    private static Object dadosSessao;

    public static void login(Usuario usuario) {
        usuarioLogado = usuario;
    }

    public static void logout() {
        usuarioLogado = null;
        dadosSessao = null;
    }

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static boolean isLoggedIn() {
        return usuarioLogado != null;
    }

    public static void setDadosSessao(Object dados) {
        dadosSessao = dados;
    }

    public static Object getDadosSessao() {
        return dadosSessao;
    }
}
