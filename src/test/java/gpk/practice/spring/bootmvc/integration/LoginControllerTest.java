package gpk.practice.spring.bootmvc.integration;

import gpk.practice.spring.bootmvc.App;
import gpk.practice.spring.bootmvc.configuration.AppConfig;
import gpk.practice.spring.bootmvc.configuration.DBTestProfileConfig;
import gpk.practice.spring.bootmvc.configuration.SecurityConfig;
import gpk.practice.spring.bootmvc.model.User;
import gpk.practice.spring.bootmvc.service.UserService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        App.class,
        AppConfig.class,
        SecurityConfig.class,
        DBTestProfileConfig.class})
@ActiveProfiles("test")
public class LoginControllerTest {
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private UserService userService;

    private MockMvc mockMvc;

    final String USERNAME = "George";
    final String PASSWORD= "password";
    final String EMAIL = "george@email.com";

    @Before
    public void init() {
        RestAssuredMockMvc.webAppContextSetup(context);

        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @After
    public void clean() {
        userService.deleteAll();
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
    public void givenContext_whenGetIndexNotAuthenticated_thenReturnsCode302 () {
         given()
                .when().get("/")
                .then().statusCode(302);
    }

    @Test
    @WithMockUser(username=USERNAME, password=PASSWORD, roles="USER")
    public void registrationAndLoggingInTest() throws Exception{

        userService.registerNewUserAccount(new User(EMAIL, USERNAME, PASSWORD));

        /*
        // TODO : instead of user creation via UserService
        mockMvc.perform(post("/register")
                        .param("name", USERNAME)
                        .param("email", EMAIL)
                        .param("password", PASSWORD))
                        // .andExpect(MockMvcResultMatchers.redirectedUrl("/index"));
                        .andExpect(status().isOk());
                        */

        given()
                .param("username", USERNAME)
                .param("password", PASSWORD)
                .when().post("/login")
                .then().statusCode(302);

        given()
                .when().get("/")
                .then().statusCode(200);  //  logged in : 200 instead of 302
    }

}
