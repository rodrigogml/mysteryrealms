#!/usr/bin/env python3
"""Renomeia identificadores Java de português para inglês conforme guidelines.md."""

import os
import re

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
SRC_DIR  = os.path.join(BASE_DIR, "src")

# ──────────────────────────────────────────────────────────────────────────────
# Novo conteúdo do record ResultadoCicloSocial (substituição total)
# ──────────────────────────────────────────────────────────────────────────────
NOVO_SOCIAL_CYCLE_RESULT = '''\
package br.eng.rodrigogml.mysteryrealms.domain.social.model;

import java.util.List;

/**
 * Resultado de um ciclo social — RF-SS-02.
 */
public record ResultadoCicloSocial(
        boolean sucesso,
        EntradaDiario entradaDiario,
        List<Marcador> marcadoresAlterados,
        Integer novoRelacionamentoNpc,
        Integer novaReputacao) {

    public ResultadoCicloSocial {
        marcadoresAlterados = marcadoresAlterados != null ? List.copyOf(marcadoresAlterados) : List.of();
    }
}
'''

# ──────────────────────────────────────────────────────────────────────────────
# Utilitários
# ──────────────────────────────────────────────────────────────────────────────

def get_all_java_files():
    result = []
    for root, _, files in os.walk(SRC_DIR):
        for f in files:
            if f.endswith('.java'):
                result.append(os.path.join(root, f))
    return sorted(result)


def apply_transforms(content, transforms):
    for old, new, use_regex in transforms:
        if use_regex:
            content = re.sub(old, new, content)
        else:
            content = content.replace(old, new)
    return content


def W(old, new):
    """Word-boundary regex rename."""
    return (r'\b' + re.escape(old) + r'\b', new, True)


def E(old, new):
    """Exact string rename."""
    return (old, new, False)


# ──────────────────────────────────────────────────────────────────────────────
# Transforms globais (aplicados a todos os arquivos)
# ──────────────────────────────────────────────────────────────────────────────

GLOBAL_TRANSFORMS = [
    # ── Fase 1: tipos (nomes mais longos primeiro) ─────────────────────────
    W('CharacterAttributeServiceTest', 'ServicoAtributosPersonagemTest'),
    W('CharacterAttributeService',     'ServicoAtributosPersonagem'),
    W('CharacterClass',                'ClassePersonagem'),
    W('CombinedPhysiologicalEffect',   'EfeitoFisiologicoCombinado'),
    W('ConsciousnessTestResult',       'ResultadoTesteConsciencia'),
    W('ActionCostService',             'ServicoCustoAcao'),
    W('HierarchyValidationService',    'ServicoValidacaoHierarquia'),
    W('PhysiologicalServiceTest',      'ServicoFisiologicoTest'),
    W('PhysiologicalService',          'ServicoFisiologico'),
    W('ProgressionServiceTest',        'ServicoProgressaoTest'),
    W('ProgressionService',            'ServicoProgressao'),
    W('RelationshipBand',              'FaixaRelacionamento'),
    W('SocialCycleResult',             'ResultadoCicloSocial'),
    W('SocialService',                 'ServicoSocial'),
    W('SpeechStyleValuation',          'AvaliacaoEstiloDiscurso'),
    W('WorldTimeService',              'ServicoTempoMundo'),
    W('NavigationService',             'ServicoNavegacao'),
    W('CombatServiceTest',             'ServicoCombateTest'),
    W('CombatService',                 'ServicoCombate'),
    W('SpeechStyle',                   'EstiloDiscurso'),
    W('PricingService',                'ServicoPrecos'),
    W('ActionClass',                   'ClasseAcao'),
    W('ActionCost',                    'CustoAcao'),
    W('DiceRoller',                    'RoladorDados'),
    W('MonetaryValue',                 'ValorMonetario'),
    W('SkillBonuses',                  'BonusHabilidades'),
    W('AttributeBonus',                'BonusAtributo'),
    W('LocationType',                  'TipoLocalizacao'),
    W('MarkerType',                    'TipoMarcador'),
    W('SocialTest',                    'TesteSocial'),
    W('DialogueEffects',               'EfeitosDialogo'),
    W('DialogueOption',                'OpcaoDialogo'),
    W('DialogueNode',                  'NoDialogo'),
    W('DiaryEntry',                    'EntradaDiario'),
    W('DiaryImpact',                   'ImpactoDiario'),
    W('ModifierEffect',                'EfeitoModificador'),
    W('ModifierOrigin',                'OrigemModificador'),
    W('ModifierService',               'ServicoModificador'),
    W('Modifier',                      'Modificador'),
    W('Marker',                        'Marcador'),
    W('ActionEffect',                  'EfeitoAcao'),
    W('ActionResult',                  'ResultadoAcao'),
    W('CombatState',                   'EstadoCombate'),
    W('DamageBonus',                   'BonusDano'),
    W('DefenseBonus',                  'BonusDefesa'),
    W('PerceptionTest',                'TestePercepcao'),
    W('FatigueStateId',                'EstadoFadiga'),
    W('ThirstStateId',                 'EstadoSede'),
    W('HungerStateId',                 'EstadoFome'),
    W('MoralStateId',                  'EstadoMoral'),

    # ── Fase 2: constantes de enum ──────────────────────────────────────────
    W('ACCUMULATES', 'ACUMULA'),
    W('REPLACES',    'SUBSTITUI'),
    W('INVALIDATES', 'INVALIDA'),
    W('FLAG',        'SINALIZADOR'),
    W('STAGE',       'ESTAGIO'),
    W('COUNTER',     'CONTADOR'),
    W('SUCCESS',     'SUCESSO'),
    W('FAINTED',     'DESMAIADO'),
    W('NONE',        'NENHUM'),
    W('NEUTRAL',     'NEUTRO'),
    W('FAVORABLE',   'FAVORAVEL'),
    W('ALLY',        'ALIADO'),

    # ── Fase 3: métodos e constantes ───────────────────────────────────────
    # CharacterAttributeService
    W('maxHp',                   'pvMaximo'),
    W('maxFatigue',              'fadigaMaxima'),
    W('characterWeight',         'pesoPersonagem'),
    W('maxCarryCapacity',        'capacidadeMaximaCarga'),
    W('criticalCarryCapacity',   'capacidadeCriticaCarga'),
    W('coinWeight',              'pesoMoedas'),
    W('currentLoad',             'cargaAtual'),
    W('precisionBonus',          'bonusPrecisao'),
    W('calculateFinalDamage',    'calcularDanoFinal'),
    W('calculateFinalDefense',   'calcularDefesaFinal'),
    W('calculateFinalBlock',     'calcularBloqueioFinal'),
    W('MAX_RESISTANCE_PLAYER',   'RESISTENCIA_MAXIMA_JOGADOR'),
    W('BALANCE_VERSION',         'VERSAO_BALANCEAMENTO'),

    # ProgressionService
    W('xpForLevel',                        'xpParaNivel'),
    W('xpTotalForLevel',                   'xpTotalParaNivel'),
    W('shouldLevelUp',                     'deveSubirNivel'),
    W('totalPeaAtLevel',                   'totalPeaNoNivel'),
    W('peaOnLevelUp',                      'peaAoSubirDeNivel'),
    W('totalPpAtLevel',                    'totalPpNoNivel'),
    W('ppOnLevelUp',                       'ppAoSubirDeNivel'),
    W('proficiencyBonus',                  'bonusProficiencia'),
    W('skillFinalValue',                   'valorFinalHabilidade'),
    W('attributeSoftCap',                  'limiteSupaveAtributo'),
    W('isAbilitySlotUnlocked',             'slotHabilidadeDesbloqueado'),
    W('isSignatureAbilityUnlocked',        'habilidadeAssinaturaDesbloqueada'),
    W('isAdvancedSpecializationUnlocked',  'especializacaoAvancadaDesbloqueada'),
    W('isMasteryCycleActive',              'cicloMaestriaAtivo'),

    # ActionCostService
    W('baseCost',                     'custoBase'),
    W('applyWeatherModifier',         'aplicarModificadorClima'),
    W('applyLoadModifier',            'aplicarModificadorCarga'),
    W('applyPhysiologicalModifier',   'aplicarModificadorFisiologico'),
    W('calculateCost',                'calcularCusto'),

    # ModifierService
    W('byOrigin',             'porOrigem'),
    W('sortedByPriority',     'ordenadosPorPrioridade'),
    W('apply',                'aplicar'),

    # PricingService
    W('applyPrice', 'aplicarPreco'),

    # WorldTimeService
    W('advanceTime',      'avancarTempo'),
    W('minuteOfDay',      'minutosDoDia'),
    W('dayOfYear',        'diaDoAno'),
    W('hourOfDay',        'horaDoDia'),
    W('minuteOfHour',     'minutoDaHora'),
    W('formatTime',       'formatarTempo'),
    W('currentDayPhase',  'faseDiaAtual'),
    W('currentSeason',    'estacaoAtual'),
    W('year',             'ano'),

    # NavigationService
    W('distanceBaseKm',                  'distanciaBaseKm'),
    W('distanceAdjustedKm',              'distanciaAjustadaKm'),
    W('resolveDestination',              'resolverDestino'),
    W('interruptionChanceForSegment',    'chanceInterrupcaoPorSegmento'),
    W('travelTimeMinutes',               'tempoDeslocamentoMinutos'),
    W('fatigueCostForLeg',               'custoFadigaPorTrecho'),

    # HierarchyValidationService
    W('hasValidPrefix',               'temPrefixoValido'),
    W('validateZonePrefix',           'validarPrefixoZona'),
    W('validateAmbientePrefix',       'validarPrefixoAmbiente'),
    W('idsAreGloballyUnique',         'idsGlobalmenteUnicos'),
    W('hasValidCoordinates',          'temCoordenadasValidas'),
    W('connectionDestinationsExist',  'destinosConexaoExistem'),
    W('tipoNavegavelCoherent',        'tipoNavegavelCoerente'),

    # CombatService
    W('testResult',               'resultadoTeste'),
    W('isSuccess',                'ehSucesso'),
    W('rollWithAdvantage',        'rolarComVantagem'),
    W('rollWithDisadvantage',     'rolarComDesvantagem'),
    W('rollInitiative',           'rolarIniciativa'),
    W('rollAttack',               'rolarAtaque'),
    W('isHit',                    'acertou'),
    W('damageAfterBlock',         'danoAposBloqueio'),
    W('damageAfterResistance',    'danoAposResistencia'),
    W('clampPlayerResistance',    'limitarResistenciaJogador'),
    W('afflictionChance',         'chanceAfliccao'),
    W('afflictionDuration',       'duracaoAfliccao'),
    W('afflictionIntensity',      'intensidadeAfliccao'),
    W('resolveDamage',            'resolverDano'),
    W('rollPerception',           'rolarPercepcao'),
    W('detectsTarget',            'detectaAlvo'),
    W('detectsVsCd',              'detectaVersusCd'),
    W('isPreparedActionCancelled','acaoPreparadaCancelada'),

    # DiceRoller
    W('roll',     'rolar'),
    W('standard', 'padrao'),
    W('fixed',    'fixo'),

    # PhysiologicalService
    W('applyMoralDeltaAllyFallen',      'aplicarDeltaMoralAliadoCaido'),
    W('applyMoralDeltaCombatVictory',   'aplicarDeltaMoralVitoriaCombate'),
    W('applyMoralDeltaGoodSleep',       'aplicarDeltaMoralBomSono'),
    W('applyMoralDeltaFainted',         'aplicarDeltaMoralDesmaio'),
    W('applyMoralDeltaFomeSede',        'aplicarDeltaMoralFomeSede'),
    W('applyMoralDeltaQuietRest',       'aplicarDeltaMoralDescansoQuieto'),
    W('applyMoralDeltaInterruptedSleep','aplicarDeltaMoralSonoInterrompido'),
    W('applyMinuteTick',                'aplicarTickDeMinuto'),
    W('applyRestRecoveryTick',          'aplicarTickDescanso'),
    W('applySleepRecoveryTick',         'aplicarTickSono'),
    W('applyItemRecovery',              'aplicarRecuperacaoPorItem'),
    W('fatigueState',                   'estadoFadiga'),
    W('thirstState',                    'estadoSede'),
    W('hungerState',                    'estadoFome'),
    W('moralState',                     'estadoMoral'),
    W('sleepQualityFactor',             'fatorQualidadeSono'),
    W('recoveryPvPerMinute',            'recuperacaoPvPorMinuto'),
    W('canWakeFromFaint',               'podeDespertarDoDesmaio'),
    W('combinedEffect',                 'efeitoCombinado'),
    W('isPvCritical',                   'ehPvCritico'),
    W('consciousnessTest',              'testeConsciencia'),
    W('moralFatigueCostMultiplier',     'multiplicadorCustoFadigaPorMoral'),
    W('MAX_SPEED_PENALTY_PCT',          'PCT_MAX_PENALIDADE_VELOCIDADE'),
    W('MAX_FATIGUE_COST_BONUS_PCT',     'PCT_MAX_BONUS_CUSTO_FADIGA'),
    W('MAX_FATIGUE_RECOVERY_PENALTY_PCT','PCT_MAX_PENALIDADE_RECUPERACAO_FADIGA'),

    # MonetaryValue
    W('ofMp',        'deMp'),
    W('ofMs',        'deMs'),
    W('weightKg',    'pesoKg'),
    W('toTotalMs',   'totalEmMs'),
    W('normalized',  'normalizado'),

    # Marker factory / instance methods
    W('flagInativo',     'sinalizadorInativo'),
    W('isFlagAtivo',     'sinalizadorAtivo'),
    W('setFlag',         'definirSinalizador'),
    W('setStage',        'definirEstagio'),
    W('getValorInteiro', 'valorInteiro'),
    W('flag',            'sinalizador'),
    W('stage',           'estagio'),
    W('counter',         'contador'),

    # SocialService
    W('executeSocialCycle', 'executarCicloSocial'),
    W('DIARY_COUNTER',      'CONTADOR_DIARIO'),

    # SocialCycleResult getters → record accessors (após isSuccess→ehSucesso)
    W('getDiaryEntry',           'entradaDiario'),
    W('getMarkersAlterados',     'marcadoresAlterados'),
    W('getNovoRelacionamentoNpc','novoRelacionamentoNpc'),
    W('getNovaReputacao',        'novaReputacao'),

    # ── Fase 4: renames qualificados ──────────────────────────────────────
    # .format() vazio → .formatar()
    E('.format()', '.formatar()'),
    # ehSucesso() sem args → sucesso() (record accessor)
    E('.ehSucesso()', '.sucesso()'),

    # of( → de( apenas para tipos específicos
    E('ValorMonetario.of(',      'ValorMonetario.de('),
    E('FaixaRelacionamento.of(', 'FaixaRelacionamento.de('),
    E('BonusHabilidades.of(',    'BonusHabilidades.de('),
    # declarações estáticas dos métodos of
    E('static ValorMonetario of(',      'static ValorMonetario de('),
    E('static FaixaRelacionamento of(', 'static FaixaRelacionamento de('),
    E('static BonusHabilidades of(',    'static BonusHabilidades de('),

    # empty() → vazio() para tipos específicos
    E('BonusHabilidades.empty()', 'BonusHabilidades.vazio()'),
    E('EfeitosDialogo.empty()',   'EfeitosDialogo.vazio()'),
    E('ImpactoDiario.empty()',    'ImpactoDiario.vazio()'),
    E('static BonusHabilidades empty()', 'static BonusHabilidades vazio()'),
    E('static EfeitosDialogo empty()',   'static EfeitosDialogo vazio()'),
    E('static ImpactoDiario empty()',    'static ImpactoDiario vazio()'),
]

# ──────────────────────────────────────────────────────────────────────────────
# Transforms específicos por arquivo (keyed by partial path)
# ──────────────────────────────────────────────────────────────────────────────

FILE_SPECIFIC = {
    # BonusHabilidades (was SkillBonuses): rename merge method, skip Map.merge
    'domain/character/model/SkillBonuses.java': [
        E('public BonusHabilidades merge(BonusHabilidades other)',
          'public BonusHabilidades mesclar(BonusHabilidades outro)'),
        E('other.bonuses', 'outro.bonuses'),
    ],
    # RoladorDados (was DiceRoller): sides→faces, value→valor (file-specific only)
    'domain/combat/model/DiceRoller.java': [
        W('sides', 'faces'),
        W('value', 'valor'),
    ],
}

# ──────────────────────────────────────────────────────────────────────────────
# Mapa de renomeação de arquivos
# ──────────────────────────────────────────────────────────────────────────────

FILE_RENAMES = [
    # character
    ('domain/character/enums/CharacterClass.java',             'domain/character/enums/ClassePersonagem.java'),
    ('domain/character/model/AttributeBonus.java',             'domain/character/model/BonusAtributo.java'),
    ('domain/character/model/SkillBonuses.java',               'domain/character/model/BonusHabilidades.java'),
    ('domain/character/service/CharacterAttributeService.java','domain/character/service/ServicoAtributosPersonagem.java'),
    ('domain/character/service/ProgressionService.java',       'domain/character/service/ServicoProgressao.java'),
    # combat
    ('domain/combat/model/DiceRoller.java',                    'domain/combat/model/RoladorDados.java'),
    ('domain/combat/service/CombatService.java',               'domain/combat/service/ServicoCombate.java'),
    # economy
    ('domain/economy/model/MonetaryValue.java',                'domain/economy/model/ValorMonetario.java'),
    ('domain/economy/service/PricingService.java',             'domain/economy/service/ServicoPrecos.java'),
    # modifier
    ('domain/modifier/enums/ActionClass.java',                 'domain/modifier/enums/ClasseAcao.java'),
    ('domain/modifier/enums/ModifierOrigin.java',              'domain/modifier/enums/OrigemModificador.java'),
    ('domain/modifier/model/ActionCost.java',                  'domain/modifier/model/CustoAcao.java'),
    ('domain/modifier/model/ModifierEffect.java',              'domain/modifier/model/EfeitoModificador.java'),
    ('domain/modifier/model/Modifier.java',                    'domain/modifier/model/Modificador.java'),
    ('domain/modifier/service/ActionCostService.java',         'domain/modifier/service/ServicoCustoAcao.java'),
    ('domain/modifier/service/ModifierService.java',           'domain/modifier/service/ServicoModificador.java'),
    # physiology
    ('domain/physiology/enums/CombinedPhysiologicalEffect.java','domain/physiology/enums/EfeitoFisiologicoCombinado.java'),
    ('domain/physiology/enums/ConsciousnessTestResult.java',   'domain/physiology/enums/ResultadoTesteConsciencia.java'),
    ('domain/physiology/service/PhysiologicalService.java',    'domain/physiology/service/ServicoFisiologico.java'),
    # social
    ('domain/social/enums/MarkerType.java',                    'domain/social/enums/TipoMarcador.java'),
    ('domain/social/enums/RelationshipBand.java',              'domain/social/enums/FaixaRelacionamento.java'),
    ('domain/social/enums/SpeechStyle.java',                   'domain/social/enums/EstiloDiscurso.java'),
    ('domain/social/enums/SpeechStyleValuation.java',          'domain/social/enums/AvaliacaoEstiloDiscurso.java'),
    ('domain/social/model/DialogueEffects.java',               'domain/social/model/EfeitosDialogo.java'),
    ('domain/social/model/DialogueNode.java',                  'domain/social/model/NoDialogo.java'),
    ('domain/social/model/DialogueOption.java',                'domain/social/model/OpcaoDialogo.java'),
    ('domain/social/model/DiaryEntry.java',                    'domain/social/model/EntradaDiario.java'),
    ('domain/social/model/DiaryImpact.java',                   'domain/social/model/ImpactoDiario.java'),
    ('domain/social/model/Marker.java',                        'domain/social/model/Marcador.java'),
    ('domain/social/model/SocialCycleResult.java',             'domain/social/model/ResultadoCicloSocial.java'),
    ('domain/social/model/SocialTest.java',                    'domain/social/model/TesteSocial.java'),
    ('domain/social/service/SocialService.java',               'domain/social/service/ServicoSocial.java'),
    # world
    ('domain/world/enums/LocationType.java',                   'domain/world/enums/TipoLocalizacao.java'),
    ('domain/world/service/HierarchyValidationService.java',   'domain/world/service/ServicoValidacaoHierarquia.java'),
    ('domain/world/service/NavigationService.java',            'domain/world/service/ServicoNavegacao.java'),
    ('domain/world/service/WorldTimeService.java',             'domain/world/service/ServicoTempoMundo.java'),
    # tests
    ('domain/character/service/CharacterAttributeServiceTest.java','domain/character/service/ServicoAtributosPersonagemTest.java'),
    ('domain/character/service/ProgressionServiceTest.java',   'domain/character/service/ServicoProgressaoTest.java'),
    ('domain/combat/service/CombatServiceTest.java',           'domain/combat/service/ServicoCombateTest.java'),
    ('domain/physiology/service/PhysiologicalServiceTest.java','domain/physiology/service/ServicoFisiologicoTest.java'),
]

# ──────────────────────────────────────────────────────────────────────────────
# Main
# ──────────────────────────────────────────────────────────────────────────────

def main():
    java_files = get_all_java_files()

    # Fase 0: substituir SocialCycleResult.java pelo novo record
    scr_path = os.path.join(
        SRC_DIR,
        'main/java/br/eng/rodrigogml/mysteryrealms/domain/social/model/SocialCycleResult.java'
    )
    if os.path.exists(scr_path):
        with open(scr_path, 'w', encoding='utf-8') as f:
            f.write(NOVO_SOCIAL_CYCLE_RESULT)
        print(f"  OVERWRITE: {os.path.relpath(scr_path, BASE_DIR)}")

    # Fase 1-4: transforms globais
    changed = 0
    for path in java_files:
        with open(path, 'r', encoding='utf-8') as f:
            original = f.read()
        content = apply_transforms(original, GLOBAL_TRANSFORMS)

        # Transforms específicos por arquivo
        rel = os.path.relpath(path, SRC_DIR)
        rel_norm = rel.replace('\\', '/')
        for key, transforms in FILE_SPECIFIC.items():
            if key in rel_norm:
                content = apply_transforms(content, transforms)

        if content != original:
            with open(path, 'w', encoding='utf-8') as f:
                f.write(content)
            changed += 1
            print(f"  CHANGED: {os.path.relpath(path, BASE_DIR)}")

    print(f"\nArquivos modificados: {changed}")

    # Fase 5: renomear arquivos via git mv
    print("\nRenomeando arquivos...")
    main_src  = 'src/main/java/br/eng/rodrigogml/mysteryrealms'
    test_src  = 'src/test/java/br/eng/rodrigogml/mysteryrealms'

    for old_rel, new_rel in FILE_RENAMES:
        # tenta em main e test
        for base in [main_src, test_src]:
            old_path = os.path.join(BASE_DIR, base, old_rel)
            new_path = os.path.join(BASE_DIR, base, new_rel)
            if os.path.exists(old_path):
                if old_path != new_path:
                    cmd = f'cd "{BASE_DIR}" && git mv "{os.path.join(base, old_rel)}" "{os.path.join(base, new_rel)}"'
                    ret = os.system(cmd)
                    status = "OK" if ret == 0 else f"ERR({ret})"
                    print(f"  {status} git mv {old_rel} -> {new_rel}")

    print("\nConcluído.")


if __name__ == '__main__':
    main()
