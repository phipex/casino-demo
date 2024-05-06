package co.com.ies.pruebas.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link co.com.ies.pruebas.domain.Operator} entity. This class is used
 * in {@link co.com.ies.pruebas.web.rest.OperatorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /operators?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OperatorCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter nit;

    private StringFilter contract;

    private Boolean distinct;

    public OperatorCriteria() {}

    public OperatorCriteria(OperatorCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.nit = other.optionalNit().map(StringFilter::copy).orElse(null);
        this.contract = other.optionalContract().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public OperatorCriteria copy() {
        return new OperatorCriteria(this);
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

    public StringFilter getContract() {
        return contract;
    }

    public Optional<StringFilter> optionalContract() {
        return Optional.ofNullable(contract);
    }

    public StringFilter contract() {
        if (contract == null) {
            setContract(new StringFilter());
        }
        return contract;
    }

    public void setContract(StringFilter contract) {
        this.contract = contract;
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
        final OperatorCriteria that = (OperatorCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(nit, that.nit) &&
            Objects.equals(contract, that.contract) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nit, contract, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OperatorCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalNit().map(f -> "nit=" + f + ", ").orElse("") +
            optionalContract().map(f -> "contract=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
