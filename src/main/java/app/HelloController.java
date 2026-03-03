package app;

@RestController
public class HelloController {

	@GetMapping("/")
	public static String index() {
		return "Greetings from Spring Boot!";
	}

	@GetMapping("/pi")
	public static String wmPi() {
		return "PI=" +Math.PI;
	}

	@GetMapping("/hello")
	public static String wmHello() {
		return "Hello WOrld";
	}

	
}