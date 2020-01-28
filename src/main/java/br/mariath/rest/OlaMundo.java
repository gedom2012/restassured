package br.mariath.rest;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;


public class OlaMundo {

	@Test
	public void testResponse() {
		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");
		assertEquals("Ola Mundo!", response.getBody().asString());
		assertEquals(200, response.statusCode());
	}

	@Test // metodo mais enxuto possivel
	public void testResponseMethodeasy() {
		get("http://restapi.wcaquino.me/ola").then().statusCode(200);
	}

	@Test // método fluente para efetuar os requests utilizando os padrões do cucumber
	public void testResponseMethodFluent() {
		given() // pré condição
				.when() // ação
				.get("http://restapi.wcaquino.me/ola").then() // assertivas
				.statusCode(201);
	}

	@Test
	public void recordMatchersamcrest() {
		assertThat("Maria", Matchers.is("Maria"));
		assertThat(128, Matchers.is(128));
		assertThat(128, Matchers.isA(Integer.class));
		assertThat(128d, Matchers.isA(double.class));
		assertThat(128, Matchers.greaterThan(120));
		assertThat(128, Matchers.lessThan(140));

		List<Integer> impares = Arrays.asList(1, 3, 5, 7, 9);
		assertThat(impares, hasSize(5));
		assertThat(impares, contains(1, 3, 5, 7, 9));
		assertThat(impares, containsInAnyOrder(1, 3, 5, 9, 7));
		assertThat(impares, hasItem(1));
		assertThat(impares, hasItems(1, 9));
		assertThat("Maria", is(not("João")));
		assertThat("Maria", not("João"));
		assertThat("Joaquina", anyOf(is("Joao"), is("Joaquina")));
		assertThat("Joaquina", allOf(startsWith("Jo"), endsWith("ina"), containsString("aqu")));

	}
	
	@Test
	public void validarBody() {
		given()
		.when()
		.get("http://restapi.wcaquino.me/ola")
		.then()
		.statusCode(200)
		.body(is("Ola Mundo!"))
		.body(containsString("Mundo"))
		.body(is(not(nullValue())));
	}

}
