package com.cst438;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cst438.domain.StudentDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class StudentControllerTest {

    @Autowired
    private MockMvc mvc;

    // List Students
    @Test
    public void listStudents() throws Exception {
        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders.get("/student")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        StudentDTO[] students = fromJsonString(response.getContentAsString(), StudentDTO[].class);
        assertNotEquals(0, students.length);
    }

 // Add Student
    @Test
    public void addStudent() throws Exception {
        StudentDTO newStudent = new StudentDTO(0, "Test Student", "test@student.com", "ACTIVE", 1);

        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders.post("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newStudent))
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(201, response.getStatus());

        // Parse the returned student_id
        int returnedStudentId = Integer.parseInt(response.getContentAsString());
        assertNotEquals(0, returnedStudentId);
    }



    @Test
    public void updateStudent() throws Exception {
        int studentIdToUpdate = 1;  // Adjust as necessary based on your test data
        StudentDTO updatedStudent = new StudentDTO(studentIdToUpdate, "Updated Name", "updated@example.com", "Updated", 0);

        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders.put("/student/" + studentIdToUpdate)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedStudent))
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        response = mvc.perform(
                MockMvcRequestBuilders.get("/student/" + studentIdToUpdate)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        StudentDTO result = fromJsonString(response.getContentAsString(), StudentDTO.class);
        assertEquals("Updated Name", result.name());
    }

    // Delete Student
    @Test
    public void deleteStudent() throws Exception {
        int studentIdToDelete = 2;  // Adjust as necessary based on your test data

        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders.delete("/student/" + studentIdToDelete))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());

        response = mvc.perform(
                MockMvcRequestBuilders.get("/student/" + studentIdToDelete)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(404, response.getStatus());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> T fromJsonString(String str, Class<T> valueType) {
        try {
            return new ObjectMapper().readValue(str, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

