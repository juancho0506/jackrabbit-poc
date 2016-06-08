package zemoga.poc.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SecurityPOCController {
	
	@RequestMapping("/welcome")
	public ModelAndView helloWorld() {
 
		String message = "<br><div style='text-align:center;'>"
				+ "<h3>********** Hello World !! **********</div><br><br>";
		return new ModelAndView("welcome", "message", message);
	}
	
}
