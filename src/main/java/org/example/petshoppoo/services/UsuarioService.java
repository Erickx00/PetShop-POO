package org.example.petshoppoo.services;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Login.Usuario;
import org.example.petshoppoo.repository.RepositoryFactory;
import org.example.petshoppoo.repository.interfaces.IUsuarioRepository;
import org.example.petshoppoo.services.interfaces.IUsuarioService;
import org.example.petshoppoo.utils.SessionManager;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UsuarioService implements IUsuarioService {
    private final IUsuarioRepository usuarioRepository;

    public UsuarioService(IUsuarioRepository usuarioRepository) throws PersistenciaException {
        this.usuarioRepository = usuarioRepository;
    }

    public void registrar(String nome, String email, String telefone, String senha) throws Exception {
        if (senha == null || senha.length() < 6) {
            throw new Exception("A senha deve ter pelo menos 6 caracteres!");
        }

        String telLimpo = telefone.replaceAll("\\D", "");
        if (telLimpo.startsWith("0")) telLimpo = telLimpo.substring(1);

        if (telLimpo.length() != 11) {
            throw new Exception("Telefone invalido! Use DDD(2) + Numeros(9). Ex: 83912345678");
        }

        if (usuarioRepository.emailExiste(email)) {
            throw new PersistenciaException("Email ja cadastrado!");
        }

        if (usuarioRepository.telefoneExiste(telefone)) {
            throw new PersistenciaException("Telefone ja cadastrado");
        }

        Usuario novoUsuario = new Usuario(nome, email, telLimpo, senha);
        usuarioRepository.salvar(novoUsuario);
    }

    public void atualizarPerfil(UUID usuarioId, String nome, String email, String telefone) throws Exception {

        // 1. Buscar o Optional
        Optional<Usuario> usuarioOptional = usuarioRepository.buscarPorId(usuarioId);

        // 2. Verificar se o Optional está vazio
        if (usuarioOptional.isEmpty()) {
            throw new PersistenciaException("Usuário não encontrado!");
        }

        // 3. Obter o usuário real do Optional
        Usuario usuario = usuarioOptional.get();

        // Validação básica do telefone (null check)
        if (telefone == null) {
            throw new Exception("Telefone não pode ser nulo!");
        }

        // Limpeza do telefone (mantém apenas números)
        String telLimpo = telefone.replaceAll("\\D", "");

        // Remove o '0' inicial se houver (DDD + 9 dígitos)
        if (telLimpo.startsWith("0")) {
            telLimpo = telLimpo.substring(1);
        }

        // Validação de tamanho (11 dígitos: DDD + 9 números)
        if (telLimpo.length() != 11) {
            throw new Exception("Telefone inválido! Use DDD(2) + Número(9). Ex: 83912345678");
        }

        // 4. Validações de duplicidade (apenas se o valor mudou)
        if (!usuario.getEmail().equals(email) && usuarioRepository.emailExiste(email)) {
            throw new PersistenciaException("Email já cadastrado para outro usuário!");
        }

        if (!usuario.getTelefone().equals(telLimpo) && usuarioRepository.telefoneExiste(telLimpo)) {
            throw new PersistenciaException("Telefone já cadastrado para outro usuário!");
        }

        // 5. Atualizar os dados
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setTelefone(telLimpo);

        // 6. Persistir
        usuarioRepository.atualizar(usuario);

        // 7. Atualizar sessão
        SessionManager.getInstance().setUsuarioLogado(usuario);
    }

    /**
     * Retorna o nome do usuário logado
     */
    public String obterNomeUsuarioLogado() {
        Usuario usuario = SessionManager.getInstance().getUsuarioLogado();
        return usuario != null ? usuario.getNome() : "";
    }

    /**
     * Retorna o email do usuário logado
     */
    public String obterEmailUsuarioLogado() {
        Usuario usuario = SessionManager.getInstance().getUsuarioLogado();
        return usuario != null ? usuario.getEmail() : "";
    }

    /**
     * Retorna o telefone do usuário logado
     */
    public String obterTelefoneUsuarioLogado() {
        Usuario usuario = SessionManager.getInstance().getUsuarioLogado();
        return usuario != null ? usuario.getTelefone() : "";
    }



    public List<Usuario> listarTodosUsuarios() {
        return usuarioRepository.listarTodos();
    }
}