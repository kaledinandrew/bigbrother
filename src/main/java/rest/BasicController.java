package rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
public class BasicController {
    @RequestMapping(value = "")
    public String hello() {
        return "For list of all possible requests see: https://github.com/kaledinandrew/bigbrother";
    }
}
