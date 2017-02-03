package com.myorg.document.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DocumentController {

	@RequestMapping("/home/{loanId}")
	public String index(@PathVariable("loanId") String loanId) {
		System.out.println("Loan Id is :"+loanId);
		return "/FileUpload.html";
	}
}
