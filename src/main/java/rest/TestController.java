package rest;

import dto.AuthenticationRequestDto;
import dto.UserDto;
import models.Status;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import repositories.EventRepository;
import repositories.RoleRepository;
import repositories.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/all")
public class TestController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EventRepository eventRepository;

    @Autowired
    public TestController(UserRepository userRepository,
                          RoleRepository roleRepository,
                          EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.eventRepository = eventRepository;
    }

    @RequestMapping("/hello")
    public String helloPage() {
        return "Hello!";
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
}
