package models;

import java.time.LocalDateTime;

public class Service {

    public static final Double COPPER_TUBE_PRICE_BY_LENGTH = 80.0;

    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime schedulingDate;
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
    private String costumerCpf;

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

    public LocalDateTime getSchedulingDate() {
        return schedulingDate;
    }

    public void setSchedulingDate(LocalDateTime schedulingDate) {
        this.schedulingDate = schedulingDate;
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

    public String getCostumerCpf() {
        return costumerCpf;
    }

    public void setCostumerCpf(String costumerCpf) {
        this.costumerCpf = costumerCpf;
    }

}
