package rest;

import com.fasterxml.jackson.databind.ser.Serializers;
import dto.AttrDto;
import dto.UserCreateDto;
import dto.UserDto;
import dto.scripts.CountContactScriptDto;
import dto.scripts.TimeIntervalContactScriptDto;
import models.Attr;
import models.User;
import models.scripts.BaseScript;
import models.scripts.CountContactScript;
import models.scripts.TimeIntervalContactScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repositories.AttrRepository;
import repositories.BaseScriptRepository;
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
    private final BaseScriptRepository scriptRepository;

    @Autowired
    public AdminController(UserRepository userRepository,
                           RoleRepository roleRepository,
                           AttrRepository attrRepository,
                           BaseScriptRepository scriptRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.attrRepository = attrRepository;
        this.scriptRepository = scriptRepository;
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

    // SCRIPTS

    @RequestMapping(value = "all_scripts")
    public ResponseEntity<List<?>> getAllScripts() {
        return new ResponseEntity<>(
                scriptRepository.findAll().stream().map(BaseScript::toChild).collect(Collectors.toList()),
                HttpStatus.OK);
    }

    // TODO add scripts to users in these functions
    // TODO add new script to this admin

    @RequestMapping(value = "add_count_script")
    public ResponseEntity<?> addCountScript(@RequestBody CountContactScriptDto countDto) {
        Map<String, String> response = new HashMap<>();

        if (userRepository.findById(countDto.getId1()).isEmpty() ||
            userRepository.findById(countDto.getId2()).isEmpty()) {
            response.put("status", "invalid users id");
            return ResponseEntity.ok(response);
        }

        scriptRepository.save(new CountContactScript(countDto));

        response.put("status", "OK");
        response.put("script_name", countDto.getScriptName());
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "edit_count_script")
    public ResponseEntity<?> editCountScript(@RequestParam(name = "id") Long id,
                                      @RequestBody CountContactScriptDto countDto) {
        BaseScript script = scriptRepository.findById(id).orElse(null);
        Map<String, String> response = new HashMap<>();

        if (script == null) {
            response.put("status", "no script with id: " + id);
            return ResponseEntity.ok(response);
        }
        if (!(script instanceof CountContactScript)) {
            response.put("status", "cannot convert script to CountContactScript by id:" + id);
            return ResponseEntity.ok(response);
        }

        script.setScriptName(countDto.getScriptName());
        script.setId1(countDto.getId1());
        script.setId2(countDto.getId2());
        ((CountContactScript) script).setCount(0L);
        scriptRepository.save(script);

        response.put("status", "OK");
        response.put("script_name", script.getScriptName());
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "add_interval_script")
    public ResponseEntity<?> addIntervalScript(@RequestBody TimeIntervalContactScriptDto scriptDto) {
        Map<String, String> response = new HashMap<>();

        if (userRepository.findById(scriptDto.getId1()).isEmpty() ||
                userRepository.findById(scriptDto.getId2()).isEmpty()) {
            response.put("status", "invalid users id");
            return ResponseEntity.ok(response);
        }
        if (scriptDto.getFrom() <= 0 || scriptDto.getTo() <= 0) {
            response.put("status", "invalid from or to field");
            return ResponseEntity.ok(response);
        }

        scriptRepository.save(new TimeIntervalContactScript(scriptDto));

        response.put("status", "OK");
        response.put("script_name", scriptDto.getScriptName());
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "edit_interval_script")
    public ResponseEntity<?> editIntervalScript(@RequestParam(name = "id") Long id,
                                             @RequestBody TimeIntervalContactScriptDto scriptDto) {
        BaseScript script = scriptRepository.findById(id).orElse(null);
        Map<String, String> response = new HashMap<>();

        if (script == null) {
            response.put("status", "no script with id: " + id);
            return ResponseEntity.ok(response);
        }
        if (!(script instanceof TimeIntervalContactScript)) {
            response.put("status", "cannot convert script to TimeIntervalContactScript by id:" + id);
            return ResponseEntity.ok(response);
        }

        script.setScriptName(scriptDto.getScriptName());
        script.setId1(scriptDto.getId1());
        script.setId2(scriptDto.getId2());
        ((TimeIntervalContactScript) script).setFrom(scriptDto.getFrom());
        ((TimeIntervalContactScript) script).setTo(scriptDto.getTo());
        ((TimeIntervalContactScript) script).setSuccess(scriptDto.getSuccess());
        scriptRepository.save(script);

        response.put("status", "OK");
        response.put("script_name", script.getScriptName());
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "delete_script")
    public ResponseEntity<?> deleteScriptById(@RequestParam(name = "id") Long id) {
        BaseScript script = scriptRepository.findById(id).orElse(null);
        Map<String, String> response = new HashMap<>();

        if (script == null) {
            response.put("status", "No script with this id");
            return ResponseEntity.ok(response);
        }

        User    u1 = userRepository.findById(script.getId1()).orElse(null),
                u2 = userRepository.findById(script.getId2()).orElse(null);
        if (u1 == null || u2 == null) {
            response.put("status", "invalid users id");
            return ResponseEntity.ok(response);
        }

        u1.removeScript(script);
        u2.removeScript(script);

        scriptRepository.deleteById(id);
        response.put("status", "OK");
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "add_script_to_user")
    public ResponseEntity<?> addScriptToUser(@RequestParam(name = "user_id") Long user_id,
                                           @RequestParam(name = "script_id") Long script_id) {
        Map<String, String> response = new HashMap<>();
        if (userRepository.findById(user_id).isEmpty() || attrRepository.findById(script_id).isEmpty()) {
            response.put("status", "No user or script with such id");
            return ResponseEntity.ok(response);
        }

        User user = userRepository.findById(user_id).get();
        BaseScript script = scriptRepository.findById(script_id).get();

        user.addScript(script);
        userRepository.save(user);

        response.put("status", "OK");
        return ResponseEntity.ok(response);
    }
}