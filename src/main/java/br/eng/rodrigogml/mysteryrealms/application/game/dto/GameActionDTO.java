package br.eng.rodrigogml.mysteryrealms.application.game.dto;

/**
 * DTO que representa uma ação da tela de jogo e se ela está disponível ou bloqueada.
 *
 * @author ?
 * @since 08-05-2026
 */
public class GameActionDTO {

    private String actionId;
    private String labelMessageKey;
    private boolean available;
    private String blockedReasonMessageKey;

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getLabelMessageKey() {
        return labelMessageKey;
    }

    public void setLabelMessageKey(String labelMessageKey) {
        this.labelMessageKey = labelMessageKey;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getBlockedReasonMessageKey() {
        return blockedReasonMessageKey;
    }

    public void setBlockedReasonMessageKey(String blockedReasonMessageKey) {
        this.blockedReasonMessageKey = blockedReasonMessageKey;
    }
}
