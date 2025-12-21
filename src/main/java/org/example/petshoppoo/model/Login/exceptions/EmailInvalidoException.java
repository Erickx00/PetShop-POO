package org.example.petshoppoo.model.Login.exceptions;

public class EmailInvalidoException extends RuntimeException{

    public EmailInvalidoException(String mensagem){
        super(mensagem);
    }
}
