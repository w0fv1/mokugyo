package pers.wofbi.mokugyo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
@RestController
@ControllerAdvice
@Configuration
@SpringBootApplication
public class MokugyoApplication {
    public MokugyoApplication(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(MokugyoApplication.class, args);
    }

    private final UserRepository userRepository;

    @PostMapping("/api/user/{id}/duang")
    void duang(@PathVariable Long id, Model model, HttpSession session) {
        if (!userRepository.existsById(id)) {
            return;
        }

        User user = userRepository.getReferenceById(id);
        if (!user.getEnable()) {
            return;
        }
        user.setCount(user.getCount() + 1);
        userRepository.save(user);
    }
    @Bean
    public  ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.findAndRegisterModules();
        return mapper;
    }

    @ModelAttribute("now")
    public Mustache.Lambda now() {
        return (fragment, writer) -> {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            writer.write(now.format(formatter));
        };
    }

}
