package se.puggan.springtest.Repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.puggan.springtest.Models.Name;

import java.util.List;

@Repository
public interface NameRepository extends CrudRepository<Name, Integer>
{
    @Query("select n from Name n")
    List<Name> all();

    @Query(
        value="select n.* from name n where CONCAT(firstname, ' ', lastname) LIKE ?1",
        nativeQuery = true
    )
    List<Name> search(String search);

    @Query(
        value="select n.* from name n where CONCAT(firstname, ' ', lastname) LIKE ?1 AND firstname LIKE ?2 AND lastname LIKE ?3",
        nativeQuery = true
    )
    List<Name> maxSearch(String search, String firstname, String lastname);
}
