package pers.wofbi.mokugyo;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import static java.util.Objects.nonNull;

@Slf4j
@Controller
public class Page {
    private final UserRepository userRepository;

    public Page(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    @GetMapping("")
    public String indexI(Model model) {
        return "forward:/index";
    }

    @GetMapping("/index")
    public String index(Model model) {

        model.addAttribute("user", Map.of(
                "id", -1,
                "to", "地球",
                "from", "W0FV1",
                "message", "再撑一天..",
                "count", 0,
                "autoCount", System.currentTimeMillis() / 1000
        ));
        return "mokugyo";
    }

    @GetMapping("/to/{to}")
    public String mokugyo(Model model, @PathVariable String to) {
        if (!userRepository.existsByTo(to)) {
            model.addAttribute("message", "并未找到您的木鱼.");
            model.addAttribute("isError", true);

            return "message";
        }
        User user = userRepository.findByTo(to);
        if (!user.getEnable()) {
            model.addAttribute("message", "这个木鱼已经被关闭了.");
            model.addAttribute("isError", true);

            return "message";
        }
        model.addAttribute("user", new Dto.UserDto(user));
        return "mokugyo";
    }

    @GetMapping("/pre/to/{to}")
    public String preMokugyo(Model model, @PathVariable String to) {
        if (!userRepository.existsByTo(to)) {
            model.addAttribute("message", "并未找到您的木鱼.");
            model.addAttribute("isError", true);

            return "message";
        }
        User user = userRepository.findByTo(to);

        model.addAttribute("user", new Dto.UserDto(user));
        return "mokugyo";
    }


    @GetMapping("/contract_me")
    public String contractMe(Model model, HttpSession session) {
        model.addAttribute("message", "联系我:w0fv1.dev@gmail.com");
        return "message";
    }

    @GetMapping("/page/manage")
    public String mLogin(@RequestParam(required = false) Long userId, Model model, HttpSession session) {
        model.addAttribute("isLoggedIn", session.getAttribute("isLoggedIn"));

        if (!Objects.equals(session.getAttribute("isLoggedIn"), true)) {
            return "manage";
        }
        List<User> users = userRepository.findAll();

        model.addAttribute("users", users.stream().map(
                Dto.UserDto::new
        ).toList());

        if (nonNull(userId)) {
            if (!userRepository.existsById(userId)) {
                model.addAttribute("message", "用户不存在.");
                model.addAttribute("isError", true);

                return "message";
            }
            model.addAttribute("user", new Dto.UserDto(userRepository.getReferenceById(userId)));
        }

        return "manage";
    }

}
