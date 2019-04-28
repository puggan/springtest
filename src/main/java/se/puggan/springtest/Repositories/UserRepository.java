package se.puggan.springtest.Repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.puggan.springtest.Models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository  extends CrudRepository<User, Integer>
{
    @Query("select u from User u")
    public List<User> all();

    @Query("select u from User u where u.username = ?1")
    public Optional<User> byUsername(String username);
}
