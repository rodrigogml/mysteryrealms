package br.eng.rodrigogml.mysteryrealms.domain.modifier;

import br.eng.rodrigogml.mysteryrealms.domain.modifier.enums.ModifierOrigin;
import br.eng.rodrigogml.mysteryrealms.domain.modifier.enums.StackingRule;
import br.eng.rodrigogml.mysteryrealms.domain.modifier.model.Modifier;
import br.eng.rodrigogml.mysteryrealms.domain.modifier.model.ModifierDuration;
import br.eng.rodrigogml.mysteryrealms.domain.modifier.model.ModifierEffect;
import br.eng.rodrigogml.mysteryrealms.domain.modifier.service.ModifierService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de modificadores — RF-MAR-04 a RF-MAR-06.
 */
class ModifierTest {

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
