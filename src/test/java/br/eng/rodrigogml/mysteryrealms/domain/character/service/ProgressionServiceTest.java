package br.eng.rodrigogml.mysteryrealms.domain.character.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes da fórmula de progressão do personagem — RF-PP-02 a RF-PP-08.
 */
class ProgressionServiceTest {

    // ── RF-PP-02 / RF-PP-03: fórmula de XP ───────────────────────────────────

    @Test
    void xpForLevel_lançaExcecaoParaNivelMenorQueUm() {
        assertThrows(IllegalArgumentException.class, () -> ProgressionService.xpForLevel(0));
        assertThrows(IllegalArgumentException.class, () -> ProgressionService.xpForLevel(-1));
    }

    @Test
    void xpForLevel_retornaValorPositivo() {
        for (int n = 1; n <= 20; n++) {
            assertTrue(ProgressionService.xpForLevel(n) > 0,
                    "XP deve ser > 0 para nível " + n);
        }
    }

    @Test
    void xpForLevel_cresceMonotonamente() {
        long prev = 0;
        for (int n = 1; n <= 50; n++) {
            long xp = ProgressionService.xpForLevel(n);
            assertTrue(xp > prev,
                    "XP do nível " + n + " deve ser maior que do nível " + (n - 1));
            prev = xp;
        }
    }

    @Test
    void xpForLevel_correspondeFórmulaMatemática() {
        for (int n = 1; n <= 10; n++) {
            long expected = (long) Math.ceil(50.0 * Math.pow(Math.log(n + 10.0), 10.0));
            assertEquals(expected, ProgressionService.xpForLevel(n),
                    "XP para nível " + n + " deve corresponder a ceil(50 × pow(ln(n+10), 10))");
        }
    }

    @Test
    void xpTotalForLevel_éSomaCumulativa() {
        long soma = 0;
        for (int n = 1; n <= 10; n++) {
            soma += ProgressionService.xpForLevel(n);
            assertEquals(soma, ProgressionService.totalXpForLevel(n),
                    "xpTotalParaNivel(" + n + ") deve ser a soma de xpParaNivel(1) a xpParaNivel(" + n + ")");
        }
    }

    // ── RF-PP-04: subida de nível ─────────────────────────────────────────────

    @Test
    void shouldLevelUp_falsoComXpInsuficiente() {
        assertFalse(ProgressionService.shouldLevelUp(0L, 1),
                "Sem XP não deve subir de nível");
    }

    @Test
    void shouldLevelUp_verdadeiroAoAtingirLimiar() {
        long limiar = ProgressionService.totalXpForLevel(2);
        assertTrue(ProgressionService.shouldLevelUp(limiar, 1),
                "Deve subir de nível ao atingir exatamente o XP total do próximo nível");
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

    // ── RF-PP-05: PEA ─────────────────────────────────────────────────────────

    @Test
    void totalPeaAtLevel_zeroPadraoEmNivel1() {
        assertEquals(0, ProgressionService.totalPeaAtLevel(1));
    }

    @Test
    void totalPeaAtLevel_umPorNivelDe2a10() {
        for (int level = 2; level <= 10; level++) {
            assertEquals(level - 1, ProgressionService.totalPeaAtLevel(level),
                    "Total PEA no nível " + level + " deve ser " + (level - 1));
        }
    }

    @Test
    void peaOnLevelUp_umPorNivelEntre2e10() {
        for (int level = 2; level <= 10; level++) {
            assertEquals(1, ProgressionService.peaOnLevelUp(level),
                    "PEA ao subir para nível " + level + " deve ser 1");
        }
    }

    @Test
    void peaOnLevelUp_aCada2NiveisEntre11e30() {
        int totalPEA = 9; // 9 PEA acumulados até nível 10
        for (int level = 11; level <= 30; level++) {
            int pea = ProgressionService.peaOnLevelUp(level);
            assertTrue(pea == 0 || pea == 1,
                    "PEA ao subir para nível " + level + " deve ser 0 ou 1");
            totalPEA += pea;
        }
        assertEquals(9 + 10, totalPEA, "Total de PEA de nível 11 a 30 deve ser 10");
    }

    @Test
    void peaOnLevelUp_aCada3NiveisApartirDe31() {
        // a cada 3 níveis: PEA em 33, 36, 39, ...
        assertEquals(0, ProgressionService.peaOnLevelUp(31));
        assertEquals(0, ProgressionService.peaOnLevelUp(32));
        assertEquals(1, ProgressionService.peaOnLevelUp(33));
        assertEquals(0, ProgressionService.peaOnLevelUp(34));
        assertEquals(0, ProgressionService.peaOnLevelUp(35));
        assertEquals(1, ProgressionService.peaOnLevelUp(36));
    }

    // ── RF-PP-06: PP ──────────────────────────────────────────────────────────

    @Test
    void ppOnLevelUp_umEmNivelImpar() {
        for (int level = 1; level <= 20; level += 2) {
            assertEquals(1, ProgressionService.ppOnLevelUp(level),
                    "PP ao subir para nível ímpar " + level + " deve ser 1");
        }
    }

    @Test
    void ppOnLevelUp_zeroEmNivelPar() {
        for (int level = 2; level <= 20; level += 2) {
            assertEquals(0, ProgressionService.ppOnLevelUp(level),
                    "PP ao subir para nível par " + level + " deve ser 0");
        }
    }

    @Test
    void totalPpAtLevel_cresceApenadEmNiveisImpares() {
        assertEquals(1, ProgressionService.totalPpAtLevel(1));
        assertEquals(1, ProgressionService.totalPpAtLevel(2));
        assertEquals(2, ProgressionService.totalPpAtLevel(3));
        assertEquals(2, ProgressionService.totalPpAtLevel(4));
        assertEquals(3, ProgressionService.totalPpAtLevel(5));
    }

    // ── RF-PP-06: bônus de proficiência ──────────────────────────────────────

    @Test
    void proficiencyBonus_zeroPadraoAbaixoDe10() {
        for (int level = 1; level <= 9; level++) {
            assertEquals(0, ProgressionService.proficiencyBonus(level));
        }
    }

    @Test
    void proficiencyBonus_umEntre10e24() {
        assertEquals(1, ProgressionService.proficiencyBonus(10));
        assertEquals(1, ProgressionService.proficiencyBonus(24));
    }

    @Test
    void proficiencyBonus_doisEntre25e49() {
        assertEquals(2, ProgressionService.proficiencyBonus(25));
        assertEquals(2, ProgressionService.proficiencyBonus(49));
    }

    @Test
    void proficiencyBonus_tresAPartirDe50() {
        assertEquals(3, ProgressionService.proficiencyBonus(50));
        assertEquals(3, ProgressionService.proficiencyBonus(100));
    }

    // ── RF-PP-05: limite suave de atributo ───────────────────────────────────

    @Test
    void attributeSoftCap_nivel1() {
        assertEquals(10, ProgressionService.softCapAttribute(1));
    }

    @Test
    void attributeSoftCap_nivel5() {
        assertEquals(11, ProgressionService.softCapAttribute(5));
    }

    @Test
    void attributeSoftCap_nivel10() {
        assertEquals(12, ProgressionService.softCapAttribute(10));
    }

    @Test
    void attributeSoftCap_nivel20() {
        assertEquals(14, ProgressionService.softCapAttribute(20));
    }

    // ── RF-PP-06: habilidade final ────────────────────────────────────────────

    @Test
    void skillFinalValue_somaCorretamente() {
        // atributo=4, pp=2, level=10 (bonusProficiency=1), mod=1 → 4+2+1+1=8
        assertEquals(8, ProgressionService.finalSkillValue(4, 2, 10, 1));
    }

    @Test
    void skillFinalValue_semModificadores() {
        // atributo=5, pp=0, level=1 (bonus=0), mod=0 → 5
        assertEquals(5, ProgressionService.finalSkillValue(5, 0, 1, 0));
    }

    // ── RF-PP-01: XP nunca diminui ───────────────────────────────────────────

    @Test
    void xpNuncaDiminui_semXpNaoSobe() {
        // RF-PP-01
        assertFalse(ProgressionService.shouldLevelUp(0L, 1),
                "Sem XP acumulado, personagem não deve subir de nível");
    }

    @Test
    void xpNuncaDiminui_xpCrescente() {
        // RF-PP-01 — totalXpForLevel é crescente (XP não diminui)
        long prev = 0;
        for (int n = 1; n <= 20; n++) {
            long total = ProgressionService.totalXpForLevel(n);
            assertTrue(total >= prev,
                    "xpTotalParaNivel deve ser crescente: nível " + n);
            prev = total;
        }
    }

    // ── RF-PP-08: versionamento de balanceamento ──────────────────────────────

    @Test
    void balanceVersion_naoNuloENaoVazio() {
        // RF-PP-08
        assertNotNull(ProgressionService.BALANCE_VERSION,
                "Versão de balanceamento não pode ser nula");
        assertFalse(ProgressionService.BALANCE_VERSION.isBlank(),
                "Versão de balanceamento não pode ser vazia");
    }

    @Test
    void balanceVersion_formatoSemantic() {
        // RF-PP-08 — formato esperado: BAL-<major>.<minor>.<patch>
        assertTrue(ProgressionService.BALANCE_VERSION.matches("BAL-\\d+\\.\\d+\\.\\d+"),
                "Versão de balanceamento deve seguir o formato BAL-<major>.<minor>.<patch>");
    }



    @Test
    void abilitySlot_slot1DesbloqueadoNivel3() {
        // RF-PP-07
        assertFalse(ProgressionService.isSkillSlotUnlocked(2, 1));
        assertTrue(ProgressionService.isSkillSlotUnlocked(3, 1));
    }

    @Test
    void abilitySlot_slot2DesbloqueadoNivel8() {
        // RF-PP-07
        assertFalse(ProgressionService.isSkillSlotUnlocked(7, 2));
        assertTrue(ProgressionService.isSkillSlotUnlocked(8, 2));
    }

    @Test
    void abilitySlot_slotInexistente_false() {
        // RF-PP-07
        assertFalse(ProgressionService.isSkillSlotUnlocked(99, 3));
    }

    @Test
    void signatureAbilityUnlocked_nivel12() {
        // RF-PP-07
        assertFalse(ProgressionService.isSignatureSkillUnlocked(11));
        assertTrue(ProgressionService.isSignatureSkillUnlocked(12));
    }

    @Test
    void advancedSpecializationUnlocked_nivel20() {
        // RF-PP-07
        assertFalse(ProgressionService.isAdvancedSpecializationUnlocked(19));
        assertTrue(ProgressionService.isAdvancedSpecializationUnlocked(20));
    }

    @Test
    void masteryCycleActive_nivel30() {
        // RF-PP-07
        assertFalse(ProgressionService.isMasteryCycleActive(29));
        assertTrue(ProgressionService.isMasteryCycleActive(30));
    }
}
