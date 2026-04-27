package br.eng.rodrigogml.mysteryrealms.domain.character.model;

import br.eng.rodrigogml.mysteryrealms.domain.character.enums.CharacterClass;
import br.eng.rodrigogml.mysteryrealms.domain.character.enums.Gender;
import br.eng.rodrigogml.mysteryrealms.domain.character.enums.Race;
import br.eng.rodrigogml.mysteryrealms.domain.character.enums.Skill;
import br.eng.rodrigogml.mysteryrealms.domain.combat.enums.DamageType;
import br.eng.rodrigogml.mysteryrealms.domain.economy.model.MonetaryValue;
import br.eng.rodrigogml.mysteryrealms.domain.economy.model.Weapon;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes do modelo de Personagem — RF-FP-01 a RF-FP-10.
 */
class CharacterTest {

    private Character buildHuman() {
        return new Character("Aragorn", "Elessar", Gender.MASCULINO, Race.HUMANO, CharacterClass.GUERREIRO, 25);
    }

    // ── RF-FP-01: Identidade ──────────────────────────────────────────────────

    @Test
    void character_identidadeArmazenada() {
        Character c = buildHuman();
        assertEquals("Aragorn", c.getNome());
        assertEquals("Elessar", c.getSobrenome());
        assertEquals(Gender.MASCULINO, c.getGenero());
        assertEquals(Race.HUMANO, c.getRaca());
        assertEquals(CharacterClass.GUERREIRO, c.getClasse());
        assertEquals(25, c.getIdadeInicial());
    }

    @Test
    void character_nomeVazioLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Character("", "Elessar", Gender.MASCULINO, Race.HUMANO, CharacterClass.GUERREIRO, 25));
    }

    @Test
    void character_sobrenomeVazioLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Character("Aragorn", "", Gender.MASCULINO, Race.HUMANO, CharacterClass.GUERREIRO, 25));
    }

    @Test
    void character_idadeInvalidaLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Character("Aragorn", "Elessar", Gender.MASCULINO, Race.HUMANO, CharacterClass.GUERREIRO, 0));
    }

    @Test
    void character_generoNuloLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Character("Aragorn", "Elessar", null, Race.HUMANO, CharacterClass.GUERREIRO, 25));
    }

    // ── RF-FP-02: Atributos combinados raça + classe ──────────────────────────

    @Test
    void character_atributosCombinamRacaEClasse() {
        // Humano: (3,3,3,3,3,3,3) + Guerreiro: (3,1,2,0,0,0,0) = (6,4,5,3,3,3,3)
        Character c = buildHuman();
        AttributeSet attrs = c.getAtributos();
        assertEquals(6, attrs.forca());
        assertEquals(4, attrs.destreza());
        assertEquals(5, attrs.constituicao());
        assertEquals(3, attrs.intelecto());
    }

    // ── RF-FP-04: Estado inicial ──────────────────────────────────────────────

    @Test
    void character_estadoInicialNivel1() {
        Character c = buildHuman();
        assertEquals(1, c.getNivelAtual());
        assertEquals(0L, c.getXpAcumulado());
    }

    @Test
    void character_pvMaxCalculadoDaConstituicao() {
        // Humano + Guerreiro: constituicao = 5 → PV max = 50
        Character c = buildHuman();
        assertEquals(50.0, c.getPontosVidaMax(), 1e-9);
        assertEquals(50.0, c.getPontosVida(), 1e-9);
    }

    @Test
    void character_fadigaMaxCalculada() {
        // constituicao=5, vontade=3 → fadiga max = (5+3)×100 = 800
        Character c = buildHuman();
        assertEquals(800.0, c.getFadigaMax(), 1e-9);
    }

    @Test
    void character_moralInicial75() {
        assertEquals(75, buildHuman().getMoral());
    }

    @Test
    void character_fomeSedePct_zerado() {
        Character c = buildHuman();
        assertEquals(0.0, c.getFomePct(), 1e-9);
        assertEquals(0.0, c.getSedePct(), 1e-9);
    }

    @Test
    void character_setPontosVidaClampadoAoMax() {
        Character c = buildHuman();
        c.setPontosVida(999.0);
        assertEquals(c.getPontosVidaMax(), c.getPontosVida(), 1e-9);
    }

    @Test
    void character_setPontosVidaNaoNegativo() {
        Character c = buildHuman();
        c.setPontosVida(-10.0);
        assertEquals(0.0, c.getPontosVida(), 1e-9);
    }

    @Test
    void character_fadigaAtualNuncaMenorQueFadigaMin() {
        Character c = buildHuman();
        c.setFadigaMin(200.0);
        c.setFadigaAtual(100.0); // abaixo do min → deve subir para 200
        assertEquals(200.0, c.getFadigaAtual(), 1e-9);
    }

    @Test
    void character_moralClampadoEntre0e100() {
        Character c = buildHuman();
        c.setMoral(150);
        assertEquals(100, c.getMoral());
        c.setMoral(-5);
        assertEquals(0, c.getMoral());
    }

    // ── RF-FP-05: Inventário ──────────────────────────────────────────────────

    @Test
    void character_inventarioInicialVazio() {
        Character c = buildHuman();
        assertTrue(c.getItensEquipados().isEmpty());
        assertTrue(c.getItensMochila().isEmpty());
        assertEquals(0L, c.getQtdMoedaPrimaria());
    }

    @Test
    void character_equiparItemUmaMao() {
        Character c = buildHuman();
        c.equiparItem(buildWeapon(1));
        assertEquals(1, c.getItensEquipados().size());
    }

    @Test
    void character_equiparItemDuasMaos() {
        Character c = buildHuman();
        c.equiparItem(buildWeapon(2));
        assertEquals(1, c.getItensEquipados().size());
    }

    @Test
    void character_naoPodeEquiparMaisDeDoiSlots() {
        Character c = buildHuman();
        c.equiparItem(buildWeapon(1));
        c.equiparItem(buildWeapon(1));
        // Terceiro item (mesmo uma mão): não há mais espaço
        assertThrows(IllegalStateException.class, () -> c.equiparItem(buildWeapon(1)));
    }

    @Test
    void character_naoPodeEquiparDuasMaosComUmaJaOcupada() {
        Character c = buildHuman();
        c.equiparItem(buildWeapon(1));
        assertThrows(IllegalStateException.class, () -> c.equiparItem(buildWeapon(2)));
    }

    @Test
    void character_cargaAtualIncluiItensEMoedas() {
        Character c = buildHuman();
        c.setQtdMoedaPrimaria(10);
        c.adicionarItemMochila(buildWeapon(1));
        assertTrue(c.getCargaAtualKg() > 0);
    }

    // ── RF-FP-09: Relacionamento e reputação ──────────────────────────────────

    @Test
    void character_relacionamentoNpcPadraoZero() {
        assertEquals(0, buildHuman().getRelacionamentoNpc("npc_001"));
    }

    @Test
    void character_setRelacionamentoClampadoEntreMenos100e100() {
        Character c = buildHuman();
        c.setRelacionamentoNpc("npc_001", 150);
        assertEquals(100, c.getRelacionamentoNpc("npc_001"));
        c.setRelacionamentoNpc("npc_001", -200);
        assertEquals(-100, c.getRelacionamentoNpc("npc_001"));
    }

    @Test
    void character_setRelacionamentoNpcIdVazioLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> buildHuman().setRelacionamentoNpc("", 10));
    }

    @Test
    void character_reputacaoLocalidadePadraoZero() {
        assertEquals(0, buildHuman().getReputacaoLocalidade("loc_cidade"));
    }

    @Test
    void character_setReputacaoLocalidade() {
        Character c = buildHuman();
        c.setReputacaoLocalidade("loc_cidade", 50);
        assertEquals(50, c.getReputacaoLocalidade("loc_cidade"));
    }

    @Test
    void character_reputacaoFaccao() {
        Character c = buildHuman();
        c.setReputacaoFaccao("fac_guilda", -30);
        assertEquals(-30, c.getReputacaoFaccao("fac_guilda"));
    }

    // ── RF-FP-03: Habilidades canônicas ──────────────────────────────────────

    @Test
    void skill_possuiExatamente12ValoresCanonicos() {
        // RF-FP-03
        assertEquals(12, Skill.values().length,
                "Skill deve ter exatamente 12 habilidades canônicas");
    }

    @Test
    void skill_chavesTecnicasCorretas() {
        // RF-FP-03
        assertEquals("persuasao",           Skill.PERSUASAO.getChave());
        assertEquals("intimidacao",         Skill.INTIMIDACAO.getChave());
        assertEquals("enganacao",           Skill.ENGANACAO.getChave());
        assertEquals("conhecimento_arcano", Skill.CONHECIMENTO_ARCANO.getChave());
        assertEquals("conhecimento_historia", Skill.CONHECIMENTO_HISTORIA.getChave());
        assertEquals("conhecimento_reliquias", Skill.CONHECIMENTO_RELIQUIAS.getChave());
        assertEquals("herbalismo",          Skill.HERBALISMO.getChave());
        assertEquals("alquimia",            Skill.ALQUIMIA.getChave());
        assertEquals("furtividade",         Skill.FURTIVIDADE.getChave());
        assertEquals("sobrevivencia",       Skill.SOBREVIVENCIA.getChave());
        assertEquals("manuseio_armas",      Skill.MANUSEIO_ARMAS.getChave());
        assertEquals("uso_magia",           Skill.USO_MAGIA.getChave());
    }

    // ── RF-FP-07: Raças canônicas ─────────────────────────────────────────────

    @Test
    void race_possuiExatamente8Valores() {
        // RF-FP-07
        assertEquals(8, Race.values().length,
                "Race deve ter exatamente 8 raças canônicas");
    }

    @Test
    void race_todasTemAtributosBaseNaoNulos() {
        // RF-FP-07
        for (Race r : Race.values()) {
            assertNotNull(r.getAtributosBase(),
                    "getAtributosBase() não pode ser nulo para " + r);
        }
    }

    // ── RF-FP-08: Classes canônicas ───────────────────────────────────────────

    @Test
    void characterClass_possuiExatamente12Valores() {
        // RF-FP-08
        assertEquals(12, CharacterClass.values().length,
                "CharacterClass deve ter exatamente 12 classes canônicas");
    }

    @Test
    void characterClass_todasTemBonusAtributosNaoNulos() {
        // RF-FP-08
        for (CharacterClass cc : CharacterClass.values()) {
            assertNotNull(cc.getBonusAtributos(),
                    "getBonusAtributos() não pode ser nulo para " + cc);
        }
    }

    // ── RF-FP-10: Recálculo de derivados ─────────────────────────────────────

    @Test
    void setAtributos_recalculaPvMaxEFadigaMax() {
        // RF-FP-10
        Character c = buildHuman();
        double pvMaxAntes    = c.getPontosVidaMax();
        double fadigaMaxAntes = c.getFadigaMax();

        // Novo atributo com constituicao=10 (muito maior)
        AttributeSet novosAtributos = new AttributeSet(6, 4, 10, 3, 3, 3, 3);
        c.setAtributos(novosAtributos);

        // pvMax = constituicao × 10 = 100
        assertEquals(100.0, c.getPontosVidaMax(), 1e-9);
        assertTrue(c.getPontosVidaMax() > pvMaxAntes,
                "pvMax deve aumentar quando constituicao aumenta");

        // fadigaMax = (const + vont) × 100 = (10+3)×100 = 1300
        assertEquals(1300.0, c.getFadigaMax(), 1e-9);
        assertTrue(c.getFadigaMax() > fadigaMaxAntes,
                "fadigaMax deve aumentar quando constituicao aumenta");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Weapon buildWeapon(int maos) {
        return new Weapon("Espada", maos, 1.5, MonetaryValue.ofMp(10),
                "tipo_espada", "1d8", DamageType.CORTE, "curto", "20/x2", 0, 0, 0, 0);
    }
}
