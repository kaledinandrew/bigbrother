package rest;

import dto.AuthenticationRequestDto;
import models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import repositories.RoleRepository;
import repositories.UserRepository;
import security.jwt.JwtTokenProvider;
import service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/auth/")
@Slf4j
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    JwtTokenProvider jwtTokenProvider,
                                    UserService userService,
                                    RoleRepository roleRepository,
                                    UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("test")
    public String testController(@RequestBody AuthenticationRequestDto body) {
        return body.getUsername() + " " + body.getPassword();
    }

    @PostMapping("make_admin")
    public String makeAdmin(@RequestBody AuthenticationRequestDto body) {
        User user = userService.findByUsername(body.getUsername());
        user.getRoles().add(roleRepository.findByName("ROLE_ADMIN"));
        return "OK";
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequestDto requestDto) {
        try {
            String username = requestDto.getUsername();
            log.info("=========== Trying to login: " + username + " " + requestDto.getPassword());

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, requestDto.getPassword());
            log.info("=========== AuthenticationToken: " + authenticationToken);
            authenticationManager.authenticate(authenticationToken);
            log.info("=========== AuthenticationManager: success");

            User user = userService.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("User with username: " + username + " not found");
            }

            // TESTING
            if (username.equals("root")) {
                user.getRoles().add(roleRepository.findByName("ROLE_ADMIN"));
            } else {
                user.getRoles().add(roleRepository.findByName("ROLE_USER"));
            }
            userRepository.save(user);
            // END TESTING

            log.info("=========== Roles of user " + username + " updated: " + user.getRoles());

            String token = jwtTokenProvider.createToken(username);

            Map<Object, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}
