package org.example.petshoppoo.model.Servico;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Agendamento {
    private UUID id;
    private UUID idPet;
    private UUID idServico;
    private UUID idUsuario;
    private LocalDateTime dataHora;
    private StatusAgendamento status;
    private String observacoes;
    private double valorCobrado;
    private LocalDateTime dataCriacao;

    public enum StatusAgendamento {
        AGENDADO("Agendado", "⏰"),
        CONFIRMADO("Confirmado", "✓"),
        EM_ANDAMENTO("Em Andamento", "▶"),
        CONCLUIDO("Concluído", "✔"),
        CANCELADO("Cancelado", "✖"),
        NAO_COMPARECEU("Não Compareceu", "⚠");

        private final String descricao;
        private final String icone;

        StatusAgendamento(String descricao, String icone) {
            this.descricao = descricao;
            this.icone = icone;
        }

        public String getDescricao() { return descricao; }
        public String getIcone() { return icone; }
    }

    public Agendamento() {
        this.id = UUID.randomUUID();
        this.status = StatusAgendamento.AGENDADO;
        this.dataCriacao = LocalDateTime.now();
    }

    public Agendamento(UUID idPet, UUID idServico, UUID idUsuario,
                       LocalDateTime dataHora, String observacoes) {
        this();
        this.idPet = idPet;
        this.idServico = idServico;
        this.idUsuario = idUsuario;
        this.dataHora = dataHora;
        this.observacoes = observacoes;
    }

    // Getters e Setters
    public UUID getId() { return id; }
    public UUID getIdPet() { return idPet; }
    public void setIdPet(UUID idPet) { this.idPet = idPet; }
    public UUID getIdServico() { return idServico; }
    public void setIdServico(UUID idServico) { this.idServico = idServico; }
    public UUID getIdUsuario() { return idUsuario; }
    public void setIdUsuario(UUID idUsuario) { this.idUsuario = idUsuario; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public StatusAgendamento getStatus() { return status; }
    public void setStatus(StatusAgendamento status) { this.status = status; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public double getValorCobrado() { return valorCobrado; }
    public void setValorCobrado(double valorCobrado) { this.valorCobrado = valorCobrado; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }

    public String getDescricaoStatus() {
        return status.getIcone() + " " + status.getDescricao();
    }
}