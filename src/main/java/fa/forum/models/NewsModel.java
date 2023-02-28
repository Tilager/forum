package fa.forum.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="news")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class NewsModel implements Serializable {
    @Id
    @Column(name="id")
    private String uuid;

    @NotEmpty
    @Column(name="name")
    private String name;

    @NotEmpty
    @Column(name="description")
    private String description;

    @Column(name= "file_name")
    private String fileName;

    @Column(name="added_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date addedDate;

}
