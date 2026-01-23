package org.example.petshoppoo.model.Servico;

public enum TipoServico {
    BANHO("Banho", 40.0, 60, "Banho completo com shampoo e condicionador"),
    TOSA("Tosa", 80.0, 60, "Tosa higiênica ou completa"),
    BANHO_TOSA("Banho e Tosa", 90.0, 60, "Banho completo + tosa"),
    VACINACAO("Vacinação", 80.0, 60, "Aplicação de vacina"),
    CONSULTA("Consulta Veterinária", 150.0, 60, "Consulta com veterinário"),
    ADESTRAMENTO("Adestramento", 200.0, 60, "Sessão de adestramento"),
    HIDRATACAO("Hidratação", 70.0, 60, "Hidratação profunda do pelo");

    private final String descricao;
    private final double precoBase;
    private final int duracaoMinutos;
    private final String detalhes;

    TipoServico(String descricao, double precoBase, int duracaoMinutos, String detalhes) {
        this.descricao = descricao;
        this.precoBase = precoBase;
        this.duracaoMinutos = duracaoMinutos;
        this.detalhes = detalhes;
    }

    public String getDescricao() { return descricao; }
    public double getPrecoBase() { return precoBase; }
    public int getDuracaoMinutos() { return duracaoMinutos; }
    public String getDetalhes() { return detalhes; }
}
