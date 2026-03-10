package app;

@RestController
public class GreetingController {

    @GetMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hello " + name;
    }

    @GetMapping("/pi")
    public String pi() {
        return String.valueOf(Math.PI);
    }

    @GetMapping("/euler")
    public String euler() {
        return "e= " + Math.E;
    }
}
