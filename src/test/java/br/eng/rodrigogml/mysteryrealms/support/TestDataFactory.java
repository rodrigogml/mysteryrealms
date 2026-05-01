package br.eng.rodrigogml.mysteryrealms.support;

import br.eng.rodrigogml.mysteryrealms.domain.character.enums.CharacterClass;
import br.eng.rodrigogml.mysteryrealms.domain.character.enums.Race;

/**
 * Factory de dados reutilizáveis para cenários de teste.
 *
 * @author ?
 * @since 01-05-2026
 */
public final class TestDataFactory {

    private TestDataFactory() {
    }

    public static RegistrationData registration(String seed) {
        return new RegistrationData(seed + "User", seed + "@example.com", "StrongP@ss1");
    }

    public static CharacterCreationData warrior(String name) {
        return new CharacterCreationData(name, Race.HUMAN, CharacterClass.WARRIOR);
    }

    public static CharacterCreationData mage(String name) {
        return new CharacterCreationData(name, Race.ELF, CharacterClass.MAGE);
    }

    public record RegistrationData(String username, String email, String password) {
    }

    public record CharacterCreationData(String name, Race race, CharacterClass characterClass) {
    }
}
