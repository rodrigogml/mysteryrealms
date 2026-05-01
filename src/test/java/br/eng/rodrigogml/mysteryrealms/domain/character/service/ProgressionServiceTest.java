package br.eng.rodrigogml.mysteryrealms.domain.character.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import br.eng.rodrigogml.mysteryrealms.common.exception.DomainException;
import br.eng.rodrigogml.mysteryrealms.common.exception.ValidationException;

/**
 * Testes da fórmula de progressão do personagem — RF-PP-02 a RF-PP-08.
 */
class ProgressionServiceTest {

    @Test
    void xpForLevel_lancaExcecaoParaNivelMenorQueUm() {
        assertThrows(ValidationException.class, () -> ProgressionService.xpForLevel(0));
        assertThrows(ValidationException.class, () -> ProgressionService.xpForLevel(-1));
    }

    @Test
    void xpForLevel_retornaValorPositivo() {
        for (int n = 1; n <= 20; n++) {
            assertTrue(ProgressionService.xpForLevel(n) > 0, "XP deve ser > 0 para nível " + n);
        }
    }

    @Test
    void xpForLevel_cresceMonotonamente() {
        long prev = 0;
        for (int n = 1; n <= 50; n++) {
            long xp = ProgressionService.xpForLevel(n);
            assertTrue(xp > prev, "XP do nível " + n + " deve ser maior que do nível " + (n - 1));
            prev = xp;
        }
    }

    @Test
    void xpForLevel_correspondeFormulaMatematica() {
        for (int n = 1; n <= 10; n++) {
            long expected = (long) Math.ceil(50.0 * Math.pow(Math.log(n + 10.0), 10.0));
            assertEquals(expected, ProgressionService.xpForLevel(n));
        }
    }

    @Test
    void totalXpForLevel_lancaExcecaoParaNivelMenorQueUm() {
        assertThrows(ValidationException.class, () -> ProgressionService.totalXpForLevel(0));
        assertThrows(ValidationException.class, () -> ProgressionService.totalXpForLevel(-1));
    }

    @Test
    void totalXpForLevel_eSomaCumulativa() {
        long soma = 0;
        for (int n = 1; n <= 10; n++) {
            soma += ProgressionService.xpForLevel(n);
            assertEquals(soma, ProgressionService.totalXpForLevel(n));
        }
    }

    @Test
    void shouldLevelUp_falsoComXpInsuficiente() {
        assertFalse(ProgressionService.shouldLevelUp(0L, 1));
    }

    @Test
    void shouldLevelUp_lancaExcecaoParaEntradasInvalidas() {
        assertThrows(ValidationException.class, () -> ProgressionService.shouldLevelUp(-1L, 1));
        assertThrows(ValidationException.class, () -> ProgressionService.shouldLevelUp(0L, 0));
    }

    @Test
    void shouldLevelUp_verdadeiroAoAtingirLimiar() {
        long limiar = ProgressionService.totalXpForLevel(2);
        assertTrue(ProgressionService.shouldLevelUp(limiar, 1));
    }

    @Test
    void shouldLevelUp_verdadeiroAcimaDoLimiar() {
        long limiar = ProgressionService.totalXpForLevel(2);
        assertTrue(ProgressionService.shouldLevelUp(limiar + 1, 1));
    }

    @Test
    void shouldLevelUp_falsoUmAbaixoDoLimiar() {
        long limiar = ProgressionService.totalXpForLevel(2);
        assertFalse(ProgressionService.shouldLevelUp(limiar - 1, 1));
    }

    @Test
    void levelFromAccumulatedXp_retornaNivel1SemXp() {
        assertEquals(1, ProgressionService.levelFromAccumulatedXp(0L));
    }

    @Test
    void levelFromAccumulatedXp_lancaExcecaoParaXpNegativo() {
        assertThrows(ValidationException.class, () -> ProgressionService.levelFromAccumulatedXp(-1L));
    }

    @Test
    void levelFromAccumulatedXp_sobeMultiplosNiveisQuandoUltrapassaVariosLimiares() {
        long xpNivel3 = ProgressionService.totalXpForLevel(3);
        assertEquals(3, ProgressionService.levelFromAccumulatedXp(xpNivel3));
        assertEquals(2, ProgressionService.levelsGained(xpNivel3, 1));
    }

    @Test
    void levelFromAccumulatedXp_respeitaLimiarExatoDoNivelAnterior() {
        long xpUmAbaixoDoNivel3 = ProgressionService.totalXpForLevel(3) - 1;
        assertEquals(2, ProgressionService.levelFromAccumulatedXp(xpUmAbaixoDoNivel3));
        assertEquals(0, ProgressionService.levelsGained(xpUmAbaixoDoNivel3, 2));
    }

    @Test
    void totalPeaAtLevel_zeroPadraoEmNivel1() {
        assertEquals(0, ProgressionService.totalPeaAtLevel(1));
    }

    @Test
    void totalPeaAtLevel_umPorNivelDe2a10() {
        for (int level = 2; level <= 10; level++) {
            assertEquals(level - 1, ProgressionService.totalPeaAtLevel(level));
        }
    }

    @Test
    void peaOnLevelUp_umPorNivelEntre2e10() {
        for (int level = 2; level <= 10; level++) {
            assertEquals(1, ProgressionService.peaOnLevelUp(level));
        }
    }

    @Test
    void peaOnLevelUp_aCada2NiveisEntre11e30() {
        int totalPea = 9;
        for (int level = 11; level <= 30; level++) {
            int pea = ProgressionService.peaOnLevelUp(level);
            assertTrue(pea == 0 || pea == 1);
            totalPea += pea;
        }
        assertEquals(19, totalPea);
    }

    @Test
    void totalPeaAtLevel_respeitaMarcosDeFaixa() {
        assertEquals(9, ProgressionService.totalPeaAtLevel(10));
        assertEquals(9, ProgressionService.totalPeaAtLevel(11));
        assertEquals(10, ProgressionService.totalPeaAtLevel(12));
        assertEquals(19, ProgressionService.totalPeaAtLevel(30));
        assertEquals(19, ProgressionService.totalPeaAtLevel(31));
        assertEquals(20, ProgressionService.totalPeaAtLevel(33));
    }

    @Test
    void peaOnLevelUp_aCada3NiveisAPartirDe31() {
        assertEquals(0, ProgressionService.peaOnLevelUp(31));
        assertEquals(0, ProgressionService.peaOnLevelUp(32));
        assertEquals(1, ProgressionService.peaOnLevelUp(33));
        assertEquals(0, ProgressionService.peaOnLevelUp(34));
        assertEquals(0, ProgressionService.peaOnLevelUp(35));
        assertEquals(1, ProgressionService.peaOnLevelUp(36));
    }

    @Test
    void ppOnLevelUp_umEmNivelImpar() {
        for (int level = 1; level <= 20; level += 2) {
            assertEquals(1, ProgressionService.ppOnLevelUp(level));
        }
    }

    @Test
    void ppOnLevelUp_zeroEmNivelPar() {
        for (int level = 2; level <= 20; level += 2) {
            assertEquals(0, ProgressionService.ppOnLevelUp(level));
        }
    }

    @Test
    void totalPpAtLevel_cresceApenasEmNiveisImpares() {
        assertEquals(1, ProgressionService.totalPpAtLevel(1));
        assertEquals(1, ProgressionService.totalPpAtLevel(2));
        assertEquals(2, ProgressionService.totalPpAtLevel(3));
        assertEquals(2, ProgressionService.totalPpAtLevel(4));
        assertEquals(3, ProgressionService.totalPpAtLevel(5));
    }

    @Test
    void totalPpAtLevel_lancaExcecaoParaNivelMenorQueUm() {
        assertThrows(ValidationException.class, () -> ProgressionService.totalPpAtLevel(0));
    }

    @Test
    void proficiencyBonus_respeitaFaixas() {
        assertEquals(0, ProgressionService.proficiencyBonus(1));
        assertEquals(0, ProgressionService.proficiencyBonus(9));
        assertEquals(1, ProgressionService.proficiencyBonus(10));
        assertEquals(1, ProgressionService.proficiencyBonus(24));
        assertEquals(2, ProgressionService.proficiencyBonus(25));
        assertEquals(2, ProgressionService.proficiencyBonus(49));
        assertEquals(3, ProgressionService.proficiencyBonus(50));
        assertEquals(3, ProgressionService.proficiencyBonus(100));
    }

    @Test
    void attributeSoftCap_respeitaFormula() {
        assertEquals(10, ProgressionService.softCapAttribute(1));
        assertEquals(11, ProgressionService.softCapAttribute(5));
        assertEquals(12, ProgressionService.softCapAttribute(10));
        assertEquals(14, ProgressionService.softCapAttribute(20));
    }

    @Test
    void skillFinalValue_somaCorretamente() {
        assertEquals(8, ProgressionService.finalSkillValue(4, 2, 10, 1));
    }

    @Test
    void skillFinalValue_semModificadores() {
        assertEquals(5, ProgressionService.finalSkillValue(5, 0, 1, 0));
    }

    @Test
    void xpNuncaDiminui_totalXpPermaneceCrescente() {
        long prev = 0;
        for (int n = 1; n <= 20; n++) {
            long total = ProgressionService.totalXpForLevel(n);
            assertTrue(total >= prev);
            prev = total;
        }
    }

    @Test
    void balanceVersion_naoNuloENaoVazio() {
        assertNotNull(ProgressionService.BALANCE_VERSION);
        assertFalse(ProgressionService.BALANCE_VERSION.isBlank());
    }

    @Test
    void balanceVersion_formatoSemantico() {
        assertTrue(ProgressionService.BALANCE_VERSION.matches("BAL-\\d+\\.\\d+\\.\\d+"));
    }

    @Test
    void balanceVersion_correspondeVersaoAtivaDoRequisito() {
        assertEquals("BAL-1.0.0", ProgressionService.BALANCE_VERSION);
    }

    @Test
    void utilityTraitUnlocked_nivel5() {
        assertFalse(ProgressionService.isUtilityTraitUnlocked(4));
        assertTrue(ProgressionService.isUtilityTraitUnlocked(5));
        assertTrue(ProgressionService.isUtilityTraitUnlocked(10));
    }

    @Test
    void abilitySlot_respeitaMarcos() {
        assertFalse(ProgressionService.isSkillSlotUnlocked(2, 1));
        assertTrue(ProgressionService.isSkillSlotUnlocked(3, 1));
        assertFalse(ProgressionService.isSkillSlotUnlocked(7, 2));
        assertTrue(ProgressionService.isSkillSlotUnlocked(8, 2));
        assertFalse(ProgressionService.isSkillSlotUnlocked(99, 3));
    }

    @Test
    void signatureAbilityUnlocked_nivel12() {
        assertFalse(ProgressionService.isSignatureSkillUnlocked(11));
        assertTrue(ProgressionService.isSignatureSkillUnlocked(12));
    }

    @Test
    void advancedSpecializationUnlocked_nivel20() {
        assertFalse(ProgressionService.isAdvancedSpecializationUnlocked(19));
        assertTrue(ProgressionService.isAdvancedSpecializationUnlocked(20));
    }

    @Test
    void masteryCycleActive_nivel30() {
        assertFalse(ProgressionService.isMasteryCycleActive(29));
        assertTrue(ProgressionService.isMasteryCycleActive(30));
    }

    @Test
    void evaluateLevelUp_semGanho_retornaZeroMarcos() {
        ProgressionService.LevelUpResult result = ProgressionService.evaluateLevelUp(0L, 1);
        assertEquals(1, result.targetLevel());
        assertEquals(0, result.levelsGained());
        assertEquals(0, result.attributePointsGranted());
        assertEquals(0, result.talentsGranted());
        assertEquals(0, result.resourcesGranted());
    }

    @Test
    void evaluateLevelUp_comMultiplosNiveis_somaMarcosCorretamente() {
        long xpNivel12 = ProgressionService.totalXpForLevel(12);
        ProgressionService.LevelUpResult result = ProgressionService.evaluateLevelUp(xpNivel12, 1);
        assertEquals(12, result.targetLevel());
        assertEquals(11, result.levelsGained());
        assertEquals(10, result.attributePointsGranted());
        assertEquals(3, result.talentsGranted());
        assertEquals(4, result.resourcesGranted());
    }

    @Test
    void milestoneForLevel_refleteTabelaCentral() {
        ProgressionService.ProgressionMilestone level12 = ProgressionService.milestoneForLevel(12);
        assertTrue(level12.attributePointGranted());
        assertTrue(level12.talentGranted());
        assertTrue(level12.resourceGranted());
    }
}
