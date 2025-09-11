package sn.malcolm.demo.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import sn.malcolm.demo.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends ICrudRepository<User>, JpaSpecificationExecutor<User> {

    Optional<User> findByKcId(String kcId);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}
