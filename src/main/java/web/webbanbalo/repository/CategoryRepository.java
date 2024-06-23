package web.webbanbalo.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import web.webbanbalo.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer>{
    @Query("SELECT c FROM Category c WHERE c.nameC = :nameC")
    Category findByNameCategory(@Param("nameC") String nameC);
}
