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
    @Query("select n from Name n where firstname || ' ' || lastname LIKE %?1% AND firstname LIKE %?2% AND lastname LIKE %?3%")
    List<Name> maxSearch(String search, String firstname, String lastname);

    @Query("select n from Name n where firstname || ' ' || lastname LIKE %?1% AND firstname LIKE %?2% AND lastname LIKE %?3%")
    Page<Name> maxSearch(Pageable p, String search, String firstname, String lastname);
}
