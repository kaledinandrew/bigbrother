package rest;

import dto.AuthenticationRequestDto;
import dto.UserAddRoleDto;
import dto.UserCreateDto;
import models.Role;
import models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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

    @GetMapping("hello")
    public String hello() {
        return "hello on /auth endpoint";
    }

    @PostMapping("test")
    public String testController(@RequestBody AuthenticationRequestDto body) {
        return body.getUsername() + " " + body.getPassword();
    }

    // TODO: permit only for admin or superadmin
    @PostMapping("add_role")
    public ResponseEntity<?> addRole(@RequestBody UserAddRoleDto body) {
        User user = userService.findByUsername(body.getUsername());
        Role role_admin = roleRepository.findByName("ROLE_ADMIN");
        Role role_user = roleRepository.findByName("ROLE_USER");

        if (body.getRole().equals("admin")       && !user.getRoles().contains(role_admin)) {
            user.getRoles().add(role_admin);
        } else if (body.getRole().equals("user") && !user.getRoles().contains(role_user)) {
            user.getRoles().add(role_user);
        }

        userRepository.save(user);
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        return ResponseEntity.ok(response);
    }

    @PostMapping("sign_up")
    public ResponseEntity<?> signUp(@RequestBody UserCreateDto userCreateDto) {
        String username = userCreateDto.getUsername();
        Map<String, String> response = new HashMap<>();

        if (userRepository.findByUsername(username) != null) {
            response.put("status", "User with this username already exists");
            return ResponseEntity.ok(response);
        }

        // userRepository.save(userCreateDto.toUser(roleRepository));

        response.put("status", "OK");
        response.put("username", username);
        return ResponseEntity.ok(response);
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequestDto requestDto) {
        Map<Object, Object> response = new HashMap<>();
        try {
            String username = requestDto.getUsername();
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));

            User user = userService.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("User with username: " + username + " not found");
            }

            String token = jwtTokenProvider.createToken(username);

            response.put("username", username);
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            // throw new BadCredentialsException("Invalid username or password");
            response.put("status", "Exception while authenticating: Invalid username or password");
            return ResponseEntity.ok(response);
        }
    }
}
