package rest;

import dto.AttrDto;
import dto.UserDto;
import models.Attr;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repositories.AttrRepository;
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
    private final AttrRepository attrRepository;

    @Autowired
    public AdminController(UserRepository userRepository,
                           AttrRepository attrRepository) {
        this.userRepository = userRepository;
        this.attrRepository = attrRepository;
    }

    @GetMapping(value = "hello")
    public String hello() {
        return "hello, admin";
    }

    @GetMapping(value = "all_users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return new ResponseEntity<>(
                userRepository.findAll().stream().map(UserDto::fromUser).collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @GetMapping(value = "all_attrs")
    public ResponseEntity<List<AttrDto>> getAllAttrs() {
        return new ResponseEntity<>(
                attrRepository.findAll().stream().map(AttrDto::fromAttr).collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @GetMapping(value = "user")
    public ResponseEntity<UserDto> getUserById(@RequestParam(name = "id") Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        UserDto result = UserDto.fromUser(user);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "add_attr")
    public ResponseEntity<?> addAttr(@RequestBody AttrDto attrDto) {
        String name = attrDto.getName();
        Map<String, String> response = new HashMap<>();

        if (attrRepository.findByName(name) != null) {
            response.put("status", "Attr with this username already exists");
            return ResponseEntity.ok(response);
        }
        attrRepository.save(new Attr(name));

        response.put("status", "OK");
        response.put("attr_name", name);
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
