package org.initialtests;

import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.is;

public class Exercicio01 {

    @BeforeClass
    public static void preCondition() {
        baseURI = "http://localhost";
        port = 3000;
    }

    @Test
    public void exercicio01() {

        Faker faker = new Faker();
        String userName = faker.name().firstName();
        String userEmail = userName.concat("@eta.cesar.school");

        //GIVEN - WHEN - THEN

        //A - Listar usu�rios
        when()
                .get("/usuarios")
                .then()
                .statusCode(HttpStatus.SC_OK);

        //B - Cadastrar usu�rio
        String userId = given()
                .body("{\n" +
                        "  \"nome\": \"" + userName + "\",\n" +
                        "  \"email\": \"" + userEmail +"\",\n" +
                        "  \"password\": \"teste\",\n" +
                        "  \"administrador\": \"true\"\n" +
                        "}")
                .contentType(ContentType.JSON)
                .when()
                .post("/usuarios")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("message", is("Cadastro realizado com sucesso"))
                .extract().path("_id");
        //B1 - Checar email duplicado
        given()
                .body("{\n" +
                        "  \"nome\": \"" + userName + "\",\n" +
                        "  \"email\": \"" + userEmail +"\",\n" +
                        "  \"password\": \"teste\",\n" +
                        "  \"administrador\": \"true\"\n" +
                        "}")
                .contentType(ContentType.JSON)
                .when()
                .post("/usuarios")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", is("Este email j� est� sendo usado"));

        //C - Listar detalhes do usu�rio
        given()
                .pathParam("_id", userId)
                .when()
                .get("/usuarios/{_id}")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("nome", is(userName))
                .body("email", is(userEmail))
                .body("password", is("teste"))
                .body("administrador", is("true"))
                .body("_id", is(userId));

        //D - Excluir usu�rio
        given()
                .pathParam("_id", userId)
                .when()
                .delete("/usuarios/{_id}")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", is("Registro exclu�do com sucesso"));
    }
}