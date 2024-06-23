package web.webbanbalo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.webbanbalo.entity.Favorite;
import web.webbanbalo.entity.Product;
import web.webbanbalo.entity.User;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    List<Favorite> findByUser(User user);

    Optional<Favorite> findByUserAndProduct(User user, Product product);

    Optional<Favorite> findByUserIdAndProductId(User user, Product product);
}
