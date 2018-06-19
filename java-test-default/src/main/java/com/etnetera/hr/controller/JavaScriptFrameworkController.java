package com.etnetera.hr.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.etnetera.hr.rest.Errors;
import com.etnetera.hr.rest.ValidationError;
import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;

/**
 * Simple REST controller for accessing application logic.
 * 
 * @author Etnetera
 *
 */
@RestController
public class JavaScriptFrameworkController extends EtnRestController {

	private final JavaScriptFrameworkRepository repository;

	@Autowired
	public JavaScriptFrameworkController(JavaScriptFrameworkRepository repository) {
		this.repository = repository;
	}

	@GetMapping("/frameworks")
	public Iterable<JavaScriptFramework> frameworks() {
		return repository.findAll();
	}

	@PostMapping("/add")
	public Errors add( @RequestBody JavaScriptFramework framework ) {
		System.out.println("JavaScriptFramework.add()" + framework.getName());
		Errors er = new Errors();
		if ( framework.getName() == null ) {
			List<ValidationError> list = new ArrayList<>();
			list.add(new ValidationError("name", "NotEmpty"));
			er.setErrors(list);
		}
		return er;
	}
}
