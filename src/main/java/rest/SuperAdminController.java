package rest;

import dto.UserCreateDto;
import dto.UserDto;
import models.Role;
import models.User;
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

    @GetMapping(value = "all_admins")
    public List<UserDto> getAllAdmins() {
        Role roleAdmin = roleRepository.findByName("ROLE_ADMIN");
        return userRepository.findAll().stream()
                .filter(user -> user.getRoles().contains(roleAdmin))
                .map(UserDto::fromUser)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "admin")
    public ResponseEntity<?> getAdminById(@RequestParam(name = "id") Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null || !user.getRoles().contains(roleRepository.findByName("ROLE_ADMIN"))) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "No admin with this id");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(UserDto.fromUser(user));
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

    @PutMapping(value = "edit_admin")
    public ResponseEntity<?> editAdmin(@RequestParam(name = "id") Long id,
                                       @RequestBody UserCreateDto userCreateDto) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "no user with id: " + id);
            return ResponseEntity.ok(response);
        }
        user.editUser(userCreateDto);
        userRepository.save(user);
        return ResponseEntity.ok(UserDto.fromUser(user));
    }

    @DeleteMapping(value = "delete_admin")
    public ResponseEntity<?> deleteAdminById(@RequestParam(name = "id") Long id) {
        User user = userRepository.findById(id).orElse(null);
        Map<String, String> response = new HashMap<>();

        if (user == null || !user.getRoles().contains(roleRepository.findByName("ROLE_ADMIN"))) {
            response.put("status", "No admin with this id");
            return ResponseEntity.ok(response);
        }

        userRepository.deleteById(id);
        response.put("status", "OK");
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "all_users")
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDto::fromUser).collect(Collectors.toList());
    }
}
