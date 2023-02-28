package fa.forum.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserModelDTO {
    private String uuid;
    private String username;
    private String password;
    private String about;
    private String confirmPassword;
    private String globalError;
    private MultipartFile logo;

    public UserModelDTO() {}
}
