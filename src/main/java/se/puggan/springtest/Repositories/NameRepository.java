package se.puggan.springtest.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import se.puggan.springtest.Models.Name;

import java.util.List;

@Repository
public interface NameRepository extends PagingAndSortingRepository<Name, Integer>
{
    @Query(
        value = "select n.* from name n where CONCAT(firstname, ' ', lastname) LIKE ?1 AND firstname LIKE ?2 AND lastname LIKE ?3",
        nativeQuery = true
    )
    List<Name> maxSearch(String search, String firstname, String lastname);

    @Query(
        value = "select n.* from name n where CONCAT(firstname, ' ', lastname) LIKE ?1 AND firstname LIKE ?2 AND lastname LIKE ?3",
        countQuery = "select count(*) from name n where CONCAT(firstname, ' ', lastname) LIKE ?1 AND firstname LIKE ?2 AND lastname LIKE ?3",
        nativeQuery = true
    )
    Page<Name> maxSearch(Pageable var1, String search, String firstname, String lastname);
}
