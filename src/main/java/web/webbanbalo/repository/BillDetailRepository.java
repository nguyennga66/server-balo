package web.webbanbalo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.webbanbalo.entity.BillDetail;

import java.util.List;

public interface BillDetailRepository extends JpaRepository<BillDetail, Integer> {
    List<BillDetail> findByProductId(int productId);
}