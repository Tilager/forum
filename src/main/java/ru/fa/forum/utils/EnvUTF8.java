package ru.fa.forum.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;

@Component
public class EnvUTF8 {
    public String getProperty(String property){
        URI path = null;
        try {
            path = Objects.requireNonNull(this.getClass().getResource("/application.properties")).toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        Properties properties = new Properties();
        File file = new File(path);

        try {
            FileInputStream inputStream = new FileInputStream(file);
            properties.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        } catch (IOException fie) {
            fie.printStackTrace();
        }

        return properties.getProperty(property);
    }

    public String getNewsUploadImgPath() {
        File dir = new File(Objects.requireNonNull(this.getClass().getClassLoader()
                .getResource("")).getPath() + getProperty("news.uploadFile.path"));

        if (!dir.exists())
            dir.mkdir();

        return dir.getAbsolutePath();
    }
}
