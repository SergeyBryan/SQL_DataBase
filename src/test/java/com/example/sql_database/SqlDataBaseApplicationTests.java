package com.example.sql_database;

import com.example.sql_database.entity.StudentDTO;
import com.example.sql_database.repository.StudentRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:test;NON_KEYWORDS=USER;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH"
        , "spring.jpa.hibernate.ddl-auto=update"
})
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SqlDataBaseApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeAll
    void prepareDataBase() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", "1");
        jsonObject.put("name", "test");
        jsonObject.put("color", "red");
        mockMvc.perform(post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk());


        JSONObject jsonStudent = new JSONObject();
        jsonStudent.put("name", "testStudent");
        jsonStudent.put("age", "20");
        jsonStudent.put("facultyId", 1);
        mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStudent.toString()))
                .andExpect(status().isOk());

    }

    @AfterAll
    void clearDataBase() throws Exception {
        int j = studentRepository.findAll().size();

        deleteAllStudentsFromDB(j, mockMvc);

        mockMvc.perform(delete("/faculty")
                        .param("id", "1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        mockMvc.perform(get("/faculty/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(1)
    void updateFacultyByIdAndCheckUpdatingIsWorking() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "newTest");
        jsonObject.put("color", "red");
        mockMvc.perform(put("/faculty")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/faculty/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("newTest"));
    }

    @Test
    @Order(2)
    void shouldFindStudentByAge() throws Exception {
        mockMvc.perform(get("/student/filterByAge")
                        .param("age", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].name").value("testStudent"));
    }

    @Test
    @Order(3)
    void findFacultyByColorAndCheck() throws Exception {
        mockMvc.perform(get("/faculty/colors")
                        .param("color", "red"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].color").value("red"));
    }

    @Test
    @Order(4)
    void findFacultyByIdAndCheck() throws Exception {
        mockMvc.perform(get("/faculty/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.color").value("red"));
    }

    @Test
    @Order(5)
    void shouldFindFacultyByColorIgnoreCase() throws Exception {
        mockMvc.perform(get("/faculty/colorIgnoreCase")
                        .param("color", "REd"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].color").value("red"));
    }

    @Test
    @Order(6)
    void shouldFindStudentsFromFacultyByFacultyId() throws Exception {
        mockMvc.perform(get("/faculty/studentsFromFaculty")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].length()").value(4))
                .andExpect(jsonPath("$[0].name").value("testStudent"));
    }

    @Test
    @Order(7)
    void shouldFindStudentById() throws Exception {
        mockMvc.perform(get("/student/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.name").value("testStudent"));
    }

    @Test
    @Order(8)
    void shouldFindAllStudents() throws Exception {
        mockMvc.perform(get("/student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @Order(9)
    void shouldFindStudentFacultyByStudentId() throws Exception {
        mockMvc.perform(get("/student/studentFaculty")
                        .param("id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(10)
    void shouldFindNumberOfStudentsInOurUniversity() throws Exception {
        mockMvc.perform(get("/student/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1));
    }

    @Test
    @Order(11)
    void shouldFindAverageOfStudentAge() throws Exception {
        mockMvc.perform(get("/student/average"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(20));
    }

    @Test
    @Order(12)
    void shouldFindStudentWithHelpToFilterByAge() throws Exception {
        mockMvc.perform(get("/student/filterByAgeBetween/")
                        .param("min", "5")
                        .param("max", "22"))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].name").value("testStudent"));
    }

    @Test
    @Order(13)
    void shouldUpdateStudentAgeAndCheckNewValue() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "testStudent");
        jsonObject.put("age", 22);
        jsonObject.put("facultyId", 1);
        mockMvc.perform(put("/student")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content(jsonObject.toString()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].length()").value(4))
                .andExpect(jsonPath("$[0].age").value(22));
    }

    @Test
    @Order(14)
    void shouldGivenWrongFacultyIdAndNotFoundFaculty() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "wrongTest");
        jsonObject.put("color", "blue");
        mockMvc.perform(put("/faculty")
                        .param("id", "99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(15)
    void shouldGivenWrongStudentIdAndNotFoundStudent() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "testStudent");
        jsonObject.put("age", 22);
        jsonObject.put("facultyId", 1);
        mockMvc.perform(put("/student")
                        .param("id", "99")
                        .contentType(MediaType.APPLICATION_JSON).content(jsonObject.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(99)
    void shouldFindFiveYoungStudents() throws Exception {
        insertFiveStudents(mockMvc);
        mockMvc.perform(get("/student/young-student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    @Order(100)
    void shouldSortingStudentsByPageAndQuantity() throws Exception {
        int studentQuantity = 3;

        MvcResult result = mockMvc.perform(get("/student/sorting")
                        .param("page", "1")
                        .param("quantity", "3"))
                .andExpect(status().isOk())
                .andReturn();

        List<StudentDTO> studentDTOS = new ObjectMapper()
                .readValue(result.getResponse().getContentAsString(), new TypeReference<List<StudentDTO>>() {
                });


        Assertions.assertEquals(studentQuantity, studentDTOS.size());
        studentDTOS.forEach(x -> Assertions.assertEquals(StudentDTO.class, x.getClass()));
    }

    private static void insertFiveStudents(MockMvc mockMvc) throws Exception {
        Random random = new Random();
        JSONObject[] jsonObjects = new JSONObject[5];
        for (int i = 0; jsonObjects[4] == null; i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", "newTest");
            jsonObject.put("age", String.valueOf(random.nextInt(17, 30)));
            jsonObject.put("facultyId", "1");
            jsonObjects[i] = jsonObject;
        }
        for (JSONObject jsonObject : jsonObjects) {
            mockMvc.perform(post("/student")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonObject.toString()));
        }
    }

    private static void deleteAllStudentsFromDB(int numberOfStudents, MockMvc mockMvc) throws Exception {
        while (numberOfStudents != 0) {
            mockMvc.perform(delete("/student")
                            .param("id", String.valueOf(numberOfStudents)))
                    .andExpect(status().isOk());
            numberOfStudents--;
        }
    }
}