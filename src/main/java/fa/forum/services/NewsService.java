package fa.forum.services;

import fa.forum.models.NewsModel;
import fa.forum.repositories.NewsRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Log4j2
public class NewsService {
    private final NewsRepository newsRepository;

    @Value("${upload_path}")
    private String uploadPath;

    @Autowired
    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public NewsModel saveNews(NewsModel news, MultipartFile file) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        String uuid = UUID.randomUUID().toString();
        String resultFileName = uuid + "." + timeStamp + ".png";

        BufferedImage bi = ImageIO.read(file.getInputStream());
        ImageIO.write(bi, "png", new File(uploadPath + "/news/" + resultFileName));

        news.setUuid(uuid);
        news.setFileName(resultFileName);
        news.setAddedDate(new Date());
        newsRepository.save(news);

        log.info("Добавлена новость: " + news.getName());
        return news;
    }

    public List<NewsModel> findAllOrderByAddedDate() {
        return newsRepository.findAllByOrderByAddedDateDesc();
    }

    public NewsModel editNews(NewsModel news) {
        Optional<NewsModel> oldNewsOp = newsRepository.findById(news.getUuid());
        if (oldNewsOp.isPresent()) {
            NewsModel oldNews = oldNewsOp.get();
            oldNews.setName(news.getName());
            oldNews.setDescription(news.getDescription());

            newsRepository.save(oldNews);
            return oldNews;
        }
        else {
            throw new RuntimeException("Not valid UUID");
        }
    }
}
