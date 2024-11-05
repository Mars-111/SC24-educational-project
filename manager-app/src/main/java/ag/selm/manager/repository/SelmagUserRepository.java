package ag.selm.manager.repository;

import ag.selm.manager.entity.MyUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SelmagUserRepository extends CrudRepository<MyUser, Integer> {
    Optional<MyUser> findByUsername(String username);
}
