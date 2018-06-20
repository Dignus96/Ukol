package com.etnetera.hr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.etnetera.hr.rest.Errors;
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
	public ResponseEntity<Errors> add( @RequestBody JavaScriptFramework framework, BindingResult bindingResult ) {
		try {
			repository.add(framework, bindingResult);
		}
		catch (MethodArgumentNotValidException ex) {
			return this.handleValidationException(ex);
		}
		return ResponseEntity.ok().body(null);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable("id") Long id) {
		try {
			repository.deleteById(id);
		}
		catch (Exception  ex) {
			if(ex instanceof IllegalArgumentException)
				return ResponseEntity.badRequest().body("IllegalArgumentException");
			return ResponseEntity.badRequest().body("Exception");
		}
		return ResponseEntity.ok().body("Deleted");
	}
}
