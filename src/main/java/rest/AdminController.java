package rest;

import dto.AttrDto;
import dto.UserCreateDto;
import dto.UserDto;
import models.Attr;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repositories.AttrRepository;
import repositories.RoleRepository;
import repositories.UserRepository;
import service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/admin/")
public class AdminController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AttrRepository attrRepository;

    @Autowired
    public AdminController(UserRepository userRepository,
                           RoleRepository roleRepository,
                           AttrRepository attrRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.attrRepository = attrRepository;
    }

    // @GetMapping(value = "hello")
    @RequestMapping(value = "hello")
    public String hello() {
        return "hello, admin";
    }

    // USERS

    // @GetMapping(value = "all_users")
    @RequestMapping(value = "all_users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return new ResponseEntity<>(
                userRepository.findAll().stream().map(UserDto::fromUser).collect(Collectors.toList()),
                HttpStatus.OK);
    }

    // @GetMapping(value = "user")
    @RequestMapping(value = "user")
    public ResponseEntity<UserDto> getUserById(@RequestParam(name = "id") Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        UserDto result = UserDto.fromUser(user);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // @PostMapping(value = "add_user")
    @RequestMapping(value = "add_user")
    public ResponseEntity<?> addAdmin(@RequestBody UserCreateDto userCreateDto) {
        String username = userCreateDto.getUsername();
        Map<String, String> response = new HashMap<>();

        if (userRepository.findByUsername(username) != null) {
            response.put("status", "User with this username already exists");
            return ResponseEntity.ok(response);
        }

        userRepository.save(userCreateDto.toUser(
                roleRepository.findByName("ROLE_USER")));

        response.put("status", "OK");
        response.put("username", username);
        return ResponseEntity.ok(response);
    }

    // @PutMapping(value = "edit_user")
    @RequestMapping(value = "edit_user")
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

    // @DeleteMapping(value = "delete_user")
    @RequestMapping(value = "delete_user")
    public ResponseEntity<?> deleteUserById(@RequestParam(name = "id") Long id) {
        User user = userRepository.findById(id).orElse(null);
        Map<String, String> response = new HashMap<>();

        if (user == null || !user.getRoles().contains(roleRepository.findByName("ROLE_USER"))) {
            response.put("status", "No user with this id");
            return ResponseEntity.ok(response);
        }

        userRepository.deleteById(id);
        response.put("status", "OK");
        return ResponseEntity.ok(response);
    }

    // ATTRS

    @RequestMapping(value = "all_attrs")
    public ResponseEntity<List<AttrDto>> getAllAttrs() {
        return new ResponseEntity<>(
                attrRepository.findAll().stream().map(AttrDto::fromAttr).collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @RequestMapping(value = "add_attr")
    public ResponseEntity<?> addAttr(@RequestBody AttrDto attrDto) {
        String name = attrDto.getName();
        Map<String, String> response = new HashMap<>();

        if (attrRepository.findByName(name) != null) {
            response.put("status", "Attr with this name already exists");
            return ResponseEntity.ok(response);
        }
        attrRepository.save(new Attr(name));

        response.put("status", "OK");
        response.put("attr_name", name);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "edit_attr")
    public ResponseEntity<?> editAttr(@RequestParam(name = "id") Long id,
                                       @RequestBody AttrDto attrDto) {
        Attr attr = attrRepository.findById(id).orElse(null);
        Map<String, String> response = new HashMap<>();

        if (attr == null) {
            response.put("status", "no attr with id: " + id);
            return ResponseEntity.ok(response);
        }
        attr.setName(attrDto.getName());
        attrRepository.save(attr);

        response.put("status", "OK");
        response.put("attr_name", attrDto.getName());
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "delete_attr")
    public ResponseEntity<?> deleteAttrById(@RequestParam(name = "id") Long id) {
        Attr attr = attrRepository.findById(id).orElse(null);
        Map<String, String> response = new HashMap<>();

        if (attr == null) {
            response.put("status", "No attr with this id");
            return ResponseEntity.ok(response);
        }

        attrRepository.deleteById(id);
        response.put("status", "OK");
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "add_attr_to_user")
    public ResponseEntity<?> addAttrToUser(@RequestParam(name = "user_id") Long user_id,
                                           @RequestParam(name = "attr_id") Long attr_id) {
        Map<String, String> response = new HashMap<>();
        if (userRepository.findById(user_id).isEmpty() || attrRepository.findById(attr_id).isEmpty()) {
            response.put("status", "No user or attr with such id");
            return ResponseEntity.ok(response);
        }
        User user = userRepository.findById(user_id).get();
        Attr attr = attrRepository.findById(attr_id).get();

        user.addAttr(attr);
        userRepository.save(user);
        response.put("status", "OK");
        return ResponseEntity.ok(response);
    }
}
