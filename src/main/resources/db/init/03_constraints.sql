-- ============================================================
-- 03_constraints.sql — Constraints adicionais de integridade
-- ============================================================

USE mysteryrealms;

-- ============================================================
-- PERSONAGENS
-- ============================================================
ALTER TABLE character
    ADD CONSTRAINT chk_character_initialAge_bounds CHECK (initialAge >= 12 AND initialAge <= 120),
    ADD CONSTRAINT chk_character_level_positive CHECK (currentLevel >= 1),
    ADD CONSTRAINT chk_character_xp_nonNegative CHECK (accumulatedXp >= 0),
    ADD CONSTRAINT chk_character_fatigue_bounds CHECK (minFatigue <= currentFatigue AND currentFatigue <= maxFatigue),
    ADD CONSTRAINT chk_character_hunger_bounds CHECK (hungerPct >= 0 AND hungerPct <= 100),
    ADD CONSTRAINT chk_character_thirst_bounds CHECK (thirstPct >= 0 AND thirstPct <= 100),
    ADD CONSTRAINT chk_character_morale_bounds CHECK (morale >= 0 AND morale <= 100),
    ADD CONSTRAINT chk_character_currency_nonNegative CHECK (primaryCurrencyQty >= 0 AND secondaryCurrencyQty >= 0);

ALTER TABLE characterSkillPoints
    ADD CONSTRAINT chk_characterSkillPoints_points_nonNegative CHECK (pointsInvested >= 0);

ALTER TABLE characterEquippedItem
    ADD CONSTRAINT chk_characterEquippedItem_slotOrder_bounds CHECK (slotOrder >= 0 AND slotOrder <= 1),
    ADD CONSTRAINT uk_characterEquippedItem_slot UNIQUE KEY (idCharacter, slotOrder);

-- ============================================================
-- INSTÂNCIA DE MUNDO
-- ============================================================
ALTER TABLE worldInstance
    ADD CONSTRAINT chk_worldInstance_currentTimeMin_nonNegative CHECK (currentTimeMin >= 0);

ALTER TABLE worldMarker
    ADD CONSTRAINT chk_worldMarker_flag_or_int CHECK (
        (markerType = 'FLAG' AND flagValue IS NOT NULL AND intValue IS NULL)
        OR (markerType IN ('STAGE', 'COUNTER') AND intValue IS NOT NULL AND flagValue IS NULL)
    );

-- ============================================================
-- SESSÕES CO-OP
-- ============================================================
ALTER TABLE coopSession
    ADD CONSTRAINT chk_coopSession_maxPlayers_bounds CHECK (maxPlayers >= 2 AND maxPlayers <= 4);

ALTER TABLE coopParticipant
    ADD CONSTRAINT uk_coopParticipant_joined UNIQUE KEY (idCoopSession, idCharacter, joinedAt),
    ADD CONSTRAINT chk_coopParticipant_leftAt_after_joinedAt CHECK (leftAt IS NULL OR leftAt >= joinedAt);
