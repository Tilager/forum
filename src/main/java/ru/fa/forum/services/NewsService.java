package ru.fa.forum.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.fa.forum.models.NewsModel;
import ru.fa.forum.repositories.NewsRepository;
import ru.fa.forum.utils.EnvUTF8;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class NewsService {
    private final NewsRepository newsRepository;
    private final EnvUTF8 envUTF8;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public NewsService(NewsRepository newsRepository, EnvUTF8 envUTF8) {
        this.newsRepository = newsRepository;
        this.envUTF8 = envUTF8;
    }

//    @PreAuthorize("hasRole('ADMIN')")
    public void saveFile(NewsModel news, MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String path = envUTF8.getNewsUploadImgPath();

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            String resultFileName = UUID.randomUUID().toString() + "." + timeStamp + ".png";

            BufferedImage bi = ImageIO.read(file.getInputStream());
            ImageIO.write(bi, "png", new File(path + "/" +
                    resultFileName));

            news.setFileName(resultFileName);
            news.setAddedDate(new Date());
            newsRepository.save(news);

            logger.info("Добавлена новость: " + news.getName());
        }
    }

    public List<NewsModel> findAllOrderByAddedDate() {
        return newsRepository.findAllByOrderByAddedDateDesc();
    }

    private String getFileExtension(String name) {
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }
}
