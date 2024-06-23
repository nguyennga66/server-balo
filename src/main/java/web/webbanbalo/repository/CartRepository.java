package web.webbanbalo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.webbanbalo.entity.Cart;
import web.webbanbalo.entity.User;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Cart findByUser(User user);
}
