package lk.ijse.HelloShoesBE.repo;

import lk.ijse.HelloShoesBE.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User,String> {
    Optional<User> findByEmail(String email);
    User findUserByEmail(String email);
    boolean existsByEmail(String email);
}