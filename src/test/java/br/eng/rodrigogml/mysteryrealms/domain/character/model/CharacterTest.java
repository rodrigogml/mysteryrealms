package br.eng.rodrigogml.mysteryrealms.domain.character.model;

import br.eng.rodrigogml.mysteryrealms.domain.character.enums.ClassePersonagem;
import br.eng.rodrigogml.mysteryrealms.domain.character.enums.Genero;
import br.eng.rodrigogml.mysteryrealms.domain.character.enums.Raca;
import br.eng.rodrigogml.mysteryrealms.domain.character.enums.Habilidade;
import br.eng.rodrigogml.mysteryrealms.domain.combat.enums.TipoDano;
import br.eng.rodrigogml.mysteryrealms.domain.economy.model.ValorMonetario;
import br.eng.rodrigogml.mysteryrealms.domain.economy.model.Arma;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes do modelo de Personagem — RF-FP-01 a RF-FP-10.
 */
class CharacterTest {

    private Personagem buildHuman() {
        return new Personagem("Aragorn", "Elessar", Genero.MASCULINO, Raca.HUMANO, ClassePersonagem.GUERREIRO, 25);
    }

    // ── RF-FP-01: Identidade ──────────────────────────────────────────────────

    @Test
    void character_identidadeArmazenada() {
        Personagem c = buildHuman();
        assertEquals("Aragorn", c.getNome());
        assertEquals("Elessar", c.getSobrenome());
        assertEquals(Genero.MASCULINO, c.getGenero());
        assertEquals(Raca.HUMANO, c.getRaca());
        assertEquals(ClassePersonagem.GUERREIRO, c.getClasse());
        assertEquals(25, c.getIdadeInicial());
    }

    @Test
    void character_nomeVazioLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Personagem("", "Elessar", Genero.MASCULINO, Raca.HUMANO, ClassePersonagem.GUERREIRO, 25));
    }

    @Test
    void character_sobrenomeVazioLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Personagem("Aragorn", "", Genero.MASCULINO, Raca.HUMANO, ClassePersonagem.GUERREIRO, 25));
    }

    @Test
    void character_idadeInvalidaLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Personagem("Aragorn", "Elessar", Genero.MASCULINO, Raca.HUMANO, ClassePersonagem.GUERREIRO, 0));
    }

    @Test
    void character_generoNuloLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Personagem("Aragorn", "Elessar", null, Raca.HUMANO, ClassePersonagem.GUERREIRO, 25));
    }

    // ── RF-FP-02: Atributos combinados raça + classe ──────────────────────────

    @Test
    void character_atributosCombinamRacaEClasse() {
        // Humano: (3,3,3,3,3,3,3) + Guerreiro: (3,1,2,0,0,0,0) = (6,4,5,3,3,3,3)
        Personagem c = buildHuman();
        ConjuntoAtributos attrs = c.getAtributos();
        assertEquals(6, attrs.forca());
        assertEquals(4, attrs.destreza());
        assertEquals(5, attrs.constituicao());
        assertEquals(3, attrs.intelecto());
    }

    // ── RF-FP-04: Estado inicial ──────────────────────────────────────────────

    @Test
    void character_estadoInicialNivel1() {
        Personagem c = buildHuman();
        assertEquals(1, c.getNivelAtual());
        assertEquals(0L, c.getXpAcumulado());
    }

    @Test
    void character_pvMaxCalculadoDaConstituicao() {
        // Humano + Guerreiro: constituicao = 5 → PV max = 50
        Personagem c = buildHuman();
        assertEquals(50.0, c.getPontosVidaMax(), 1e-9);
        assertEquals(50.0, c.getPontosVida(), 1e-9);
    }

    @Test
    void character_fadigaMaxCalculada() {
        // constituicao=5, vontade=3 → fadiga max = (5+3)×100 = 800
        Personagem c = buildHuman();
        assertEquals(800.0, c.getFadigaMax(), 1e-9);
    }

    @Test
    void character_moralInicial75() {
        assertEquals(75, buildHuman().getMoral());
    }

    @Test
    void character_fomeSedePct_zerado() {
        Personagem c = buildHuman();
        assertEquals(0.0, c.getFomePct(), 1e-9);
        assertEquals(0.0, c.getSedePct(), 1e-9);
    }

    @Test
    void character_setPontosVidaClampadoAoMax() {
        Personagem c = buildHuman();
        c.setPontosVida(999.0);
        assertEquals(c.getPontosVidaMax(), c.getPontosVida(), 1e-9);
    }

    @Test
    void character_setPontosVidaNaoNegativo() {
        Personagem c = buildHuman();
        c.setPontosVida(-10.0);
        assertEquals(0.0, c.getPontosVida(), 1e-9);
    }

    @Test
    void character_fadigaAtualNuncaMenorQueFadigaMin() {
        Personagem c = buildHuman();
        c.setFadigaMin(200.0);
        c.setFadigaAtual(100.0); // abaixo do min → deve subir para 200
        assertEquals(200.0, c.getFadigaAtual(), 1e-9);
    }

    @Test
    void character_moralClampadoEntre0e100() {
        Personagem c = buildHuman();
        c.setMoral(150);
        assertEquals(100, c.getMoral());
        c.setMoral(-5);
        assertEquals(0, c.getMoral());
    }

    // ── RF-FP-05: Inventário ──────────────────────────────────────────────────

    @Test
    void character_inventarioInicialVazio() {
        Personagem c = buildHuman();
        assertTrue(c.getItensEquipados().isEmpty());
        assertTrue(c.getItensMochila().isEmpty());
        assertEquals(0L, c.getQtdMoedaPrimaria());
    }

    @Test
    void character_equiparItemUmaMao() {
        Personagem c = buildHuman();
        c.equiparItem(buildArma(1));
        assertEquals(1, c.getItensEquipados().size());
    }

    @Test
    void character_equiparItemDuasMaos() {
        Personagem c = buildHuman();
        c.equiparItem(buildArma(2));
        assertEquals(1, c.getItensEquipados().size());
    }

    @Test
    void character_naoPodeEquiparMaisDeDoiSlots() {
        Personagem c = buildHuman();
        c.equiparItem(buildArma(1));
        c.equiparItem(buildArma(1));
        // Terceiro item (mesmo uma mão): não há mais espaço
        assertThrows(IllegalStateException.class, () -> c.equiparItem(buildArma(1)));
    }

    @Test
    void character_naoPodeEquiparDuasMaosComUmaJaOcupada() {
        Personagem c = buildHuman();
        c.equiparItem(buildArma(1));
        assertThrows(IllegalStateException.class, () -> c.equiparItem(buildArma(2)));
    }

    @Test
    void character_cargaAtualIncluiItensEMoedas() {
        Personagem c = buildHuman();
        c.setQtdMoedaPrimaria(10);
        c.adicionarItemMochila(buildArma(1));
        assertTrue(c.getCargaAtualKg() > 0);
    }

    // ── RF-FP-09: Relacionamento e reputação ──────────────────────────────────

    @Test
    void character_relacionamentoNpcPadraoZero() {
        assertEquals(0, buildHuman().getRelacionamentoNpc("npc_001"));
    }

    @Test
    void character_setRelacionamentoClampadoEntreMenos100e100() {
        Personagem c = buildHuman();
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
        Personagem c = buildHuman();
        c.setReputacaoLocalidade("loc_cidade", 50);
        assertEquals(50, c.getReputacaoLocalidade("loc_cidade"));
    }

    @Test
    void character_reputacaoFaccao() {
        Personagem c = buildHuman();
        c.setReputacaoFaccao("fac_guilda", -30);
        assertEquals(-30, c.getReputacaoFaccao("fac_guilda"));
    }

    // ── RF-FP-03: Habilidades canônicas ──────────────────────────────────────

    @Test
    void skill_possuiExatamente12ValoresCanonicos() {
        // RF-FP-03
        assertEquals(12, Habilidade.values().length,
                "Habilidade deve ter exatamente 12 habilidades canônicas");
    }

    @Test
    void skill_chavesTecnicasCorretas() {
        // RF-FP-03
        assertEquals("persuasao",           Habilidade.PERSUASAO.getChave());
        assertEquals("intimidacao",         Habilidade.INTIMIDACAO.getChave());
        assertEquals("enganacao",           Habilidade.ENGANACAO.getChave());
        assertEquals("conhecimento_arcano", Habilidade.CONHECIMENTO_ARCANO.getChave());
        assertEquals("conhecimento_historia", Habilidade.CONHECIMENTO_HISTORIA.getChave());
        assertEquals("conhecimento_reliquias", Habilidade.CONHECIMENTO_RELIQUIAS.getChave());
        assertEquals("herbalismo",          Habilidade.HERBALISMO.getChave());
        assertEquals("alquimia",            Habilidade.ALQUIMIA.getChave());
        assertEquals("furtividade",         Habilidade.FURTIVIDADE.getChave());
        assertEquals("sobrevivencia",       Habilidade.SOBREVIVENCIA.getChave());
        assertEquals("manuseio_armas",      Habilidade.MANUSEIO_ARMAS.getChave());
        assertEquals("uso_magia",           Habilidade.USO_MAGIA.getChave());
    }

    // ── RF-FP-07: Raças canônicas ─────────────────────────────────────────────

    @Test
    void race_possuiExatamente8Valores() {
        // RF-FP-07
        assertEquals(8, Raca.values().length,
                "Raca deve ter exatamente 8 raças canônicas");
    }

    @Test
    void race_todasTemAtributosBaseNaoNulos() {
        // RF-FP-07
        for (Raca r : Raca.values()) {
            assertNotNull(r.getAtributosBase(),
                    "getAtributosBase() não pode ser nulo para " + r);
        }
    }

    // ── RF-FP-08: Classes canônicas ───────────────────────────────────────────

    @Test
    void characterClass_possuiExatamente12Valores() {
        // RF-FP-08
        assertEquals(12, ClassePersonagem.values().length,
                "ClassePersonagem deve ter exatamente 12 classes canônicas");
    }

    @Test
    void characterClass_todasTemBonusAtributosNaoNulos() {
        // RF-FP-08
        for (ClassePersonagem cc : ClassePersonagem.values()) {
            assertNotNull(cc.getBonusAtributos(),
                    "getBonusAtributos() não pode ser nulo para " + cc);
        }
    }

    // ── RF-FP-10: Recálculo de derivados ─────────────────────────────────────

    @Test
    void setAtributos_recalculaPvMaxEFadigaMax() {
        // RF-FP-10
        Personagem c = buildHuman();
        double pvMaxAntes    = c.getPontosVidaMax();
        double fadigaMaxAntes = c.getFadigaMax();

        // Novo atributo com constituicao=10 (muito maior)
        ConjuntoAtributos novosAtributos = new ConjuntoAtributos(6, 4, 10, 3, 3, 3, 3);
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

    private Arma buildArma(int maos) {
        return new Arma("Espada", maos, 1.5, ValorMonetario.deMp(10),
                "tipo_espada", "1d8", TipoDano.CORTE, "curto", "20/x2", 0, 0, 0, 0);
    }
}
