package gpk.practice.spring.bootmvc.integration;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@RunWith(SpringRunner.class)
public class LoginControllerTest {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private boolean mockMvcInitialized = false;
    @Before
    public void init() {
        if (!mockMvcInitialized) {
            RestAssuredMockMvc.webAppContextSetup(context);
            mockMvcInitialized = true;

            // mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        }
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
    public void givenContext_whenPostRegister_thenReturnsCode200 () throws Exception {
        /*
        UserDto userDto = new UserDto("test_user", "example@mail.com", "password");
        given()
                .contentType("application/json")
                .spec()
                .when().post("/register", userDto)
                .then().statusCode(200);
                */
        // mockMvc.perform(get("/"))
                // .andExpect(status().isOk());
    }

    @Test
    public void givenContext_whenGetIndexNotAuthenticated_thenReturnsCode302 () {
         given()
                .when()
                .get("/")
                .then().statusCode(302);
    }
}
