package at.gdn.backend.entities;


import at.gdn.backend.enums.Department;
import at.gdn.backend.persistence.converter.DepartmentConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name="categories")
public class Category{

@EmbeddedId
  private CategoryId id;

    @Column(name="category_name", unique = false)
    private String categoryName;

    @Column(name="category_description", unique = false)
    private String categoryDescription;

    @Column(columnDefinition = DepartmentConverter.COLUMN_DEFINTION)
    private Department department;

    public Category(String s) {
    }

  public record CategoryId(@GeneratedValue(
          strategy = GenerationType.SEQUENCE,
          generator = "categories_seq"
  )
                          @SequenceGenerator(
                                  name = "categories_seq",
                                  sequenceName = "categories_seq",  // Sequenzname in der Datenbank
                                  allocationSize = 1             // Schrittweite setzen
                          )
                          @NotNull Long id){}
}