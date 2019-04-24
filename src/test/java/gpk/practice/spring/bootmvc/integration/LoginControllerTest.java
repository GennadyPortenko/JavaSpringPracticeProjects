package gpk.practice.spring.bootmvc.integration;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@RunWith(SpringRunner.class)
public class LoginControllerTest {
    @Autowired
    private WebApplicationContext context;

    @Before
    public void init() {
        RestAssuredMockMvc.webAppContextSetup(context);
    }

    @Test
    public void givenContext_whenGetRegistration_thenReturnsCode200 () {
        given()
                .when()
                .get("/registration")
                .then().statusCode(200);
    }

    @Test
    public void givenContext_whenGetLogin_thenReturnsCode200 () {
        given()
                .when()
                .get("/login")
                .then().statusCode(200);
    }

    @Test
    public void givenContext_whenPostRegister_thenReturnsCode200 () {
        given()
                .when()
                .post("/register")
                .then().statusCode(200);
    }

    @Test
    public void givenContext_whenGetIndexNotAuthenticated_thenReturnsCode302 () {
         given()
                .when()
                .get("/")
                .then().statusCode(302);
    }
}
