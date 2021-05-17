package rest;

import dto.AuthenticationRequestDto;
import dto.UserDto;
import models.Attr;
import models.Role;
import models.Status;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import repositories.AttrRepository;
import repositories.EventRepository;
import repositories.RoleRepository;
import repositories.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/all")
public class TestController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EventRepository eventRepository;
    private final AttrRepository attrRepository;

    @Autowired
    public TestController(UserRepository userRepository,
                          RoleRepository roleRepository,
                          EventRepository eventRepository,
                          AttrRepository attrRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.eventRepository = eventRepository;
        this.attrRepository = attrRepository;
    }

    @RequestMapping("/hello")
    public String helloPage() {
        return "hello, someone";
    }

    @PostMapping("/post_user")
    public User postUser(@RequestBody AuthenticationRequestDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setFirstName("test");
        user.setLastName("test");
        user.setCreated(new Date());
        user.setUpdated(new Date());
        user.setStatus(Status.ACTIVE);
        user.getRoles().add(roleRepository.findByName("ROLE_ADMIN"));
        userRepository.save(user);
        return user;
    }

    @GetMapping("/users")
    public List<UserDto> getUsers() {
        return userRepository.findAll().stream()
                .map(UserDto::fromUser).collect(Collectors.toList());
    }

    @PutMapping("/put_user")
    public AuthenticationRequestDto putUser(@RequestBody AuthenticationRequestDto userDto) {
        User user = userRepository.findByUsername(userDto.getUsername());
        user.getRoles().add(roleRepository.findByName("ROLE_ADMIN"));
        userRepository.save(user);
        return userDto;
    }

    @PostMapping("/create_super_admin")
    public String createSuperAdmin() {
        User user = userRepository.findByUsername("super_admin");
        if (!user.getRoles().contains(roleRepository.findByName("ROLE_SUPER_ADMIN"))) {
            user.getRoles().add(roleRepository.findByName("ROLE_SUPER_ADMIN"));
        }
        userRepository.save(user);
        return "OK";
    }

    @PostMapping("/clean_up_user_roles")
    public String cleanUpUserRoles() {
        for (User user : userRepository.findAll()) {
            user.setRoles(new HashSet<>(user.getRoles()));
            userRepository.save(user);
        }
        return "OK";
    }

    @RequestMapping("/add_attr")
    public String addAttr() {
        Attr new_attr = new Attr("test_attr");
        attrRepository.save(new_attr);
        return attrRepository.findAll().toString();
    }

    @RequestMapping("/add_attr_to_user1")
    public String addAttrToUser() {
        String attr_name = "for_user1";
        Attr new_attr = attrRepository.findByName(attr_name);
        if (new_attr == null) {
            new_attr = new Attr(attr_name);
            attrRepository.save(new_attr);
        }

        User user1 = userRepository.findById(1L).get();
        user1.addAttr(new_attr);
        userRepository.save(user1);
        return user1.getAttrs().toString();
    }
}
