package br.eng.rodrigogml.mysteryrealms.domain.economy.model;

/**
 * Representa os slots de mão equipados de um personagem — RF-EI-05.
 *
 * <p>Regras:
 * <ul>
 *   <li>O personagem possui até 2 mãos ocupáveis (slot 1 e slot 2).</li>
 *   <li>Um item de 1 mão ocupa exatamente 1 slot.</li>
 *   <li>Um item de 2 mãos ocupa ambos os slots simultaneamente.</li>
 *   <li>Bônus de item só são computados enquanto o item estiver efetivamente equipado.</li>
 *   <li>A troca de item equipado é uma ação de pré-turno (ação rápida), conforme RF-CT-06.</li>
 * </ul>
 *
 * @author ?
 * @since 28-04-2026
 */
public class EquippedHands {

    /** Slot da mão principal (mão 1). {@code null} se vazio. */
    private HandItem slotOne;

    /** Slot da mão secundária (mão 2). {@code null} se vazio. */
    private HandItem slotTwo;

    /** Cria um par de mãos completamente vazio. */
    public EquippedHands() {
        this.slotOne = null;
        this.slotTwo = null;
    }

    /**
     * Verifica se o item informado pode ser equipado no estado atual dos slots.
     *
     * @param item item a verificar; não pode ser {@code null}
     * @return {@code true} se há slots suficientes disponíveis
     */
    public boolean canEquip(HandItem item) {
        if (item == null) throw new IllegalArgumentException("item não pode ser nulo");
        if (isEquipped(item)) return false;
        if (item instanceof Shield && hasShieldEquipped()) return false;
        if (item.getHandsRequired() == 2) {
            return slotOne == null && slotTwo == null;
        }
        return slotOne == null || slotTwo == null;
    }

    /**
     * Equipa o item nos slots disponíveis.
     * <ul>
     *   <li>Item de 1 mão: ocupa o primeiro slot vazio (slot 1 ou slot 2).</li>
     *   <li>Item de 2 mãos: ocupa ambos os slots; lança exceção se algum estiver ocupado.</li>
     * </ul>
     *
     * @param item item a equipar; não pode ser {@code null}
     * @throws IllegalStateException se não houver slot(s) disponível(is) para o item
     */
    public void equip(HandItem item) {
        if (item == null) throw new IllegalArgumentException("item não pode ser nulo");
        if (!canEquip(item)) {
            throw new IllegalStateException("Slots insuficientes para equipar: " + item.getName());
        }
        if (item.getHandsRequired() == 2) {
            slotOne = item;
            slotTwo = item;
        } else {
            if (slotOne == null) {
                slotOne = item;
            } else {
                slotTwo = item;
            }
        }
    }

    /**
     * Desequipa o item informado, liberando o(s) slot(s) que ele ocupava.
     *
     * @param item item a remover; não pode ser {@code null}
     * @throws IllegalStateException se o item não estiver equipado
     */
    public void unequip(HandItem item) {
        if (item == null) throw new IllegalArgumentException("item não pode ser nulo");
        if (!isEquipped(item)) {
            throw new IllegalStateException("Item não está equipado: " + item.getName());
        }
        if (slotOne == item) slotOne = null;
        if (slotTwo == item) slotTwo = null;
    }

    /**
     * Verifica se o item informado está equipado em algum slot.
     *
     * @param item item a verificar
     * @return {@code true} se o item estiver no slot 1 ou no slot 2
     */
    public boolean isEquipped(HandItem item) {
        return item != null && (slotOne == item || slotTwo == item);
    }

    /**
     * Retorna {@code true} se ambos os slots estão vazios.
     *
     * @return {@code true} se ambas as mãos estiverem livres
     */
    public boolean isEmpty() {
        return slotOne == null && slotTwo == null;
    }

    /**
     * Retorna {@code true} se ao menos um slot está disponível para um item de 1 mão.
     *
     * @return {@code true} se há pelo menos um slot vazio
     */
    public boolean hasFreeSlot() {
        return slotOne == null || slotTwo == null;
    }

    /**
     * Retorna o item no slot 1, ou {@code null} se estiver vazio.
     *
     * @return {@link HandItem} do slot 1, ou {@code null}
     */
    public HandItem getSlotOne() {
        return slotOne;
    }

    /**
     * Retorna o item no slot 2, ou {@code null} se estiver vazio.
     *
     * @return {@link HandItem} do slot 2, ou {@code null}
     */
    public HandItem getSlotTwo() {
        return slotTwo;
    }

    private boolean hasShieldEquipped() {
        return slotOne instanceof Shield || slotTwo instanceof Shield;
    }
}
