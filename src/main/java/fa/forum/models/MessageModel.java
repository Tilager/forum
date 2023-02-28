package fa.forum.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "messages")
@Getter
@Setter
@ToString
public class MessageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "uuid")
    private UserModel fromUser;

    @Column(name = "content_text")
    private String contentText;

    @Column(name = "content_file_name")
    private String contentFileName;

    @Column(name="sending_time")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime sendingTime;

    public MessageModel() {}
}
