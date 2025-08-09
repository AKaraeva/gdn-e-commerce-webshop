package at.gdn.backend.persistence.repository;

import at.gdn.backend.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Category.CategoryId> {

}
