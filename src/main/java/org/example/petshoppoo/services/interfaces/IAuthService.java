package org.example.petshoppoo.services.interfaces;

public interface IAuthService {
    void login(String email, String senha) throws Exception;
    void logout();
    boolean temUsuarioLogado();
}