package ru.fa.forum.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

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
}
