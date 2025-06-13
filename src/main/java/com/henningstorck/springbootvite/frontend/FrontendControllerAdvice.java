package com.henningstorck.springbootvite.frontend;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class FrontendControllerAdvice {
	private final FrontendService frontendService;

	public FrontendControllerAdvice(FrontendService frontendService) {
		this.frontendService = frontendService;
	}

	@ModelAttribute("frontend")
	public Frontend getFrontend(Model model) {
		List<String> stylesheets = frontendService.getStylesheets();
		List<String> scripts = frontendService.getScripts();
		return new Frontend(stylesheets, scripts);
	}
}
