package models;

import java.time.LocalDateTime;

public class Service {

    public static final Double COPPER_TUBE_PRICE_BY_LENGTH = 80.0;

    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime scheduled_to = LocalDateTime.now();
    private boolean cancelled;
    private boolean completed;

    // installation
    private Integer installations = 0;              // number of machines to be installed.
    private Double tubeLength = 0.0;                  // copper tube length necessary for installation.
    private boolean rappelRequired;                 // marked if rappel is necessary to access the air conditioners machines. (extra value)

    // removal
    private Integer removal = 0;                    // number of machines to be removed.

    // maintenance
    private Integer maintenance = 0;                // number of machines to me maintained

    private Double value = 0.0;

    // costumer
    private Long costumer_id;
    private Costumer costumer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getScheduledTo() {
        return scheduled_to;
    }

    public void setScheduledTo(LocalDateTime scheduled_to) {
        this.scheduled_to = scheduled_to;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Integer getInstallations() {
        return installations;
    }

    public void setInstallations(Integer installations) {
        this.installations = installations;
    }

    public Double getTubeLength() {
        return tubeLength;
    }

    public void setTubeLength(Double tubeLength) {
        this.tubeLength = tubeLength;
    }

    public boolean isRappelRequired() {
        return rappelRequired;
    }

    public void setRappelRequired(boolean rappelRequired) {
        this.rappelRequired = rappelRequired;
    }

    public Integer getRemoval() {
        return removal;
    }

    public void setRemoval(Integer removal) {
        this.removal = removal;
    }

    public Integer getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(Integer maintenance) {
        this.maintenance = maintenance;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Long getCostumerId() {
        if (costumer_id == null) {
            return (costumer != null) ? costumer.getId() : null;
        }
        return costumer_id;
    }

    public void setCostumerId(Long costumer_id) {
        this.costumer_id = costumer_id;
    }

    public Costumer getCostumer() {
        return costumer;
    }

    public void setCostumer(Costumer costumer) {
        this.costumer = costumer;
        if (costumer != null) {
            this.costumer_id = costumer.getId();
        }
    }

}
