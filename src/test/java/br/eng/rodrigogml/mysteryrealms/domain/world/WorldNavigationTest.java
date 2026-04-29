package br.eng.rodrigogml.mysteryrealms.domain.world;

import br.eng.rodrigogml.mysteryrealms.domain.world.enums.ClusterNavigationMode;
import br.eng.rodrigogml.mysteryrealms.domain.world.enums.ConnectionClassification;
import br.eng.rodrigogml.mysteryrealms.domain.world.enums.LocationType;
import br.eng.rodrigogml.mysteryrealms.domain.world.model.*;
import br.eng.rodrigogml.mysteryrealms.domain.world.service.NavigationService;
import br.eng.rodrigogml.mysteryrealms.domain.world.service.HierarchyValidationService;
import br.eng.rodrigogml.mysteryrealms.domain.world.service.WorldTimeService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import br.eng.rodrigogml.mysteryrealms.domain.physiology.service.PhysiologyService;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de Mundo e Navegação — RF-MN-01 a RF-MN-13.
 */
class WorldNavigationTest {

    // ── RF-MN-01 / RF-MN-02: Zone ────────────────────────────────────────────

    @Test
    void zone_tipoNavegavelEhZona() {
        assertEquals(LocationType.ZONA, Zone.TIPO_NAVEGAVEL);
    }

    @Test
    void zone_criacaoValida() {
        Zone z = buildZona("zona_praca", "Praça Central");
        assertEquals("zona_praca", z.zoneId());
        assertEquals("Praça Central", z.name());
        assertTrue(z.accessible());
    }

    @Test
    void zone_idSemPrefixoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Zone("praca", "Praça", "loc_cidade", 1.0, 1.0, true, null));
    }

    @Test
    void zone_nomeVazioLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Zone("zona_praca", "", "loc_cidade", 1.0, 1.0, true, null));
    }

    @Test
    void zone_idComMaiusculaLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Zone("zona_Praca", "Praça", "loc_cidade", 1.0, 1.0, true, null));
    }

    @Test
    void zone_idLocalidadeSemPrefixoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Zone("zona_praca", "Praça", "cidade", 1.0, 1.0, true, null));
    }

    @Test
    void zone_coordenadaInvalidaLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Zone("zona_praca", "Praça", "loc_cidade", Double.NaN, 1.0, true, null));
    }

    // ── RF-MN-03: GameEnvironment ────────────────────────────────────────────

    @Test
    void environment_tipoNavegavelEhAmbiente() {
        assertEquals(LocationType.ENVIRONMENT, GameEnvironment.TIPO_NAVEGAVEL);
    }

    @Test
    void environment_criacaoValida() {
        GameEnvironment e = buildEnv("amb_taverna");
        assertEquals("amb_taverna", e.environmentId());
        assertEquals("zona_praca", e.zoneId());
    }

    @Test
    void environment_idSemPrefixoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new GameEnvironment("taverna", "Taverna", "zona_praca", 0, 0, true, null));
    }

    @Test
    void environment_idZonaSemPrefixoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new GameEnvironment("amb_taverna", "Taverna", "praca", 0, 0, true, null));
    }

    @Test
    void environment_coordenadaInvalidaLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new GameEnvironment("amb_taverna", "Taverna", "zona_praca", 0, Double.POSITIVE_INFINITY, true, null));
    }

    // ── RF-MN-04: Connection ────────────────────────────────────────────────

    @Test
    void connection_criacaoValida() {
        Connection c = buildConnection("conn_001", "zona_praca", List.of("zona_mercado"));
        assertEquals("conn_001", c.connectionId());
        assertEquals("zona_praca", c.originId());
        assertEquals(ConnectionClassification.PACIFIED, c.classification());
    }

    @Test
    void connection_semDestinosLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Connection("conn_001", "zona_praca", List.of(),
                        true, ConnectionClassification.PACIFIED, 0, 0, "tab_001"));
    }

    @Test
    void connection_penaldadeForaDaFaixaLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Connection("conn_001", "zona_praca", List.of("zona_m"),
                        true, ConnectionClassification.PACIFIED, 20, 0, "tab_001"));
    }

    @Test
    void connection_chanceInterrupcaoForaDaFaixaLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Connection("conn_001", "zona_praca", List.of("zona_m"),
                        true, ConnectionClassification.PACIFIED, 5, 101, "tab_001"));
    }

    @Test
    void connection_origemInvalidaLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Connection("conn_001", "origem", List.of("zona_m"),
                        true, ConnectionClassification.PACIFIED, 5, 10, "tab_001"));
    }

    @Test
    void connection_destinoInvalidoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Connection("conn_001", "zona_praca", List.of("mercado"),
                        true, ConnectionClassification.PACIFIED, 5, 10, "tab_001"));
    }

    // ── RF-MN-05: Cluster ───────────────────────────────────────────────────

    @Test
    void cluster_criacaoValida() {
        Cluster cl = new Cluster("cl_cidade", "Centro", "loc_cidade",
                List.of("zona_praca", "zona_mercado"), List.of("conn_001"),
                ClusterNavigationMode.FREE);
        assertEquals("cl_cidade", cl.clusterId());
        assertEquals(ClusterNavigationMode.FREE, cl.navigationMode());
    }

    @Test
    void cluster_semPontosLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Cluster("cl_001", "Nome", "loc_001",
                        List.of(), List.of(), ClusterNavigationMode.FREE));
    }

    // ── RF-MN-07: Distâncias ─────────────────────────────────────────────────

    @Test
    void navigationService_distanciaBase_pontoMesmo() {
        assertEquals(0.0, NavigationService.baseDistanceKm(1.0, 1.0, 1.0, 1.0), 1e-9);
    }

    @Test
    void navigationService_distanciaBase_horizontalUmUnidade() {
        // sqrt(1^2 + 0^2) × 10 = 10 km
        assertEquals(10.0, NavigationService.baseDistanceKm(0.0, 0.0, 1.0, 0.0), 1e-9);
    }

    @Test
    void navigationService_distanciaBase_diagonal() {
        // sqrt(3^2 + 4^2) × 10 = 5 × 10 = 50 km
        assertEquals(50.0, NavigationService.baseDistanceKm(0, 0, 3, 4), 1e-9);
    }

    @Test
    void navigationService_distanciaAjustada_semPenalidade() {
        assertEquals(50.0, NavigationService.adjustedDistanceKm(50.0, 0.0), 1e-9);
    }

    @Test
    void navigationService_distanciaAjustada_comPenalidade10Pct() {
        // 50 × 1.10 = 55
        assertEquals(55.0, NavigationService.adjustedDistanceKm(50.0, 10.0), 1e-9);
    }

    // ── RF-MN-08: Resolução de destino ──────────────────────────────────────

    @Test
    void navigationService_resolveDestination_primeiroAcessivel() {
        Connection conn = buildConnection("c", "zona_origem", List.of("zona_a", "zona_b", "zona_c"));
        Map<String, Boolean> nodes = Map.of("zona_a", false, "zona_b", true, "zona_c", true);
        Optional<String> dest = NavigationService.resolveDestination(conn, nodes);
        assertTrue(dest.isPresent());
        assertEquals("zona_b", dest.get());
    }

    @Test
    void navigationService_resolveDestination_nenhumAcessivel() {
        Connection conn = buildConnection("c", "zona_origem", List.of("zona_a", "zona_b"));
        Map<String, Boolean> nodes = Map.of("zona_a", false, "zona_b", false);
        assertTrue(NavigationService.resolveDestination(conn, nodes).isEmpty());
    }

    @Test
    void navigationService_resolveDestination_nodeDesconhecidoNaoAcessivel() {
        Connection conn = buildConnection("c", "zona_origem", List.of("zona_x"));
        // "zona_x" não está no mapa → não é acessível
        assertTrue(NavigationService.resolveDestination(conn, Map.of()).isEmpty());
    }

    // ── RF-MN-09: Interrupções ──────────────────────────────────────────────

    @Test
    void navigationService_interrupcao_segmentoCompleto() {
        assertEquals(10.0, NavigationService.interruptionChancePerSegment(10.0, 1.0), 1e-9);
    }

    @Test
    void navigationService_interrupcao_segmentoParcial() {
        // chance 10% × 0.5 km = 5%
        assertEquals(5.0, NavigationService.interruptionChancePerSegment(10.0, 0.5), 1e-9);
    }

    @Test
    void navigationService_interrupcao_acimaDe1kmUsaChanceDireta() {
        assertEquals(10.0, NavigationService.interruptionChancePerSegment(10.0, 5.0), 1e-9);
    }

    // ── RF-MN-10: Tempo de deslocamento ─────────────────────────────────────

    @Test
    void navigationService_travelTime_formulaCorreta() {
        // 10 km / 5 kmh = 2 h × 60 min/h = 120 min
        assertEquals(120L, NavigationService.travelTimeMinutes(10.0, 5.0, 60));
    }

    @Test
    void navigationService_travelTime_arredondamentoCeil() {
        // 1 km / 3 kmh = 0.333 h × 60 = 20 min (ceil)
        assertEquals(20L, NavigationService.travelTimeMinutes(1.0, 3.0, 60));
    }

    @Test
    void navigationService_travelTime_minimoUmMinuto() {
        assertEquals(1L, NavigationService.travelTimeMinutes(0.001, 100.0, 60));
    }

    @Test
    void navigationService_velocidadeZeroLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> NavigationService.travelTimeMinutes(10.0, 0.0, 60));
    }

    // ── RF-MN-12: WorldConfig ────────────────────────────────────────────────

    @Test
    void worldConfig_criacaoValida() {
        WorldConfig config = buildConfiguracaoMundo();
        assertEquals("mundo_teste", config.worldId());
        assertEquals(60, config.minutesPerHour());
        assertEquals(24, config.hoursPerDay());
        assertEquals(360, config.daysPerYear());
        assertEquals(1440, config.minutesPerDay());
    }

    @Test
    void worldConfig_minutosPorHoraInvalidoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new WorldConfig("mundo_teste", 0, 24, 360,
                        List.of(new DayPhase("dia", 0, 1439)),
                        List.of(new Season("verao", 1, 360)), 0));
    }

    @Test
    void worldConfig_semFazesDiaLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new WorldConfig("mundo_teste", 60, 24, 360,
                        List.of(),
                        List.of(new Season("verao", 1, 360)), 0));
    }

    @Test
    void worldConfig_idSemPrefixoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new WorldConfig("teste", 60, 24, 360,
                        List.of(new DayPhase("dia", 0, 1439)),
                        List.of(new Season("verao", 1, 360)), 0));
    }

    @Test
    void worldConfig_fasesDiaComLacunaLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new WorldConfig("mundo_teste", 60, 24, 360,
                        List.of(
                                new DayPhase("noite", 0, 300),
                                new DayPhase("dia", 302, 1439)
                        ),
                        List.of(new Season("verao", 1, 360)), 0));
    }

    @Test
    void worldConfig_fasesDiaNaoCobremDiaCompletoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new WorldConfig("mundo_teste", 60, 24, 360,
                        List.of(new DayPhase("dia", 0, 1438)),
                        List.of(new Season("verao", 1, 360)), 0));
    }

    @Test
    void worldConfig_estacoesComLacunaLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new WorldConfig("mundo_teste", 60, 24, 360,
                        List.of(new DayPhase("dia", 0, 1439)),
                        List.of(
                                new Season("verao", 1, 100),
                                new Season("outono", 102, 360)
                        ), 0));
    }

    // ── RF-MN-12: DayPhase e Season ─────────────────────────────────────────

    @Test
    void dayPhase_criacaoValida() {
        DayPhase fase = new DayPhase("dia", 360, 1080);
        assertEquals("dia", fase.phaseId());
        assertEquals(360, fase.startMinOfDay());
    }

    @Test
    void dayPhase_fimMenorQueInicioLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> new DayPhase("dia", 500, 400));
    }

    @Test
    void season_criacaoValida() {
        Season s = new Season("verao", 1, 90);
        assertEquals("verao", s.seasonId());
    }

    @Test
    void season_fimMenorQueInicioLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> new Season("verao", 90, 1));
    }

    // ── RF-MN-12 / RF-MN-13: WorldTimeService ───────────────────────────────

    @Test
    void worldTimeService_avancaTempo() {
        assertEquals(100L, WorldTimeService.advanceTime(0L, 100L));
        assertEquals(200L, WorldTimeService.advanceTime(100L, 100L));
    }

    @Test
    void worldTimeService_avancaTempoNegativoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> WorldTimeService.advanceTime(100L, -1L));
    }

    @Test
    void worldTimeService_minuteOfDay() {
        WorldConfig config = buildConfiguracaoMundo(); // 1440 min/dia
        assertEquals(0, WorldTimeService.minutesOfDay(0L, config));
        assertEquals(60, WorldTimeService.minutesOfDay(60L, config));
        assertEquals(0, WorldTimeService.minutesOfDay(1440L, config)); // novo dia
    }

    @Test
    void worldTimeService_dayOfYear() {
        WorldConfig config = buildConfiguracaoMundo(); // 360 dias/ano
        assertEquals(1L, WorldTimeService.dayOfYear(0L, config));
        assertEquals(1L, WorldTimeService.dayOfYear(1439L, config));
        assertEquals(2L, WorldTimeService.dayOfYear(1440L, config)); // segundo dia
    }

    @Test
    void worldTimeService_year() {
        WorldConfig config = buildConfiguracaoMundo(); // 360 dias/ano × 1440 min/dia
        long minPorAno = 360L * 1440L;
        assertEquals(1L, WorldTimeService.ano(0L, config));
        assertEquals(2L, WorldTimeService.ano(minPorAno, config));
    }

    @Test
    void worldTimeService_hourOfDay() {
        WorldConfig config = buildConfiguracaoMundo(); // 60 min/hora
        assertEquals(1, WorldTimeService.hourOfDay(60L, config));
        assertEquals(2, WorldTimeService.hourOfDay(120L, config));
    }

    @Test
    void worldTimeService_formatTime() {
        WorldConfig config = buildConfiguracaoMundo();
        String fmt = WorldTimeService.formatTime(0L, config);
        assertEquals("Ano1 D1-00:00", fmt);
    }

    @Test
    void worldTimeService_formatTime_segundoDia() {
        WorldConfig config = buildConfiguracaoMundo();
        String fmt = WorldTimeService.formatTime(1440L, config);
        assertEquals("Ano1 D2-00:00", fmt);
    }

    @Test
    void worldTimeService_currentFaseDia() {
        WorldConfig config = buildConfiguracaoMundo();
        // A config tem fase "dia" de 360 a 1080 min
        DayPhase fase = WorldTimeService.currentDayPhase(360L, config);
        assertNotNull(fase);
        assertEquals("dia", fase.phaseId());
    }

    @Test
    void worldTimeService_currentSeason() {
        WorldConfig config = buildConfiguracaoMundo();
        Season estacao = WorldTimeService.currentSeason(0L, config);
        assertNotNull(estacao);
        assertEquals("unica_estacao", estacao.seasonId());
    }

    // ── RF-MN-06: validações hierárquicas ────────────────────────────────────

    @Test
    void hierarchyValidation_prefixoZonaValido() {
        // RF-MN-06
        assertTrue(HierarchyValidationService.validateZonePrefix("zona_mercado"));
        assertFalse(HierarchyValidationService.validateZonePrefix("mercado"));
    }

    @Test
    void hierarchyValidation_prefixoAmbienteValido() {
        // RF-MN-06
        assertTrue(HierarchyValidationService.validateEnvironmentPrefix("amb_taverna"));
        assertFalse(HierarchyValidationService.validateEnvironmentPrefix("taverna"));
    }

    @Test
    void hierarchyValidation_idsGlobalmenteUnicos() {
        // RF-MN-06
        assertTrue(HierarchyValidationService.idsAreGloballyUnique(
                List.of("zona_a", "zona_b"), List.of("amb_x", "amb_y")));
    }

    @Test
    void hierarchyValidation_idsDuplicadosEntreListas() {
        // RF-MN-06
        assertFalse(HierarchyValidationService.idsAreGloballyUnique(
                List.of("zona_a", "zona_b"), List.of("zona_a")));
    }

    @Test
    void hierarchyValidation_coordenadasFinitasValidas() {
        // RF-MN-06
        assertTrue(HierarchyValidationService.hasValidCoordinates(0.0, 0.0));
        assertFalse(HierarchyValidationService.hasValidCoordinates(Double.NaN, 0.0));
        assertFalse(HierarchyValidationService.hasValidCoordinates(0.0, Double.POSITIVE_INFINITY));
    }

    @Test
    void hierarchyValidation_destinosDeConexaoExistem() {
        // RF-MN-06
        Connection conn = buildConnection("conn_01", "zona_a", List.of("zona_b", "amb_x"));
        Set<String> nos = Set.of("zona_a", "zona_b", "amb_x");
        assertTrue(HierarchyValidationService.connectionDestinationsExist(conn, nos));
    }

    @Test
    void hierarchyValidation_destinoAusenteRetornaFalse() {
        // RF-MN-06
        Connection conn = buildConnection("conn_02", "zona_a", List.of("zona_b"));
        Set<String> nos = Set.of("zona_a"); // zona_b ausente
        assertFalse(HierarchyValidationService.connectionDestinationsExist(conn, nos));
    }

    @Test
    void hierarchyValidation_idNavegavelCanonico() {
        assertTrue(HierarchyValidationService.isValidNavigableId("zona_mercado_1"));
        assertTrue(HierarchyValidationService.isValidNavigableId("amb_taverna"));
        assertFalse(HierarchyValidationService.isValidNavigableId("Zona_mercado"));
        assertFalse(HierarchyValidationService.isValidNavigableId("loc_cidade"));
    }

    @Test
    void hierarchyValidation_conexaoPrecisaTerOrigemEAoMenosUmDestinoAcessivel() {
        Connection conn = buildConnection("conn_03", "zona_a", List.of("zona_b", "amb_x"));
        assertTrue(HierarchyValidationService.connectionHasAccessibleEndpoint(
                conn, Map.of("zona_a", true, "zona_b", false, "amb_x", true)));
        assertFalse(HierarchyValidationService.connectionHasAccessibleEndpoint(
                conn, Map.of("zona_a", false, "zona_b", true, "amb_x", true)));
        assertFalse(HierarchyValidationService.connectionHasAccessibleEndpoint(
                conn, Map.of("zona_a", true, "zona_b", false, "amb_x", false)));
    }

    @Test
    void hierarchyValidation_tipoNavegavelCoerente() {
        // RF-MN-06
        assertTrue(HierarchyValidationService.isNavigableTypeCoherent("ZONA", "zona"));
        assertFalse(HierarchyValidationService.isNavigableTypeCoherent("ambiente", "zona"));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Zone buildZona(String id, String name) {
        return new Zone(id, name, "loc_cidade", 1.0, 1.0, true, "Descrição.");
    }

    private GameEnvironment buildEnv(String id) {
        return new GameEnvironment(id, "Taverna do Lobo", "zona_praca", 1.1, 1.1, true, null);
    }

    private Connection buildConnection(String id, String origin, List<String> destinos) {
        return new Connection(id, origin, destinos, false,
                ConnectionClassification.PACIFIED, 5.0, 10.0, "tab_001");
    }

    private WorldConfig buildConfiguracaoMundo() {
        return new WorldConfig(
                "mundo_teste",
                60,  // minutos por hora
                24,  // horas por dia
                360, // dias por ano
                List.of(
                        new DayPhase("noite", 0, 359),
                        new DayPhase("dia", 360, 1080),
                        new DayPhase("anoitecer", 1081, 1439)
                ),
                List.of(new Season("unica_estacao", 1, 360)),
                0
        );
    }

    // ── RF-MN-11: atualização fisiológica por trecho ─────────────────────────

    @Test
    void navigationService_fatigueCostPerSegment_proporcionalAoTempo() {
        // RF-MN-11
        double custo = NavigationService.fatigueCostPerSegment(2.0, 10L);
        assertEquals(20.0, custo, 1e-9);
    }

    @Test
    void navigationService_fatigueCostPerSegment_custoZeroMinutos0() {
        // RF-MN-11
        assertEquals(0.0, NavigationService.fatigueCostPerSegment(3.0, 0L), 1e-9);
    }

    @Test
    void navigationService_thirstDeltaPerSegment_proporcionalAoTempo() {
        // RF-MN-11 — delta de sede para 60 minutos = 60 × (100/2880) ≈ 2,0833...
        double esperado = PhysiologyService.THIRST_RATE_PCT_PER_MIN * 60;
        assertEquals(esperado, NavigationService.thirstDeltaPerSegment(60L), 1e-9);
    }

    @Test
    void navigationService_hungerDeltaPerSegment_proporcionalAoTempo() {
        // RF-MN-11 — delta de fome para 60 minutos
        double esperado = PhysiologyService.HUNGER_RATE_PCT_PER_MIN * 60;
        assertEquals(esperado, NavigationService.hungerDeltaPerSegment(60L), 1e-9);
    }

    @Test
    void navigationService_thirstDeltaPerSegment_1minuteConsistente() {
        // RF-MN-11 — delta por minuto deve ser consistente com THIRST_RATE_PCT_PER_MIN
        assertEquals(
                PhysiologyService.THIRST_RATE_PCT_PER_MIN,
                NavigationService.thirstDeltaPerSegment(1L),
                1e-9);
    }

    @Test
    void navigationService_hungerDeltaPerSegment_1minuteConsistente() {
        // RF-MN-11 — delta por minuto deve ser consistente com HUNGER_RATE_PCT_PER_MIN
        assertEquals(
                PhysiologyService.HUNGER_RATE_PCT_PER_MIN,
                NavigationService.hungerDeltaPerSegment(1L),
                1e-9);
    }
}
