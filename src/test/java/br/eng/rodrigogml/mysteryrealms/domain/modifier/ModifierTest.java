package br.eng.rodrigogml.mysteryrealms.domain.modifier;

import br.eng.rodrigogml.mysteryrealms.domain.combat.enums.AfflictionType;
import br.eng.rodrigogml.mysteryrealms.domain.combat.enums.DamageType;
import br.eng.rodrigogml.mysteryrealms.domain.combat.enums.ResistanceType;
import br.eng.rodrigogml.mysteryrealms.domain.modifier.enums.ClasseAcao;
import br.eng.rodrigogml.mysteryrealms.domain.modifier.enums.OrigemModificador;
import br.eng.rodrigogml.mysteryrealms.domain.modifier.enums.StackingRule;
import br.eng.rodrigogml.mysteryrealms.domain.modifier.model.CustoAcao;
import br.eng.rodrigogml.mysteryrealms.domain.modifier.model.Modificador;
import br.eng.rodrigogml.mysteryrealms.domain.modifier.model.ModifierDuration;
import br.eng.rodrigogml.mysteryrealms.domain.modifier.model.EfeitoModificador;
import br.eng.rodrigogml.mysteryrealms.domain.modifier.service.ServicoCustoAcao;
import br.eng.rodrigogml.mysteryrealms.domain.modifier.service.ServicoModificador;
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

    // ── RF-MAR-07/08: ClasseAcao e custos base ───────────────────────────────

    @Test
    void actionClass_possuiExatamente4Valores() {
        // RF-MAR-07
        assertEquals(4, ClasseAcao.values().length);
    }

    @Test
    void baseCost_passiva() {
        // RF-MAR-08
        CustoAcao c = ServicoCustoAcao.custoBase(ClasseAcao.PASSIVA);
        assertEquals(5,     c.getTempoGastoMin());
        assertEquals(2.0,   c.getCustoFadiga(),  1e-9);
        assertEquals(0.05,  c.getDeltaFomePct(), 1e-9);
        assertEquals(0.15,  c.getDeltaSedePct(), 1e-9);
    }

    @Test
    void baseCost_moderada() {
        // RF-MAR-08
        CustoAcao c = ServicoCustoAcao.custoBase(ClasseAcao.MODERADA);
        assertEquals(10,   c.getTempoGastoMin());
        assertEquals(8.0,  c.getCustoFadiga(),  1e-9);
        assertEquals(0.20, c.getDeltaFomePct(), 1e-9);
        assertEquals(0.60, c.getDeltaSedePct(), 1e-9);
    }

    @Test
    void baseCost_exigente() {
        // RF-MAR-08
        CustoAcao c = ServicoCustoAcao.custoBase(ClasseAcao.EXIGENTE);
        assertEquals(15,   c.getTempoGastoMin());
        assertEquals(20.0, c.getCustoFadiga(),  1e-9);
        assertEquals(0.50, c.getDeltaFomePct(), 1e-9);
        assertEquals(1.20, c.getDeltaSedePct(), 1e-9);
    }

    @Test
    void baseCost_recuperativa() {
        // RF-MAR-08
        CustoAcao c = ServicoCustoAcao.custoBase(ClasseAcao.RECUPERATIVA);
        assertEquals(20,    c.getTempoGastoMin());
        assertEquals(-12.0, c.getCustoFadiga(),  1e-9);
        assertEquals(-0.40, c.getDeltaFomePct(), 1e-9);
        assertEquals(-0.80, c.getDeltaSedePct(), 1e-9);
    }

    // ── RF-MAR-09: modificadores de clima ────────────────────────────────────

    @Test
    void weatherModifier_heat_aumentaFadigaESede() {
        // RF-MAR-09
        CustoAcao c = ServicoCustoAcao.custoBase(ClasseAcao.MODERADA);
        ServicoCustoAcao.aplicarModificadorClima(c, ClasseAcao.MODERADA, "HEAT");
        assertEquals(8.0 * 1.20,  c.getCustoFadiga(),  1e-9);
        assertEquals(0.60 * 1.35, c.getDeltaSedePct(), 1e-9);
    }

    @Test
    void weatherModifier_cold_aumentaFadiga() {
        // RF-MAR-09
        CustoAcao c = ServicoCustoAcao.custoBase(ClasseAcao.EXIGENTE);
        ServicoCustoAcao.aplicarModificadorClima(c, ClasseAcao.EXIGENTE, "COLD");
        assertEquals(20.0 * 1.15, c.getCustoFadiga(), 1e-9);
    }

    @Test
    void weatherModifier_rain_aumentaTempoEmModerada() {
        // RF-MAR-09
        CustoAcao c = ServicoCustoAcao.custoBase(ClasseAcao.MODERADA);
        ServicoCustoAcao.aplicarModificadorClima(c, ClasseAcao.MODERADA, "RAIN");
        assertEquals((int) Math.ceil(10 * 1.10), c.getTempoGastoMin());
    }

    @Test
    void weatherModifier_rain_naoAplicaEmPassiva() {
        // RF-MAR-09
        CustoAcao c = ServicoCustoAcao.custoBase(ClasseAcao.PASSIVA);
        ServicoCustoAcao.aplicarModificadorClima(c, ClasseAcao.PASSIVA, "RAIN");
        assertEquals(5, c.getTempoGastoMin(), "PASSIVA não deve ter tempo alterado por RAIN");
    }

    // ── RF-MAR-09: modificadores de carga ────────────────────────────────────

    @Test
    void loadModifier_abaixoDe50Pct_semAjuste() {
        // RF-MAR-09
        CustoAcao c = ServicoCustoAcao.custoBase(ClasseAcao.MODERADA);
        double original = c.getCustoFadiga();
        ServicoCustoAcao.aplicarModificadorCarga(c, ClasseAcao.MODERADA, 0.49);
        assertEquals(original, c.getCustoFadiga(), 1e-9);
    }

    @Test
    void loadModifier_entre51e80Pct_aumentaFadigaModerada() {
        // RF-MAR-09
        CustoAcao c = ServicoCustoAcao.custoBase(ClasseAcao.MODERADA);
        ServicoCustoAcao.aplicarModificadorCarga(c, ClasseAcao.MODERADA, 0.70);
        assertEquals(8.0 * 1.10, c.getCustoFadiga(), 1e-9);
    }

    @Test
    void loadModifier_acimaDe80Pct_aumentaFadigaETempo() {
        // RF-MAR-09
        CustoAcao c = ServicoCustoAcao.custoBase(ClasseAcao.MODERADA);
        ServicoCustoAcao.aplicarModificadorCarga(c, ClasseAcao.MODERADA, 0.90);
        assertEquals(8.0 * 1.25, c.getCustoFadiga(), 1e-9);
        assertEquals((int) Math.ceil(10 * 1.10), c.getTempoGastoMin());
    }

    @Test
    void loadModifier_acimaDe100Pct_exigenteBloqueada() {
        // RF-MAR-09
        CustoAcao c = ServicoCustoAcao.custoBase(ClasseAcao.EXIGENTE);
        assertThrows(IllegalStateException.class,
                () -> ServicoCustoAcao.aplicarModificadorCarga(c, ClasseAcao.EXIGENTE, 1.01));
    }

    @Test
    void loadModifier_acimaDe100Pct_moderadaPermitida() {
        // RF-MAR-09
        CustoAcao c = ServicoCustoAcao.custoBase(ClasseAcao.MODERADA);
        ServicoCustoAcao.aplicarModificadorCarga(c, ClasseAcao.MODERADA, 1.10);
        assertEquals(8.0 * 1.50, c.getCustoFadiga(), 1e-9);
    }

    // ── RF-MAR-09: modificadores fisiológicos ────────────────────────────────

    @Test
    void physiologicalModifier_exaustao_aumentaFadiga() {
        // RF-MAR-09
        CustoAcao c = ServicoCustoAcao.custoBase(ClasseAcao.MODERADA);
        ServicoCustoAcao.aplicarModificadorFisiologico(c, true, false, false, false, false, false, false);
        assertEquals(8.0 * 1.15, c.getCustoFadiga(), 1e-9);
    }

    @Test
    void physiologicalModifier_desmaioLancaExcecao() {
        // RF-MAR-09
        CustoAcao c = ServicoCustoAcao.custoBase(ClasseAcao.MODERADA);
        assertThrows(IllegalStateException.class,
                () -> ServicoCustoAcao.aplicarModificadorFisiologico(c, false, false, false, false, false, true, false));
    }

    @Test
    void physiologicalModifier_estadoCriticoLancaExcecao() {
        // RF-MAR-09
        CustoAcao c = ServicoCustoAcao.custoBase(ClasseAcao.MODERADA);
        assertThrows(IllegalStateException.class,
                () -> ServicoCustoAcao.aplicarModificadorFisiologico(c, false, false, false, false, false, false, true));
    }

    @Test
    void calculateCost_ordemCorretaDeModificadores() {
        // RF-MAR-09 — verifica método consolidado sem erro
        CustoAcao c = ServicoCustoAcao.calcularCusto(
                ClasseAcao.MODERADA, "HEAT", 0.60, true, false, false, false, false, false, false);
        assertTrue(c.getCustoFadiga() > 8.0,
                "Fadiga deve ser maior que a base após HEAT+carga+exaustao");
    }

    // ── RF-MAR-04: Estrutura de modificador ──────────────────────────────────

    @Test
    void modifier_criacaoValida() {
        Modificador m = buildModifier("ataque_bonus", StackingRule.ACUMULA);
        assertEquals("ataque_bonus", m.id());
        assertEquals("Bônus de Ataque", m.nomeExibicao());
        assertEquals(OrigemModificador.EQUIPAMENTO, m.origem());
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
        assertThrows(IllegalArgumentException.class, () -> new EfeitoModificador(""));
    }

    // ── RF-MAR-05: Prioridade de origem ─────────────────────────────────────

    @Test
    void modifierOrigin_estadoCriticoTemMaiorPrioridade() {
        assertTrue(OrigemModificador.ESTADO_CRITICO_COMBATE.getPrioridade()
                < OrigemModificador.HABILIDADE_ATIVA.getPrioridade());
    }

    @Test
    void modifierOrigin_racaMenorPrioridadeQueEquipamento() {
        assertTrue(OrigemModificador.EQUIPAMENTO.getPrioridade()
                < OrigemModificador.RACA.getPrioridade());
    }

    // ── RF-MAR-06: Empilhamento ────────────────────────────────────────────

    @Test
    void modifierService_accumulates_adicionaAmbos() {
        List<Modificador> ativos = new ArrayList<>();
        Modificador m1 = buildModifier("bonus_dano", StackingRule.ACUMULA);
        Modificador m2 = buildModifier("bonus_dano", StackingRule.ACUMULA);
        ServicoModificador.aplicar(ativos, m1);
        ServicoModificador.aplicar(ativos, m2);
        assertEquals(2, ativos.size());
    }

    @Test
    void modifierService_replaces_substituiExistente() {
        List<Modificador> ativos = new ArrayList<>();
        Modificador m1 = buildModifier("bonus_dano", StackingRule.SUBSTITUI);
        Modificador m2 = buildModifier("bonus_dano", StackingRule.SUBSTITUI);
        ServicoModificador.aplicar(ativos, m1);
        ServicoModificador.aplicar(ativos, m2);
        assertEquals(1, ativos.size());
        assertSame(m2, ativos.get(0)); // novo prevalece
    }

    @Test
    void modifierService_replaces_adicionaSeNaoHouverConflito() {
        List<Modificador> ativos = new ArrayList<>();
        Modificador m1 = buildModifier("bonus_dano", StackingRule.SUBSTITUI);
        Modificador m2 = buildModifier("outro_bonus", StackingRule.SUBSTITUI);
        ServicoModificador.aplicar(ativos, m1);
        ServicoModificador.aplicar(ativos, m2);
        assertEquals(2, ativos.size());
    }

    @Test
    void modifierService_invalidates_removeExistenteENaoAdiciona() {
        List<Modificador> ativos = new ArrayList<>();
        Modificador m1 = buildModifier("bonus_dano", StackingRule.ACUMULA);
        Modificador m2 = buildModifier("bonus_dano", StackingRule.INVALIDA);
        ServicoModificador.aplicar(ativos, m1);
        ServicoModificador.aplicar(ativos, m2);
        assertTrue(ativos.isEmpty());
    }

    @Test
    void modifierService_byOrigin_filtraCorretamente() {
        List<Modificador> ativos = new ArrayList<>();
        ServicoModificador.aplicar(ativos, buildModifier("m1", StackingRule.ACUMULA));
        ServicoModificador.aplicar(ativos, buildModifierComOrigem("m2", OrigemModificador.RACA));
        List<Modificador> equipamento = ServicoModificador.porOrigem(ativos, OrigemModificador.EQUIPAMENTO);
        assertEquals(1, equipamento.size());
        assertEquals("m1", equipamento.get(0).id());
    }

    @Test
    void modifierService_sortedByPriority_ordenaCorretamente() {
        List<Modificador> ativos = new ArrayList<>();
        ServicoModificador.aplicar(ativos, buildModifierComOrigem("m_raca", OrigemModificador.RACA));
        ServicoModificador.aplicar(ativos, buildModifierComOrigem("m_estado", OrigemModificador.ESTADO_CRITICO_COMBATE));
        List<Modificador> sorted = ServicoModificador.ordenadosPorPrioridade(ativos);
        assertEquals(OrigemModificador.ESTADO_CRITICO_COMBATE, sorted.get(0).origem());
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Modificador buildModifier(String id, StackingRule rule) {
        return new Modificador(id, "Bônus de Ataque", "ao_equipar",
                new EfeitoModificador("Aumenta dano em +2"),
                ModifierDuration.dePermanente(),
                rule,
                OrigemModificador.EQUIPAMENTO);
    }

    private Modificador buildModifierWithId(String id) {
        return new Modificador(id, "Bônus", "ao_equipar",
                new EfeitoModificador("Aumenta dano"),
                ModifierDuration.turnos(3),
                StackingRule.ACUMULA,
                OrigemModificador.EQUIPAMENTO);
    }

    private Modificador buildModifierComOrigem(String id, OrigemModificador origem) {
        return new Modificador(id, "Bônus", "ao_equipar",
                new EfeitoModificador("Aumenta dano"),
                ModifierDuration.dePermanente(),
                StackingRule.ACUMULA,
                origem);
    }
}
