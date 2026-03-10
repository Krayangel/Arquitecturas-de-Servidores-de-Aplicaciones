package app;

@RestController
public class HelloController {

	@GetMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}

	@GetMapping("/hello")
	public String hello() {
		return "Hello World from MicroSpringBoot!";
	}
}