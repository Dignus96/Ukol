package com.etnetera.hr;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.TreeSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Class used for Spring Boot/MVC based tests.
 * 
 * @author Etnetera
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
//@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class JavaScriptFrameworkTests {

	@Autowired
	private MockMvc mockMvc;
	
	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private JavaScriptFrameworkRepository repository;

	private void prepareData() throws Exception {
		repository.deleteAll();
		JavaScriptFramework react = new JavaScriptFramework("ReactJS");
		JavaScriptFramework vue = new JavaScriptFramework("Vue.js");

		repository.save(react);
		repository.save(vue);
		repository.save(new JavaScriptFramework("Meteor.js"));
	}

	@Test
	public void frameworksTest() throws Exception {
		prepareData();
		mockMvc.perform(get("/frameworks"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0].name", is("ReactJS")))
				.andExpect(jsonPath("$[1].name", is("Vue.js")))
				.andExpect(jsonPath("$[2].name", is("Meteor.js")));

		repository.deleteAll();
		mockMvc.perform(get("/frameworks"))
		.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$", hasSize(0)));
	}
	
	@Test
	public void addFrameworkInvalid() throws JsonProcessingException, Exception {
		
		JavaScriptFramework framework = new JavaScriptFramework();
		mockMvc.perform(post("/add").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors", hasSize(1)))
				.andExpect(jsonPath("$.errors[0].field", is("name")))
				.andExpect(jsonPath("$.errors[0].message", is("NotEmpty")));

		framework.setName("verylongnameofthejavascriptframeworkjavaisthebest");
		mockMvc.perform(post("/add").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors", hasSize(1)))
				.andExpect(jsonPath("$.errors[0].field", is("name")))
				.andExpect(jsonPath("$.errors[0].message", is("Size")));
	}
	
	@Test
	public void addFrameworkTest() throws JsonProcessingException, Exception {
		prepareData();
		JavaScriptFramework framework = new JavaScriptFramework("Blah");
		
		mockMvc.perform(post("/add").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
				.andExpect(status().isOk());
		
		mockMvc.perform(get("/frameworks"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", hasSize(4)))
				.andExpect(jsonPath("$[0].name", is("ReactJS")))
				.andExpect(jsonPath("$[1].name", is("Vue.js")))
				.andExpect(jsonPath("$[2].name", is("Meteor.js")))
				.andExpect(jsonPath("$[3].name", is("Blah")));
	}
	
	@Test
	public void deleteFrameworkByIDTest() throws Exception {
		prepareData();

		mockMvc.perform(delete("/{id}", 666))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$", is("Exception")));
		
		for (Iterator<JavaScriptFramework> iterator = repository.findAll().iterator(); iterator.hasNext();) {
			long id = iterator.next().getId();
			mockMvc.perform(delete("/{id}", id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", is("Deleted")));
			
			mockMvc.perform(delete("/{id}", id))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$", is("Exception")));
		}
	}

	@Test
	public void findFrameworkByNameTest() throws Exception {
		prepareData();
		assert ( repository.findByName("") == null );
		assert ( repository.findByName("NotExisting") == null );
		assert ( repository.findByName("ReactJS").getName().equals("ReactJS") );
	}

	@Test
	public void updateFrameworkTest() throws Exception {
		prepareData();
		JavaScriptFramework framework = repository.findByName("ReactJS");
		framework.setVersion(new TreeSet<String>(Arrays.asList("1.0", "1.1", "1.2")));
		framework.setDeprecationDate(new GregorianCalendar(2020, 1, 1).getTime());
		framework.setHypeLevel("Just great");
		framework.addVersion("2.0");
		mockMvc.perform(put("/update").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", is("Updated")));
		assert ( framework.getLastVersion().equals(repository.findByName("ReactJS").getLastVersion()) );
		assert ( framework.getDeprecationDate().equals(repository.findByName("ReactJS").getDeprecationDate()) );
		assert ( framework.getHypeLevel().equals(repository.findByName("ReactJS").getHypeLevel()) );
		framework.setName("Whatever");
		mockMvc.perform(put("/update").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$", is("Framework not found")));
	}
}
