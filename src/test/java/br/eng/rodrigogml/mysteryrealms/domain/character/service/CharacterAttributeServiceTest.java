package br.eng.rodrigogml.mysteryrealms.domain.character.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testes dos attributes derivados do personagem — RF-FP-06.
 */
class CharacterAttributeServiceTest {

    @Test
    void maxHpEFadiga_respeitamFormulasBase() {
        assertEquals(50.0, CharacterAttributeService.maxHealthPoints(5), 1e-9);
        assertEquals(700.0, CharacterAttributeService.maxFatigue(4, 3), 1e-9);
    }

    @Test
    void characterWeight_marcosConstituicao() {
        assertEquals(75.0 * 0.9, CharacterAttributeService.characterWeight(75, 1), 1e-9);
        assertEquals(75.0, CharacterAttributeService.characterWeight(75, 3), 1e-9);
        assertEquals(75.0 * 1.1, CharacterAttributeService.characterWeight(75, 5), 1e-9);
    }

    @Test
    void cargaAtual_respeitaComposicaoComMoedas() {
        assertEquals(0.512, CharacterAttributeService.currencyWeight(2, 100), 1e-9);
        assertEquals(12.5, CharacterAttributeService.currentLoad(5.0, 7.0, 0.5), 1e-9);
    }

    @Test
    void precisao_respeitaSomaSemDado() {
        assertEquals(8, CharacterAttributeService.precisionBonus(4, 2, 2));
    }

    @Test
    void dano_final_aplicaFloorAntesDeFlat() {
        int dano = CharacterAttributeService.calculateFinalDamage(10, 0, 0, 0, 0, 0.15, 0, 1, 0);
        assertEquals(12, dano); // 10 * 1.15 = 11.5 -> floor 11 -> +1 = 12
    }

    @Test
    void dano_final_naoPermiteResultadoNegativo() {
        int dano = CharacterAttributeService.calculateFinalDamage(1, 0, 0, 0, 0, 0, 0, 0, 100);
        assertEquals(0, dano);
    }

    @Test
    void defesaEBloqueio_reproduzemMesmaRegraDeArredondamento() {
        int defesa = CharacterAttributeService.calculateFinalDefense(10, 0, 0, 0, 0, 0.15, 0, 1, 0);
        int bloqueio = CharacterAttributeService.calculateFinalBlock(10, 0, 0, 0, 0.15, 0, 1, 0);
        assertEquals(12, defesa);
        assertEquals(12, bloqueio);
    }

    @Test
    void matrizDeArredondamento_percentualPositivoENegativo() {
        assertEquals(11, CharacterAttributeService.calculateFinalDamage(10, 0, 0, 0, 0, 0.19, 0, 0, 0));
        assertEquals(8, CharacterAttributeService.calculateFinalDamage(10, 0, 0, 0, 0, 0, 0.11, 0, 0));
        assertEquals(9, CharacterAttributeService.calculateFinalDamage(10, 0, 0, 0, 0, 0.20, 0.21, 0, 0));
    }
}
