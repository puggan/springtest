package se.puggan.springtest.Repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.puggan.springtest.Models.User;
import se.puggan.springtest.Models.UserAuth;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAuthRepository extends CrudRepository<UserAuth, Integer>
{
    @Query("select ua from UserAuth ua")
    public List<UserAuth> all();

    @Query("select ua from UserAuth ua where ua.user = ?1 and ua.authtype = ?2")
    public Optional<UserAuth> byType(User user, String authtype);
}
