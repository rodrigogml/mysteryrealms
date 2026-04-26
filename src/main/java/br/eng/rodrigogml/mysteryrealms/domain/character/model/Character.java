package br.eng.rodrigogml.mysteryrealms.domain.character.model;

import br.eng.rodrigogml.mysteryrealms.domain.character.enums.CharacterClass;
import br.eng.rodrigogml.mysteryrealms.domain.character.enums.Gender;
import br.eng.rodrigogml.mysteryrealms.domain.character.enums.Race;
import br.eng.rodrigogml.mysteryrealms.domain.character.service.CharacterAttributeService;
import br.eng.rodrigogml.mysteryrealms.domain.character.service.ProgressionService;
import br.eng.rodrigogml.mysteryrealms.domain.economy.model.HandItem;
import br.eng.rodrigogml.mysteryrealms.domain.economy.model.MonetaryValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Agregado raiz do personagem jogador — RF-FP-01 a RF-FP-09.
 *
 * Os atributos derivados (PV máximo, fadiga máxima, peso etc.) são calculados
 * automaticamente a partir dos atributos base usando {@link CharacterAttributeService}.
 */
public class Character {

    // ── RF-FP-01: Identidade ──────────────────────────────────────────────────
    private final String nome;
    private final String sobrenome;
    private final Gender genero;
    private final Race raca;
    private final CharacterClass classe;
    private final int idadeInicial;

    // ── RF-FP-02: Atributos principais ───────────────────────────────────────
    private AttributeSet atributos;

    // ── RF-FP-04: Estado dinâmico ─────────────────────────────────────────────
    private long xpAcumulado;       // >= 0
    private int nivelAtual;         // >= 1
    private String balanceVersion;  // ex.: "BAL-1.0.0"
    private double pontosVida;      // [0, pontosVidaMax]
    private double pontosVidaMax;   // = constituicao × 10
    private double fadigaAtual;     // >= fadigaMin
    private double fadigaMin;       // cresce com o tempo acordado
    private double fadigaMax;       // = (const + vont) × 100
    private double fomePct;         // [0, 100]
    private double sedePct;         // [0, 100]
    private int moral;              // [0, 100]

    // ── RF-FP-05: Inventário e recursos ──────────────────────────────────────
    private long qtdMoedaPrimaria;   // >= 0
    private long qtdMoedaSecundaria; // >= 0
    /** Máximo de 2 mãos — RF-EI-05. */
    private final List<HandItem> itensEquipados;
    private final List<HandItem> itensMochila;

    // ── RF-FP-09: Relacionamento e reputação ─────────────────────────────────
    /** Escala [-100, 100] — RF-FP-09. */
    private final Map<String, Integer> relacionamentoNpc;
    /** Sem limite fixo — RF-FP-09. */
    private final Map<String, Integer> reputacaoLocalidade;
    /** Sem limite fixo — RF-FP-09. */
    private final Map<String, Integer> reputacaoFaccao;

    /**
     * Cria um personagem de nível 1 com atributos derivados da combinação raça + classe — RF-FP-07, RF-FP-08.
     */
    public Character(
            String nome,
            String sobrenome,
            Gender genero,
            Race raca,
            CharacterClass classe,
            int idadeInicial) {

        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("nome não pode ser vazio");
        if (sobrenome == null || sobrenome.isBlank())
            throw new IllegalArgumentException("sobrenome não pode ser vazio");
        if (genero == null)
            throw new IllegalArgumentException("genero não pode ser nulo");
        if (raca == null)
            throw new IllegalArgumentException("raca não pode ser nula");
        if (classe == null)
            throw new IllegalArgumentException("classe não pode ser nula");
        if (idadeInicial < 1)
            throw new IllegalArgumentException("idadeInicial deve ser >= 1, recebido: " + idadeInicial);

        this.nome = nome;
        this.sobrenome = sobrenome;
        this.genero = genero;
        this.raca = raca;
        this.classe = classe;
        this.idadeInicial = idadeInicial;

        // Atributos: base da raça + bônus da classe (RF-FP-07, RF-FP-08)
        this.atributos = raca.getAtributosBase().plus(classe.getBonusAtributos());

        // Estado inicial
        this.xpAcumulado = 0;
        this.nivelAtual = 1;
        this.balanceVersion = ProgressionService.BALANCE_VERSION;
        this.pontosVidaMax = CharacterAttributeService.maxHp(atributos.constituicao());
        this.pontosVida = this.pontosVidaMax;
        this.fadigaMax = CharacterAttributeService.maxFatigue(atributos.constituicao(), atributos.vontade());
        this.fadigaMin = 0.0;
        this.fadigaAtual = 0.0;
        this.fomePct = 0.0;
        this.sedePct = 0.0;
        this.moral = 75;

        // Inventário vazio
        this.qtdMoedaPrimaria = 0;
        this.qtdMoedaSecundaria = 0;
        this.itensEquipados = new ArrayList<>();
        this.itensMochila = new ArrayList<>();

        // Relacionamentos vazios
        this.relacionamentoNpc = new HashMap<>();
        this.reputacaoLocalidade = new HashMap<>();
        this.reputacaoFaccao = new HashMap<>();
    }

    // ── Identidade ────────────────────────────────────────────────────────────
    public String getNome() { return nome; }
    public String getSobrenome() { return sobrenome; }
    public Gender getGenero() { return genero; }
    public Race getRaca() { return raca; }
    public CharacterClass getClasse() { return classe; }
    public int getIdadeInicial() { return idadeInicial; }

    // ── Atributos ─────────────────────────────────────────────────────────────
    public AttributeSet getAtributos() { return atributos; }

    /**
     * Atualiza os atributos e recalcula todos os derivados — RF-FP-10.
     */
    public void setAtributos(AttributeSet atributos) {
        if (atributos == null) throw new IllegalArgumentException("atributos não podem ser nulos");
        this.atributos = atributos;
        recalcularDerivados();
    }

    // ── Estado dinâmico ───────────────────────────────────────────────────────
    public long getXpAcumulado() { return xpAcumulado; }

    public void setXpAcumulado(long xpAcumulado) {
        if (xpAcumulado < 0) throw new IllegalArgumentException("xpAcumulado deve ser >= 0");
        this.xpAcumulado = xpAcumulado;
    }

    public int getNivelAtual() { return nivelAtual; }

    public void setNivelAtual(int nivelAtual) {
        if (nivelAtual < 1) throw new IllegalArgumentException("nivelAtual deve ser >= 1");
        this.nivelAtual = nivelAtual;
    }

    public String getBalanceVersion() { return balanceVersion; }
    public void setBalanceVersion(String balanceVersion) { this.balanceVersion = balanceVersion; }

    public double getPontosVida() { return pontosVida; }

    public void setPontosVida(double pontosVida) {
        this.pontosVida = Math.max(0.0, Math.min(pontosVidaMax, pontosVida));
    }

    public double getPontosVidaMax() { return pontosVidaMax; }

    public double getFadigaAtual() { return fadigaAtual; }

    public void setFadigaAtual(double fadigaAtual) {
        this.fadigaAtual = Math.max(fadigaMin, fadigaAtual);
    }

    public double getFadigaMin() { return fadigaMin; }

    public void setFadigaMin(double fadigaMin) {
        this.fadigaMin = Math.max(0.0, fadigaMin);
        // Garante invariante: fadigaAtual >= fadigaMin
        if (this.fadigaAtual < this.fadigaMin) {
            this.fadigaAtual = this.fadigaMin;
        }
    }

    public double getFadigaMax() { return fadigaMax; }

    public double getFomePct() { return fomePct; }

    public void setFomePct(double fomePct) {
        this.fomePct = Math.max(0.0, Math.min(100.0, fomePct));
    }

    public double getSedePct() { return sedePct; }

    public void setSedePct(double sedePct) {
        this.sedePct = Math.max(0.0, Math.min(100.0, sedePct));
    }

    public int getMoral() { return moral; }

    public void setMoral(int moral) {
        this.moral = Math.max(0, Math.min(100, moral));
    }

    // ── Inventário ────────────────────────────────────────────────────────────
    public long getQtdMoedaPrimaria() { return qtdMoedaPrimaria; }

    public void setQtdMoedaPrimaria(long qtdMoedaPrimaria) {
        if (qtdMoedaPrimaria < 0) throw new IllegalArgumentException("qtdMoedaPrimaria deve ser >= 0");
        this.qtdMoedaPrimaria = qtdMoedaPrimaria;
    }

    public long getQtdMoedaSecundaria() { return qtdMoedaSecundaria; }

    public void setQtdMoedaSecundaria(long qtdMoedaSecundaria) {
        if (qtdMoedaSecundaria < 0) throw new IllegalArgumentException("qtdMoedaSecundaria deve ser >= 0");
        this.qtdMoedaSecundaria = qtdMoedaSecundaria;
    }

    public MonetaryValue getMoedas() {
        return MonetaryValue.of(qtdMoedaPrimaria, qtdMoedaSecundaria);
    }

    /** Itens equipados (máx. 2 mãos) — RF-EI-05. */
    public List<HandItem> getItensEquipados() {
        return Collections.unmodifiableList(itensEquipados);
    }

    public List<HandItem> getItensMochila() {
        return Collections.unmodifiableList(itensMochila);
    }

    /**
     * Equipa um item nas mãos do personagem — RF-EI-05.
     * Valida que não excedam 2 mãos ocupáveis.
     */
    public void equiparItem(HandItem item) {
        if (item == null) throw new IllegalArgumentException("item não pode ser nulo");
        int maosOcupadas = itensEquipados.stream().mapToInt(HandItem::getMaosNecessarias).sum();
        if (maosOcupadas + item.getMaosNecessarias() > 2) {
            throw new IllegalStateException("Não há mãos disponíveis para equipar o item '" + item.getNome() + "'");
        }
        itensEquipados.add(item);
    }

    /** Remove o item equipado pelo índice (0-based). */
    public HandItem desequiparItem(int index) {
        return itensEquipados.remove(index);
    }

    public void adicionarItemMochila(HandItem item) {
        if (item == null) throw new IllegalArgumentException("item não pode ser nulo");
        itensMochila.add(item);
    }

    public HandItem removerItemMochila(int index) {
        return itensMochila.remove(index);
    }

    // ── Carga ─────────────────────────────────────────────────────────────────

    /** Carga atual em kg (equipados + mochila + moedas) — RF-FP-06.4. */
    public double getCargaAtualKg() {
        double pesoEquipados = itensEquipados.stream().mapToDouble(HandItem::getPesoKg).sum();
        double pesoBolsa = itensMochila.stream().mapToDouble(HandItem::getPesoKg).sum();
        double pesoMoedas = MonetaryValue.of(qtdMoedaPrimaria, qtdMoedaSecundaria).weightKg();
        return CharacterAttributeService.currentLoad(pesoEquipados, pesoBolsa, pesoMoedas);
    }

    // ── RF-FP-09: Relacionamento e reputação ─────────────────────────────────

    /**
     * Retorna o valor de relacionamento com o NPC indicado (0 se não registrado).
     */
    public int getRelacionamentoNpc(String npcId) {
        return relacionamentoNpc.getOrDefault(npcId, 0);
    }

    /**
     * Define o relacionamento com um NPC, clampado em [-100, 100] — RF-FP-09.
     */
    public void setRelacionamentoNpc(String npcId, int valor) {
        if (npcId == null || npcId.isBlank()) throw new IllegalArgumentException("npcId não pode ser vazio");
        relacionamentoNpc.put(npcId, Math.max(-100, Math.min(100, valor)));
    }

    public Map<String, Integer> getRelacionamentosNpc() {
        return Collections.unmodifiableMap(relacionamentoNpc);
    }

    public int getReputacaoLocalidade(String locId) {
        return reputacaoLocalidade.getOrDefault(locId, 0);
    }

    public void setReputacaoLocalidade(String locId, int valor) {
        if (locId == null || locId.isBlank()) throw new IllegalArgumentException("locId não pode ser vazio");
        reputacaoLocalidade.put(locId, valor);
    }

    public Map<String, Integer> getReputacoesLocalidade() {
        return Collections.unmodifiableMap(reputacaoLocalidade);
    }

    public int getReputacaoFaccao(String faccaoId) {
        return reputacaoFaccao.getOrDefault(faccaoId, 0);
    }

    public void setReputacaoFaccao(String faccaoId, int valor) {
        if (faccaoId == null || faccaoId.isBlank()) throw new IllegalArgumentException("faccaoId não pode ser vazio");
        reputacaoFaccao.put(faccaoId, valor);
    }

    public Map<String, Integer> getReputacoesFaccao() {
        return Collections.unmodifiableMap(reputacaoFaccao);
    }

    // ── RF-FP-10: Recálculo de derivados ─────────────────────────────────────

    /**
     * Recalcula atributos derivados sempre que os atributos base mudam — RF-FP-10.
     */
    private void recalcularDerivados() {
        double novoPvMax = CharacterAttributeService.maxHp(atributos.constituicao());
        double novaFadigaMax = CharacterAttributeService.maxFatigue(atributos.constituicao(), atributos.vontade());

        // Ajustar PV proporcionalmente se o max mudou
        if (pontosVidaMax > 0) {
            this.pontosVida = Math.min(pontosVida, novoPvMax);
        }
        this.pontosVidaMax = novoPvMax;
        this.fadigaMax = novaFadigaMax;
    }
}
