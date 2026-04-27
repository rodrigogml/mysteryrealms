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
                "TipoDano deve ter exatamente 10 valores canônicos");
    }

    @Test
    void damageType_eletricidadeLegacyMapeiaParaRaio() {
        // RF-MAR-01
        assertEquals(DamageType.LIGHTNING, DamageType.fromLegacy("eletricidade"));
        assertEquals(DamageType.LIGHTNING, DamageType.fromLegacy("Eletricidade"));
    }

    @Test
    void damageType_chavesTecnicasCorretas() {
        // RF-MAR-01
        assertEquals("corte",        DamageType.SLASHING.getKey());
        assertEquals("perfuracao",   DamageType.PIERCING.getKey());
        assertEquals("esmagamento",  DamageType.BLUDGEONING.getKey());
        assertEquals("fogo",         DamageType.FIRE.getKey());
        assertEquals("gelo",         DamageType.ICE.getKey());
        assertEquals("raio",         DamageType.LIGHTNING.getKey());
        assertEquals("acido",        DamageType.ACID.getKey());
        assertEquals("magia_pura",   DamageType.PURE_MAGIC.getKey());
        assertEquals("sangramento",  DamageType.BLEEDING.getKey());
        assertEquals("veneno_letal", DamageType.LETHAL_POISON.getKey());
    }

    // ── RF-MAR-02: AfflictionType ─────────────────────────────────────────────

    @Test
    void afflictionType_possuiExatamente10ValoresCanonicos() {
        // RF-MAR-02
        assertEquals(10, AfflictionType.values().length,
                "TipoAfliccao deve ter exatamente 10 valores canônicos");
    }

    @Test
    void afflictionType_chavesTecnicasCorretas() {
        // RF-MAR-02
        assertEquals("psiquica",                      AfflictionType.PSYCHIC.getKey());
        assertEquals("espiritual",                    AfflictionType.SPIRITUAL.getKey());
        assertEquals("medo",                          AfflictionType.FEAR.getKey());
        assertEquals("paralisia",                     AfflictionType.PARALYSIS.getKey());
        assertEquals("cegueira",                      AfflictionType.BLINDNESS.getKey());
        assertEquals("surdez_mudez",                  AfflictionType.DEAFNESS_MUTENESS.getKey());
        assertEquals("fadiga",                        AfflictionType.FATIGUE.getKey());
        assertEquals("doenca_magica",                 AfflictionType.MAGICAL_DISEASE.getKey());
        assertEquals("alucinacao_ilusao_persistente", AfflictionType.PERSISTENT_HALLUCINATION.getKey());
        assertEquals("sono_torpor",                   AfflictionType.SLEEP_TORPOR.getKey());
    }

    // ── RF-MAR-03: ResistanceType ─────────────────────────────────────────────

    @Test
    void resistanceType_possuiExatamente19ValoresCanonicos() {
        // RF-MAR-03
        assertEquals(19, ResistanceType.values().length,
                "TipoResistencia deve ter exatamente 19 valores canônicos");
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
        ActionCost c = ActionCostService.baseActionCost(ActionClass.PASSIVE);
        assertEquals(5,     c.getTimeSpentMin());
        assertEquals(2.0,   c.getFatigueCost(),  1e-9);
        assertEquals(0.05,  c.getHungerDeltaPct(), 1e-9);
        assertEquals(0.15,  c.getThirstDeltaPct(), 1e-9);
    }

    @Test
    void baseCost_moderada() {
        // RF-MAR-08
        ActionCost c = ActionCostService.baseActionCost(ActionClass.MODERATE);
        assertEquals(10,   c.getTimeSpentMin());
        assertEquals(8.0,  c.getFatigueCost(),  1e-9);
        assertEquals(0.20, c.getHungerDeltaPct(), 1e-9);
        assertEquals(0.60, c.getThirstDeltaPct(), 1e-9);
    }

    @Test
    void baseCost_exigente() {
        // RF-MAR-08
        ActionCost c = ActionCostService.baseActionCost(ActionClass.DEMANDING);
        assertEquals(15,   c.getTimeSpentMin());
        assertEquals(20.0, c.getFatigueCost(),  1e-9);
        assertEquals(0.50, c.getHungerDeltaPct(), 1e-9);
        assertEquals(1.20, c.getThirstDeltaPct(), 1e-9);
    }

    @Test
    void baseCost_recuperativa() {
        // RF-MAR-08
        ActionCost c = ActionCostService.baseActionCost(ActionClass.RESTORATIVE);
        assertEquals(20,    c.getTimeSpentMin());
        assertEquals(-12.0, c.getFatigueCost(),  1e-9);
        assertEquals(-0.40, c.getHungerDeltaPct(), 1e-9);
        assertEquals(-0.80, c.getThirstDeltaPct(), 1e-9);
    }

    // ── RF-MAR-09: modificadores de clima ────────────────────────────────────

    @Test
    void weatherModifier_heat_aumentaFadigaESede() {
        // RF-MAR-09
        ActionCost c = ActionCostService.baseActionCost(ActionClass.MODERATE);
        ActionCostService.applyWeatherModifier(c, ActionClass.MODERATE, "HEAT");
        assertEquals(8.0 * 1.20,  c.getFatigueCost(),  1e-9);
        assertEquals(0.60 * 1.35, c.getThirstDeltaPct(), 1e-9);
    }

    @Test
    void weatherModifier_cold_aumentaFadiga() {
        // RF-MAR-09
        ActionCost c = ActionCostService.baseActionCost(ActionClass.DEMANDING);
        ActionCostService.applyWeatherModifier(c, ActionClass.DEMANDING, "COLD");
        assertEquals(20.0 * 1.15, c.getFatigueCost(), 1e-9);
    }

    @Test
    void weatherModifier_rain_aumentaTempoEmModerada() {
        // RF-MAR-09
        ActionCost c = ActionCostService.baseActionCost(ActionClass.MODERATE);
        ActionCostService.applyWeatherModifier(c, ActionClass.MODERATE, "RAIN");
        assertEquals((int) Math.ceil(10 * 1.10), c.getTimeSpentMin());
    }

    @Test
    void weatherModifier_rain_naoAplicaEmPassiva() {
        // RF-MAR-09
        ActionCost c = ActionCostService.baseActionCost(ActionClass.PASSIVE);
        ActionCostService.applyWeatherModifier(c, ActionClass.PASSIVE, "RAIN");
        assertEquals(5, c.getTimeSpentMin(), "PASSIVA não deve ter tempo alterado por RAIN");
    }

    // ── RF-MAR-09: modificadores de carga ────────────────────────────────────

    @Test
    void loadModifier_abaixoDe50Pct_semAjuste() {
        // RF-MAR-09
        ActionCost c = ActionCostService.baseActionCost(ActionClass.MODERATE);
        double original = c.getFatigueCost();
        ActionCostService.applyLoadModifier(c, ActionClass.MODERATE, 0.49);
        assertEquals(original, c.getFatigueCost(), 1e-9);
    }

    @Test
    void loadModifier_entre51e80Pct_aumentaFadigaModerada() {
        // RF-MAR-09
        ActionCost c = ActionCostService.baseActionCost(ActionClass.MODERATE);
        ActionCostService.applyLoadModifier(c, ActionClass.MODERATE, 0.70);
        assertEquals(8.0 * 1.10, c.getFatigueCost(), 1e-9);
    }

    @Test
    void loadModifier_acimaDe80Pct_aumentaFadigaETempo() {
        // RF-MAR-09
        ActionCost c = ActionCostService.baseActionCost(ActionClass.MODERATE);
        ActionCostService.applyLoadModifier(c, ActionClass.MODERATE, 0.90);
        assertEquals(8.0 * 1.25, c.getFatigueCost(), 1e-9);
        assertEquals((int) Math.ceil(10 * 1.10), c.getTimeSpentMin());
    }

    @Test
    void loadModifier_acimaDe100Pct_exigenteBloqueada() {
        // RF-MAR-09
        ActionCost c = ActionCostService.baseActionCost(ActionClass.DEMANDING);
        assertThrows(IllegalStateException.class,
                () -> ActionCostService.applyLoadModifier(c, ActionClass.DEMANDING, 1.01));
    }

    @Test
    void loadModifier_acimaDe100Pct_moderadaPermitida() {
        // RF-MAR-09
        ActionCost c = ActionCostService.baseActionCost(ActionClass.MODERATE);
        ActionCostService.applyLoadModifier(c, ActionClass.MODERATE, 1.10);
        assertEquals(8.0 * 1.50, c.getFatigueCost(), 1e-9);
    }

    // ── RF-MAR-09: modificadores fisiológicos ────────────────────────────────

    @Test
    void physiologicalModifier_exaustao_aumentaFadiga() {
        // RF-MAR-09
        ActionCost c = ActionCostService.baseActionCost(ActionClass.MODERATE);
        ActionCostService.applyPhysiologyModifier(c, true, false, false, false, false, false, false);
        assertEquals(8.0 * 1.15, c.getFatigueCost(), 1e-9);
    }

    @Test
    void physiologicalModifier_desmaioLancaExcecao() {
        // RF-MAR-09
        ActionCost c = ActionCostService.baseActionCost(ActionClass.MODERATE);
        assertThrows(IllegalStateException.class,
                () -> ActionCostService.applyPhysiologyModifier(c, false, false, false, false, false, true, false));
    }

    @Test
    void physiologicalModifier_estadoCriticoLancaExcecao() {
        // RF-MAR-09
        ActionCost c = ActionCostService.baseActionCost(ActionClass.MODERATE);
        assertThrows(IllegalStateException.class,
                () -> ActionCostService.applyPhysiologyModifier(c, false, false, false, false, false, false, true));
    }

    @Test
    void calculateCost_ordemCorretaDeModificadores() {
        // RF-MAR-09 — verifica método consolidado sem erro
        ActionCost c = ActionCostService.calculateCost(
                ActionClass.MODERATE, "HEAT", 0.60, true, false, false, false, false, false, false);
        assertTrue(c.getFatigueCost() > 8.0,
                "Fadiga deve ser maior que a base após HEAT+carga+exaustao");
    }

    // ── RF-MAR-04: Estrutura de modificador ──────────────────────────────────

    @Test
    void modifier_criacaoValida() {
        Modifier m = buildModifier("ataque_bonus", StackingRule.ACCUMULATE);
        assertEquals("ataque_bonus", m.id());
        assertEquals("Bônus de Ataque", m.displayName());
        assertEquals(ModifierOrigin.EQUIPMENT, m.origin());
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
        ModifierDuration d = ModifierDuration.turns(3);
        assertFalse(d.permanente());
        assertEquals(3, d.value());
        assertEquals("turnos", d.unidade());
    }

    @Test
    void modifier_duracaoPermanente() {
        ModifierDuration d = ModifierDuration.ofPermanent();
        assertTrue(d.permanente());
        assertEquals("permanente", d.toString());
    }

    @Test
    void modifier_duracaoMinutosInvalidoLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> ModifierDuration.minutes(0));
    }

    @Test
    void modifierEffect_descricaoVaziaLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> new ModifierEffect(""));
    }

    // ── RF-MAR-05: Prioridade de origin ─────────────────────────────────────

    @Test
    void modifierOrigin_estadoCriticoTemMaiorPrioridade() {
        assertTrue(ModifierOrigin.CRITICAL_COMBAT_STATE.getPriority()
                < ModifierOrigin.ACTIVE_SKILL.getPriority());
    }

    @Test
    void modifierOrigin_racaMenorPrioridadeQueEquipamento() {
        assertTrue(ModifierOrigin.EQUIPMENT.getPriority()
                < ModifierOrigin.RACE.getPriority());
    }

    // ── RF-MAR-06: Empilhamento ────────────────────────────────────────────

    @Test
    void modifierService_accumulates_adicionaAmbos() {
        List<Modifier> ativos = new ArrayList<>();
        Modifier m1 = buildModifier("bonus_dano", StackingRule.ACCUMULATE);
        Modifier m2 = buildModifier("bonus_dano", StackingRule.ACCUMULATE);
        ModifierService.apply(ativos, m1);
        ModifierService.apply(ativos, m2);
        assertEquals(2, ativos.size());
    }

    @Test
    void modifierService_replaces_substituiExistente() {
        List<Modifier> ativos = new ArrayList<>();
        Modifier m1 = buildModifier("bonus_dano", StackingRule.REPLACE);
        Modifier m2 = buildModifier("bonus_dano", StackingRule.REPLACE);
        ModifierService.apply(ativos, m1);
        ModifierService.apply(ativos, m2);
        assertEquals(1, ativos.size());
        assertSame(m2, ativos.get(0)); // novo prevalece
    }

    @Test
    void modifierService_replaces_adicionaSeNaoHouverConflito() {
        List<Modifier> ativos = new ArrayList<>();
        Modifier m1 = buildModifier("bonus_dano", StackingRule.REPLACE);
        Modifier m2 = buildModifier("outro_bonus", StackingRule.REPLACE);
        ModifierService.apply(ativos, m1);
        ModifierService.apply(ativos, m2);
        assertEquals(2, ativos.size());
    }

    @Test
    void modifierService_invalidates_removeExistenteENaoAdiciona() {
        List<Modifier> ativos = new ArrayList<>();
        Modifier m1 = buildModifier("bonus_dano", StackingRule.ACCUMULATE);
        Modifier m2 = buildModifier("bonus_dano", StackingRule.INVALIDATE);
        ModifierService.apply(ativos, m1);
        ModifierService.apply(ativos, m2);
        assertTrue(ativos.isEmpty());
    }

    @Test
    void modifierService_byOrigin_filtraCorretamente() {
        List<Modifier> ativos = new ArrayList<>();
        ModifierService.apply(ativos, buildModifier("m1", StackingRule.ACCUMULATE));
        ModifierService.apply(ativos, buildModifierComOrigem("m2", ModifierOrigin.RACE));
        List<Modifier> equipamento = ModifierService.byOrigin(ativos, ModifierOrigin.EQUIPMENT);
        assertEquals(1, equipamento.size());
        assertEquals("m1", equipamento.get(0).id());
    }

    @Test
    void modifierService_sortedByPriority_ordenaCorretamente() {
        List<Modifier> ativos = new ArrayList<>();
        ModifierService.apply(ativos, buildModifierComOrigem("m_raca", ModifierOrigin.RACE));
        ModifierService.apply(ativos, buildModifierComOrigem("m_estado", ModifierOrigin.CRITICAL_COMBAT_STATE));
        List<Modifier> sorted = ModifierService.orderedByPriority(ativos);
        assertEquals(ModifierOrigin.CRITICAL_COMBAT_STATE, sorted.get(0).origin());
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Modifier buildModifier(String id, StackingRule rule) {
        return new Modifier(id, "Bônus de Ataque", "ao_equipar",
                new ModifierEffect("Aumenta dano em +2"),
                ModifierDuration.ofPermanent(),
                rule,
                ModifierOrigin.EQUIPMENT);
    }

    private Modifier buildModifierWithId(String id) {
        return new Modifier(id, "Bônus", "ao_equipar",
                new ModifierEffect("Aumenta dano"),
                ModifierDuration.turns(3),
                StackingRule.ACCUMULATE,
                ModifierOrigin.EQUIPMENT);
    }

    private Modifier buildModifierComOrigem(String id, ModifierOrigin origin) {
        return new Modifier(id, "Bônus", "ao_equipar",
                new ModifierEffect("Aumenta dano"),
                ModifierDuration.ofPermanent(),
                StackingRule.ACCUMULATE,
                origin);
    }
}
