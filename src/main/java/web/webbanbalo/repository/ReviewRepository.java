package web.webbanbalo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import web.webbanbalo.entity.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    @Query("SELECT r FROM Review r JOIN FETCH r.user WHERE r.product.id = :productId")
    List<Review> findByProductIdWithUser(@Param("productId") int productId);

}