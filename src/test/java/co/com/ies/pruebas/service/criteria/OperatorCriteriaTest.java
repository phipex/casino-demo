package co.com.ies.pruebas.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OperatorCriteriaTest {

    @Test
    void newOperatorCriteriaHasAllFiltersNullTest() {
        var operatorCriteria = new OperatorCriteria();
        assertThat(operatorCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void operatorCriteriaFluentMethodsCreatesFiltersTest() {
        var operatorCriteria = new OperatorCriteria();

        setAllFilters(operatorCriteria);

        assertThat(operatorCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void operatorCriteriaCopyCreatesNullFilterTest() {
        var operatorCriteria = new OperatorCriteria();
        var copy = operatorCriteria.copy();

        assertThat(operatorCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(operatorCriteria)
        );
    }

    @Test
    void operatorCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var operatorCriteria = new OperatorCriteria();
        setAllFilters(operatorCriteria);

        var copy = operatorCriteria.copy();

        assertThat(operatorCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(operatorCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var operatorCriteria = new OperatorCriteria();

        assertThat(operatorCriteria).hasToString("OperatorCriteria{}");
    }

    private static void setAllFilters(OperatorCriteria operatorCriteria) {
        operatorCriteria.id();
        operatorCriteria.name();
        operatorCriteria.nit();
        operatorCriteria.contract();
        operatorCriteria.distinct();
    }

    private static Condition<OperatorCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getNit()) &&
                condition.apply(criteria.getContract()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OperatorCriteria> copyFiltersAre(OperatorCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getNit(), copy.getNit()) &&
                condition.apply(criteria.getContract(), copy.getContract()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
