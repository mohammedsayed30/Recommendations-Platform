package recommendations.user;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserLoginTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    @DisplayName("CT_01 : test Login API with correct credentials that should success ")
    public void registerThenLogin_shouldSucceed() throws Exception
    {


        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                       {
                        "fullName": "aliklay",
                        "email": "test@example.com",
                        "password": "123456",
                        "jobTitle": "Software engineer",
                        "linkedinProfile": "https://linkedin.com/in/test",
                        "yearsOfExperience": 5
                        }
                        """))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "email": "test@example.com",
                        "password": "123456"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());

    }

    @Test
    @DisplayName("CT_02 : test Login API with wrong credentials(email not found) that should fail ")
    public void registerThenLogin_shouldFailWithWrongEmail() throws Exception
    {


        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                       {
                        "fullName": "aliklay",
                        "email": "test@example.com",
                        "password": "123456",
                        "jobTitle": "Software engineer",
                        "linkedinProfile": "https://linkedin.com/in/test",
                        "yearsOfExperience": 5
                        }
                        """))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                        "email": "tes@example.com",
                        "password": "123456"
                        }
                        """))
                .andExpect(status().isForbidden());

    }

    @Test
    @DisplayName("CT_02 : test Login API with wrong credentials(wrong password) that should fail ")
    public void registerThenLogin_shouldFailWithWrongPass() throws Exception
    {


        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                       {
                        "fullName": "aliklay",
                        "email": "test@example.com",
                        "password": "123456",
                        "jobTitle": "Software engineer",
                        "linkedinProfile": "https://linkedin.com/in/test",
                        "yearsOfExperience": 5
                        }
                        """))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                        "email": "test@example.com",
                        "password": "12345"
                        }
                        """))
                .andExpect(status().isInternalServerError());

    }
}
