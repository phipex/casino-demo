package co.com.ies.pruebas.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link co.com.ies.pruebas.domain.Casino} entity. This class is used
 * in {@link co.com.ies.pruebas.web.rest.CasinoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /casinos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CasinoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nit;

    private StringFilter name;

    private StringFilter direction;

    private LongFilter operatorId;

    private Boolean distinct;

    public CasinoCriteria() {}

    public CasinoCriteria(CasinoCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nit = other.optionalNit().map(StringFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.direction = other.optionalDirection().map(StringFilter::copy).orElse(null);
        this.operatorId = other.optionalOperatorId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CasinoCriteria copy() {
        return new CasinoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNit() {
        return nit;
    }

    public Optional<StringFilter> optionalNit() {
        return Optional.ofNullable(nit);
    }

    public StringFilter nit() {
        if (nit == null) {
            setNit(new StringFilter());
        }
        return nit;
    }

    public void setNit(StringFilter nit) {
        this.nit = nit;
    }

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDirection() {
        return direction;
    }

    public Optional<StringFilter> optionalDirection() {
        return Optional.ofNullable(direction);
    }

    public StringFilter direction() {
        if (direction == null) {
            setDirection(new StringFilter());
        }
        return direction;
    }

    public void setDirection(StringFilter direction) {
        this.direction = direction;
    }

    public LongFilter getOperatorId() {
        return operatorId;
    }

    public Optional<LongFilter> optionalOperatorId() {
        return Optional.ofNullable(operatorId);
    }

    public LongFilter operatorId() {
        if (operatorId == null) {
            setOperatorId(new LongFilter());
        }
        return operatorId;
    }

    public void setOperatorId(LongFilter operatorId) {
        this.operatorId = operatorId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CasinoCriteria that = (CasinoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nit, that.nit) &&
            Objects.equals(name, that.name) &&
            Objects.equals(direction, that.direction) &&
            Objects.equals(operatorId, that.operatorId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nit, name, direction, operatorId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CasinoCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNit().map(f -> "nit=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalDirection().map(f -> "direction=" + f + ", ").orElse("") +
            optionalOperatorId().map(f -> "operatorId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
