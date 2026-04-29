package br.eng.rodrigogml.mysteryrealms.application.character.service;

import br.eng.rodrigogml.mysteryrealms.application.character.entity.CharacterEntity;
import br.eng.rodrigogml.mysteryrealms.application.character.repository.CharacterBackpackItemRepository;
import br.eng.rodrigogml.mysteryrealms.application.character.repository.CharacterEquippedItemRepository;
import br.eng.rodrigogml.mysteryrealms.application.character.repository.CharacterFactionReputationRepository;
import br.eng.rodrigogml.mysteryrealms.application.character.repository.CharacterLocalityReputationRepository;
import br.eng.rodrigogml.mysteryrealms.application.character.repository.CharacterNpcRelationshipRepository;
import br.eng.rodrigogml.mysteryrealms.application.character.repository.CharacterRepository;
import br.eng.rodrigogml.mysteryrealms.application.character.repository.CharacterSkillPointsRepository;
import br.eng.rodrigogml.mysteryrealms.application.coop.entity.CoopSessionEntity;
import br.eng.rodrigogml.mysteryrealms.application.coop.repository.CoopParticipantRepository;
import br.eng.rodrigogml.mysteryrealms.application.coop.repository.CoopSessionRepository;
import br.eng.rodrigogml.mysteryrealms.application.world.entity.DiaryEntryEntity;
import br.eng.rodrigogml.mysteryrealms.application.world.entity.WorldInstanceEntity;
import br.eng.rodrigogml.mysteryrealms.application.world.repository.DiaryEntryRepository;
import br.eng.rodrigogml.mysteryrealms.application.world.repository.DiaryImpactMarkerRepository;
import br.eng.rodrigogml.mysteryrealms.application.world.repository.DiaryImpactRelationshipRepository;
import br.eng.rodrigogml.mysteryrealms.application.world.repository.DiaryImpactReputationRepository;
import br.eng.rodrigogml.mysteryrealms.application.world.repository.WorldInstanceRepository;
import br.eng.rodrigogml.mysteryrealms.application.world.repository.WorldLocationStateRepository;
import br.eng.rodrigogml.mysteryrealms.application.world.repository.WorldMarkerRepository;
import br.eng.rodrigogml.mysteryrealms.application.world.repository.WorldNpcStateRepository;
import br.eng.rodrigogml.mysteryrealms.application.world.repository.WorldQuestStateRepository;
import br.eng.rodrigogml.mysteryrealms.domain.character.enums.CharacterClass;
import br.eng.rodrigogml.mysteryrealms.domain.character.enums.Gender;
import br.eng.rodrigogml.mysteryrealms.domain.character.enums.Race;
import br.eng.rodrigogml.mysteryrealms.domain.character.model.AttributeSet;
import br.eng.rodrigogml.mysteryrealms.domain.character.model.Character;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Serviço de aplicação responsável pelo gerenciamento de personagens jogáveis:
 * criação, listagem, seleção, renomeação e exclusão.
 *
 * @author ?
 * @since 28-04-2026
 */
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final CharacterNpcRelationshipRepository npcRelationshipRepository;
    private final CharacterLocalityReputationRepository localityReputationRepository;
    private final CharacterFactionReputationRepository factionReputationRepository;
    private final CharacterSkillPointsRepository skillPointsRepository;
    private final CharacterEquippedItemRepository equippedItemRepository;
    private final CharacterBackpackItemRepository backpackItemRepository;
    private final WorldInstanceRepository worldInstanceRepository;
    private final WorldQuestStateRepository worldQuestStateRepository;
    private final WorldNpcStateRepository worldNpcStateRepository;
    private final WorldLocationStateRepository worldLocationStateRepository;
    private final WorldMarkerRepository worldMarkerRepository;
    private final DiaryEntryRepository diaryEntryRepository;
    private final DiaryImpactRelationshipRepository diaryImpactRelationshipRepository;
    private final DiaryImpactReputationRepository diaryImpactReputationRepository;
    private final DiaryImpactMarkerRepository diaryImpactMarkerRepository;
    private final CoopSessionRepository coopSessionRepository;
    private final CoopParticipantRepository coopParticipantRepository;

    /**
     * Cria o serviço com as dependências necessárias.
     *
     * @param characterRepository          repositório de personagens
     * @param npcRelationshipRepository    repositório de relacionamentos com NPCs
     * @param localityReputationRepository repositório de reputações em localidades
     * @param factionReputationRepository  repositório de reputações em facções
     * @param skillPointsRepository        repositório de pontos de habilidade
     * @param equippedItemRepository       repositório de itens equipados
     * @param backpackItemRepository       repositório de itens na mochila
     * @param worldInstanceRepository      repositório de instâncias de mundo
     * @param worldQuestStateRepository    repositório de estados de quests do mundo
     * @param worldNpcStateRepository      repositório de estados de NPCs do mundo
     * @param worldLocationStateRepository repositório de estados de localidades do mundo
     * @param worldMarkerRepository        repositório de marcadores do mundo
     * @param diaryEntryRepository         repositório de entradas de diário
     * @param diaryImpactRelationshipRepository repositório de impactos de relacionamento do diário
     * @param diaryImpactReputationRepository  repositório de impactos de reputação do diário
     * @param diaryImpactMarkerRepository       repositório de impactos de marcador do diário
     * @param coopSessionRepository        repositório de sessões cooperativas
     * @param coopParticipantRepository    repositório de participantes cooperativos
     */
    public CharacterService(CharacterRepository characterRepository,
            CharacterNpcRelationshipRepository npcRelationshipRepository,
            CharacterLocalityReputationRepository localityReputationRepository,
            CharacterFactionReputationRepository factionReputationRepository,
            CharacterSkillPointsRepository skillPointsRepository,
            CharacterEquippedItemRepository equippedItemRepository,
            CharacterBackpackItemRepository backpackItemRepository,
            WorldInstanceRepository worldInstanceRepository,
            WorldQuestStateRepository worldQuestStateRepository,
            WorldNpcStateRepository worldNpcStateRepository,
            WorldLocationStateRepository worldLocationStateRepository,
            WorldMarkerRepository worldMarkerRepository,
            DiaryEntryRepository diaryEntryRepository,
            DiaryImpactRelationshipRepository diaryImpactRelationshipRepository,
            DiaryImpactReputationRepository diaryImpactReputationRepository,
            DiaryImpactMarkerRepository diaryImpactMarkerRepository,
            CoopSessionRepository coopSessionRepository,
            CoopParticipantRepository coopParticipantRepository) {
        this.characterRepository = characterRepository;
        this.npcRelationshipRepository = npcRelationshipRepository;
        this.localityReputationRepository = localityReputationRepository;
        this.factionReputationRepository = factionReputationRepository;
        this.skillPointsRepository = skillPointsRepository;
        this.equippedItemRepository = equippedItemRepository;
        this.backpackItemRepository = backpackItemRepository;
        this.worldInstanceRepository = worldInstanceRepository;
        this.worldQuestStateRepository = worldQuestStateRepository;
        this.worldNpcStateRepository = worldNpcStateRepository;
        this.worldLocationStateRepository = worldLocationStateRepository;
        this.worldMarkerRepository = worldMarkerRepository;
        this.diaryEntryRepository = diaryEntryRepository;
        this.diaryImpactRelationshipRepository = diaryImpactRelationshipRepository;
        this.diaryImpactReputationRepository = diaryImpactReputationRepository;
        this.diaryImpactMarkerRepository = diaryImpactMarkerRepository;
        this.coopSessionRepository = coopSessionRepository;
        this.coopParticipantRepository = coopParticipantRepository;
    }

    /**
     * Cria um novo personagem para o usuário, inicializando atributos a partir da raça e classe.
     *
     * @param userId         o ID do usuário dono do personagem
     * @param name           o nome do personagem
     * @param race           a raça do personagem
     * @param characterClass a classe do personagem
     * @return a entidade do personagem criado
     * @throws IllegalArgumentException se o nome for inválido, o limite de personagens for atingido
     *                                  ou já existir um personagem com o mesmo nome
     */
    public CharacterEntity createCharacter(Long userId, String name, Race race, CharacterClass characterClass) {
        String normalizedName = normalizeAndValidateCharacterName(name);
        if (characterRepository.countByIdUser(userId) >= 50) {
            throw new IllegalArgumentException("character.error.limitReached");
        }
        if (characterRepository.existsByIdUserAndName(userId, normalizedName)) {
            throw new IllegalArgumentException("character.error.nameTaken");
        }

        // Utiliza o domínio para calcular atributos iniciais
        Character domainChar = new Character(normalizedName, "Unknown", Gender.OTHER, race, characterClass, 20);
        AttributeSet attrs = domainChar.getAttributes();

        CharacterEntity entity = new CharacterEntity();
        entity.setIdUser(userId);
        entity.setName(normalizedName);
        entity.setSurname(domainChar.getSurname());
        entity.setGender(domainChar.getGender());
        entity.setRace(domainChar.getRace());
        entity.setCharacterClass(domainChar.getCharacterClass());
        entity.setInitialAge(domainChar.getInitialAge());
        entity.setStrength(attrs.strength());
        entity.setDexterity(attrs.dexterity());
        entity.setConstitution(attrs.constitution());
        entity.setIntellect(attrs.intellect());
        entity.setPerception(attrs.perception());
        entity.setCharisma(attrs.charisma());
        entity.setWillpower(attrs.willpower());
        entity.setCurrentLevel(domainChar.getCurrentLevel());
        entity.setAccumulatedXp(domainChar.getAccumulatedXp());
        entity.setBalanceVersion(domainChar.getBalanceVersion());
        entity.setHealthPoints(domainChar.getHealthPoints());
        entity.setMaxHealthPoints(domainChar.getMaxHealthPoints());
        entity.setCurrentFatigue(domainChar.getCurrentFatigue());
        entity.setMinFatigue(domainChar.getMinFatigue());
        entity.setMaxFatigue(domainChar.getMaxFatigue());
        entity.setHungerPct(domainChar.getHungerPct());
        entity.setThirstPct(domainChar.getThirstPct());
        entity.setMorale(domainChar.getMorale());
        entity.setPrimaryCurrencyQty(domainChar.getPrimaryCurrencyQty());
        entity.setSecondaryCurrencyQty(domainChar.getSecondaryCurrencyQty());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setLastAccessedAt(null);
        entity = characterRepository.save(entity);

        // Cria a instância de mundo inicial para o personagem
        WorldInstanceEntity worldInstance = new WorldInstanceEntity();
        worldInstance.setIdCharacter(entity.getId());
        worldInstance.setCurrentTimeMin(0);
        worldInstance.setCurrentLocationId(null);
        worldInstanceRepository.save(worldInstance);

        return entity;
    }

    /**
     * Lista todos os personagens de um usuário.
     *
     * @param userId o ID do usuário
     * @return lista de personagens do usuário
     */
    public List<CharacterEntity> listCharacters(Long userId) {
        return characterRepository.findAllByIdUser(userId);
    }

    /**
     * Seleciona um personagem para jogar, atualizando a data do último acesso.
     *
     * @param userId      o ID do usuário
     * @param characterId o ID do personagem
     * @return o personagem selecionado
     * @throws IllegalArgumentException se o personagem não existir ou não pertencer ao usuário
     */
    public CharacterEntity selectCharacter(Long userId, Long characterId) {
        CharacterEntity character = characterRepository.findById(characterId)
                .orElseThrow(() -> new IllegalArgumentException("character.error.notFound"));

        if (!character.getIdUser().equals(userId)) {
            throw new IllegalArgumentException("character.error.notOwned");
        }

        character.setLastAccessedAt(LocalDateTime.now());
        return characterRepository.save(character);
    }

    /**
     * Renomeia um personagem do usuário.
     *
     * @param userId      o ID do usuário
     * @param characterId o ID do personagem
     * @param newName     o novo nome desejado
     * @throws IllegalArgumentException se o personagem não existir, não pertencer ao usuário,
     *                                  o nome exceder o limite ou já existir outro personagem com o mesmo nome
     */
    public void renameCharacter(Long userId, Long characterId, String newName) {
        CharacterEntity character = characterRepository.findById(characterId)
                .orElseThrow(() -> new IllegalArgumentException("character.error.notFound"));
        String normalizedName = normalizeAndValidateCharacterName(newName);

        if (!character.getIdUser().equals(userId)) {
            throw new IllegalArgumentException("character.error.notOwned");
        }
        if (character.getName().equals(normalizedName)) {
            return;
        }
        if (characterRepository.existsByIdUserAndName(userId, normalizedName)) {
            throw new IllegalArgumentException("character.error.nameTaken");
        }

        character.setName(normalizedName);
        characterRepository.save(character);
    }

    private String normalizeAndValidateCharacterName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("character.error.nameBlank");
        }

        String normalizedName = name.trim();
        if (normalizedName.length() > 100) {
            throw new IllegalArgumentException("character.error.nameTooLong");
        }
        return normalizedName;
    }

    /**
     * Exclui permanentemente um personagem e todos os seus dados relacionados.
     *
     * @param userId      o ID do usuário
     * @param characterId o ID do personagem
     * @throws IllegalArgumentException se o personagem não existir ou não pertencer ao usuário
     */
    public void deleteCharacter(Long userId, Long characterId) {
        CharacterEntity character = characterRepository.findById(characterId)
                .orElseThrow(() -> new IllegalArgumentException("character.error.notFound"));

        if (!character.getIdUser().equals(userId)) {
            throw new IllegalArgumentException("character.error.notOwned");
        }

        coopParticipantRepository.deleteAllByIdCharacter(characterId);
        worldInstanceRepository.findByIdCharacter(characterId).ifPresent(this::deleteWorldInstanceGraph);
        npcRelationshipRepository.deleteAllByIdCharacter(characterId);
        localityReputationRepository.deleteAllByIdCharacter(characterId);
        factionReputationRepository.deleteAllByIdCharacter(characterId);
        skillPointsRepository.deleteAllByIdCharacter(characterId);
        equippedItemRepository.deleteAllByIdCharacter(characterId);
        backpackItemRepository.deleteAllByIdCharacter(characterId);
        characterRepository.delete(character);
    }

    private void deleteWorldInstanceGraph(WorldInstanceEntity worldInstance) {
        Long worldInstanceId = worldInstance.getId();

        for (CoopSessionEntity session : coopSessionRepository.findAllByIdWorldInstance(worldInstanceId)) {
            coopParticipantRepository.deleteAllByIdCoopSession(session.getId());
        }
        coopSessionRepository.deleteAllByIdWorldInstance(worldInstanceId);

        for (DiaryEntryEntity diaryEntry : diaryEntryRepository.findAllByIdWorldInstance(worldInstanceId)) {
            diaryImpactRelationshipRepository.deleteAllByIdDiaryEntry(diaryEntry.getId());
            diaryImpactReputationRepository.deleteAllByIdDiaryEntry(diaryEntry.getId());
            diaryImpactMarkerRepository.deleteAllByIdDiaryEntry(diaryEntry.getId());
        }
        diaryEntryRepository.deleteAllByIdWorldInstance(worldInstanceId);

        worldQuestStateRepository.deleteAllByIdWorldInstance(worldInstanceId);
        worldNpcStateRepository.deleteAllByIdWorldInstance(worldInstanceId);
        worldLocationStateRepository.deleteAllByIdWorldInstance(worldInstanceId);
        worldMarkerRepository.deleteAllByIdWorldInstance(worldInstanceId);
        worldInstanceRepository.deleteByIdCharacter(worldInstance.getIdCharacter());
    }
}
