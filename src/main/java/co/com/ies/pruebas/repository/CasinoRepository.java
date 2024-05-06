package co.com.ies.pruebas.repository;

import co.com.ies.pruebas.domain.Casino;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Casino entity.
 */
@Repository
public interface CasinoRepository extends JpaRepository<Casino, Long>, JpaSpecificationExecutor<Casino> {
    default Optional<Casino> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Casino> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Casino> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select casino from Casino casino left join fetch casino.operator",
        countQuery = "select count(casino) from Casino casino"
    )
    Page<Casino> findAllWithToOneRelationships(Pageable pageable);

    @Query("select casino from Casino casino left join fetch casino.operator")
    List<Casino> findAllWithToOneRelationships();

    @Query("select casino from Casino casino left join fetch casino.operator where casino.id =:id")
    Optional<Casino> findOneWithToOneRelationships(@Param("id") Long id);
}
