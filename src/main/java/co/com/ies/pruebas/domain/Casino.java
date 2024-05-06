package co.com.ies.pruebas.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Casino.
 */
@Entity
@Table(name = "casino")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Casino implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nit", nullable = false)
    private String nit;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "direction", nullable = false)
    private String direction;

    @ManyToOne(fetch = FetchType.LAZY)
    private Operator operator;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Casino id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNit() {
        return this.nit;
    }

    public Casino nit(String nit) {
        this.setNit(nit);
        return this;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getName() {
        return this.name;
    }

    public Casino name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirection() {
        return this.direction;
    }

    public Casino direction(String direction) {
        this.setDirection(direction);
        return this;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Operator getOperator() {
        return this.operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Casino operator(Operator operator) {
        this.setOperator(operator);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Casino)) {
            return false;
        }
        return getId() != null && getId().equals(((Casino) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Casino{" +
            "id=" + getId() +
            ", nit='" + getNit() + "'" +
            ", name='" + getName() + "'" +
            ", direction='" + getDirection() + "'" +
            "}";
    }
}
