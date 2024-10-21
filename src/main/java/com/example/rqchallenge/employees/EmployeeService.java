package com.example.rqchallenge.employees;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final ObjectMapper objectMapper = new ObjectMapper();

//I was getting a 409 from the dummy endpoints at from 1:30 - 1:55 pm on Oct 20 (I tried a few times and then started mocking)
//I tested okhttp and my code with a separate easy-to-hit API, https://jsonplaceholder.typicode.com/posts, and it worked,
//so I'm not quite sure what's going on with https://dummy.restapiexample.com.
//https://jsonplaceholder.typicode.com/posts is a pretty similar thing, which tests the ability to deserialize,
// change the URL, and set up an API call.

//The "get by id method" is a little different than if the URL was built for that method, because I wouldn't filter it myself
//in a stream. So I included the request I would use if https://dummy.restapiexample.com was working.
//Generally the api calls will all be fairly similar except for creating the requests correctly.

//Here's a sample request that works with a different API.

//Would put the at the class level so it wouldn't need to be instantiated each time.
//        OkHttpClient okHttpClient = new OkHttpClient();
//
//        Request request = new Request.Builder()
////            .url("https://dummy.restapiexample.com/api/v1/employees")
//            .url("https://jsonplaceholder.typicode.com/posts")
//            .get()
//            .build();
//        try (Response response = okHttpClient.newCall(request).execute()) {
//            if (response.isSuccessful()) {
//                ResponseBody responseBody = response.body();
//                if (responseBody != null) {
//                    String responseBodyString = responseBody.string();
//                    List<FakeUser> fakeUsers = objectMapper.readValue(responseBodyString, new TypeReference<>() {});
//                    System.out.println("Debugging");
//                }
//            } else {
//                System.out.println("Request failed with status: " + response.code());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


//Also, since this is a dummy application I'm going to throw the exceptions to the api layer but if there was more core
//business logic it'd probably be worth a try-catch block and then throwing a more custom exception to the api layer

    public List<Employee> getAllEmployees() throws IOException {
//        Request request = new Request.Builder()
//                .url("https://dummy.restapiexample.com/api/v1/employees")
//                .get()
//                .build();

        ClassPathResource resource = new ClassPathResource("employee.json");
        List<Employee> employees = objectMapper.readValue(resource.getFile(),
                new TypeReference<>() {});
        return employees;
    }

    public List<Employee> getEmployeesByNameSearch(String searchString) throws IOException {
//        Request request = new Request.Builder()
//                .url("https://dummy.restapiexample.com/api/v1/employees")
//                .get()
//                .build();

        ClassPathResource resource = new ClassPathResource("employee.json");
        List<Employee> employees = objectMapper.readValue(resource.getFile(),
                new TypeReference<>() {});
        return employees.stream()
                .filter(employee -> employee.getEmployeeName().toLowerCase().contains(searchString.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Employee getEmployeeById(String id) throws IOException {
//        Request request = new Request.Builder()
//                .url("https://dummy.restapiexample.com/api/v1/employee/" + id)
//                .get()
//                .build();

        ClassPathResource resource = new ClassPathResource("employee.json");
        List<Employee> employees = objectMapper.readValue(resource.getFile(),
                new TypeReference<>() {
                });
        return employees.stream()
                .filter(employee -> employee.getId() == Integer.parseInt(id)).findFirst().orElse(null);
    }

    public Integer getHighestSalaryOfEmployees() throws IOException {
//        Request request = new Request.Builder()
//                .url("https://dummy.restapiexample.com/api/v1/employees")
//                .get()
//                .build();

        ClassPathResource resource = new ClassPathResource("employee.json");
        List<Employee> employees = objectMapper.readValue(resource.getFile(),
                new TypeReference<>() {
                });
        return employees.stream()
                .max(Comparator.comparingInt(Employee::getEmployeeSalary))
                .map(Employee::getEmployeeSalary)
                .orElseThrow(() -> new IllegalArgumentException("No employees found"));
    }

    public List<String> getTopTenHighestEarningEmployeeNames() throws IOException {
//        Request request = new Request.Builder()
//                .url("https://dummy.restapiexample.com/api/v1/employees")
//                .get()
//                .build();

        ClassPathResource resource = new ClassPathResource("employee.json");
        List<Employee> employees = objectMapper.readValue(resource.getFile(),
                new TypeReference<>() {
                });

        return employees.stream()
                .sorted(Comparator.comparingInt(Employee::getEmployeeSalary).reversed())
                .limit(10)
                .map(Employee::getEmployeeName)
                .collect(Collectors.toList());
    }

    //name
    //salary
    //age
    public Employee createEmployee(Map<String, Object> employeeInput) throws JsonProcessingException {

        String name = (String) employeeInput.getOrDefault("name", "Unknown");
        int salary = ((int) employeeInput.getOrDefault("salary", 0));
        int age = (int) employeeInput.getOrDefault("age", 0);

        //next id
        //maybe profile image is updated later
        Employee employee = new Employee(99,
                name,
                salary,
                age,
                null);

        ObjectMapper objectMapper = new ObjectMapper();
        String bodyAsString = objectMapper.writeValueAsString(employee);

        RequestBody requestBody = RequestBody.create(bodyAsString, JSON);
        Request request = new Request.Builder()
                .url("https://dummy.restapiexample.com/api/v1/create")
                .post(requestBody)
                .build();
        return null;
    }

    public String deleteEmployeeById(String id) throws IOException {
//        Request request = new Request.Builder()
//                .url("https://dummy.restapiexample.com/api/v1/delete/" + id)
//                .delete()
//                .build();

        ClassPathResource resource = new ClassPathResource("employee.json");
        List<Employee> employees = objectMapper.readValue(resource.getFile(),
                new TypeReference<>() {
                });

        Employee employeeWithId = employees.stream()
                .filter(employee -> employee.getId() == Integer.parseInt(id))
                .findFirst()
                .orElse(null);

        //Really this "if" statement is an artifact of an api calling another api which doesn't return
        //the needed information, so hopefully the secondary api would return this information
        //in reality, or we'd be calling a database to delete by id only if the employee exists (if the
        //name is needed to be returned to the primary api) or something like that.

        if (employeeWithId != null) {
            return employeeWithId.getEmployeeName();
        } else {
            return null;
        }
    }
}
