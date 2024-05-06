package co.com.ies.pruebas.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link co.com.ies.pruebas.domain.Model} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ModelDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private ManufacturerDTO manufacturer;

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

    public ManufacturerDTO getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(ManufacturerDTO manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ModelDTO)) {
            return false;
        }

        ModelDTO modelDTO = (ModelDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, modelDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ModelDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", manufacturer=" + getManufacturer() +
            "}";
    }
}
