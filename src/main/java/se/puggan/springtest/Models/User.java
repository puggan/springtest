package se.puggan.springtest.Models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class User
{
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Integer id;
    public String username;
    public String displayname;

    @OneToMany //(mappedBy = "bookCategory", cascade = CascadeType.ALL)
    private Set<UserAuth> auths;
}
