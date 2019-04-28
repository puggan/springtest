package se.puggan.springtest.Models;

import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(indexes={
    @Index(columnList="user_id, authtype")
})
public class UserAuth
{
    public static final String PASSWORD = "password";

    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Integer id;
    @Column(nullable = false)
    public String authtype;
    @Column(nullable = false)
    public String secret;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public void setPassword(String password)
    {
        authtype = PASSWORD;
        secret = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean verify(String input)
    {
        switch (authtype)
        {
            case PASSWORD:
                return verifyPassword(input);
            default:
                return secret.equals(input);
        }
    }

    public boolean verifyPassword(String password)
    {
        return BCrypt.checkpw(password, secret);
    }
}
