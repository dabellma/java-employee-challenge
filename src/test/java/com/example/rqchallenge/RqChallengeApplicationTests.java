package com.example.rqchallenge;

import com.example.rqchallenge.employees.Employee;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RqChallengeApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

//if there was a base request mapping

//    @Test
//    public void testGetEmployees() throws Exception {
//        String result = mockMvc.perform(get("a valid url")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        List<Employee> employees = objectMapper.readValue(result, new TypeReference<>() {});
//
//        assertEquals(employees.size(), 24);
//    }

    @Test
    public void testGetEmployeesByNameSearchDai_R() throws Exception {
        String result = mockMvc.perform(get("/search/Dai R")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        List<Employee> employees = objectMapper.readValue(result, new TypeReference<>() {});

        assertEquals(employees.size(), 1);
    }

    @Test
    public void testGetEmployeesByNameSearchll() throws Exception {
        String result = mockMvc.perform(get("/search/LL")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        List<Employee> employees = objectMapper.readValue(result, new TypeReference<>() {});

        //Cedric Kelly
        //Brielle Williamson
        //Colleen Hurst
        //Charde Marshall
        //Jenette Caldwell
        assertEquals(employees.size(), 5);
    }

    @Test
    public void testGetEmployeeById() throws Exception {
        String result = mockMvc.perform(get("/21")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        Employee employee = objectMapper.readValue(result, Employee.class);

        assertEquals(employee.getEmployeeName(), "Jenette Caldwell");
    }

    @Test
    public void testGetEmployeeByIdDoesntExist() throws Exception {
        String result = mockMvc.perform(get("/44")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        assertEquals(result, "");
    }

    @Test
    public void testGetHighestSalary() throws Exception {
        String result = mockMvc.perform(get("/highestSalary")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        Integer highestSalary = objectMapper.readValue(result, Integer.class);

        assertEquals(highestSalary, 725000);
    }

    @Test
    public void testGetTopTenHighestEarningNames() throws Exception {
        String result = mockMvc.perform(get("/topTenHighestEarningEmployeeNames")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        List<String> employeeNames = objectMapper.readValue(result, new TypeReference<>() {});

        assertEquals(employeeNames.size(), 10);
        assertThat(employeeNames.contains("Paul Byrd"));
        //etc.
    }

    @Test
    public void testDeleteEmployeeById() throws Exception {
        String result = mockMvc.perform(delete("/15")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(result, "Tatyana Fitzpatrick");
    }

    @Test
    public void testDeleteEmployeeByIdDoesntExist() throws Exception {
        String result = mockMvc.perform(delete("/44")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(result, "");
    }

}
