package web.webbanbalo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import web.webbanbalo.entity.Bill;

import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Integer> {
    Page<Bill> findByUserId(int userId, Pageable pageable);

    @Query("SELECT b FROM Bill b JOIN b.billDetails bd WHERE b.cart.user.id = :userId AND bd.product.id = :productId")
    Optional<Bill> findFirstByCartUserAndBillDetailsProduct(@Param("userId") int userId, @Param("productId") int productId);
}