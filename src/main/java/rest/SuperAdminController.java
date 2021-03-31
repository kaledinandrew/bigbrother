package rest;

import dto.UserCreateDto;
import dto.UserDto;
import models.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repositories.RoleRepository;
import repositories.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/super_admin/")
public class SuperAdminController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public SuperAdminController(UserRepository userRepository,
                                RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping(value = "hello")
    public String hello() {
        return "hello, superuser";
    }

    @PostMapping(value = "add_admin")
    public ResponseEntity<?> addAdmin(@RequestBody UserCreateDto userCreateDto) {
        String username = userCreateDto.getUsername();
        Map<String, String> response = new HashMap<>();

        if (userRepository.findByUsername(username) != null) {
            response.put("status", "User with this username already exists");
            return ResponseEntity.ok(response);
        }

        userRepository.save(userCreateDto.toUser(
                roleRepository.findByName("ROLE_ADMIN")));

        response.put("status", "OK");
        response.put("username", username);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "users")
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDto::fromUser).collect(Collectors.toList());
    }

    @GetMapping(value = "admins")
    public List<UserDto> getAllAdmins() {
        Role roleAdmin = roleRepository.findByName("ROLE_ADMIN");
        return userRepository.findAll().stream()
                .filter(user -> user.getRoles().contains(roleAdmin))
                .map(UserDto::fromUser)
                .collect(Collectors.toList());
    }
}
