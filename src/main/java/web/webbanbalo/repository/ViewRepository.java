package web.webbanbalo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.webbanbalo.entity.View;

public interface ViewRepository extends JpaRepository<View, Integer> {
    View findByProductId(int productId);
}
