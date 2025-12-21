package org.example.petshoppoo.model.Login;

import java.util.UUID;

public class Usuario {
        private UUID id;
        private String usuario;
        private String email;
        private String senha;

        public Usuario(String usuario, String email, String senha) {
            this.id = UUID.randomUUID();
            this.usuario = usuario;
            this.email = email;
            this.senha = senha;
        }

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public String getUsuario() {
            return usuario;
        }

        public void setUsuario(String usuario) {
            this.usuario = usuario;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getSenha() {
            return senha;
        }

        public void setSenha(String senha) {
            this.senha = senha;
        }

    }

