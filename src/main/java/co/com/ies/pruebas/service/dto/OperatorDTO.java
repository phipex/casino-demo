package co.com.ies.pruebas.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link co.com.ies.pruebas.domain.Operator} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OperatorDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String nit;

    @NotNull
    private String contract;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OperatorDTO)) {
            return false;
        }

        OperatorDTO operatorDTO = (OperatorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, operatorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OperatorDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", nit='" + getNit() + "'" +
            ", contract='" + getContract() + "'" +
            "}";
    }
}
