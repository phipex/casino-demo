package co.com.ies.pruebas.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link co.com.ies.pruebas.domain.Casino} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CasinoDTO implements Serializable {

    private Long id;

    @NotNull
    private String nit;

    @NotNull
    private String name;

    @NotNull
    private String direction;

    private OperatorDTO operator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public OperatorDTO getOperator() {
        return operator;
    }

    public void setOperator(OperatorDTO operator) {
        this.operator = operator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CasinoDTO)) {
            return false;
        }

        CasinoDTO casinoDTO = (CasinoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, casinoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CasinoDTO{" +
            "id=" + getId() +
            ", nit='" + getNit() + "'" +
            ", name='" + getName() + "'" +
            ", direction='" + getDirection() + "'" +
            ", operator=" + getOperator() +
            "}";
    }
}
