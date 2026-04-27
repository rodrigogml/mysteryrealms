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
 * Testes do modelo de Character — RF-FP-01 a RF-FP-10.
 */
class CharacterTest {

    private Character buildHuman() {
        return new Character("Aragorn", "Elessar", Gender.MALE, Race.HUMAN, CharacterClass.WARRIOR, 25);
    }

    // ── RF-FP-01: Identidade ──────────────────────────────────────────────────

    @Test
    void character_identidadeArmazenada() {
        Character c = buildHuman();
        assertEquals("Aragorn", c.getName());
        assertEquals("Elessar", c.getSurname());
        assertEquals(Gender.MALE, c.getGender());
        assertEquals(Race.HUMAN, c.getRace());
        assertEquals(CharacterClass.WARRIOR, c.getCharacterClass());
        assertEquals(25, c.getInitialAge());
    }

    @Test
    void character_nomeVazioLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Character("", "Elessar", Gender.MALE, Race.HUMAN, CharacterClass.WARRIOR, 25));
    }

    @Test
    void character_sobrenomeVazioLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Character("Aragorn", "", Gender.MALE, Race.HUMAN, CharacterClass.WARRIOR, 25));
    }

    @Test
    void character_idadeInvalidaLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Character("Aragorn", "Elessar", Gender.MALE, Race.HUMAN, CharacterClass.WARRIOR, 0));
    }

    @Test
    void character_generoNuloLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Character("Aragorn", "Elessar", null, Race.HUMAN, CharacterClass.WARRIOR, 25));
    }

    // ── RF-FP-02: Atributos combinados raça + classe ──────────────────────────

    @Test
    void character_atributosCombinamRacaEClasse() {
        // Humano: (3,3,3,3,3,3,3) + Guerreiro: (3,1,2,0,0,0,0) = (6,4,5,3,3,3,3)
        Character c = buildHuman();
        AttributeSet attrs = c.getAttributes();
        assertEquals(6, attrs.strength());
        assertEquals(4, attrs.dexterity());
        assertEquals(5, attrs.constitution());
        assertEquals(3, attrs.intellect());
    }

    // ── RF-FP-04: Estado initial ──────────────────────────────────────────────

    @Test
    void character_estadoInicialNivel1() {
        Character c = buildHuman();
        assertEquals(1, c.getCurrentLevel());
        assertEquals(0L, c.getAccumulatedXp());
    }

    @Test
    void character_pvMaxCalculadoDaConstituicao() {
        // Humano + Guerreiro: constitution = 5 → PV max = 50
        Character c = buildHuman();
        assertEquals(50.0, c.getMaxHealthPoints(), 1e-9);
        assertEquals(50.0, c.getHealthPoints(), 1e-9);
    }

    @Test
    void character_fadigaMaxCalculada() {
        // constitution=5, willpower=3 → fadiga max = (5+3)×100 = 800
        Character c = buildHuman();
        assertEquals(800.0, c.getMaxFatigue(), 1e-9);
    }

    @Test
    void character_moralInicial75() {
        assertEquals(75, buildHuman().getMorale());
    }

    @Test
    void character_fomeSedePct_zerado() {
        Character c = buildHuman();
        assertEquals(0.0, c.getHungerPct(), 1e-9);
        assertEquals(0.0, c.getThirstPct(), 1e-9);
    }

    @Test
    void character_setPontosVidaClampadoAoMax() {
        Character c = buildHuman();
        c.setHealthPoints(999.0);
        assertEquals(c.getMaxHealthPoints(), c.getHealthPoints(), 1e-9);
    }

    @Test
    void character_setPontosVidaNaoNegativo() {
        Character c = buildHuman();
        c.setHealthPoints(-10.0);
        assertEquals(0.0, c.getHealthPoints(), 1e-9);
    }

    @Test
    void character_fadigaAtualNuncaMenorQueFadigaMin() {
        Character c = buildHuman();
        c.setMinFatigue(200.0);
        c.setCurrentFatigue(100.0); // abaixo do min → deve subir para 200
        assertEquals(200.0, c.getCurrentFatigue(), 1e-9);
    }

    @Test
    void character_moralClampadoEntre0e100() {
        Character c = buildHuman();
        c.setMorale(150);
        assertEquals(100, c.getMorale());
        c.setMorale(-5);
        assertEquals(0, c.getMorale());
    }

    // ── RF-FP-05: Inventário ──────────────────────────────────────────────────

    @Test
    void character_inventarioInicialVazio() {
        Character c = buildHuman();
        assertTrue(c.getEquippedItems().isEmpty());
        assertTrue(c.getBackpackItems().isEmpty());
        assertEquals(0L, c.getPrimaryCurrencyQty());
    }

    @Test
    void character_equiparItemUmaMao() {
        Character c = buildHuman();
        c.equipItem(buildArma(1));
        assertEquals(1, c.getEquippedItems().size());
    }

    @Test
    void character_equiparItemDuasMaos() {
        Character c = buildHuman();
        c.equipItem(buildArma(2));
        assertEquals(1, c.getEquippedItems().size());
    }

    @Test
    void character_naoPodeEquiparMaisDeDoiSlots() {
        Character c = buildHuman();
        c.equipItem(buildArma(1));
        c.equipItem(buildArma(1));
        // Terceiro item (mesmo uma mão): não há mais espaço
        assertThrows(IllegalStateException.class, () -> c.equipItem(buildArma(1)));
    }

    @Test
    void character_naoPodeEquiparDuasMaosComUmaJaOcupada() {
        Character c = buildHuman();
        c.equipItem(buildArma(1));
        assertThrows(IllegalStateException.class, () -> c.equipItem(buildArma(2)));
    }

    @Test
    void character_cargaAtualIncluiItensEMoedas() {
        Character c = buildHuman();
        c.setPrimaryCurrencyQty(10);
        c.addToBackpack(buildArma(1));
        assertTrue(c.getCurrentLoadKg() > 0);
    }

    // ── RF-FP-09: Relacionamento e reputação ──────────────────────────────────

    @Test
    void character_relacionamentoNpcPadraoZero() {
        assertEquals(0, buildHuman().getNpcRelationship("npc_001"));
    }

    @Test
    void character_setRelacionamentoClampadoEntreMenos100e100() {
        Character c = buildHuman();
        c.setNpcRelationship("npc_001", 150);
        assertEquals(100, c.getNpcRelationship("npc_001"));
        c.setNpcRelationship("npc_001", -200);
        assertEquals(-100, c.getNpcRelationship("npc_001"));
    }

    @Test
    void character_setRelacionamentoNpcIdVazioLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> buildHuman().setNpcRelationship("", 10));
    }

    @Test
    void character_reputacaoLocalidadePadraoZero() {
        assertEquals(0, buildHuman().getLocalityReputation("loc_cidade"));
    }

    @Test
    void character_setReputacaoLocalidade() {
        Character c = buildHuman();
        c.setLocalityReputation("loc_cidade", 50);
        assertEquals(50, c.getLocalityReputation("loc_cidade"));
    }

    @Test
    void character_reputacaoFaccao() {
        Character c = buildHuman();
        c.setFactionReputation("fac_guilda", -30);
        assertEquals(-30, c.getFactionReputation("fac_guilda"));
    }

    // ── RF-FP-03: Habilidades canônicas ──────────────────────────────────────

    @Test
    void skill_possuiExatamente12ValoresCanonicos() {
        // RF-FP-03
        assertEquals(12, Skill.values().length,
                "Habilidade deve ter exatamente 12 habilidades canônicas");
    }

    @Test
    void skill_chavesTecnicasCorretas() {
        // RF-FP-03
        assertEquals("persuasao",           Skill.PERSUASION.getKey());
        assertEquals("intimidacao",         Skill.INTIMIDATION.getKey());
        assertEquals("enganacao",           Skill.DECEPTION.getKey());
        assertEquals("conhecimento_arcano", Skill.ARCANE_KNOWLEDGE.getKey());
        assertEquals("conhecimento_historia", Skill.HISTORY_KNOWLEDGE.getKey());
        assertEquals("conhecimento_reliquias", Skill.RELIC_KNOWLEDGE.getKey());
        assertEquals("herbalismo",          Skill.HERBALISM.getKey());
        assertEquals("alquimia",            Skill.ALCHEMY.getKey());
        assertEquals("furtividade",         Skill.STEALTH.getKey());
        assertEquals("sobrevivencia",       Skill.SURVIVAL.getKey());
        assertEquals("manuseio_armas",      Skill.WEAPON_HANDLING.getKey());
        assertEquals("uso_magia",           Skill.MAGIC_USE.getKey());
    }

    // ── RF-FP-07: Raças canônicas ─────────────────────────────────────────────

    @Test
    void race_possuiExatamente8Valores() {
        // RF-FP-07
        assertEquals(8, Race.values().length,
                "Raca deve ter exatamente 8 raças canônicas");
    }

    @Test
    void race_todasTemAtributosBaseNaoNulos() {
        // RF-FP-07
        for (Race r : Race.values()) {
            assertNotNull(r.getBaseAttributes(),
                    "getAtributosBase() não pode ser nulo para " + r);
        }
    }

    // ── RF-FP-08: Classes canônicas ───────────────────────────────────────────

    @Test
    void characterClass_possuiExatamente12Valores() {
        // RF-FP-08
        assertEquals(12, CharacterClass.values().length,
                "ClassePersonagem deve ter exatamente 12 classes canônicas");
    }

    @Test
    void characterClass_todasTemBonusAtributosNaoNulos() {
        // RF-FP-08
        for (CharacterClass cc : CharacterClass.values()) {
            assertNotNull(cc.getAttributeBonus(),
                    "getBonusAtributos() não pode ser nulo para " + cc);
        }
    }

    // ── RF-FP-10: Recálculo de derivados ─────────────────────────────────────

    @Test
    void setAtributos_recalculaPvMaxEFadigaMax() {
        // RF-FP-10
        Character c = buildHuman();
        double pvMaxAntes    = c.getMaxHealthPoints();
        double fadigaMaxAntes = c.getMaxFatigue();

        // Novo atributo com constitution=10 (muito maior)
        AttributeSet novosAtributos = new AttributeSet(6, 4, 10, 3, 3, 3, 3);
        c.setAttributes(novosAtributos);

        // pvMax = constitution × 10 = 100
        assertEquals(100.0, c.getMaxHealthPoints(), 1e-9);
        assertTrue(c.getMaxHealthPoints() > pvMaxAntes,
                "pvMax deve aumentar quando constituicao aumenta");

        // maxFatigue = (const + vont) × 100 = (10+3)×100 = 1300
        assertEquals(1300.0, c.getMaxFatigue(), 1e-9);
        assertTrue(c.getMaxFatigue() > fadigaMaxAntes,
                "fadigaMax deve aumentar quando constituicao aumenta");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Weapon buildArma(int maos) {
        return new Weapon("Espada", maos, 1.5, MonetaryValue.ofMp(10),
                "tipo_espada", "1d8", DamageType.SLASHING, "curto", "20/x2", 0, 0, 0, 0);
    }
}
