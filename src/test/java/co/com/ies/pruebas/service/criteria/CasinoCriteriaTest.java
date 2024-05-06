package co.com.ies.pruebas.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CasinoCriteriaTest {

    @Test
    void newCasinoCriteriaHasAllFiltersNullTest() {
        var casinoCriteria = new CasinoCriteria();
        assertThat(casinoCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void casinoCriteriaFluentMethodsCreatesFiltersTest() {
        var casinoCriteria = new CasinoCriteria();

        setAllFilters(casinoCriteria);

        assertThat(casinoCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void casinoCriteriaCopyCreatesNullFilterTest() {
        var casinoCriteria = new CasinoCriteria();
        var copy = casinoCriteria.copy();

        assertThat(casinoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(casinoCriteria)
        );
    }

    @Test
    void casinoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var casinoCriteria = new CasinoCriteria();
        setAllFilters(casinoCriteria);

        var copy = casinoCriteria.copy();

        assertThat(casinoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(casinoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var casinoCriteria = new CasinoCriteria();

        assertThat(casinoCriteria).hasToString("CasinoCriteria{}");
    }

    private static void setAllFilters(CasinoCriteria casinoCriteria) {
        casinoCriteria.id();
        casinoCriteria.nit();
        casinoCriteria.name();
        casinoCriteria.direction();
        casinoCriteria.operatorId();
        casinoCriteria.distinct();
    }

    private static Condition<CasinoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNit()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDirection()) &&
                condition.apply(criteria.getOperatorId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CasinoCriteria> copyFiltersAre(CasinoCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNit(), copy.getNit()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDirection(), copy.getDirection()) &&
                condition.apply(criteria.getOperatorId(), copy.getOperatorId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
