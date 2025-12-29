package org.example.petshoppoo.utils;

import org.example.petshoppoo.model.Dono.Dono;
import org.example.petshoppoo.model.Login.Usuario;

import java.util.UUID;

public class SessionManager {
    private static Usuario usuarioLogado;
    private static Dono donoLogado;
    private static Object dadosSessao;

    public static void login(Usuario usuario, Dono dono) {
        usuarioLogado = usuario;
        donoLogado = dono;
        System.out.println("Sessão iniciada para: " + usuario.getNome());
    }

    public static void logout() {
        System.out.println("Sessão encerrada para: " +
                (usuarioLogado != null ? usuarioLogado.getNome() : "N/A"));
        usuarioLogado = null;
        donoLogado = null;
        dadosSessao = null;
    }

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static Dono getDonoLogado() {
        return donoLogado;
    }

    public static UUID getUsuarioId() {
        return usuarioLogado != null ? usuarioLogado.getId() : null;
    }

    public static UUID getDonoId() {
        return donoLogado != null ? donoLogado.getId() : null;
    }

    public static boolean isLoggedIn() {
        return usuarioLogado != null && donoLogado != null;
    }

    public static void setDadosSessao(Object dados) {
        dadosSessao = dados;
    }

    public static Object getDadosSessao() {
        return dadosSessao;
    }
}
