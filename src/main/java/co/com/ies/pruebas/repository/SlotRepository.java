package co.com.ies.pruebas.repository;

import co.com.ies.pruebas.domain.Slot;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Slot entity.
 */
@Repository
public interface SlotRepository extends JpaRepository<Slot, Long>, JpaSpecificationExecutor<Slot> {
    default Optional<Slot> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Slot> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Slot> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select slot from Slot slot left join fetch slot.casino left join fetch slot.model",
        countQuery = "select count(slot) from Slot slot"
    )
    Page<Slot> findAllWithToOneRelationships(Pageable pageable);

    @Query("select slot from Slot slot left join fetch slot.casino left join fetch slot.model")
    List<Slot> findAllWithToOneRelationships();

    @Query("select slot from Slot slot left join fetch slot.casino left join fetch slot.model where slot.id =:id")
    Optional<Slot> findOneWithToOneRelationships(@Param("id") Long id);
}
