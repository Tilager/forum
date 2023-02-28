package fa.forum.dto;

import fa.forum.models.MessageModel;
import fa.forum.models.NewsModel;
import fa.forum.models.UserModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConvertersDTO {
    private final ModelMapper modelMapper;

    @Autowired
    public ConvertersDTO(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserModel DTOtoUserModel(UserModelDTO user) {
        return modelMapper.map(user, UserModel.class);
    }

    public UserModelDTO UserModelToDTO(UserModel user) { return modelMapper.map(user, UserModelDTO.class); }

    public MessageModel DTOtoMsgModel(MessageModelDTO msgDTO) { return modelMapper.map(msgDTO, MessageModel.class); }

    public MessageModelDTO MsgModelToDTO(MessageModel msg) { return modelMapper.map(msg, MessageModelDTO.class); }

    public NewsModelDTO NewsModelToDTO(NewsModel news) { return modelMapper.map(news, NewsModelDTO.class); }

    public NewsModel DTOtoNewsModel(NewsModelDTO newsDTO) { return modelMapper.map(newsDTO, NewsModel.class); }
}
