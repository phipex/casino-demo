package co.com.ies.pruebas.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * A Slot.
 */
@Entity
@Table(name = "slot")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Slot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "id_casino", nullable = false)
    private Integer idCasino;

    @NotNull
    @Column(name = "serial", nullable = false)
    private String serial;

    @NotNull
    @Column(name = "nuc", nullable = false)
    private String nuc;

    @NotNull
    @Column(name = "initialized", nullable = false)
    private ZonedDateTime initialized;

    @Column(name = "balance", precision = 21, scale = 2)
    private BigDecimal balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "operator" }, allowSetters = true)
    private Casino casino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "manufacturer" }, allowSetters = true)
    private Model model;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Slot id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdCasino() {
        return this.idCasino;
    }

    public Slot idCasino(Integer idCasino) {
        this.setIdCasino(idCasino);
        return this;
    }

    public void setIdCasino(Integer idCasino) {
        this.idCasino = idCasino;
    }

    public String getSerial() {
        return this.serial;
    }

    public Slot serial(String serial) {
        this.setSerial(serial);
        return this;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getNuc() {
        return this.nuc;
    }

    public Slot nuc(String nuc) {
        this.setNuc(nuc);
        return this;
    }

    public void setNuc(String nuc) {
        this.nuc = nuc;
    }

    public ZonedDateTime getInitialized() {
        return this.initialized;
    }

    public Slot initialized(ZonedDateTime initialized) {
        this.setInitialized(initialized);
        return this;
    }

    public void setInitialized(ZonedDateTime initialized) {
        this.initialized = initialized;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public Slot balance(BigDecimal balance) {
        this.setBalance(balance);
        return this;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Casino getCasino() {
        return this.casino;
    }

    public void setCasino(Casino casino) {
        this.casino = casino;
    }

    public Slot casino(Casino casino) {
        this.setCasino(casino);
        return this;
    }

    public Model getModel() {
        return this.model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Slot model(Model model) {
        this.setModel(model);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Slot)) {
            return false;
        }
        return getId() != null && getId().equals(((Slot) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Slot{" +
            "id=" + getId() +
            ", idCasino=" + getIdCasino() +
            ", serial='" + getSerial() + "'" +
            ", nuc='" + getNuc() + "'" +
            ", initialized='" + getInitialized() + "'" +
            ", balance=" + getBalance() +
            "}";
    }
}
