package co.com.ies.pruebas.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ManufacturerAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertManufacturerAllPropertiesEquals(Manufacturer expected, Manufacturer actual) {
        assertManufacturerAutoGeneratedPropertiesEquals(expected, actual);
        assertManufacturerAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertManufacturerAllUpdatablePropertiesEquals(Manufacturer expected, Manufacturer actual) {
        assertManufacturerUpdatableFieldsEquals(expected, actual);
        assertManufacturerUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertManufacturerAutoGeneratedPropertiesEquals(Manufacturer expected, Manufacturer actual) {
        assertThat(expected)
            .as("Verify Manufacturer auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertManufacturerUpdatableFieldsEquals(Manufacturer expected, Manufacturer actual) {
        assertThat(expected)
            .as("Verify Manufacturer relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertManufacturerUpdatableRelationshipsEquals(Manufacturer expected, Manufacturer actual) {}
}