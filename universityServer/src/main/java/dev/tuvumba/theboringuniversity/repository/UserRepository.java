package dev.tuvumba.theboringuniversity.repository;

import dev.tuvumba.theboringuniversity.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
}
