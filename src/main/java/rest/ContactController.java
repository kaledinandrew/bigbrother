package rest;

import dto.ContactDto;
import models.User;
import models.scripts.BaseScript;
import models.scripts.CountContactScript;
import models.scripts.TimeIntervalContactScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import repositories.AttrRepository;
import repositories.BaseScriptRepository;
import repositories.RoleRepository;
import repositories.UserRepository;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/contact/")
public class ContactController {

    private final UserRepository userRepository;
    private final BaseScriptRepository scriptRepository;

    @Autowired
    public ContactController(UserRepository userRepository, BaseScriptRepository scriptRepository) {
        this.userRepository = userRepository;
        this.scriptRepository = scriptRepository;
    }

    @RequestMapping(value = "send_contact")
    public ResponseEntity<?> sendContact(@RequestBody ContactDto contactDto) {
        User u1 = userRepository.findById(contactDto.getId1()).orElse(null),
                u2 = userRepository.findById(contactDto.getId2()).orElse(null);
        Long time = contactDto.getTime();
        Map<String, String> response = new HashMap<>();

        if (u1 == null || u2 == null) {
            response.put("status", "no users with such id");
            return ResponseEntity.ok(response);
        }
        if (time < 0) {
            response.put("status", "invalid time value");
            return ResponseEntity.ok(response);
        }

        if (u1.getScripts().size() < u2.getScripts().size()) {
            processContact(u1, u2, time);
        } else {
            processContact(u2, u1, time);
        }

        response.put("status", "OK");
        return ResponseEntity.ok(response);
    }

    private void processContact(User u1, User u2, Long time) {
        for (BaseScript script : u1.getScripts()) {
            if (!(script.getId1().equals(u1.getId()) && script.getId2().equals(u2.getId())) &&
                    !(script.getId1().equals(u2.getId()) && script.getId2().equals(u1.getId()))) {
                continue;
            }
            if (script instanceof CountContactScript) {
                ((CountContactScript) script).incrementCount();
            } else if (script instanceof TimeIntervalContactScript) {
                Long from = ((TimeIntervalContactScript) script).getFrom(),
                        to = ((TimeIntervalContactScript) script).getTo();
                if (from <= time && time <= to) {
                    ((TimeIntervalContactScript) script).setSuccess(true);
                }
            }
            scriptRepository.save(script);
        }
    }
}
