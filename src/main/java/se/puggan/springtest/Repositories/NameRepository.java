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
    public List<Name> all();
}
