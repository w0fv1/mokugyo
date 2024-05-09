package pers.wofbi.mokugyo;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

import static java.lang.Thread.sleep;

@Slf4j
@Controller
public class Api {

    @Value("${app.username}")
    private String username;
    @Value("${app.password}")
    private String password;

    private final UserRepository userRepository;

    public Api(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/api/login")
    synchronized String login(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model
    ) throws InterruptedException {
        sleep(new Random().nextInt(1000));
        if (username.equals(this.username) && password.equals(this.password)) {
            session.setAttribute("isLoggedIn", true);
            model.addAttribute("isLoggedIn", true);
            return "redirect:/page/manage";
        }
        model.addAttribute("message", "登录失败");
        return "redirect:/page/manage";
    }

    @GetMapping("/api/logout")
    synchronized String logout(
            HttpSession session,
            Model model
    ) {
        session.setAttribute("isLoggedIn", false);
        model.addAttribute("isLoggedIn", false);
        model.addAttribute("isError", true);

        return "redirect:/page/manage";
    }


    @Transactional
    @PostMapping("/api/user/save")
    synchronized String saveUser(Dto.SaveUserDto saveUserDTO, Model model, HttpSession session) {
        if (saveUserDTO.getTo() == null||saveUserDTO.getTo().isEmpty()) {
            model.addAttribute("message", "To不能为空");
            model.addAttribute("isError", true);

            return "message";
        }
        if (saveUserDTO.getEnable() == null) {
            saveUserDTO.setEnable(false);
        }
        System.out.println(saveUserDTO);
        User user;
        if (saveUserDTO.getId() != null) {
            if (!userRepository.existsById(saveUserDTO.getId())) {
                model.addAttribute("message", "User不存在");
                model.addAttribute("isError", true);

                return "message";
            }
            user = userRepository.getReferenceById(saveUserDTO.getId());
        } else {
            if (userRepository.existsByTo(saveUserDTO.getTo())) {
                model.addAttribute("message", "User已存在");
                model.addAttribute("isError", true);

                return "message";
            }
            user = new User();
        }
        user.merge(saveUserDTO);
        userRepository.save(user);
        return "redirect:/page/manage";
    }


    @GetMapping("/api/user/{id}/delete")
    String deleteUser(@PathVariable Long id, Model model, HttpSession session) {
        if (!userRepository.existsById(id)) {
            model.addAttribute("message", "User不存在");
            model.addAttribute("isError", true);

            return "message";
        }
        User user = userRepository.getReferenceById(id);
        userRepository.delete(user);
        return "redirect:/page/manage";
    }

}
