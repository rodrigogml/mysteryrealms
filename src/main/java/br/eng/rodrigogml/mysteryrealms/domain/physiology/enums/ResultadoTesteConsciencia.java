package br.eng.rodrigogml.mysteryrealms.domain.physiology.enums;

/**
 * Resultado do Teste de Conscientização quando os PV chegam a zero, conforme RF-EF-11.
 */
public enum ResultadoTesteConsciencia {

    /**
     * Teste passou: personagem retorna com PV=1 e fadiga elevada.
     */
    SUCESSO,

    /**
     * Teste falhou: personagem entra em estado de desmaio.
     */
    DESMAIADO
}
