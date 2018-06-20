package com.etnetera.hr.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.etnetera.hr.data.JavaScriptFramework;

import java.util.Iterator;

/**
 * Spring data repository interface used for accessing the data in database.
 * 
 * @author Etnetera
 *
 */
public interface JavaScriptFrameworkRepository extends CrudRepository<JavaScriptFramework, Long> {

	default public void add( JavaScriptFramework framework, BindingResult result ) throws MethodArgumentNotValidException {
		if ( framework.getName() == null ) {
			result.rejectValue("name", "NotEmpty");
			throw new MethodArgumentNotValidException(null, result);
		}
		if ( framework.getName().length() > 30 ) {
			result.rejectValue("name", "Size");
			throw new MethodArgumentNotValidException(null, result);
		}
		this.save(framework);
	}

	default public JavaScriptFramework findByName( String name ) {
		for (Iterator<JavaScriptFramework> iterator = this.findAll().iterator(); iterator.hasNext();) {
			JavaScriptFramework framework = iterator.next();
			if ( framework.getName().equals(name) )
				return framework;
		}
		return null;
	}
}
