package br.mariath.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class UserJson {

	@Test
	public void validarPrimeiroNivel() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users/1")
		.then()
			.statusCode(200)
			.body("id", is(1))
			.body("name", containsString("Silva"))
			.body("age", greaterThan(18))
		;

	}
	
	@Test
	public void validarPrimeiroNivelOuttrasFormas() {
		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/users/1");
		
		//path		
		assertEquals(new Integer(1), response.path("id"));
		assertEquals(new Integer(1), response.path("%s", "id"));
		
		//jsonPath
		JsonPath jpath = new JsonPath(response.asString());
		assertEquals(1, jpath.getInt("id"));
		
		//from
		int id = JsonPath.from(response.asString()).getInt("id");
		assertEquals(1, id);
	}
	
	@Test
	public void validarSegundoNivel() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users/2")
		.then()
			.statusCode(200)
			.body("id", is(2))
			.body("name", containsString("Joaquina"))
			.body("endereco.rua", is("Rua dos bobos"))
			.body("endereco.numero", is(0))
		;
	}
	
	
	@Test
	public void validarSegundoNivelList() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users/3")
		.then()
			.statusCode(200)
			.body("id", is(3))
			.body("name", containsString("Ana"))
			.body("filhos", hasSize(2))
			.body("filhos.name", hasItem("Zezinho"))
			.body("filhos.name", hasItems("Zezinho", "Luizinho"))
			.body("filhos[0].name", is("Zezinho"))
			.body("filhos[1].name", is("Luizinho"))
			.body("filhos.name", contains("Zezinho", "Luizinho"))
			;
	}
	
	@Test
	public void validarError() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users/4")
		.then()
			.statusCode(404)
			.body("error", is("Usuário inexistente"))
		;
	}
	@Test
	public void deveVerificarListaRaiz() {
		given()
		.when()
		.get("http://restapi.wcaquino.me/users")
		.then()
		.statusCode(200)
		.body("$", hasSize(3))
		.body("name", hasItems("João da Silva", "Maria Joaquina", "Ana Júlia"))
		.body("age[1]", is(25))
		.body("filhos.name", hasItem(Arrays.asList("Zezinho", "Luizinho")))
		.body("salary", contains(1234.5678f, 2500, null))
		
		;
	}
	
	@Test
	public void verificarAvancado() {
		given()
		.when()
		.get("http://restapi.wcaquino.me/users")
		.then()
		.statusCode(200)
		.body("age.findAll{it <= 25}.size()", is(2))
		.body("age.findAll{it <=25 && it > 20}.size()", is(1))
		.body("findAll{it.age <=25 && it.age > 20}.name", hasItem("Maria Joaquina"))
		.body("findAll{it.age <=25}[0].name", is("Maria Joaquina")) //busca o primeiro da lista
		.body("findAll{it.age <=25}[-1].name", is("Ana Júlia")) // bulca o ultimo da lista
		.body("find{it.age <=25}.name", is("Maria Joaquina"))
		.body("findAll{it.name.contains('n')}.name", hasItems("Maria Joaquina", "Ana Júlia"))
		.body("findAll{it.name.length() > 10}.name", hasItems("João da Silva", "Maria Joaquina"))
		.body("name.collect{it.toUpperCase()}", hasItem("MARIA JOAQUINA"))
		;
		
	}
	
	@Test
	public void unirJsonPathComJAVA() {
		ArrayList<String> names = 
		given()
		.when()
		.get("http://restapi.wcaquino.me/users")
		.then()
		.statusCode(200)
		.extract().path("name.findAll{it.startsWith('Maria')}")
		;
		System.out.println(names);
		assertEquals(1, names.size());
		assertTrue(names.get(0).equalsIgnoreCase("MaRIA JOAquina"));
		assertEquals(names.get(0).toUpperCase(), "maria joaquina".toUpperCase());
		
	}
}

