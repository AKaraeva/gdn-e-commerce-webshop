package at.gdn.backend.persistence.repository;

import at.gdn.backend.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Country.CountryId> {

}
