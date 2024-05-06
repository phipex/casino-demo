package co.com.ies.pruebas.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ModelCriteriaTest {

    @Test
    void newModelCriteriaHasAllFiltersNullTest() {
        var modelCriteria = new ModelCriteria();
        assertThat(modelCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void modelCriteriaFluentMethodsCreatesFiltersTest() {
        var modelCriteria = new ModelCriteria();

        setAllFilters(modelCriteria);

        assertThat(modelCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void modelCriteriaCopyCreatesNullFilterTest() {
        var modelCriteria = new ModelCriteria();
        var copy = modelCriteria.copy();

        assertThat(modelCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(modelCriteria)
        );
    }

    @Test
    void modelCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var modelCriteria = new ModelCriteria();
        setAllFilters(modelCriteria);

        var copy = modelCriteria.copy();

        assertThat(modelCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(modelCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var modelCriteria = new ModelCriteria();

        assertThat(modelCriteria).hasToString("ModelCriteria{}");
    }

    private static void setAllFilters(ModelCriteria modelCriteria) {
        modelCriteria.id();
        modelCriteria.name();
        modelCriteria.manufacturerId();
        modelCriteria.distinct();
    }

    private static Condition<ModelCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getManufacturerId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ModelCriteria> copyFiltersAre(ModelCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getManufacturerId(), copy.getManufacturerId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
