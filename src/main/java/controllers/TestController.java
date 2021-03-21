package controllers;

import entities.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import repositories.EventRepository;
import repositories.PersonRepository;

import java.util.List;

@RestController
public class TestController {
    @Autowired
    private final PersonRepository personRepository;
    @Autowired
    private final EventRepository eventRepository;

    public TestController(PersonRepository personRepository, EventRepository eventRepository) {
        this.personRepository = personRepository;
        this.eventRepository = eventRepository;
    }

    @RequestMapping("/people")
    public List<Person> getAll() {
        return personRepository.findAll();
    }

    @RequestMapping("/hello")
    public String helloPage() {
        return "Hello!";
    }
}
