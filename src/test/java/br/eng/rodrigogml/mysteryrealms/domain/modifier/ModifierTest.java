package br.eng.rodrigogml.mysteryrealms.domain.modifier;

import br.eng.rodrigogml.mysteryrealms.domain.combat.enums.AfflictionType;
import br.eng.rodrigogml.mysteryrealms.domain.combat.enums.DamageType;
import br.eng.rodrigogml.mysteryrealms.domain.combat.enums.ResistanceType;
import br.eng.rodrigogml.mysteryrealms.domain.modifier.enums.ActionClass;
import br.eng.rodrigogml.mysteryrealms.domain.modifier.enums.ModifierOrigin;
import br.eng.rodrigogml.mysteryrealms.domain.modifier.enums.StackingRule;
import br.eng.rodrigogml.mysteryrealms.domain.modifier.model.ActionCost;
import br.eng.rodrigogml.mysteryrealms.domain.modifier.model.Modifier;
import br.eng.rodrigogml.mysteryrealms.domain.modifier.model.ModifierDuration;
import br.eng.rodrigogml.mysteryrealms.domain.modifier.model.ModifierEffect;
import br.eng.rodrigogml.mysteryrealms.domain.modifier.service.ActionCostService;
import br.eng.rodrigogml.mysteryrealms.domain.modifier.service.ModifierService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de modificadores — RF-MAR-01 a RF-MAR-09.
 */
class ModifierTest {

    // ── RF-MAR-01: DamageType ─────────────────────────────────────────────────

    @Test
    void damageType_possuiExatamente10ValoresCanonicos() {
        // RF-MAR-01
        assertEquals(10, DamageType.values().length,
                "DamageType deve ter exatamente 10 valores canônicos");
    }

    @Test
    void damageType_eletricidadeLegacyMapeiaParaRaio() {
        // RF-MAR-01
        assertEquals(DamageType.RAIO, DamageType.fromLegacy("eletricidade"));
        assertEquals(DamageType.RAIO, DamageType.fromLegacy("Eletricidade"));
    }

    @Test
    void damageType_chavesTecnicasCorretas() {
        // RF-MAR-01
        assertEquals("corte",        DamageType.CORTE.getChave());
        assertEquals("perfuracao",   DamageType.PERFURACAO.getChave());
        assertEquals("esmagamento",  DamageType.ESMAGAMENTO.getChave());
        assertEquals("fogo",         DamageType.FOGO.getChave());
        assertEquals("gelo",         DamageType.GELO.getChave());
        assertEquals("raio",         DamageType.RAIO.getChave());
        assertEquals("acido",        DamageType.ACIDO.getChave());
        assertEquals("magia_pura",   DamageType.MAGIA_PURA.getChave());
        assertEquals("sangramento",  DamageType.SANGRAMENTO.getChave());
        assertEquals("veneno_letal", DamageType.VENENO_LETAL.getChave());
    }

    // ── RF-MAR-02: AfflictionType ─────────────────────────────────────────────

    @Test
    void afflictionType_possuiExatamente10ValoresCanonicos() {
        // RF-MAR-02
        assertEquals(10, AfflictionType.values().length,
                "AfflictionType deve ter exatamente 10 valores canônicos");
    }

    @Test
    void afflictionType_chavesTecnicasCorretas() {
        // RF-MAR-02
        assertEquals("psiquica",                      AfflictionType.PSIQUICA.getChave());
        assertEquals("espiritual",                    AfflictionType.ESPIRITUAL.getChave());
        assertEquals("medo",                          AfflictionType.MEDO.getChave());
        assertEquals("paralisia",                     AfflictionType.PARALISIA.getChave());
        assertEquals("cegueira",                      AfflictionType.CEGUEIRA.getChave());
        assertEquals("surdez_mudez",                  AfflictionType.SURDEZ_MUDEZ.getChave());
        assertEquals("fadiga",                        AfflictionType.FADIGA.getChave());
        assertEquals("doenca_magica",                 AfflictionType.DOENCA_MAGICA.getChave());
        assertEquals("alucinacao_ilusao_persistente", AfflictionType.ALUCINACAO_ILUSAO_PERSISTENTE.getChave());
        assertEquals("sono_torpor",                   AfflictionType.SONO_TORPOR.getChave());
    }

    // ── RF-MAR-03: ResistanceType ─────────────────────────────────────────────

    @Test
    void resistanceType_possuiExatamente19ValoresCanonicos() {
        // RF-MAR-03
        assertEquals(19, ResistanceType.values().length,
                "ResistanceType deve ter exatamente 19 valores canônicos");
    }

    // ── RF-MAR-07/08: ActionClass e custos base ───────────────────────────────

    @Test
    void actionClass_possuiExatamente4Valores() {
        // RF-MAR-07
        assertEquals(4, ActionClass.values().length);
    }

    @Test
    void baseCost_passiva() {
        // RF-MAR-08
        ActionCost c = ActionCostService.baseCost(ActionClass.PASSIVA);
        assertEquals(5,     c.getTempoGastoMin());
        assertEquals(2.0,   c.getCustoFadiga(),  1e-9);
        assertEquals(0.05,  c.getDeltaFomePct(), 1e-9);
        assertEquals(0.15,  c.getDeltaSedePct(), 1e-9);
    }

    @Test
    void baseCost_moderada() {
        // RF-MAR-08
        ActionCost c = ActionCostService.baseCost(ActionClass.MODERADA);
        assertEquals(10,   c.getTempoGastoMin());
        assertEquals(8.0,  c.getCustoFadiga(),  1e-9);
        assertEquals(0.20, c.getDeltaFomePct(), 1e-9);
        assertEquals(0.60, c.getDeltaSedePct(), 1e-9);
    }

    @Test
    void baseCost_exigente() {
        // RF-MAR-08
        ActionCost c = ActionCostService.baseCost(ActionClass.EXIGENTE);
        assertEquals(15,   c.getTempoGastoMin());
        assertEquals(20.0, c.getCustoFadiga(),  1e-9);
        assertEquals(0.50, c.getDeltaFomePct(), 1e-9);
        assertEquals(1.20, c.getDeltaSedePct(), 1e-9);
    }

    @Test
    void baseCost_recuperativa() {
        // RF-MAR-08
        ActionCost c = ActionCostService.baseCost(ActionClass.RECUPERATIVA);
        assertEquals(20,    c.getTempoGastoMin());
        assertEquals(-12.0, c.getCustoFadiga(),  1e-9);
        assertEquals(-0.40, c.getDeltaFomePct(), 1e-9);
        assertEquals(-0.80, c.getDeltaSedePct(), 1e-9);
    }

    // ── RF-MAR-09: modificadores de clima ────────────────────────────────────

    @Test
    void weatherModifier_heat_aumentaFadigaESede() {
        // RF-MAR-09
        ActionCost c = ActionCostService.baseCost(ActionClass.MODERADA);
        ActionCostService.applyWeatherModifier(c, ActionClass.MODERADA, "HEAT");
        assertEquals(8.0 * 1.20,  c.getCustoFadiga(),  1e-9);
        assertEquals(0.60 * 1.35, c.getDeltaSedePct(), 1e-9);
    }

    @Test
    void weatherModifier_cold_aumentaFadiga() {
        // RF-MAR-09
        ActionCost c = ActionCostService.baseCost(ActionClass.EXIGENTE);
        ActionCostService.applyWeatherModifier(c, ActionClass.EXIGENTE, "COLD");
        assertEquals(20.0 * 1.15, c.getCustoFadiga(), 1e-9);
    }

    @Test
    void weatherModifier_rain_aumentaTempoEmModerada() {
        // RF-MAR-09
        ActionCost c = ActionCostService.baseCost(ActionClass.MODERADA);
        ActionCostService.applyWeatherModifier(c, ActionClass.MODERADA, "RAIN");
        assertEquals((int) Math.ceil(10 * 1.10), c.getTempoGastoMin());
    }

    @Test
    void weatherModifier_rain_naoAplicaEmPassiva() {
        // RF-MAR-09
        ActionCost c = ActionCostService.baseCost(ActionClass.PASSIVA);
        ActionCostService.applyWeatherModifier(c, ActionClass.PASSIVA, "RAIN");
        assertEquals(5, c.getTempoGastoMin(), "PASSIVA não deve ter tempo alterado por RAIN");
    }

    // ── RF-MAR-09: modificadores de carga ────────────────────────────────────

    @Test
    void loadModifier_abaixoDe50Pct_semAjuste() {
        // RF-MAR-09
        ActionCost c = ActionCostService.baseCost(ActionClass.MODERADA);
        double original = c.getCustoFadiga();
        ActionCostService.applyLoadModifier(c, ActionClass.MODERADA, 0.49);
        assertEquals(original, c.getCustoFadiga(), 1e-9);
    }

    @Test
    void loadModifier_entre51e80Pct_aumentaFadigaModerada() {
        // RF-MAR-09
        ActionCost c = ActionCostService.baseCost(ActionClass.MODERADA);
        ActionCostService.applyLoadModifier(c, ActionClass.MODERADA, 0.70);
        assertEquals(8.0 * 1.10, c.getCustoFadiga(), 1e-9);
    }

    @Test
    void loadModifier_acimaDe80Pct_aumentaFadigaETempo() {
        // RF-MAR-09
        ActionCost c = ActionCostService.baseCost(ActionClass.MODERADA);
        ActionCostService.applyLoadModifier(c, ActionClass.MODERADA, 0.90);
        assertEquals(8.0 * 1.25, c.getCustoFadiga(), 1e-9);
        assertEquals((int) Math.ceil(10 * 1.10), c.getTempoGastoMin());
    }

    @Test
    void loadModifier_acimaDe100Pct_exigenteBloqueada() {
        // RF-MAR-09
        ActionCost c = ActionCostService.baseCost(ActionClass.EXIGENTE);
        assertThrows(IllegalStateException.class,
                () -> ActionCostService.applyLoadModifier(c, ActionClass.EXIGENTE, 1.01));
    }

    @Test
    void loadModifier_acimaDe100Pct_moderadaPermitida() {
        // RF-MAR-09
        ActionCost c = ActionCostService.baseCost(ActionClass.MODERADA);
        ActionCostService.applyLoadModifier(c, ActionClass.MODERADA, 1.10);
        assertEquals(8.0 * 1.50, c.getCustoFadiga(), 1e-9);
    }

    // ── RF-MAR-09: modificadores fisiológicos ────────────────────────────────

    @Test
    void physiologicalModifier_exaustao_aumentaFadiga() {
        // RF-MAR-09
        ActionCost c = ActionCostService.baseCost(ActionClass.MODERADA);
        ActionCostService.applyPhysiologicalModifier(c, true, false, false, false, false, false, false);
        assertEquals(8.0 * 1.15, c.getCustoFadiga(), 1e-9);
    }

    @Test
    void physiologicalModifier_desmaioLancaExcecao() {
        // RF-MAR-09
        ActionCost c = ActionCostService.baseCost(ActionClass.MODERADA);
        assertThrows(IllegalStateException.class,
                () -> ActionCostService.applyPhysiologicalModifier(c, false, false, false, false, false, true, false));
    }

    @Test
    void physiologicalModifier_estadoCriticoLancaExcecao() {
        // RF-MAR-09
        ActionCost c = ActionCostService.baseCost(ActionClass.MODERADA);
        assertThrows(IllegalStateException.class,
                () -> ActionCostService.applyPhysiologicalModifier(c, false, false, false, false, false, false, true));
    }

    @Test
    void calculateCost_ordemCorretaDeModificadores() {
        // RF-MAR-09 — verifica método consolidado sem erro
        ActionCost c = ActionCostService.calculateCost(
                ActionClass.MODERADA, "HEAT", 0.60, true, false, false, false, false, false, false);
        assertTrue(c.getCustoFadiga() > 8.0,
                "Fadiga deve ser maior que a base após HEAT+carga+exaustao");
    }

    // ── RF-MAR-04: Estrutura de modificador ──────────────────────────────────

    @Test
    void modifier_criacaoValida() {
        Modifier m = buildModifier("ataque_bonus", StackingRule.ACCUMULATES);
        assertEquals("ataque_bonus", m.id());
        assertEquals("Bônus de Ataque", m.nomeExibicao());
        assertEquals(ModifierOrigin.EQUIPAMENTO, m.origem());
    }

    @Test
    void modifier_idComEspacoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> buildModifierWithId("ataque bonus"));
    }

    @Test
    void modifier_idComMaiusculasLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> buildModifierWithId("AtaqueBonus"));
    }

    @Test
    void modifier_idVazioLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> buildModifierWithId(""));
    }

    @Test
    void modifier_duracaoTurnos() {
        ModifierDuration d = ModifierDuration.turnos(3);
        assertFalse(d.permanente());
        assertEquals(3, d.valor());
        assertEquals("turnos", d.unidade());
    }

    @Test
    void modifier_duracaoPermanente() {
        ModifierDuration d = ModifierDuration.dePermanente();
        assertTrue(d.permanente());
        assertEquals("permanente", d.toString());
    }

    @Test
    void modifier_duracaoMinutosInvalidoLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> ModifierDuration.minutos(0));
    }

    @Test
    void modifierEffect_descricaoVaziaLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> new ModifierEffect(""));
    }

    // ── RF-MAR-05: Prioridade de origem ─────────────────────────────────────

    @Test
    void modifierOrigin_estadoCriticoTemMaiorPrioridade() {
        assertTrue(ModifierOrigin.ESTADO_CRITICO_COMBATE.getPrioridade()
                < ModifierOrigin.HABILIDADE_ATIVA.getPrioridade());
    }

    @Test
    void modifierOrigin_racaMenorPrioridadeQueEquipamento() {
        assertTrue(ModifierOrigin.EQUIPAMENTO.getPrioridade()
                < ModifierOrigin.RACA.getPrioridade());
    }

    // ── RF-MAR-06: Empilhamento ────────────────────────────────────────────

    @Test
    void modifierService_accumulates_adicionaAmbos() {
        List<Modifier> ativos = new ArrayList<>();
        Modifier m1 = buildModifier("bonus_dano", StackingRule.ACCUMULATES);
        Modifier m2 = buildModifier("bonus_dano", StackingRule.ACCUMULATES);
        ModifierService.apply(ativos, m1);
        ModifierService.apply(ativos, m2);
        assertEquals(2, ativos.size());
    }

    @Test
    void modifierService_replaces_substituiExistente() {
        List<Modifier> ativos = new ArrayList<>();
        Modifier m1 = buildModifier("bonus_dano", StackingRule.REPLACES);
        Modifier m2 = buildModifier("bonus_dano", StackingRule.REPLACES);
        ModifierService.apply(ativos, m1);
        ModifierService.apply(ativos, m2);
        assertEquals(1, ativos.size());
        assertSame(m2, ativos.get(0)); // novo prevalece
    }

    @Test
    void modifierService_replaces_adicionaSeNaoHouverConflito() {
        List<Modifier> ativos = new ArrayList<>();
        Modifier m1 = buildModifier("bonus_dano", StackingRule.REPLACES);
        Modifier m2 = buildModifier("outro_bonus", StackingRule.REPLACES);
        ModifierService.apply(ativos, m1);
        ModifierService.apply(ativos, m2);
        assertEquals(2, ativos.size());
    }

    @Test
    void modifierService_invalidates_removeExistenteENaoAdiciona() {
        List<Modifier> ativos = new ArrayList<>();
        Modifier m1 = buildModifier("bonus_dano", StackingRule.ACCUMULATES);
        Modifier m2 = buildModifier("bonus_dano", StackingRule.INVALIDATES);
        ModifierService.apply(ativos, m1);
        ModifierService.apply(ativos, m2);
        assertTrue(ativos.isEmpty());
    }

    @Test
    void modifierService_byOrigin_filtraCorretamente() {
        List<Modifier> ativos = new ArrayList<>();
        ModifierService.apply(ativos, buildModifier("m1", StackingRule.ACCUMULATES));
        ModifierService.apply(ativos, buildModifierComOrigem("m2", ModifierOrigin.RACA));
        List<Modifier> equipamento = ModifierService.byOrigin(ativos, ModifierOrigin.EQUIPAMENTO);
        assertEquals(1, equipamento.size());
        assertEquals("m1", equipamento.get(0).id());
    }

    @Test
    void modifierService_sortedByPriority_ordenaCorretamente() {
        List<Modifier> ativos = new ArrayList<>();
        ModifierService.apply(ativos, buildModifierComOrigem("m_raca", ModifierOrigin.RACA));
        ModifierService.apply(ativos, buildModifierComOrigem("m_estado", ModifierOrigin.ESTADO_CRITICO_COMBATE));
        List<Modifier> sorted = ModifierService.sortedByPriority(ativos);
        assertEquals(ModifierOrigin.ESTADO_CRITICO_COMBATE, sorted.get(0).origem());
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Modifier buildModifier(String id, StackingRule rule) {
        return new Modifier(id, "Bônus de Ataque", "ao_equipar",
                new ModifierEffect("Aumenta dano em +2"),
                ModifierDuration.dePermanente(),
                rule,
                ModifierOrigin.EQUIPAMENTO);
    }

    private Modifier buildModifierWithId(String id) {
        return new Modifier(id, "Bônus", "ao_equipar",
                new ModifierEffect("Aumenta dano"),
                ModifierDuration.turnos(3),
                StackingRule.ACCUMULATES,
                ModifierOrigin.EQUIPAMENTO);
    }

    private Modifier buildModifierComOrigem(String id, ModifierOrigin origem) {
        return new Modifier(id, "Bônus", "ao_equipar",
                new ModifierEffect("Aumenta dano"),
                ModifierDuration.dePermanente(),
                StackingRule.ACCUMULATES,
                origem);
    }
}
