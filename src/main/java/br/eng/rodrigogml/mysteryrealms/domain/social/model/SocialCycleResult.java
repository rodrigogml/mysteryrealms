package br.eng.rodrigogml.mysteryrealms.domain.social.model;

import java.util.List;

/**
 * Resultado do ciclo social obrigatório — RF-SS-02.
 *
 * Encapsula o sucesso/falha do teste social, a entrada de diário gerada,
 * os marcadores alterados e as variações de relacionamento/reputação.
 */
public class SocialCycleResult {

    /** {@code true} se o teste social passou (ou não havia teste). */
    private final boolean success;

    /** Entrada de diário gerada, ou {@code null} se não houve impacto registrável. */
    private final DiaryEntry diaryEntry;

    /** Marcadores que foram alterados durante o ciclo. */
    private final List<Marker> markersAlterados;

    /** Novo valor de relacionamento com o NPC alvo, ou {@code null} se não aplicável. */
    private final Integer novoRelacionamentoNpc;

    /** Novo valor de reputação (localidade/facção), ou {@code null} se não aplicável. */
    private final Integer novaReputacao;

    public SocialCycleResult(
            boolean success,
            DiaryEntry diaryEntry,
            List<Marker> markersAlterados,
            Integer novoRelacionamentoNpc,
            Integer novaReputacao) {
        this.success = success;
        this.diaryEntry = diaryEntry;
        this.markersAlterados = markersAlterados != null ? List.copyOf(markersAlterados) : List.of();
        this.novoRelacionamentoNpc = novoRelacionamentoNpc;
        this.novaReputacao = novaReputacao;
    }

    public boolean isSuccess() { return success; }
    public DiaryEntry getDiaryEntry() { return diaryEntry; }
    public List<Marker> getMarkersAlterados() { return markersAlterados; }
    public Integer getNovoRelacionamentoNpc() { return novoRelacionamentoNpc; }
    public Integer getNovaReputacao() { return novaReputacao; }
}
