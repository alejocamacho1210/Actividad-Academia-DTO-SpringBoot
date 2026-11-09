package com.academy;

import com.academy.persistence.entity.StudentStatus;
import com.academy.presentation.dto.StudentRequest;
import com.academy.presentation.dto.StudentStatusRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class StudentControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void createStudentReturns201AndLocation() throws Exception {
        StudentRequest request = new StudentRequest("Ana Torres", 22, "ana@mail.com", StudentStatus.ACTIVE);

        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/v1/students/")))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Ana Torres"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void duplicateEmailReturnsConflict() throws Exception {
        StudentRequest first = new StudentRequest("Luis Gomez", 24, "luis@mail.com", StudentStatus.ACTIVE);
        StudentRequest second = new StudentRequest("Maria Gomez", 25, "luis@mail.com", StudentStatus.INACTIVE);

        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(first)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(second)))
                .andExpect(status().isConflict());
    }

    @Test
    void patchStatusUpdatesOnlyStatus() throws Exception {
        StudentRequest request = new StudentRequest("Pedro Ruiz", 30, "pedro@mail.com", StudentStatus.ACTIVE);
        StudentStatusRequest statusRequest = new StudentStatusRequest(StudentStatus.INACTIVE);

        MvcResult created = mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String location = created.getResponse().getHeader("Location");
        String studentId = location.substring(location.lastIndexOf('/') + 1);

        mockMvc.perform(patch("/api/v1/students/" + studentId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("INACTIVE"));
    }
}
