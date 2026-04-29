package br.eng.rodrigogml.mysteryrealms.domain.character.model;

import br.eng.rodrigogml.mysteryrealms.domain.character.enums.CharacterClass;
import br.eng.rodrigogml.mysteryrealms.domain.character.enums.Gender;
import br.eng.rodrigogml.mysteryrealms.domain.character.enums.Race;
import br.eng.rodrigogml.mysteryrealms.domain.character.enums.Skill;
import br.eng.rodrigogml.mysteryrealms.domain.character.service.CharacterAttributeService;
import br.eng.rodrigogml.mysteryrealms.domain.combat.enums.DamageType;
import br.eng.rodrigogml.mysteryrealms.domain.economy.model.MonetaryValue;
import br.eng.rodrigogml.mysteryrealms.domain.economy.model.Weapon;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testes do modelo de Character - RF-FP-01 a RF-FP-10.
 */
class CharacterTest {

    private Character buildHuman() {
        return new Character("Aragorn", "Elessar", Gender.MALE, Race.HUMAN, CharacterClass.WARRIOR, 25);
    }

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

    @Test
    void character_atributosCombinamRacaEClasse() {
        Character c = buildHuman();
        AttributeSet attrs = c.getAttributes();
        assertEquals(6, attrs.strength());
        assertEquals(4, attrs.dexterity());
        assertEquals(5, attrs.constitution());
        assertEquals(3, attrs.intellect());
    }

    @Test
    void character_estadoInicialNivel1() {
        Character c = buildHuman();
        assertEquals(1, c.getCurrentLevel());
        assertEquals(0L, c.getAccumulatedXp());
    }

    @Test
    void character_pvMaxCalculadoDaConstituicao() {
        Character c = buildHuman();
        assertEquals(50.0, c.getMaxHealthPoints(), 1e-9);
        assertEquals(50.0, c.getHealthPoints(), 1e-9);
    }

    @Test
    void character_fadigaMaxCalculada() {
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
        c.setCurrentFatigue(100.0);
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

    @Test
    void character_fadigaAtualClampadaEm120PctDaFadigaMax() {
        Character c = buildHuman();
        c.setCurrentFatigue(9999.0);
        assertEquals(c.getMaxFatigue() * 1.2, c.getCurrentFatigue(), 1e-9);
    }

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

    @Test
    void character_pesoCapacidadesDerivadasBatemComServico() {
        Character c = buildHuman();
        assertEquals(CharacterAttributeService.characterWeight(Race.HUMAN.getBaseWeightKg(Gender.MALE), 5),
                c.getCharacterWeightKg(), 1e-9);
        assertEquals(CharacterAttributeService.maxLoadCapacity(6), c.getMaxLoadCapacityKg(), 1e-9);
        assertEquals(CharacterAttributeService.criticalLoadCapacity(60), c.getCriticalLoadCapacityKg(), 1e-9);
    }

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

    @Test
    void skill_possuiExatamente12ValoresCanonicos() {
        assertEquals(12, Skill.values().length,
                "Habilidade deve ter exatamente 12 habilidades canonicas");
    }

    @Test
    void skill_chavesTecnicasCorretas() {
        assertEquals("persuasao", Skill.PERSUASION.getKey());
        assertEquals("intimidacao", Skill.INTIMIDATION.getKey());
        assertEquals("enganacao", Skill.DECEPTION.getKey());
        assertEquals("conhecimento_arcano", Skill.ARCANE_KNOWLEDGE.getKey());
        assertEquals("conhecimento_historia", Skill.HISTORY_KNOWLEDGE.getKey());
        assertEquals("conhecimento_reliquias", Skill.RELIC_KNOWLEDGE.getKey());
        assertEquals("herbalismo", Skill.HERBALISM.getKey());
        assertEquals("alquimia", Skill.ALCHEMY.getKey());
        assertEquals("furtividade", Skill.STEALTH.getKey());
        assertEquals("sobrevivencia", Skill.SURVIVAL.getKey());
        assertEquals("manuseio_armas", Skill.WEAPON_HANDLING.getKey());
        assertEquals("uso_magia", Skill.MAGIC_USE.getKey());
    }

    @Test
    void race_possuiExatamente8Valores() {
        assertEquals(8, Race.values().length,
                "Raca deve ter exatamente 8 racas canonicas");
    }

    @Test
    void race_todasTemAtributosBaseNaoNulos() {
        for (Race r : Race.values()) {
            assertNotNull(r.getBaseAttributes(),
                    "getBaseAttributes() nao pode ser nulo para " + r);
        }
    }

    @Test
    void race_todasTemPesoBasePorGeneroValido() {
        for (Race r : Race.values()) {
            assertTrue(r.getBaseWeightKg(Gender.MALE) > 0);
            assertTrue(r.getBaseWeightKg(Gender.FEMALE) > 0);
            assertTrue(r.getBaseWeightKg(Gender.OTHER) > 0);
        }
    }

    @Test
    void characterClass_possuiExatamente12Valores() {
        assertEquals(12, CharacterClass.values().length,
                "CharacterClass deve ter exatamente 12 classes canonicas");
    }

    @Test
    void characterClass_todasTemBonusAtributosNaoNulos() {
        for (CharacterClass cc : CharacterClass.values()) {
            assertNotNull(cc.getAttributeBonus(),
                    "getAttributeBonus() nao pode ser nulo para " + cc);
        }
    }

    @Test
    void raceEClasse_expoemBonificacoesDeHabilidade() {
        assertEquals(2, Race.ELF.getSkillBonuses().getBonus(Skill.ARCANE_KNOWLEDGE));
        assertEquals(3, CharacterClass.HUNTER.getSkillBonuses().getBonus(Skill.SURVIVAL));
    }

    @Test
    void skillBonuses_mergeSomaValoresEPreservaImutabilidade() {
        SkillBonuses base = new SkillBonuses(Map.of(Skill.STEALTH, 2));
        SkillBonuses extra = new SkillBonuses(Map.of(Skill.STEALTH, 1, Skill.SURVIVAL, 3));

        SkillBonuses merged = base.merge(extra);

        assertEquals(3, merged.getBonus(Skill.STEALTH));
        assertEquals(3, merged.getBonus(Skill.SURVIVAL));
        assertThrows(UnsupportedOperationException.class,
                () -> merged.bonuses().put(Skill.PERSUASION, 1));
    }

    @Test
    void setAtributos_recalculaPvMaxEFadigaMax() {
        Character c = buildHuman();
        double pvMaxAntes = c.getMaxHealthPoints();
        double fadigaMaxAntes = c.getMaxFatigue();

        AttributeSet novosAtributos = new AttributeSet(6, 4, 10, 3, 3, 3, 3);
        c.setAttributes(novosAtributos);

        assertEquals(100.0, c.getMaxHealthPoints(), 1e-9);
        assertTrue(c.getMaxHealthPoints() > pvMaxAntes);
        assertEquals(1300.0, c.getMaxFatigue(), 1e-9);
        assertTrue(c.getMaxFatigue() > fadigaMaxAntes);
    }

    @Test
    void setAtributos_reclampaFadigaAtualQuandoFadigaMaxDiminui() {
        Character c = buildHuman();
        c.setCurrentFatigue(c.getMaxFatigue() * 1.2);

        c.setAttributes(new AttributeSet(6, 4, 2, 3, 3, 3, 1));

        assertEquals(c.getMaxFatigue() * 1.2, c.getCurrentFatigue(), 1e-9);
    }

    private Weapon buildArma(int maos) {
        return new Weapon("Espada", maos, 1.5, MonetaryValue.ofMp(10),
                "tipo_espada", "1d8", DamageType.SLASHING, "curto", "20/x2", 0, 0, 0, 0);
    }
}
