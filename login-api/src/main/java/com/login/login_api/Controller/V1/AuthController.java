package com.login.login_api.Controller.V1;

import com.login.login_api.domain.User;
import com.login.login_api.dto.LoginRequestDto;
import com.login.login_api.dto.RegisterDTO;
import com.login.login_api.dto.ResponseDTO;
import com.login.login_api.infra.security.TokenService;
import com.login.login_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    /****
     *40:24
     */

    @PostMapping("/login")
    public ResponseEntity Login(@RequestBody LoginRequestDto body){
        User user = this.userRepository.findByEmail(body.email()).orElseThrow(()-> new RuntimeException("User not Found"));
        if (passwordEncoder.matches(user.getPassword(),body.password())){
            String token = this.tokenService.generationToken(user);
            return ResponseEntity.ok(new ResponseDTO(user.getNome(),token));
        }
        return ResponseEntity.badRequest().build();
    }



    @PostMapping("register")
    public ResponseEntity register(@RequestBody RegisterDTO body){
        Optional<User> user = this.userRepository.findByEmail(body.email());
        if (user.isEmpty()){
            User newUser = new User();
            newUser.setNome(body.name());
            newUser.setEmail(body.email());
            newUser.setPassword(body.password());

            String token = this.tokenService.generationToken(newUser);
            return ResponseEntity.ok(new ResponseDTO(newUser.getNome(),token));
        }

        return ResponseEntity.badRequest().build();
    }


}
