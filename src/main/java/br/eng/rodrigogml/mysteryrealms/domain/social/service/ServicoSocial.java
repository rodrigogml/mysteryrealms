package br.eng.rodrigogml.mysteryrealms.domain.social.service;

import br.eng.rodrigogml.mysteryrealms.domain.social.enums.FaixaRelacionamento;
import br.eng.rodrigogml.mysteryrealms.domain.social.enums.AvaliacaoEstiloDiscurso;
import br.eng.rodrigogml.mysteryrealms.domain.social.model.EntradaDiario;
import br.eng.rodrigogml.mysteryrealms.domain.social.model.ImpactoDiario;
import br.eng.rodrigogml.mysteryrealms.domain.social.model.EfeitosDialogo;
import br.eng.rodrigogml.mysteryrealms.domain.social.model.NoDialogo;
import br.eng.rodrigogml.mysteryrealms.domain.social.model.OpcaoDialogo;
import br.eng.rodrigogml.mysteryrealms.domain.social.model.Marcador;
import br.eng.rodrigogml.mysteryrealms.domain.social.model.ResultadoCicloSocial;
import br.eng.rodrigogml.mysteryrealms.domain.world.model.WorldConfig;
import br.eng.rodrigogml.mysteryrealms.domain.world.service.ServicoTempoMundo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Serviço do sistema social — RF-SS-02 a RF-SS-07.
 *
 * Implementa relacionamentos, reputação, testes sociais e ajustes de estilo de fala.
 * Stateless: todos os métodos são estáticos.
 */
public final class ServicoSocial {

    /** Contador monotônico para IDs de diário gerados automaticamente. */
    private static final AtomicLong CONTADOR_DIARIO = new AtomicLong(1);

    private ServicoSocial() {}

    // ── RF-SS-05: Teste social ────────────────────────────────────────────────

    /**
     * Aplica o ajuste de estilo de fala ao resultado do teste — RF-SS-04 / RF-SS-05.
     *
     * @param resultadoTeste resultado base do teste (valor_base + 1d20 + modificadores)
     * @param valoracao      valoração do NPC para o estilo de fala escolhido
     * @return resultado ajustado (+2, 0 ou -2)
     */
    public static int ajustarPorEstiloFala(int resultadoTeste, AvaliacaoEstiloDiscurso valoracao) {
        if (valoracao == null) return resultadoTeste;
        return resultadoTeste + valoracao.getAjusteTeste();
    }

    // ── RF-SS-06: Relacionamento com NPC ─────────────────────────────────────

    /**
     * Aplica delta de relacionamento com NPC, clampado em [-100, 100] — RF-SS-06.
     *
     * @param valorAtual valor atual do relacionamento
     * @param delta      incremento (pode ser negativo)
     * @return novo valor clampado
     */
    public static int aplicarDeltaRelacionamento(int valorAtual, int delta) {
        return Math.max(-100, Math.min(100, valorAtual + delta));
    }

    /**
     * Retorna a faixa canônica de relacionamento — RF-SS-06.
     */
    public static FaixaRelacionamento faixaRelacionamento(int valor) {
        return FaixaRelacionamento.de(valor);
    }

    // ── RF-SS-07: Reputação por localidade/facção ─────────────────────────────

    /**
     * Aplica delta de reputação (sem limite fixo de clamp) — RF-SS-07.
     *
     * @param valorAtual valor atual de reputação
     * @param delta      incremento (pode ser negativo)
     * @return novo valor
     */
    public static int aplicarDeltaReputacao(int valorAtual, int delta) {
        return valorAtual + delta;
    }

    // ── RF-SS-02: Ciclo social obrigatório ───────────────────────────────────

    /**
     * Executa o ciclo social completo para uma opção de diálogo — RF-SS-02.
     *
     * Ordem obrigatória:
     * escolha → teste social (quando aplicável) → atualização de relacionamento/reputação
     * → registro no diário → atualização de marcadores.
     *
     * @param npcRelacionamentoAtual  valor atual do relacionamento com o NPC alvo (pode ser null)
     * @param reputacaoAtual          valor atual de reputação no alvo (pode ser null)
     * @param node                    nó de diálogo
     * @param opcaoId                 ID da opção escolhida pelo jogador
     * @param testePassed             resultado externo do teste social (dado já rolado fora)
     * @param tempoAtualMin           tempo atual em minutos (para o campo dataJogo do diário)
     * @param worldConfig             configuração do mundo (para formatar a data)
     * @return resultado completo do ciclo social
     * @throws IllegalArgumentException se opcaoId não for encontrado no nó
     */
    public static ResultadoCicloSocial executarCicloSocial(
            Integer npcRelacionamentoAtual,
            Integer reputacaoAtual,
            NoDialogo node,
            String opcaoId,
            boolean testePassed,
            long tempoAtualMin,
            WorldConfig worldConfig) {

        // 1. Localizar a opção escolhida
        OpcaoDialogo opcao = node.opcoes().stream()
                .filter(o -> o.opcaoId().equals(opcaoId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "opcaoId '" + opcaoId + "' não encontrado no nó '" + node.dialogoId() + "'"));

        // 2. Determinar sucesso: se não há teste, sucesso automático
        boolean sucesso = (opcao.testeSocial() == null) || testePassed;

        // 3. Selecionar os efeitos aplicáveis
        EfeitosDialogo efeitos = sucesso ? opcao.efeitosSucesso() : opcao.efeitosFalha();

        // 4. Aplicar deltas de relacionamento e reputação
        Integer novoRelacionamento = null;
        Integer novaReputacao = null;
        Map<String, Integer> deltasNpc = efeitos.deltasRelacionamentoNpc();
        Map<String, Integer> deltasLoc = efeitos.deltasReputacaoLocalidade();
        Map<String, Integer> deltasFac = efeitos.deltasReputacaoFaccao();

        // Relacionamento com NPC principal do nó
        if (!deltasNpc.isEmpty() && npcRelacionamentoAtual != null) {
            int deltaTotal = deltasNpc.values().stream().mapToInt(Integer::intValue).sum();
            novoRelacionamento = aplicarDeltaRelacionamento(npcRelacionamentoAtual, deltaTotal);
        } else if (!deltasNpc.isEmpty()) {
            int deltaTotal = deltasNpc.values().stream().mapToInt(Integer::intValue).sum();
            novoRelacionamento = aplicarDeltaRelacionamento(0, deltaTotal);
        }

        // Reputação (localidade ou facção, primeiro encontrado)
        if (!deltasLoc.isEmpty() && reputacaoAtual != null) {
            int deltaTotal = deltasLoc.values().stream().mapToInt(Integer::intValue).sum();
            novaReputacao = aplicarDeltaReputacao(reputacaoAtual, deltaTotal);
        } else if (!deltasFac.isEmpty() && reputacaoAtual != null) {
            int deltaTotal = deltasFac.values().stream().mapToInt(Integer::intValue).sum();
            novaReputacao = aplicarDeltaReputacao(reputacaoAtual, deltaTotal);
        }

        // 5. Verificar se deve registrar no diário
        boolean temImpacto = novoRelacionamento != null
                || novaReputacao != null
                || !deltasNpc.isEmpty()
                || !deltasLoc.isEmpty()
                || !deltasFac.isEmpty();

        EntradaDiario diaryEntry = null;
        if (temImpacto) {
            String dataJogo = formatDataJogo(tempoAtualMin, worldConfig);
            String entradaId = "diary_" + node.dialogoId() + "_" + opcaoId + "_" + CONTADOR_DIARIO.getAndIncrement();
            String titulo = resumirTitulo(opcao.textoOpcao());
            String resumo = sucesso ? "Ação bem-sucedida com " + node.npcId() + "."
                    : "Ação falhou com " + node.npcId() + ".";

            // Consolidar todos os deltas para o impacto do diário
            Map<String, Integer> todosNpc = new HashMap<>(deltasNpc);
            Map<String, Integer> todosLoc = new HashMap<>(deltasLoc);
            Map<String, Integer> todosFac = new HashMap<>(deltasFac);
            List<String> marcadoresIds = new ArrayList<>();

            ImpactoDiario impactos = new ImpactoDiario(todosNpc, todosLoc, todosFac, marcadoresIds);

            diaryEntry = new EntradaDiario(
                    entradaId, titulo, resumo, dataJogo,
                    node.dialogoId(), opcaoId, impactos);
        }

        // 6. Marcadores (sem implementação de marcadores dinâmicos neste ciclo — RF-SS-09 é gerenciado externamente)
        List<Marcador> marcadoresAlterados = List.of();

        return new ResultadoCicloSocial(sucesso, diaryEntry, marcadoresAlterados, novoRelacionamento, novaReputacao);
    }

    /**
     * Formata o tempo do mundo no formato {@code D<n>-HH:MM} — RF-SS-08.
     */
    private static String formatDataJogo(long tempoAtualMin, WorldConfig worldConfig) {
        long dia = ServicoTempoMundo.diaDoAno(tempoAtualMin, worldConfig);
        int hora = ServicoTempoMundo.horaDoDia(tempoAtualMin, worldConfig);
        int min = ServicoTempoMundo.minutoDaHora(tempoAtualMin, worldConfig);
        return String.format("D%d-%02d:%02d", dia, hora, min);
    }

    /**
     * Gera um título de no máximo 8 palavras a partir do texto da opção — RF-SS-08.
     */
    private static String resumirTitulo(String textoOpcao) {
        String[] palavras = textoOpcao.trim().split("\\s+");
        if (palavras.length <= 8) return textoOpcao.trim();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            if (i > 0) sb.append(' ');
            sb.append(palavras[i]);
        }
        return sb.toString();
    }
}
