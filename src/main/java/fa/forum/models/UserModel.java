package fa.forum.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="users")
@Getter
@Setter
@ToString
public class UserModel implements Serializable {
    @Id
    private String uuid;

    @Column(unique = true)
    private String username;

    private String password;

    private String about;

    private String role;

    @OneToMany(mappedBy = "fromUser")
    @JsonIgnore
    @ToString.Exclude
    private List<MessageModel> messages;

    public UserModel(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserModel userModel = (UserModel) o;
        return uuid != null && Objects.equals(uuid, userModel.uuid);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
