package org.osiam.addons.administration.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * This controller contains all handler for the start page ( the root path / ).
 */
@Controller
@RequestMapping
public class StartController {

	@RequestMapping
	public String handleStart(){
		return "home";
	}
}
