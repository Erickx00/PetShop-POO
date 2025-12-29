package org.example.petshoppoo.repository;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Dono.Dono;
import org.example.petshoppoo.model.Login.Usuario;
import org.example.petshoppoo.utils.FilePaths;

import java.util.List;
import java.util.UUID;

import static org.example.petshoppoo.model.Usuario.Perfil.CLIENTE;

public class UsuarioRepository {
    private List<Usuario> usuarios;
    private DonoRepository donoRepository; // Adicione esta referência

    public UsuarioRepository() throws PersistenciaException {
        this.donoRepository = new DonoRepository(); // Inicialize o repositório
        carregarUsuarios();
    }

    private void carregarUsuarios() throws PersistenciaException {
        this.usuarios = JsonFileManager.carregar(FilePaths.USUARIOS_JSON, Usuario.class);
    }

    public void adicionar(Usuario usuario) throws PersistenciaException {
        usuarios.add(usuario);
        salvarUsuarios();

        // Se o usuário é um CLIENTE, cria um Dono automaticamente
        if (usuario.getPerfil() == CLIENTE) {
            criarDonoParaUsuario(usuario);
        }
    }

    private void criarDonoParaUsuario(Usuario usuario) throws PersistenciaException {
        // Cria um novo Dono com os dados do usuário
        Dono dono = new Dono(
                UUID.randomUUID(), // Gera um novo ID para o dono
                usuario.getNome(),
                "", // Telefone vazio por enquanto (o usuário pode atualizar depois)
                usuario.getEmail()
        );

        // Salva o dono
        donoRepository.adicionar(dono);

        // Atualiza o usuário com o ID do dono
        usuario.setIdDono(dono.getId());
        salvarUsuarios(); // Salva novamente com o idDono atualizado
    }

    public Usuario buscarPorEmail(String email) {
        return usuarios.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    public boolean emailExiste(String email) {
        return usuarios.stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }

    private void salvarUsuarios() throws PersistenciaException {
        JsonFileManager.salvar(FilePaths.USUARIOS_JSON, usuarios);
    }
}