package br.eng.rodrigogml.mysteryrealms.domain.world;

import br.eng.rodrigogml.mysteryrealms.domain.world.enums.ModoNavegacaoCluster;
import br.eng.rodrigogml.mysteryrealms.domain.world.enums.ClassificacaoConexao;
import br.eng.rodrigogml.mysteryrealms.domain.world.enums.TipoLocalizacao;
import br.eng.rodrigogml.mysteryrealms.domain.world.model.*;
import br.eng.rodrigogml.mysteryrealms.domain.world.service.ServicoNavegacao;
import br.eng.rodrigogml.mysteryrealms.domain.world.service.ServicoValidacaoHierarquia;
import br.eng.rodrigogml.mysteryrealms.domain.world.service.ServicoTempoMundo;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de Mundo e Navegação — RF-MN-01 a RF-MN-13.
 */
class WorldNavigationTest {

    // ── RF-MN-01 / RF-MN-02: Zona ────────────────────────────────────────────

    @Test
    void zone_tipoNavegavelEhZona() {
        assertEquals(TipoLocalizacao.ZONA, Zona.TIPO_NAVEGAVEL);
    }

    @Test
    void zone_criacaoValida() {
        Zona z = buildZona("zona_praca", "Praça Central");
        assertEquals("zona_praca", z.idZona());
        assertEquals("Praça Central", z.nome());
        assertTrue(z.acessivel());
    }

    @Test
    void zone_idSemPrefixoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Zona("praca", "Praça", "loc_cidade", 1.0, 1.0, true, null));
    }

    @Test
    void zone_nomeVazioLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Zona("zona_praca", "", "loc_cidade", 1.0, 1.0, true, null));
    }

    // ── RF-MN-03: AmbienteJogo ────────────────────────────────────────────

    @Test
    void environment_tipoNavegavelEhAmbiente() {
        assertEquals(TipoLocalizacao.AMBIENTE, AmbienteJogo.TIPO_NAVEGAVEL);
    }

    @Test
    void environment_criacaoValida() {
        AmbienteJogo e = buildEnv("amb_taverna");
        assertEquals("amb_taverna", e.idAmbiente());
        assertEquals("zona_praca", e.idZona());
    }

    @Test
    void environment_idSemPrefixoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new AmbienteJogo("taverna", "Taverna", "zona_praca", 0, 0, true, null));
    }

    // ── RF-MN-04: Conexao ────────────────────────────────────────────────

    @Test
    void connection_criacaoValida() {
        Conexao c = buildConnection("conn_001", "zona_praca", List.of("zona_mercado"));
        assertEquals("conn_001", c.idConexao());
        assertEquals("zona_praca", c.origemId());
        assertEquals(ClassificacaoConexao.PACIFICADO, c.classificacao());
    }

    @Test
    void connection_semDestinosLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Conexao("conn_001", "zona_praca", List.of(),
                        true, ClassificacaoConexao.PACIFICADO, 0, 0, "tab_001"));
    }

    @Test
    void connection_penaldadeForaDaFaixaLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Conexao("conn_001", "zona_praca", List.of("zona_m"),
                        true, ClassificacaoConexao.PACIFICADO, 20, 0, "tab_001"));
    }

    @Test
    void connection_chanceInterrupcaoForaDaFaixaLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Conexao("conn_001", "zona_praca", List.of("zona_m"),
                        true, ClassificacaoConexao.PACIFICADO, 5, 101, "tab_001"));
    }

    // ── RF-MN-05: Cluster ───────────────────────────────────────────────────

    @Test
    void cluster_criacaoValida() {
        Cluster cl = new Cluster("cl_cidade", "Centro", "loc_cidade",
                List.of("zona_praca", "zona_mercado"), List.of("conn_001"),
                ModoNavegacaoCluster.LIVRE);
        assertEquals("cl_cidade", cl.idCluster());
        assertEquals(ModoNavegacaoCluster.LIVRE, cl.modoNavegacao());
    }

    @Test
    void cluster_semPontosLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Cluster("cl_001", "Nome", "loc_001",
                        List.of(), List.of(), ModoNavegacaoCluster.LIVRE));
    }

    // ── RF-MN-07: Distâncias ─────────────────────────────────────────────────

    @Test
    void navigationService_distanciaBase_pontoMesmo() {
        assertEquals(0.0, ServicoNavegacao.distanciaBaseKm(1.0, 1.0, 1.0, 1.0), 1e-9);
    }

    @Test
    void navigationService_distanciaBase_horizontalUmUnidade() {
        // sqrt(1^2 + 0^2) × 10 = 10 km
        assertEquals(10.0, ServicoNavegacao.distanciaBaseKm(0.0, 0.0, 1.0, 0.0), 1e-9);
    }

    @Test
    void navigationService_distanciaBase_diagonal() {
        // sqrt(3^2 + 4^2) × 10 = 5 × 10 = 50 km
        assertEquals(50.0, ServicoNavegacao.distanciaBaseKm(0, 0, 3, 4), 1e-9);
    }

    @Test
    void navigationService_distanciaAjustada_semPenalidade() {
        assertEquals(50.0, ServicoNavegacao.distanciaAjustadaKm(50.0, 0.0), 1e-9);
    }

    @Test
    void navigationService_distanciaAjustada_comPenalidade10Pct() {
        // 50 × 1.10 = 55
        assertEquals(55.0, ServicoNavegacao.distanciaAjustadaKm(50.0, 10.0), 1e-9);
    }

    // ── RF-MN-08: Resolução de destino ──────────────────────────────────────

    @Test
    void navigationService_resolveDestination_primeiroAcessivel() {
        Conexao conn = buildConnection("c", "o", List.of("zona_a", "zona_b", "zona_c"));
        Map<String, Boolean> nodes = Map.of("zona_a", false, "zona_b", true, "zona_c", true);
        Optional<String> dest = ServicoNavegacao.resolverDestino(conn, nodes);
        assertTrue(dest.isPresent());
        assertEquals("zona_b", dest.get());
    }

    @Test
    void navigationService_resolveDestination_nenhumAcessivel() {
        Conexao conn = buildConnection("c", "o", List.of("zona_a", "zona_b"));
        Map<String, Boolean> nodes = Map.of("zona_a", false, "zona_b", false);
        assertTrue(ServicoNavegacao.resolverDestino(conn, nodes).isEmpty());
    }

    @Test
    void navigationService_resolveDestination_nodeDesconhecidoNaoAcessivel() {
        Conexao conn = buildConnection("c", "o", List.of("zona_x"));
        // "zona_x" não está no mapa → não é acessível
        assertTrue(ServicoNavegacao.resolverDestino(conn, Map.of()).isEmpty());
    }

    // ── RF-MN-09: Interrupções ──────────────────────────────────────────────

    @Test
    void navigationService_interrupcao_segmentoCompleto() {
        assertEquals(10.0, ServicoNavegacao.chanceInterrupcaoPorSegmento(10.0, 1.0), 1e-9);
    }

    @Test
    void navigationService_interrupcao_segmentoParcial() {
        // chance 10% × 0.5 km = 5%
        assertEquals(5.0, ServicoNavegacao.chanceInterrupcaoPorSegmento(10.0, 0.5), 1e-9);
    }

    @Test
    void navigationService_interrupcao_acimaDe1kmUsaChanceDireta() {
        assertEquals(10.0, ServicoNavegacao.chanceInterrupcaoPorSegmento(10.0, 5.0), 1e-9);
    }

    // ── RF-MN-10: Tempo de deslocamento ─────────────────────────────────────

    @Test
    void navigationService_travelTime_formulaCorreta() {
        // 10 km / 5 kmh = 2 h × 60 min/h = 120 min
        assertEquals(120L, ServicoNavegacao.tempoDeslocamentoMinutos(10.0, 5.0, 60));
    }

    @Test
    void navigationService_travelTime_arredondamentoCeil() {
        // 1 km / 3 kmh = 0.333 h × 60 = 20 min (ceil)
        assertEquals(20L, ServicoNavegacao.tempoDeslocamentoMinutos(1.0, 3.0, 60));
    }

    @Test
    void navigationService_travelTime_minimoUmMinuto() {
        assertEquals(1L, ServicoNavegacao.tempoDeslocamentoMinutos(0.001, 100.0, 60));
    }

    @Test
    void navigationService_velocidadeZeroLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> ServicoNavegacao.tempoDeslocamentoMinutos(10.0, 0.0, 60));
    }

    // ── RF-MN-12: ConfiguracaoMundo ────────────────────────────────────────────────

    @Test
    void worldConfig_criacaoValida() {
        ConfiguracaoMundo config = buildConfiguracaoMundo();
        assertEquals("mundo_teste", config.idMundo());
        assertEquals(60, config.minutosPorHora());
        assertEquals(24, config.horasPorDia());
        assertEquals(360, config.diasPorAno());
        assertEquals(1440, config.minutosPorDia());
    }

    @Test
    void worldConfig_minutosPorHoraInvalidoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new ConfiguracaoMundo("m", 0, 24, 360,
                        List.of(new FaseDia("dia", 0, 1439)),
                        List.of(new Estacao("verao", 1, 360)), 0));
    }

    @Test
    void worldConfig_semFazesDiaLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new ConfiguracaoMundo("m", 60, 24, 360,
                        List.of(),
                        List.of(new Estacao("verao", 1, 360)), 0));
    }

    // ── RF-MN-12: FaseDia e Estacao ─────────────────────────────────────────

    @Test
    void dayPhase_criacaoValida() {
        FaseDia fase = new FaseDia("dia", 360, 1080);
        assertEquals("dia", fase.idFase());
        assertEquals(360, fase.inicioMinDia());
    }

    @Test
    void dayPhase_fimMenorQueInicioLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> new FaseDia("dia", 500, 400));
    }

    @Test
    void season_criacaoValida() {
        Estacao s = new Estacao("verao", 1, 90);
        assertEquals("verao", s.idEstacao());
    }

    @Test
    void season_fimMenorQueInicioLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> new Estacao("verao", 90, 1));
    }

    // ── RF-MN-12 / RF-MN-13: ServicoTempoMundo ───────────────────────────────

    @Test
    void worldTimeService_avancaTempo() {
        assertEquals(100L, ServicoTempoMundo.avancarTempo(0L, 100L));
        assertEquals(200L, ServicoTempoMundo.avancarTempo(100L, 100L));
    }

    @Test
    void worldTimeService_avancaTempoNegativoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> ServicoTempoMundo.avancarTempo(100L, -1L));
    }

    @Test
    void worldTimeService_minuteOfDay() {
        ConfiguracaoMundo config = buildConfiguracaoMundo(); // 1440 min/dia
        assertEquals(0, ServicoTempoMundo.minutosDoDia(0L, config));
        assertEquals(60, ServicoTempoMundo.minutosDoDia(60L, config));
        assertEquals(0, ServicoTempoMundo.minutosDoDia(1440L, config)); // novo dia
    }

    @Test
    void worldTimeService_dayOfYear() {
        ConfiguracaoMundo config = buildConfiguracaoMundo(); // 360 dias/ano
        assertEquals(1L, ServicoTempoMundo.diaDoAno(0L, config));
        assertEquals(1L, ServicoTempoMundo.diaDoAno(1439L, config));
        assertEquals(2L, ServicoTempoMundo.diaDoAno(1440L, config)); // segundo dia
    }

    @Test
    void worldTimeService_year() {
        ConfiguracaoMundo config = buildConfiguracaoMundo(); // 360 dias/ano × 1440 min/dia
        long minPorAno = 360L * 1440L;
        assertEquals(1L, ServicoTempoMundo.ano(0L, config));
        assertEquals(2L, ServicoTempoMundo.ano(minPorAno, config));
    }

    @Test
    void worldTimeService_hourOfDay() {
        ConfiguracaoMundo config = buildConfiguracaoMundo(); // 60 min/hora
        assertEquals(1, ServicoTempoMundo.horaDoDia(60L, config));
        assertEquals(2, ServicoTempoMundo.horaDoDia(120L, config));
    }

    @Test
    void worldTimeService_formatTime() {
        ConfiguracaoMundo config = buildConfiguracaoMundo();
        String fmt = ServicoTempoMundo.formatarTempo(0L, config);
        assertEquals("Ano1 D1-00:00", fmt);
    }

    @Test
    void worldTimeService_formatTime_segundoDia() {
        ConfiguracaoMundo config = buildConfiguracaoMundo();
        String fmt = ServicoTempoMundo.formatarTempo(1440L, config);
        assertEquals("Ano1 D2-00:00", fmt);
    }

    @Test
    void worldTimeService_currentFaseDia() {
        ConfiguracaoMundo config = buildConfiguracaoMundo();
        // A config tem fase "dia" de 360 a 1080 min
        FaseDia fase = ServicoTempoMundo.faseDiaAtual(360L, config);
        assertNotNull(fase);
        assertEquals("dia", fase.idFase());
    }

    @Test
    void worldTimeService_currentSeason() {
        ConfiguracaoMundo config = buildConfiguracaoMundo();
        Estacao estacao = ServicoTempoMundo.estacaoAtual(0L, config);
        assertNotNull(estacao);
        assertEquals("unica_estacao", estacao.idEstacao());
    }

    // ── RF-MN-06: validações hierárquicas ────────────────────────────────────

    @Test
    void hierarchyValidation_prefixoZonaValido() {
        // RF-MN-06
        assertTrue(ServicoValidacaoHierarquia.validarPrefixoZona("zona_mercado"));
        assertFalse(ServicoValidacaoHierarquia.validarPrefixoZona("mercado"));
    }

    @Test
    void hierarchyValidation_prefixoAmbienteValido() {
        // RF-MN-06
        assertTrue(ServicoValidacaoHierarquia.validarPrefixoAmbiente("amb_taverna"));
        assertFalse(ServicoValidacaoHierarquia.validarPrefixoAmbiente("taverna"));
    }

    @Test
    void hierarchyValidation_idsGlobalmenteUnicos() {
        // RF-MN-06
        assertTrue(ServicoValidacaoHierarquia.idsGlobalmenteUnicos(
                List.of("zona_a", "zona_b"), List.of("amb_x", "amb_y")));
    }

    @Test
    void hierarchyValidation_idsDuplicadosEntreListas() {
        // RF-MN-06
        assertFalse(ServicoValidacaoHierarquia.idsGlobalmenteUnicos(
                List.of("zona_a", "zona_b"), List.of("zona_a")));
    }

    @Test
    void hierarchyValidation_coordenadasFinitasValidas() {
        // RF-MN-06
        assertTrue(ServicoValidacaoHierarquia.temCoordenadasValidas(0.0, 0.0));
        assertFalse(ServicoValidacaoHierarquia.temCoordenadasValidas(Double.NaN, 0.0));
        assertFalse(ServicoValidacaoHierarquia.temCoordenadasValidas(0.0, Double.POSITIVE_INFINITY));
    }

    @Test
    void hierarchyValidation_destinosDeConexaoExistem() {
        // RF-MN-06
        Conexao conn = buildConnection("conn_01", "zona_a", List.of("zona_b", "amb_x"));
        Set<String> nos = Set.of("zona_a", "zona_b", "amb_x");
        assertTrue(ServicoValidacaoHierarquia.destinosConexaoExistem(conn, nos));
    }

    @Test
    void hierarchyValidation_destinoAusenteRetornaFalse() {
        // RF-MN-06
        Conexao conn = buildConnection("conn_02", "zona_a", List.of("zona_b"));
        Set<String> nos = Set.of("zona_a"); // zona_b ausente
        assertFalse(ServicoValidacaoHierarquia.destinosConexaoExistem(conn, nos));
    }

    @Test
    void hierarchyValidation_tipoNavegavelCoerente() {
        // RF-MN-06
        assertTrue(ServicoValidacaoHierarquia.tipoNavegavelCoerente("ZONA", "zona"));
        assertFalse(ServicoValidacaoHierarquia.tipoNavegavelCoerente("ambiente", "zona"));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Zona buildZona(String id, String nome) {
        return new Zona(id, nome, "loc_cidade", 1.0, 1.0, true, "Descrição.");
    }

    private AmbienteJogo buildEnv(String id) {
        return new AmbienteJogo(id, "Taverna do Lobo", "zona_praca", 1.1, 1.1, true, null);
    }

    private Conexao buildConnection(String id, String origem, List<String> destinos) {
        return new Conexao(id, origem, destinos, false,
                ClassificacaoConexao.PACIFICADO, 5.0, 10.0, "tab_001");
    }

    private ConfiguracaoMundo buildConfiguracaoMundo() {
        return new ConfiguracaoMundo(
                "mundo_teste",
                60,  // minutos por hora
                24,  // horas por dia
                360, // dias por ano
                List.of(
                        new FaseDia("noite", 0, 359),
                        new FaseDia("dia", 360, 1080),
                        new FaseDia("anoitecer", 1081, 1439)
                ),
                List.of(new Estacao("unica_estacao", 1, 360)),
                0
        );
    }
}
