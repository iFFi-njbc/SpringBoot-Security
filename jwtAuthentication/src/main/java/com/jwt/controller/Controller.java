package com.jwt.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

	@RequestMapping("/welcome")
	public String welcome()
	{
		String text = "Page is not allowed to unauthenticated users";
		return text;
	}
}
