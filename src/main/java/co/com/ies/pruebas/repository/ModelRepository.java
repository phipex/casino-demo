package co.com.ies.pruebas.repository;

import co.com.ies.pruebas.domain.Model;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Model entity.
 */
@Repository
public interface ModelRepository extends JpaRepository<Model, Long>, JpaSpecificationExecutor<Model> {
    default Optional<Model> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Model> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Model> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select model from Model model left join fetch model.manufacturer", countQuery = "select count(model) from Model model")
    Page<Model> findAllWithToOneRelationships(Pageable pageable);

    @Query("select model from Model model left join fetch model.manufacturer")
    List<Model> findAllWithToOneRelationships();

    @Query("select model from Model model left join fetch model.manufacturer where model.id =:id")
    Optional<Model> findOneWithToOneRelationships(@Param("id") Long id);
}
