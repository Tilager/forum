package fa.forum.services;

import fa.forum.dto.ConvertersDTO;
import fa.forum.dto.MessageModelDTO;
import fa.forum.models.MessageModel;
import fa.forum.models.UserModel;
import fa.forum.repositories.MessageRepository;
import fa.forum.utils.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {
    private final SimpMessagingTemplate template;
    private final SessionUtil sessionUtil;
    private final ConvertersDTO convertersDTO;
    private final MessageRepository msgRepository;

    @Value("${upload_path}")
    private String uploadPath;

    @Autowired
    public MessageService(SimpMessagingTemplate template, SessionUtil sessionUtil, ConvertersDTO convertersDTO, MessageRepository messageRepository) {
        this.template = template;
        this.sessionUtil = sessionUtil;
        this.convertersDTO = convertersDTO;
        this.msgRepository = messageRepository;
    }

    public List<MessageModel> findAll() {
        return msgRepository.findAll();
    }

    public List<MessageModel> findAllReversed() {
        return msgRepository.findAllByOrderBySendingTimeDesc();
    }

    public List<MessageModel> getAllMsgWithFile() {
        return msgRepository.findByContentFileNameIsNotNull();
    }
    public void sendMessage(String msg, MultipartFile msgFile) {
        MessageModelDTO msgDTO = new MessageModelDTO();
        UserModel user = sessionUtil.getLoggedUser();

        if (msgFile != null) {
            String resultFileName = msgFile.getOriginalFilename();

            File uploadDir = new File(uploadPath + "/files/" + user.getUuid() + "/uploadFiles");
            if (!uploadDir.exists())
                uploadDir.mkdir();

            try {
                msgFile.transferTo(new File(uploadDir + "/" + resultFileName));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            msgDTO.setContentFileName(resultFileName);
        }

        if (!msg.equals("") || msgFile!=null) {
            msgDTO.setContentText(msg);
            msgDTO.setFromUser(user);

            MessageModel message = convertersDTO.DTOtoMsgModel(msgDTO);
            message.setSendingTime(LocalDateTime.now());

            msgRepository.save(message);
            this.template.convertAndSend("/topic/greetings", msgDTO);
        }
    }
}
