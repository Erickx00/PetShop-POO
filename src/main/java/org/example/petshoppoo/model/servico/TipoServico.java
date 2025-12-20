package org.example.petshoppoo.model.servico;

public enum TipoServico {
    BANHO("Banho", 40.0, 30, "Banho completo com shampoo e condicionador"),
    TOSA("Tosa", 60.0, 60, "Tosa higiênica ou completa"),
    BANHO_TOSA("Banho e Tosa", 90.0, 90, "Banho completo + tosa"),
    VACINACAO("Vacinação", 80.0, 15, "Aplicação de vacina"),

    //Aqui usara parte da IA
    CONSULTA("Consulta Veterinária", 150.0, 30, "Consulta com veterinário"),

    ADESTRAMENTO("Adestramento", 200.0, 60, "Sessão de adestramento"),
    HIDRATACAO("Hidratação", 70.0, 45, "Hidratação profunda do pelo");

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
