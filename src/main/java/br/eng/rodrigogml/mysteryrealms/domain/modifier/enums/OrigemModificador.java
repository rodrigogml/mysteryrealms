package br.eng.rodrigogml.mysteryrealms.domain.modifier.enums;

/**
 * Origens de modificadores, em ordem canônica de prioridade (1 = maior), conforme RF-MAR-05.
 */
public enum OrigemModificador {

    ESTADO_CRITICO_COMBATE(1),
    HABILIDADE_ATIVA(2),
    EQUIPAMENTO(3),
    CLASSE(4),
    RACA(5),
    EFEITO_TEMPORARIO_GENERICO(6);

    private final int prioridade;

    OrigemModificador(int prioridade) {
        this.prioridade = prioridade;
    }

    public int getPrioridade() {
        return prioridade;
    }
}
