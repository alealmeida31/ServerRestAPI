package org.serverest.controller;

import org.serverest.model.UsuarioDTO;
import io.restassured.http.ContentType;
import org.serverest.util.Endpoint;

import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.is;
import static io.restassured.RestAssured.given;
public class Usuario {
    public static void listar(Integer statuscode, String ambiente) {
        when()
                .get(ambiente.concat(Endpoint.usuarios))
                .then()
                .statusCode(statuscode);
    }
    public static String cadastrar(UsuarioDTO usuarioDTO, Integer statusCode, String mensagem, String ambiente) {
        String usuarioId = given()
                .body("{\n" +
                        "  \"nome\": \"" + usuarioDTO.getNome() + "\",\n" +
                        "  \"email\": \"" + usuarioDTO.getEmail() + "\",\n" +
                        "  \"password\": \"" + usuarioDTO.getPassword() + "\",\n" +
                        "  \"administrador\": \"" + usuarioDTO.getAdministrador() + "\"\n" +
                        "}")
                .contentType(ContentType.JSON)
                .when()
                .post(ambiente.concat(Endpoint.usuarios))
                .then()
                .statusCode(statusCode)
                .body("message", is(mensagem))
                .extract().path("_id");
        return usuarioId;
    }

    public static String  logar(UsuarioDTO usuarioDTO, Integer statusCode, String mensagem, String ambiente){
        String authorization = given()
                .body("{\n" +
                        "  \"email\": \"" + usuarioDTO.getEmail() + "\",\n" +
                        "  \"password\": \"" + usuarioDTO.getPassword() + "\"\n" +
                        "}")
                .contentType(ContentType.JSON)
                .when()
                .post(ambiente.concat(Endpoint.login))
                .then()
                .statusCode(statusCode)
                .body("message", is(mensagem))
                .extract().path("authorization");


        return authorization;
    }

    public static void buscarPorId(String id, UsuarioDTO usuarioDTO, Integer statusCode, String ambiente) {
        given()
                .pathParam("_id", id)
                .when()
                .get(ambiente.concat(Endpoint.usuariosId))
                .then()
                .statusCode(statusCode)
                .body("nome", is(usuarioDTO.getNome()))
                .body("email", is(usuarioDTO.getEmail()))
                .body("password", is(usuarioDTO.getPassword()))
                .body("administrador", is(usuarioDTO.getAdministrador()))
                .body("_id", is(usuarioDTO.getId()));
    }

    public static void buscarPorIdExluido(String id, Integer statusCode, String mensagem, String ambiente) {
        given()
                .pathParam("_id", id)
                .when()
                .get(ambiente.concat(Endpoint.usuariosId))
                .then()
                .statusCode(statusCode)
                .body("message", is(mensagem));
    }

    public static void excluir(String id, Integer statusCode, String mensagem, String ambiente) {
        given()
                .pathParam("_id", id)
                .when()
                .delete(ambiente.concat(Endpoint.usuariosId))
                .then()
                .statusCode(statusCode)
                .body("message", is(mensagem));
    }



    public static String editar(String id, UsuarioDTO usuarioDTO, Integer statusCode, String mensagem, String ambiente) {
        String usuarioId = given()
                .pathParam("_id", id)
                .body("{\n" +
                        "  \"nome\": \"" + usuarioDTO.getNome() + "\",\n" +
                        "  \"email\": \"" + usuarioDTO.getEmail() + "\",\n" +
                        "  \"password\": \"" + usuarioDTO.getPassword() + "\",\n" +
                        "  \"administrador\": \"" + usuarioDTO.getAdministrador() + "\"\n" +
                        "}")
                .contentType(ContentType.JSON)
                .when()
                .put(ambiente.concat(Endpoint.usuariosId))
                .then()
                .statusCode(statusCode)
                .body("message", is(mensagem))
                .extract().path("_id");
        return usuarioId;
    }

    public static String autenticar(UsuarioDTO usuarioDTO, Integer statusCode, String message, String ambiente) {

        return given()
                .body("{\n" +
                        "  \"email\": \"" + usuarioDTO.getEmail() + "\",\n" +
                        "  \"password\": \"" + usuarioDTO.getPassword() + "\"\n" +
                        "}")
                .contentType(ContentType.JSON)
                .when()
                .post(ambiente.concat(Endpoint.login))
                .then()
                .statusCode(statusCode)
                .body("message", is(message))
                .extract().path("authorization");
    }
}