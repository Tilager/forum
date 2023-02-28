package fa.forum;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;

@SpringBootApplication
public class ForumApplication {
	@Value("${upload_path}")
	private String uploadPath;

	public static void main(String[] args) {
		SpringApplication.run(ForumApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public ModelMapper modelMapper() { return new ModelMapper(); }

	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		new File(uploadPath + "/files/").mkdirs();
		new File(uploadPath + "/news/").mkdirs();
	}
}
