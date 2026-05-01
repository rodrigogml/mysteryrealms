package br.eng.rodrigogml.mysteryrealms.domain.character.service;

import br.eng.rodrigogml.mysteryrealms.common.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes da fórmula de progressão do personagem — RF-PP-02 a RF-PP-08.
 */
class ProgressionServiceTest {

    @Test
    void xpForLevel_aplicaCeilDeterministico() {
        int n = 7;
        double bruto = 50.0 * Math.pow(Math.log(n + 10.0), 10.0);
        long esperado = (long) Math.ceil(bruto);
        assertEquals(esperado, ProgressionService.xpForLevel(n));
        assertTrue(esperado >= bruto);
        assertTrue(esperado - bruto < 1.0);
    }

    @Test
    void xpForLevel_lancaExcecaoParaNivelMenorQueUm() {
        assertThrows(ValidationException.class, () -> ProgressionService.xpForLevel(0));
        assertThrows(ValidationException.class, () -> ProgressionService.xpForLevel(-1));
    }

    @Test
    void totalXpForLevel_somaNivelArredondadoIndividualmente() {
        long esperado = 0;
        for (int i = 1; i <= 15; i++) {
            esperado += ProgressionService.xpForLevel(i);
        }
        assertEquals(esperado, ProgressionService.totalXpForLevel(15));
    }

    @Test
    void levelFromAccumulatedXp_marcoInferiorESuperiorDoNivel2() {
        long limiarNivel2 = ProgressionService.totalXpForLevel(2);

        assertEquals(1, ProgressionService.levelFromAccumulatedXp(limiarNivel2 - 1));
        assertEquals(2, ProgressionService.levelFromAccumulatedXp(limiarNivel2));
        assertEquals(2, ProgressionService.levelFromAccumulatedXp(limiarNivel2 + 1));
    }

    @Test
    void levelFromAccumulatedXp_marcoInferiorESuperiorDoNivel12() {
        long limiarNivel12 = ProgressionService.totalXpForLevel(12);
        assertEquals(11, ProgressionService.levelFromAccumulatedXp(limiarNivel12 - 1));
        assertEquals(12, ProgressionService.levelFromAccumulatedXp(limiarNivel12));
    }

    @Test
    void evaluateLevelUp_semGanho() {
        ProgressionService.LevelUpResult result = ProgressionService.evaluateLevelUp(0L, 1);
        assertEquals(1, result.targetLevel());
        assertEquals(0, result.levelsGained());
        assertEquals(0, result.attributePointsGranted());
        assertEquals(0, result.talentsGranted());
        assertEquals(0, result.resourcesGranted());
    }

    @Test
    void evaluateLevelUp_distribuiPontosNosMarcosAoSubirDe1Para12() {
        long xpNivel12 = ProgressionService.totalXpForLevel(12);
        ProgressionService.LevelUpResult result = ProgressionService.evaluateLevelUp(xpNivel12, 1);

        assertEquals(12, result.targetLevel());
        assertEquals(11, result.levelsGained());
        assertEquals(10, result.attributePointsGranted()); // níveis 2..10 (+9) e nível 12 (+1)
        assertEquals(3, result.talentsGranted()); // níveis 4, 8 e 12
        assertEquals(4, result.resourcesGranted()); // níveis 3, 6, 9 e 12
    }

    @Test
    void peaOnLevelUp_marcosDeFaixa_10_11_12_30_31_33() {
        assertEquals(1, ProgressionService.peaOnLevelUp(10));
        assertEquals(0, ProgressionService.peaOnLevelUp(11));
        assertEquals(1, ProgressionService.peaOnLevelUp(12));
        assertEquals(0, ProgressionService.peaOnLevelUp(30));
        assertEquals(0, ProgressionService.peaOnLevelUp(31));
        assertEquals(1, ProgressionService.peaOnLevelUp(33));
    }

    @Test
    void totalPeaAtLevel_marcosInferiorESuperiorDasFaixas() {
        assertEquals(9, ProgressionService.totalPeaAtLevel(10));
        assertEquals(9, ProgressionService.totalPeaAtLevel(11));
        assertEquals(19, ProgressionService.totalPeaAtLevel(30));
        assertEquals(19, ProgressionService.totalPeaAtLevel(31));
        assertEquals(20, ProgressionService.totalPeaAtLevel(33));
    }

    @Test
    void softCapAttribute_aplicaFloorNoDivisorDeNivel() {
        assertEquals(10, ProgressionService.softCapAttribute(4));
        assertEquals(11, ProgressionService.softCapAttribute(5));
        assertEquals(11, ProgressionService.softCapAttribute(9));
        assertEquals(12, ProgressionService.softCapAttribute(10));
    }

    @Test
    void ppEProficiencia_respeitamMarcos() {
        assertEquals(1, ProgressionService.ppOnLevelUp(7));
        assertEquals(0, ProgressionService.ppOnLevelUp(8));
        assertEquals(0, ProgressionService.proficiencyBonus(9));
        assertEquals(1, ProgressionService.proficiencyBonus(10));
        assertEquals(2, ProgressionService.proficiencyBonus(25));
    }

    @Test
    void unlocksBasicos_respeitamRequisitos() {
        assertTrue(ProgressionService.isSkillSlotUnlocked(3, 1));
        assertFalse(ProgressionService.isSkillSlotUnlocked(7, 2));
        assertTrue(ProgressionService.isSkillSlotUnlocked(8, 2));
        assertFalse(ProgressionService.isSkillSlotUnlocked(30, 3));
        assertFalse(ProgressionService.isUtilityTraitUnlocked(4));
        assertTrue(ProgressionService.isUtilityTraitUnlocked(5));
    }
}
