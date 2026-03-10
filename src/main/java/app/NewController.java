package app;

@RestController
public class NewController {

    @GetMapping("/newController")
    public String newController() {
        return "This is a response from the New Controller!";
    }
}
