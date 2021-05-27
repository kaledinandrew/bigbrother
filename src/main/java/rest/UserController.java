package rest;

import dto.AttrDto;
import dto.UserDto;
import models.User;
import models.scripts.BaseScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import repositories.AttrRepository;
import repositories.BaseScriptRepository;
import repositories.RoleRepository;
import repositories.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/user/")
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AttrRepository attrRepository;
    private final BaseScriptRepository scriptRepository;

    @Autowired
    public UserController(UserRepository userRepository,
                           RoleRepository roleRepository,
                           AttrRepository attrRepository,
                           BaseScriptRepository scriptRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.attrRepository = attrRepository;
        this.scriptRepository = scriptRepository;
    }

    @GetMapping(value = "hello")
    public String hello() {
        return "hello, user";
    }

    @RequestMapping(value = "info")
    public ResponseEntity<?> getUserInfo(Authentication auth) {
        User user = userRepository.findByUsername(auth.getName());
        Map<String, String> response = new HashMap<>();
        if (user == null) {
            response.put("status", "Cannot find user");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(UserDto.fromUser(user));
    }

    @RequestMapping(value = "scripts")
    public ResponseEntity<?> getScripts(Authentication auth) {
        User user = userRepository.findByUsername(auth.getName());
        Map<String, String> response = new HashMap<>();
        if (user == null) {
            response.put("status", "Cannot find user");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(user.getScripts().stream().map(BaseScript::toChild).collect(Collectors.toList()));
    }

    @RequestMapping(value = "attrs")
    public ResponseEntity<?> getAttrs(Authentication auth) {
        User user = userRepository.findByUsername(auth.getName());
        Map<String, String> response = new HashMap<>();
        if (user == null) {
            response.put("status", "Cannot find user");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(user.getAttrs().stream().map(AttrDto::fromAttr).collect(Collectors.toList()));
    }
}
