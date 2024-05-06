package co.com.ies.pruebas.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link co.com.ies.pruebas.domain.Slot} entity. This class is used
 * in {@link co.com.ies.pruebas.web.rest.SlotResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /slots?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SlotCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter idCasino;

    private StringFilter serial;

    private StringFilter nuc;

    private ZonedDateTimeFilter initialized;

    private BigDecimalFilter balance;

    private LongFilter casinoId;

    private LongFilter modelId;

    private Boolean distinct;

    public SlotCriteria() {}

    public SlotCriteria(SlotCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.idCasino = other.optionalIdCasino().map(IntegerFilter::copy).orElse(null);
        this.serial = other.optionalSerial().map(StringFilter::copy).orElse(null);
        this.nuc = other.optionalNuc().map(StringFilter::copy).orElse(null);
        this.initialized = other.optionalInitialized().map(ZonedDateTimeFilter::copy).orElse(null);
        this.balance = other.optionalBalance().map(BigDecimalFilter::copy).orElse(null);
        this.casinoId = other.optionalCasinoId().map(LongFilter::copy).orElse(null);
        this.modelId = other.optionalModelId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SlotCriteria copy() {
        return new SlotCriteria(this);
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

    public IntegerFilter getIdCasino() {
        return idCasino;
    }

    public Optional<IntegerFilter> optionalIdCasino() {
        return Optional.ofNullable(idCasino);
    }

    public IntegerFilter idCasino() {
        if (idCasino == null) {
            setIdCasino(new IntegerFilter());
        }
        return idCasino;
    }

    public void setIdCasino(IntegerFilter idCasino) {
        this.idCasino = idCasino;
    }

    public StringFilter getSerial() {
        return serial;
    }

    public Optional<StringFilter> optionalSerial() {
        return Optional.ofNullable(serial);
    }

    public StringFilter serial() {
        if (serial == null) {
            setSerial(new StringFilter());
        }
        return serial;
    }

    public void setSerial(StringFilter serial) {
        this.serial = serial;
    }

    public StringFilter getNuc() {
        return nuc;
    }

    public Optional<StringFilter> optionalNuc() {
        return Optional.ofNullable(nuc);
    }

    public StringFilter nuc() {
        if (nuc == null) {
            setNuc(new StringFilter());
        }
        return nuc;
    }

    public void setNuc(StringFilter nuc) {
        this.nuc = nuc;
    }

    public ZonedDateTimeFilter getInitialized() {
        return initialized;
    }

    public Optional<ZonedDateTimeFilter> optionalInitialized() {
        return Optional.ofNullable(initialized);
    }

    public ZonedDateTimeFilter initialized() {
        if (initialized == null) {
            setInitialized(new ZonedDateTimeFilter());
        }
        return initialized;
    }

    public void setInitialized(ZonedDateTimeFilter initialized) {
        this.initialized = initialized;
    }

    public BigDecimalFilter getBalance() {
        return balance;
    }

    public Optional<BigDecimalFilter> optionalBalance() {
        return Optional.ofNullable(balance);
    }

    public BigDecimalFilter balance() {
        if (balance == null) {
            setBalance(new BigDecimalFilter());
        }
        return balance;
    }

    public void setBalance(BigDecimalFilter balance) {
        this.balance = balance;
    }

    public LongFilter getCasinoId() {
        return casinoId;
    }

    public Optional<LongFilter> optionalCasinoId() {
        return Optional.ofNullable(casinoId);
    }

    public LongFilter casinoId() {
        if (casinoId == null) {
            setCasinoId(new LongFilter());
        }
        return casinoId;
    }

    public void setCasinoId(LongFilter casinoId) {
        this.casinoId = casinoId;
    }

    public LongFilter getModelId() {
        return modelId;
    }

    public Optional<LongFilter> optionalModelId() {
        return Optional.ofNullable(modelId);
    }

    public LongFilter modelId() {
        if (modelId == null) {
            setModelId(new LongFilter());
        }
        return modelId;
    }

    public void setModelId(LongFilter modelId) {
        this.modelId = modelId;
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
        final SlotCriteria that = (SlotCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(idCasino, that.idCasino) &&
            Objects.equals(serial, that.serial) &&
            Objects.equals(nuc, that.nuc) &&
            Objects.equals(initialized, that.initialized) &&
            Objects.equals(balance, that.balance) &&
            Objects.equals(casinoId, that.casinoId) &&
            Objects.equals(modelId, that.modelId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idCasino, serial, nuc, initialized, balance, casinoId, modelId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SlotCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalIdCasino().map(f -> "idCasino=" + f + ", ").orElse("") +
            optionalSerial().map(f -> "serial=" + f + ", ").orElse("") +
            optionalNuc().map(f -> "nuc=" + f + ", ").orElse("") +
            optionalInitialized().map(f -> "initialized=" + f + ", ").orElse("") +
            optionalBalance().map(f -> "balance=" + f + ", ").orElse("") +
            optionalCasinoId().map(f -> "casinoId=" + f + ", ").orElse("") +
            optionalModelId().map(f -> "modelId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
