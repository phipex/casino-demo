package co.com.ies.pruebas.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link co.com.ies.pruebas.domain.Slot} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SlotDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer idCasino;

    @NotNull
    private String serial;

    @NotNull
    private String nuc;

    @NotNull
    private ZonedDateTime initialized;

    private BigDecimal balance;

    private CasinoDTO casino;

    private ModelDTO model;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdCasino() {
        return idCasino;
    }

    public void setIdCasino(Integer idCasino) {
        this.idCasino = idCasino;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getNuc() {
        return nuc;
    }

    public void setNuc(String nuc) {
        this.nuc = nuc;
    }

    public ZonedDateTime getInitialized() {
        return initialized;
    }

    public void setInitialized(ZonedDateTime initialized) {
        this.initialized = initialized;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public CasinoDTO getCasino() {
        return casino;
    }

    public void setCasino(CasinoDTO casino) {
        this.casino = casino;
    }

    public ModelDTO getModel() {
        return model;
    }

    public void setModel(ModelDTO model) {
        this.model = model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SlotDTO)) {
            return false;
        }

        SlotDTO slotDTO = (SlotDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, slotDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SlotDTO{" +
            "id=" + getId() +
            ", idCasino=" + getIdCasino() +
            ", serial='" + getSerial() + "'" +
            ", nuc='" + getNuc() + "'" +
            ", initialized='" + getInitialized() + "'" +
            ", balance=" + getBalance() +
            ", casino=" + getCasino() +
            ", model=" + getModel() +
            "}";
    }
}
