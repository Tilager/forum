package fa.forum.services;

import fa.forum.models.UserModel;
import fa.forum.repositories.UsersRepository;
import fa.forum.security.UserDetailsIm;
import lombok.extern.log4j.Log4j2;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
public class UserService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final SimpUserRegistry simpUserRegistry;

    @Value("${upload_path}")
    private String uploadPath;

    @Autowired
    public UserService(UsersRepository usersRepository, PasswordEncoder passwordEncoder, SimpUserRegistry simpUserRegistry) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.simpUserRegistry = simpUserRegistry;
    }

    public void save(UserModel user) {
        String uuid = UUID.randomUUID().toString();

        user.setUuid(uuid);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");

        File userFolder = new File(uploadPath + "/files/" + uuid);
        if (!userFolder.mkdirs())
            throw new RuntimeException("Create folder exception!");

        try {
            new File(uploadPath + "/files/" + uuid + "/uploadFiles/").mkdirs();
            File logo = ResourceUtils.getFile("classpath:static/img/logo1.png");
            FileUtil.copyFile(logo, new File(userFolder.getPath() + "/" + "logo.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        usersRepository.save(user);
    }

    public Optional<UserModel> findByUsername(String username) {
        return usersRepository.findByUsername(username);
    }

    public UserModel findByUUID(String uuid) { return usersRepository.findByUuid(uuid).orElse(null); }

    public void update(UserModel user, MultipartFile logo) {
        UserModel userFromDB = findByUUID(user.getUuid());

        if (!user.getUsername().equals(""))
            userFromDB.setUsername(user.getUsername());
        if (!user.getPassword().equals(""))
            userFromDB.setPassword(passwordEncoder.encode(user.getPassword()));
        if (logo != null) {
            try {
                BufferedImage bi = ImageIO.read(logo.getInputStream());

                ImageIO.write(bi, "png",
                                new File(uploadPath + "/files/" + user.getUuid() + "/" + "logo.png"));
            } catch (IOException e) {
                log.error("FILE ERROR~!");
                throw new RuntimeException(e);
            }
        }

        userFromDB.setAbout(user.getAbout());

        usersRepository.save(userFromDB);

        if (!user.getUsername().equals(""))
            SecurityContextHolder.getContext().setAuthentication(null);

        log.info("Updated user - " + userFromDB.getUsername());
    }

    public List<UserModel> findAllByOnline() {
        return new ArrayList<>() {{
            for (SimpUser simpUser: simpUserRegistry.getUsers()) {
                UsernamePasswordAuthenticationToken upat = (UsernamePasswordAuthenticationToken) simpUser.getPrincipal();
                UserDetailsIm userDetailsIm = (UserDetailsIm) upat.getPrincipal();
                add(userDetailsIm.getUser());
            }
        }};
    }

    public List<String> getAllFilesNameByUUID (String uuid) {
        File[] files = new File(uploadPath + "/files/" + uuid + "/" + "uploadFiles").listFiles();

        return new ArrayList<>() {{
            for (File f: files) {
                add(f.getName());
            }
        }};
    }
}
