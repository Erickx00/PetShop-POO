package org.example.petshoppoo.repository.interfaces;

import org.example.petshoppoo.model.Servico.Servico;

import java.util.List;

public interface IServicoRepository extends IRepository<Servico> {
    List<Servico> listarAtivos();
}