package br.eng.rodrigogml.mysteryrealms.application.character.service;

import br.eng.rodrigogml.mysteryrealms.application.character.entity.CharacterEntity;
import br.eng.rodrigogml.mysteryrealms.application.character.repository.CharacterBackpackItemRepository;
import br.eng.rodrigogml.mysteryrealms.application.character.repository.CharacterEquippedItemRepository;
import br.eng.rodrigogml.mysteryrealms.application.character.repository.CharacterFactionReputationRepository;
import br.eng.rodrigogml.mysteryrealms.application.character.repository.CharacterLocalityReputationRepository;
import br.eng.rodrigogml.mysteryrealms.application.character.repository.CharacterNpcRelationshipRepository;
import br.eng.rodrigogml.mysteryrealms.application.character.repository.CharacterRepository;
import br.eng.rodrigogml.mysteryrealms.application.character.repository.CharacterSkillPointsRepository;
import br.eng.rodrigogml.mysteryrealms.application.world.entity.WorldInstanceEntity;
import br.eng.rodrigogml.mysteryrealms.application.world.repository.WorldInstanceRepository;
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
     */
    public CharacterService(CharacterRepository characterRepository,
            CharacterNpcRelationshipRepository npcRelationshipRepository,
            CharacterLocalityReputationRepository localityReputationRepository,
            CharacterFactionReputationRepository factionReputationRepository,
            CharacterSkillPointsRepository skillPointsRepository,
            CharacterEquippedItemRepository equippedItemRepository,
            CharacterBackpackItemRepository backpackItemRepository,
            WorldInstanceRepository worldInstanceRepository) {
        this.characterRepository = characterRepository;
        this.npcRelationshipRepository = npcRelationshipRepository;
        this.localityReputationRepository = localityReputationRepository;
        this.factionReputationRepository = factionReputationRepository;
        this.skillPointsRepository = skillPointsRepository;
        this.equippedItemRepository = equippedItemRepository;
        this.backpackItemRepository = backpackItemRepository;
        this.worldInstanceRepository = worldInstanceRepository;
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
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("character name cannot be blank");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("character.error.nameTooLong");
        }
        if (characterRepository.countByIdUser(userId) >= 50) {
            throw new IllegalArgumentException("character.error.limitReached");
        }
        if (characterRepository.existsByIdUserAndName(userId, name)) {
            throw new IllegalArgumentException("character.error.nameTaken");
        }

        // Utiliza o domínio para calcular atributos iniciais
        Character domainChar = new Character(name, "Unknown", Gender.OTHER, race, characterClass, 20);
        AttributeSet attrs = domainChar.getAttributes();

        CharacterEntity entity = new CharacterEntity();
        entity.setIdUser(userId);
        entity.setName(domainChar.getName());
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

        if (!character.getIdUser().equals(userId)) {
            throw new IllegalArgumentException("character.error.notOwned");
        }
        if (newName.length() > 100) {
            throw new IllegalArgumentException("character.error.nameTooLong");
        }
        if (characterRepository.existsByIdUserAndName(userId, newName)) {
            throw new IllegalArgumentException("character.error.nameTaken");
        }

        character.setName(newName);
        characterRepository.save(character);
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

        worldInstanceRepository.deleteByIdCharacter(characterId);
        npcRelationshipRepository.deleteAllByIdCharacter(characterId);
        localityReputationRepository.deleteAllByIdCharacter(characterId);
        factionReputationRepository.deleteAllByIdCharacter(characterId);
        skillPointsRepository.deleteAllByIdCharacter(characterId);
        equippedItemRepository.deleteAllByIdCharacter(characterId);
        backpackItemRepository.deleteAllByIdCharacter(characterId);
        characterRepository.delete(character);
    }
}
