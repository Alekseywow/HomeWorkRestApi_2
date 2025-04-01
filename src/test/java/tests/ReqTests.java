package tests;

import io.restassured.RestAssured;
import models.UserRequestModel;
import models.UserResponseModel;
import models.UserFullResponseModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.TestSpec.*;
import static specs.TestSpec.getBaseResponseSpec;


@Tag("simple")
public class ReqTests {

    @BeforeAll
    public static void setUp(){
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }
    @Test
    @DisplayName("Проверить успешное создание пользователя")
    void successfulCreateNewUserTest() {
        UserRequestModel request = new UserRequestModel();
        request.setName("Aleksey");
        request.setJob("Aqa");
        UserResponseModel response =
                step("Необходимо создать нового пользователя, указать имя и работу",
                        () -> given(requestSpec)
                                .body(request)
                                .contentType(JSON)
                                .when()
                                .post("/users")
                                .then()
                                .spec(getBaseResponseSpec(201))
                                .extract().as(UserResponseModel.class));

                step("В ответ вернулись указанные в запросе данные", () -> {
                    assertEquals(response.getName(), "Aleksey");
                    assertEquals(response.getJob(), "Aqa");
                    assertThat(response.getId()).isNotEmpty();
                    assertThat(response.getCreatedAt()).isNotEmpty();

                });
    }
    @Test
    @DisplayName("Проверить успешное получение информацию о пользователе")
    void successfulRetrievalUserInformTest() {
        UserFullResponseModel response =
                step("Получить пользователя по id",
                        () -> given(requestSpec)
                                .contentType(JSON)
                                .when()
                                .get("/users/2")
                                .then()
                                .spec(getBaseResponseSpec(200))
                                .extract().as(UserFullResponseModel.class));

                step("Проверить, что в ответе вернулись данные о пользователе", () -> {
                    assertEquals(response.getData().getId(), "2");
                    assertEquals(response.getData().getEmail(), "janet.weaver@reqres.in");
                    assertEquals(response.getData().getFirstName(), "Janet");
                    assertEquals(response.getData().getLastName(), "Weaver");
                    assertEquals(response.getData().getAvatar(), "https://reqres.in/img/faces/2-image.jpg");
                    assertEquals(response.getSupport().getUrl(), "https://contentcaddy.io?utm_source=reqres&utm_medium=json&utm_campaign=referral");
                    assertEquals(response.getSupport().getText(), "Tired of writing endless social media content? Let Content Caddy generate it for you.");
                });
    }
    @Test
    @DisplayName("Проверить обновление данных о пользователе")
    void successUpdateInfoUserTest() {
        UserRequestModel request = new UserRequestModel();
        request.setName("ALEKSEY");
        request.setJob("AQA");

        UserResponseModel response =
                step("Внести изменения данных пользователя",
                        () -> given(requestSpec)
                                .body(request)
                                .contentType(JSON)
                                .when()
                                .put("/users/95")
                                .then()
                                .spec(getBaseResponseSpec(200))
                                .extract().as(UserResponseModel.class));

                step("Проверить, что данные о пользователе обновились",
                        () -> {
                    assertEquals(response.getName(), "ALEKSEY");
                    assertEquals(response.getJob(), "AQA");
                    assertThat(response.getUpdatedAt().isEmpty());
                        });
    }
    @Test
    @DisplayName("Проверить, что пользователь успешно удален")
    void successDeleteUserTest() {
        step("Удалить пользователя по id",
                () -> given(requestSpec)
                        .contentType(JSON)
                        .when()
                        .delete("/users/2")
                        .then()
                        .spec(getBaseResponseSpec(204)));
    }
    @Test
    @DisplayName("Проверить, что для незарегистрированного пользователя приходит статус код 404")
    void unsuccessfulUserNotFoundTest() {
        step("Получить пользователя по несуществующему id",
                () -> given(requestSpec)
                        .contentType(JSON)
                        .when()
                        .get("/users/23")
                        .then()
                        .spec(getBaseResponseSpec(404)));
    }
}