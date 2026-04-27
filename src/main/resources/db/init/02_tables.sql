-- ============================================================
-- 02_tables.sql — Criação de todas as tabelas
-- Ordem: sem FKs circulares; as FKs circulares ficam em 03_constraints.sql
-- ============================================================

USE mysteryrealms;

-- ============================================================
-- USUÁRIOS (RF-UA-01 a RF-UA-06)
-- ============================================================

-- Tabela principal de usuários
CREATE TABLE user (
    id                  BIGINT          NOT NULL AUTO_INCREMENT,
    username            VARCHAR(50)     NOT NULL,
    email               VARCHAR(255)    NOT NULL,
    passwordHash        VARCHAR(255)    NOT NULL,
    emailConfirmed      BOOLEAN         NOT NULL DEFAULT FALSE,
    -- Status: PENDING_CONFIRMATION, ACTIVE, LOCKED, DELETED
    status              VARCHAR(50)     NOT NULL DEFAULT 'PENDING_CONFIRMATION',
    createdAt           DATETIME(3)     NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_username (username),
    UNIQUE KEY uk_user_email (email)
);

-- Sessões autenticadas (RF-UA-02)
CREATE TABLE session (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    idUser      BIGINT          NOT NULL,
    token       VARCHAR(512)    NOT NULL,
    createdAt   DATETIME(3)     NOT NULL,
    expiresAt   DATETIME(3)     NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_session_token (token),
    KEY idx_session_idUser (idUser),
    CONSTRAINT fk_session_user FOREIGN KEY (idUser) REFERENCES user (id)
);

-- Tentativas de login para controle de força bruta (RF-UA-02)
CREATE TABLE loginAttempt (
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    idUser      BIGINT      NOT NULL,
    ipAddress   VARCHAR(45) NOT NULL,
    attemptTime DATETIME(3) NOT NULL,
    success     BOOLEAN     NOT NULL,
    PRIMARY KEY (id),
    KEY idx_loginAttempt_idUser (idUser),
    CONSTRAINT fk_loginAttempt_user FOREIGN KEY (idUser) REFERENCES user (id)
);

-- Bloqueio de conta por tentativas excessivas (RF-UA-02)
CREATE TABLE accountLock (
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    idUser      BIGINT      NOT NULL,
    lockedAt    DATETIME(3) NOT NULL,
    unlockAt    DATETIME(3) NOT NULL,
    PRIMARY KEY (id),
    KEY idx_accountLock_idUser (idUser),
    CONSTRAINT fk_accountLock_user FOREIGN KEY (idUser) REFERENCES user (id)
);

-- Código numérico de desbloqueio enviado por e-mail (RF-UA-02)
CREATE TABLE unlockCode (
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    idUser      BIGINT      NOT NULL,
    code        VARCHAR(10) NOT NULL,
    expiresAt   DATETIME(3) NOT NULL,
    used        BOOLEAN     NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id),
    KEY idx_unlockCode_idUser (idUser),
    CONSTRAINT fk_unlockCode_user FOREIGN KEY (idUser) REFERENCES user (id)
);

-- Confirmações de e-mail: cadastro e alteração de e-mail (RF-UA-01, RF-UA-04)
CREATE TABLE emailConfirmation (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    idUser      BIGINT          NOT NULL,
    token       VARCHAR(512)    NOT NULL,
    -- Tipo: REGISTRATION, EMAIL_CHANGE
    type        VARCHAR(50)     NOT NULL,
    -- Novo e-mail, preenchido somente quando type = EMAIL_CHANGE
    newEmail    VARCHAR(255),
    expiresAt   DATETIME(3)     NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_emailConfirmation_token (token),
    KEY idx_emailConfirmation_idUser (idUser),
    CONSTRAINT fk_emailConfirmation_user FOREIGN KEY (idUser) REFERENCES user (id)
);

-- Tokens de redefinição de senha (RF-UA-03)
CREATE TABLE passwordReset (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    idUser      BIGINT          NOT NULL,
    token       VARCHAR(512)    NOT NULL,
    expiresAt   DATETIME(3)     NOT NULL,
    used        BOOLEAN         NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id),
    UNIQUE KEY uk_passwordReset_token (token),
    KEY idx_passwordReset_idUser (idUser),
    CONSTRAINT fk_passwordReset_user FOREIGN KEY (idUser) REFERENCES user (id)
);

-- Configuração de autenticação de dois fatores (RF-UA-06)
CREATE TABLE twoFactorAuth (
    id      BIGINT          NOT NULL AUTO_INCREMENT,
    idUser  BIGINT          NOT NULL,
    -- Método: EMAIL, TOTP
    method  VARCHAR(50)     NOT NULL,
    -- Chave secreta TOTP; nulo quando method = EMAIL
    secret  VARCHAR(255),
    active  BOOLEAN         NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id),
    KEY idx_twoFactorAuth_idUser (idUser),
    CONSTRAINT fk_twoFactorAuth_user FOREIGN KEY (idUser) REFERENCES user (id)
);

-- Códigos de recuperação de 2FA (RF-UA-06)
CREATE TABLE recoveryCode (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    idUser      BIGINT          NOT NULL,
    codeHash    VARCHAR(255)    NOT NULL,
    used        BOOLEAN         NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id),
    KEY idx_recoveryCode_idUser (idUser),
    CONSTRAINT fk_recoveryCode_user FOREIGN KEY (idUser) REFERENCES user (id)
);

-- ============================================================
-- PERSONAGENS (RF-FP-01 a RF-FP-09, RF-PE-01 a RF-PE-05)
-- ============================================================

-- Ficha completa do personagem
CREATE TABLE character (
    id                      BIGINT          NOT NULL AUTO_INCREMENT,
    idUser                  BIGINT          NOT NULL,

    -- RF-FP-01: Identidade
    name                    VARCHAR(100)    NOT NULL,
    surname                 VARCHAR(100)    NOT NULL,
    -- Enum Gender: MALE, FEMALE, OTHER
    gender                  VARCHAR(50)     NOT NULL,
    -- Enum Race: HUMAN, ELF, DWARF, ORC, HALFLING, GNOME, HALF_ELF, DARK_ELF
    race                    VARCHAR(50)     NOT NULL,
    -- Enum CharacterClass: WARRIOR, MAGE, ...
    characterClass          VARCHAR(50)     NOT NULL,
    initialAge              INT             NOT NULL,

    -- RF-FP-02: Atributos principais (base raça + bônus classe)
    strength                INT             NOT NULL,
    dexterity               INT             NOT NULL,
    constitution            INT             NOT NULL,
    intellect               INT             NOT NULL,
    willpower               INT             NOT NULL,
    perception              INT             NOT NULL,
    charisma                INT             NOT NULL,

    -- RF-FP-04: Estado de progressão
    currentLevel            INT             NOT NULL DEFAULT 1,
    accumulatedXp           BIGINT          NOT NULL DEFAULT 0,
    balanceVersion          VARCHAR(50)     NOT NULL,

    -- RF-FP-04: Estado vital
    healthPoints            DOUBLE          NOT NULL,
    maxHealthPoints         DOUBLE          NOT NULL,
    currentFatigue          DOUBLE          NOT NULL DEFAULT 0,
    minFatigue              DOUBLE          NOT NULL DEFAULT 0,
    maxFatigue              DOUBLE          NOT NULL,
    hungerPct               DOUBLE          NOT NULL DEFAULT 0,
    thirstPct               DOUBLE          NOT NULL DEFAULT 0,
    morale                  INT             NOT NULL DEFAULT 75,

    -- RF-FP-05: Recursos monetários
    primaryCurrencyQty      BIGINT          NOT NULL DEFAULT 0,
    secondaryCurrencyQty    BIGINT          NOT NULL DEFAULT 0,

    -- Metadados
    createdAt               DATETIME(3)     NOT NULL,
    lastAccessedAt          DATETIME(3),

    PRIMARY KEY (id),
    -- Nome único por usuário (RF-PE-02, RF-PE-04)
    UNIQUE KEY uk_character_idUser_name (idUser, name),
    KEY idx_character_idUser (idUser),
    CONSTRAINT fk_character_user FOREIGN KEY (idUser) REFERENCES user (id)
);

-- Relacionamento do personagem com NPCs (RF-FP-09)
CREATE TABLE characterNpcRelationship (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    idCharacter BIGINT          NOT NULL,
    npcId       VARCHAR(100)    NOT NULL,
    value       INT             NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_characterNpcRelationship (idCharacter, npcId),
    KEY idx_characterNpcRelationship_idCharacter (idCharacter),
    CONSTRAINT fk_characterNpcRelationship_character FOREIGN KEY (idCharacter) REFERENCES character (id)
);

-- Reputação do personagem em localidades (RF-FP-09)
CREATE TABLE characterLocalityReputation (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    idCharacter BIGINT          NOT NULL,
    localityId  VARCHAR(100)    NOT NULL,
    value       INT             NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_characterLocalityReputation (idCharacter, localityId),
    KEY idx_characterLocalityReputation_idCharacter (idCharacter),
    CONSTRAINT fk_characterLocalityReputation_character FOREIGN KEY (idCharacter) REFERENCES character (id)
);

-- Reputação do personagem em facções (RF-FP-09)
CREATE TABLE characterFactionReputation (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    idCharacter BIGINT          NOT NULL,
    factionId   VARCHAR(100)    NOT NULL,
    value       INT             NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_characterFactionReputation (idCharacter, factionId),
    KEY idx_characterFactionReputation_idCharacter (idCharacter),
    CONSTRAINT fk_characterFactionReputation_character FOREIGN KEY (idCharacter) REFERENCES character (id)
);

-- Pontos de proficiência investidos por habilidade (RF-PP-06)
CREATE TABLE characterSkillPoints (
    id              BIGINT      NOT NULL AUTO_INCREMENT,
    idCharacter     BIGINT      NOT NULL,
    -- Chave técnica da habilidade, ex.: persuasao, furtividade (RF-FP-03)
    skillKey        VARCHAR(50) NOT NULL,
    pointsInvested  INT         NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY uk_characterSkillPoints (idCharacter, skillKey),
    KEY idx_characterSkillPoints_idCharacter (idCharacter),
    CONSTRAINT fk_characterSkillPoints_character FOREIGN KEY (idCharacter) REFERENCES character (id)
);

-- ============================================================
-- ITENS E INVENTÁRIO (RF-EI-01 a RF-EI-08)
-- ============================================================

-- Item de mão: discriminador por subtipo (RF-EI-03)
-- Campos de arma e escudo ficam nulos quando o subtipo não se aplica
CREATE TABLE item (
    id                      BIGINT          NOT NULL AUTO_INCREMENT,

    -- RF-EI-03: Atributos comuns
    -- Enum HandItemSubtype: ARMA, ESCUDO, FOCO_MAGICO, FERRAMENTA
    subtype                 VARCHAR(50)     NOT NULL,
    -- Enum HandItemCategory: ATTACK, DEFENSE, SUPPORT, ILLUMINATION, MANIPULATION
    useCategory             VARCHAR(50)     NOT NULL,
    name                    VARCHAR(100)    NOT NULL,
    handsRequired           INT             NOT NULL,
    weightKg                DOUBLE          NOT NULL,
    priceBaseMp             BIGINT          NOT NULL DEFAULT 0,
    priceBaseMs             BIGINT          NOT NULL DEFAULT 0,

    -- RF-EI-07: Campos específicos de arma
    weaponTypeId            VARCHAR(100),
    baseDamageDie           VARCHAR(20),
    -- Enum DamageType: corte, perfuracao, esmagamento, fogo, gelo, raio, acido, magia_pura, sangramento, veneno_letal
    damageType              VARCHAR(50),
    range                   VARCHAR(50),
    -- Perfil de crítico no formato "valor/multiplicador", ex.: "20/x2"
    criticalProfile         VARCHAR(20),
    -- RF-EI-04: Bônus de item — arma
    itemPrecisionBonusFlat  INT             NOT NULL DEFAULT 0,
    itemPrecisionBonusPct   DOUBLE          NOT NULL DEFAULT 0,
    itemDamageBonusFlat     INT             NOT NULL DEFAULT 0,
    itemDamageBonusPct      DOUBLE          NOT NULL DEFAULT 0,

    -- RF-EI-08: Campos específicos de escudo
    baseBlockValue          INT,
    coverageDescription     VARCHAR(255),
    penaltyDexterity        INT,
    -- RF-EI-04: Bônus de item — escudo
    itemDefenseBonusFlat    INT             NOT NULL DEFAULT 0,
    itemDefenseBonusPct     DOUBLE          NOT NULL DEFAULT 0,
    itemBlockBonusFlat      INT             NOT NULL DEFAULT 0,
    itemBlockBonusPct       DOUBLE          NOT NULL DEFAULT 0,

    PRIMARY KEY (id)
);

-- Itens equipados nas mãos do personagem — máx. 2 mãos (RF-EI-05)
CREATE TABLE characterEquippedItem (
    id          BIGINT  NOT NULL AUTO_INCREMENT,
    idCharacter BIGINT  NOT NULL,
    idItem      BIGINT  NOT NULL,
    -- Ordem do slot (0 = primeira mão, 1 = segunda mão)
    slotOrder   INT     NOT NULL,
    PRIMARY KEY (id),
    KEY idx_characterEquippedItem_idCharacter (idCharacter),
    CONSTRAINT fk_characterEquippedItem_character FOREIGN KEY (idCharacter) REFERENCES character (id),
    CONSTRAINT fk_characterEquippedItem_item FOREIGN KEY (idItem) REFERENCES item (id)
);

-- Itens na mochila do personagem (RF-FP-05)
CREATE TABLE characterBackpackItem (
    id          BIGINT  NOT NULL AUTO_INCREMENT,
    idCharacter BIGINT  NOT NULL,
    idItem      BIGINT  NOT NULL,
    PRIMARY KEY (id),
    KEY idx_characterBackpackItem_idCharacter (idCharacter),
    CONSTRAINT fk_characterBackpackItem_character FOREIGN KEY (idCharacter) REFERENCES character (id),
    CONSTRAINT fk_characterBackpackItem_item FOREIGN KEY (idItem) REFERENCES item (id)
);

-- ============================================================
-- INSTÂNCIA DE MUNDO (RF-IM-01 a RF-IM-05)
-- ============================================================

-- Instância de mundo exclusiva de cada personagem (RF-IM-01)
CREATE TABLE worldInstance (
    id                  BIGINT          NOT NULL AUTO_INCREMENT,
    idCharacter         BIGINT          NOT NULL,
    -- Tempo interno da instância em minutos (RF-MN-13)
    currentTimeMin      BIGINT          NOT NULL DEFAULT 0,
    -- Localização atual do personagem no mapa
    currentLocationId   VARCHAR(100),
    PRIMARY KEY (id),
    -- Uma instância por personagem (RF-IM-01)
    UNIQUE KEY uk_worldInstance_idCharacter (idCharacter),
    CONSTRAINT fk_worldInstance_character FOREIGN KEY (idCharacter) REFERENCES character (id)
);

-- Estado das quests na instância de mundo (RF-IM-01)
CREATE TABLE worldQuestState (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    idWorldInstance BIGINT          NOT NULL,
    questId         VARCHAR(100)    NOT NULL,
    -- Estado: AVAILABLE, ACTIVE, COMPLETED, FAILED
    state           VARCHAR(50)     NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_worldQuestState (idWorldInstance, questId),
    KEY idx_worldQuestState_idWorldInstance (idWorldInstance),
    CONSTRAINT fk_worldQuestState_worldInstance FOREIGN KEY (idWorldInstance) REFERENCES worldInstance (id)
);

-- Estado dos NPCs na instância de mundo (RF-IM-01)
CREATE TABLE worldNpcState (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    idWorldInstance BIGINT          NOT NULL,
    npcId           VARCHAR(100)    NOT NULL,
    alive           BOOLEAN         NOT NULL DEFAULT TRUE,
    -- Estado de progresso de diálogo do NPC
    dialogState     VARCHAR(100),
    PRIMARY KEY (id),
    UNIQUE KEY uk_worldNpcState (idWorldInstance, npcId),
    KEY idx_worldNpcState_idWorldInstance (idWorldInstance),
    CONSTRAINT fk_worldNpcState_worldInstance FOREIGN KEY (idWorldInstance) REFERENCES worldInstance (id)
);

-- Estado das localidades na instância de mundo (RF-IM-01)
CREATE TABLE worldLocationState (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    idWorldInstance BIGINT          NOT NULL,
    locationId      VARCHAR(100)    NOT NULL,
    discovered      BOOLEAN         NOT NULL DEFAULT FALSE,
    accessible      BOOLEAN         NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id),
    UNIQUE KEY uk_worldLocationState (idWorldInstance, locationId),
    KEY idx_worldLocationState_idWorldInstance (idWorldInstance),
    CONSTRAINT fk_worldLocationState_worldInstance FOREIGN KEY (idWorldInstance) REFERENCES worldInstance (id)
);

-- Marcadores de progresso da instância de mundo (RF-SS-09, RF-IM-01)
CREATE TABLE worldMarker (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    idWorldInstance BIGINT          NOT NULL,
    -- ID no formato mk_<dominio>_<descricaoCamelCase>
    markerId        VARCHAR(100)    NOT NULL,
    -- Enum MarkerType: FLAG, STAGE, COUNTER
    markerType      VARCHAR(50)     NOT NULL,
    -- Valor booleano para marcadores do tipo FLAG
    flagValue       BOOLEAN,
    -- Valor inteiro para marcadores do tipo STAGE ou COUNTER
    intValue        INT,
    PRIMARY KEY (id),
    UNIQUE KEY uk_worldMarker (idWorldInstance, markerId),
    KEY idx_worldMarker_idWorldInstance (idWorldInstance),
    CONSTRAINT fk_worldMarker_worldInstance FOREIGN KEY (idWorldInstance) REFERENCES worldInstance (id)
);

-- ============================================================
-- DIÁRIO DO JOGADOR (RF-SS-08)
-- ============================================================

-- Entradas do diário do jogador na instância de mundo
CREATE TABLE diaryEntry (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    idWorldInstance BIGINT          NOT NULL,
    -- ID único no escopo da instância, prefixo diary_
    entryId         VARCHAR(100)    NOT NULL,
    title           VARCHAR(255)    NOT NULL,
    summary         TEXT            NOT NULL,
    -- Formato D<n>-HH:MM
    gameDate        VARCHAR(50)     NOT NULL,
    dialogId        VARCHAR(100)    NOT NULL,
    optionId        VARCHAR(100)    NOT NULL,
    createdAt       DATETIME(3)     NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_diaryEntry_entryId (idWorldInstance, entryId),
    KEY idx_diaryEntry_idWorldInstance (idWorldInstance),
    CONSTRAINT fk_diaryEntry_worldInstance FOREIGN KEY (idWorldInstance) REFERENCES worldInstance (id)
);

-- Impactos de relacionamento registrados em entradas do diário
CREATE TABLE diaryImpactRelationship (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    idDiaryEntry    BIGINT          NOT NULL,
    -- ID do NPC alvo do delta
    targetId        VARCHAR(100)    NOT NULL,
    delta           INT             NOT NULL,
    PRIMARY KEY (id),
    KEY idx_diaryImpactRelationship_idDiaryEntry (idDiaryEntry),
    CONSTRAINT fk_diaryImpactRelationship_diaryEntry FOREIGN KEY (idDiaryEntry) REFERENCES diaryEntry (id)
);

-- Impactos de reputação registrados em entradas do diário
CREATE TABLE diaryImpactReputation (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    idDiaryEntry    BIGINT          NOT NULL,
    -- ID da localidade ou facção alvo do delta
    targetId        VARCHAR(100)    NOT NULL,
    delta           INT             NOT NULL,
    PRIMARY KEY (id),
    KEY idx_diaryImpactReputation_idDiaryEntry (idDiaryEntry),
    CONSTRAINT fk_diaryImpactReputation_diaryEntry FOREIGN KEY (idDiaryEntry) REFERENCES diaryEntry (id)
);

-- Impactos de marcadores registrados em entradas do diário
CREATE TABLE diaryImpactMarker (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    idDiaryEntry    BIGINT          NOT NULL,
    markerId        VARCHAR(100)    NOT NULL,
    -- Operação: SET, ACTIVATE, DEACTIVATE, INCREMENT, DECREMENT
    operation       VARCHAR(50)     NOT NULL,
    -- Valor usado na operação SET/INCREMENT/DECREMENT; nulo para ACTIVATE/DEACTIVATE
    value           INT,
    PRIMARY KEY (id),
    KEY idx_diaryImpactMarker_idDiaryEntry (idDiaryEntry),
    CONSTRAINT fk_diaryImpactMarker_diaryEntry FOREIGN KEY (idDiaryEntry) REFERENCES diaryEntry (id)
);

-- ============================================================
-- MULTIJOGADOR CO-OP (RF-MJ-02 a RF-MJ-07)
-- ============================================================

-- Sessão co-op hospedada pelo anfitrião (RF-MJ-02)
CREATE TABLE coopSession (
    id              BIGINT      NOT NULL AUTO_INCREMENT,
    -- Instância de mundo do anfitrião
    idWorldInstance BIGINT      NOT NULL,
    -- Máximo de jogadores simultâneos incluindo o anfitrião (RF-MJ-06)
    maxPlayers      INT         NOT NULL DEFAULT 4,
    -- Estado: ACTIVE, CLOSED
    status          VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    createdAt       DATETIME(3) NOT NULL,
    PRIMARY KEY (id),
    KEY idx_coopSession_idWorldInstance (idWorldInstance),
    CONSTRAINT fk_coopSession_worldInstance FOREIGN KEY (idWorldInstance) REFERENCES worldInstance (id)
);

-- Participantes de uma sessão co-op (RF-MJ-03)
CREATE TABLE coopParticipant (
    id              BIGINT      NOT NULL AUTO_INCREMENT,
    idCoopSession   BIGINT      NOT NULL,
    -- Personagem do jogador convidado
    idCharacter     BIGINT      NOT NULL,
    joinedAt        DATETIME(3) NOT NULL,
    -- Nulo enquanto o jogador estiver na sessão
    leftAt          DATETIME(3),
    PRIMARY KEY (id),
    KEY idx_coopParticipant_idCoopSession (idCoopSession),
    KEY idx_coopParticipant_idCharacter (idCharacter),
    CONSTRAINT fk_coopParticipant_coopSession FOREIGN KEY (idCoopSession) REFERENCES coopSession (id),
    CONSTRAINT fk_coopParticipant_character FOREIGN KEY (idCharacter) REFERENCES character (id)
);
