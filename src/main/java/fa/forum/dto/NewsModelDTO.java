package fa.forum.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class NewsModelDTO {
    private String uuid;

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    private String fileName;

    @NotEmpty
    MultipartFile logo;


    public NewsModelDTO() {}

    public NewsModelDTO(String name, String description, String fileName) {
        this.name = name;
        this.description = description;
    }
}
