package web.webbanbalo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.webbanbalo.entity.Cart;
import web.webbanbalo.entity.CartItem;
import web.webbanbalo.entity.Product;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
    List<CartItem> findByCart(Cart cart);
}
