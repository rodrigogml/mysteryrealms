package br.eng.rodrigogml.mysteryrealms.application.character.repository;

import br.eng.rodrigogml.mysteryrealms.application.character.entity.ItemEntity;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório Spring Data JPA para {@link ItemEntity}.
 *
 * @author ?
 * @since 28-04-2026
 */
public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
}
