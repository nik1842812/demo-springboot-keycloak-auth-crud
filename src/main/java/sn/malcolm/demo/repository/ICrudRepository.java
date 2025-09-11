package sn.malcolm.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@NoRepositoryBean
public interface ICrudRepository<T> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {

    @Query("SELECT e FROM #{#entityName} e WHERE e.id = :id AND (e.status = 1 OR e.status IS NULL)")
    Optional<T> findByIdNotDeleted(@Param("id") Long id);

}
