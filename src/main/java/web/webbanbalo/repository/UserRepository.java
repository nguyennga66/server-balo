package web.webbanbalo.repository;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.webbanbalo.entity.User;

@Repository
@SpringBootApplication
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
}