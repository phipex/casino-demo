package co.com.ies.pruebas.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SlotCriteriaTest {

    @Test
    void newSlotCriteriaHasAllFiltersNullTest() {
        var slotCriteria = new SlotCriteria();
        assertThat(slotCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void slotCriteriaFluentMethodsCreatesFiltersTest() {
        var slotCriteria = new SlotCriteria();

        setAllFilters(slotCriteria);

        assertThat(slotCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void slotCriteriaCopyCreatesNullFilterTest() {
        var slotCriteria = new SlotCriteria();
        var copy = slotCriteria.copy();

        assertThat(slotCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(slotCriteria)
        );
    }

    @Test
    void slotCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var slotCriteria = new SlotCriteria();
        setAllFilters(slotCriteria);

        var copy = slotCriteria.copy();

        assertThat(slotCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(slotCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var slotCriteria = new SlotCriteria();

        assertThat(slotCriteria).hasToString("SlotCriteria{}");
    }

    private static void setAllFilters(SlotCriteria slotCriteria) {
        slotCriteria.id();
        slotCriteria.idCasino();
        slotCriteria.serial();
        slotCriteria.nuc();
        slotCriteria.initialized();
        slotCriteria.balance();
        slotCriteria.casinoId();
        slotCriteria.modelId();
        slotCriteria.distinct();
    }

    private static Condition<SlotCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getIdCasino()) &&
                condition.apply(criteria.getSerial()) &&
                condition.apply(criteria.getNuc()) &&
                condition.apply(criteria.getInitialized()) &&
                condition.apply(criteria.getBalance()) &&
                condition.apply(criteria.getCasinoId()) &&
                condition.apply(criteria.getModelId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SlotCriteria> copyFiltersAre(SlotCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getIdCasino(), copy.getIdCasino()) &&
                condition.apply(criteria.getSerial(), copy.getSerial()) &&
                condition.apply(criteria.getNuc(), copy.getNuc()) &&
                condition.apply(criteria.getInitialized(), copy.getInitialized()) &&
                condition.apply(criteria.getBalance(), copy.getBalance()) &&
                condition.apply(criteria.getCasinoId(), copy.getCasinoId()) &&
                condition.apply(criteria.getModelId(), copy.getModelId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
