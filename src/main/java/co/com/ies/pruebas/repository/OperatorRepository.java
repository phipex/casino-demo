package co.com.ies.pruebas.repository;

import co.com.ies.pruebas.domain.Operator;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Operator entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OperatorRepository extends JpaRepository<Operator, Long>, JpaSpecificationExecutor<Operator> {}
