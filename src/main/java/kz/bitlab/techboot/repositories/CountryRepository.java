package kz.bitlab.techboot.repositories;

import jakarta.transaction.Transactional;
import kz.bitlab.techboot.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface CountryRepository extends JpaRepository<Country, Long> {
}
