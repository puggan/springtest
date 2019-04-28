package se.puggan.springtest.Models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(indexes={
    @Index(columnList="username", unique=true)
})
public class User
{
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Integer id;
    @Column(nullable = false)
    public String username;
    @Column(nullable = false)
    public String displayname;

    @OneToMany
    @JoinColumn
    private Set<UserAuth> auths;
}
