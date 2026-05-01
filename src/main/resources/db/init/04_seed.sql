-- ============================================================
-- 04_seed.sql — Dados iniciais de bootstrap
-- ============================================================

USE mysteryrealms;

-- Catálogo mínimo de itens base para equipar personagens em ambiente novo.
-- Arma simples (1 mão)
INSERT INTO item (
    subtype, useCategory, name, handsRequired, weightKg, priceBaseMp, priceBaseMs,
    weaponTypeId, baseDamageDie, damageType, `range`, criticalProfile,
    itemPrecisionBonusFlat, itemPrecisionBonusPct, itemDamageBonusFlat, itemDamageBonusPct,
    baseBlockValue, coverageDescription, penaltyDexterity,
    itemDefenseBonusFlat, itemDefenseBonusPct, itemBlockBonusFlat, itemBlockBonusPct
) VALUES (
    'WEAPON', 'ATTACK', 'Rusty Sword', 1, 2.5, 0, 35,
    'swordOneHand', '1d6', 'CUT', 'MELEE', '20/x2',
    0, 0, 0, 0,
    NULL, NULL, NULL,
    0, 0, 0, 0
);

-- Escudo simples (1 mão)
INSERT INTO item (
    subtype, useCategory, name, handsRequired, weightKg, priceBaseMp, priceBaseMs,
    weaponTypeId, baseDamageDie, damageType, `range`, criticalProfile,
    itemPrecisionBonusFlat, itemPrecisionBonusPct, itemDamageBonusFlat, itemDamageBonusPct,
    baseBlockValue, coverageDescription, penaltyDexterity,
    itemDefenseBonusFlat, itemDefenseBonusPct, itemBlockBonusFlat, itemBlockBonusPct
) VALUES (
    'SHIELD', 'DEFENSE', 'Wooden Buckler', 1, 3.0, 0, 20,
    NULL, NULL, NULL, NULL, NULL,
    0, 0, 0, 0,
    2, 'light', 0,
    0, 0, 0, 0
);
