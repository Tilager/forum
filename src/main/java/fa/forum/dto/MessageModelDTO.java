package fa.forum.dto;

import fa.forum.models.UserModel;
import lombok.Data;

@Data
public class MessageModelDTO {
    private UserModel fromUser;
    private String contentText;
    private String contentFileName;
}
