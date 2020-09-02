package io.anush;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.anush.controller.DepEmpController;
import io.anush.model.Department;
import io.anush.model.DepartmentList;
import io.anush.model.Employee;
import io.anush.model.EmployeeList;

//@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(value = DepEmpController.class)

public class DepEmpCrudApplicationTests {

	@Autowired
	private RestTemplate restTemplate;

	@MockBean
	private DepEmpController depEmpController;

	ObjectMapper obj = new ObjectMapper();

	@Autowired
	private MockMvc mockmvc;

	@Test
	public void getAllDepartments() {
		Department department1 = new Department(1, "HR");
		Department department2 = new Department(2, "SUPPORT");
		List<Department> list = new ArrayList<>();
		list.add(department1);
		list.add(department2);
		DepartmentList list1 = new DepartmentList();
		list1.setDeptList(list);
		when(depEmpController.getDepartmentList()).thenReturn(list1);

		try {
			mockmvc.perform(get("/DeptList").contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk())
					.andReturn();
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(2, list1.getDeptList().size());
	}

	@Test
	public void addDepartment() {

		Department department = new Department(1, "HR");

		try {
			when(depEmpController.addDepartment(department)).thenReturn(department);
			String jasontype = obj.writeValueAsString(department);
			mockmvc.perform(post("/CreateDepartment").content(jasontype).contentType(MediaType.APPLICATION_JSON_VALUE))
					.andExpect(status().isOk());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void updateDepartment() {

		Department department = new Department(1, "HR");

		try {
			verify(depEmpController, times(0)).upadateDept(department, department.getDeptId());
			String jasontype = obj.writeValueAsString(department);
			mockmvc.perform(post("/UpdateDepartment").content(jasontype).contentType(MediaType.APPLICATION_JSON_VALUE))
					.andExpect(status().isOk());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void deleteDepartment() {
		Department department= new Department(1, "HR");

		try {
			verify(depEmpController, times(0)).deleteDept(department.getDeptId());
			mockmvc.perform(delete("/DeleteDepartment").contentType(MediaType.APPLICATION_JSON_VALUE))
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void getAllEmployees() {
		Employee employee1 = new Employee(1, "Anusha", 24, 1);
		Employee employee2 = new Employee(2, "Kiranmai", 23, 1);
		EmployeeList listemp = new EmployeeList();
		List<Employee> list = new ArrayList<>();
		list.add(employee1);
		list.add(employee2);
		listemp.setEmployeeList(list);
		when(depEmpController.getAllDepartments(1)).thenReturn(listemp);

		try {
			mockmvc.perform(get("/emplist").contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk())
					.andReturn();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(2, listemp.getEmployeeList().size());
	}

	@Test
	public void addEmployee() {
		Employee employee = new Employee(1, "Anusha", 24, 1);

		try {
			when(depEmpController.addEmployee(employee, employee.getEdid())).thenReturn(employee);
			String jasontype = obj.writeValueAsString(employee);
			mockmvc.perform(post("/saveEmployee").content(jasontype).contentType(MediaType.APPLICATION_JSON_VALUE))
					.andExpect(status().isOk());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void deleteEmployee() {
		Employee employee = new Employee(1, "Anusha", 24, 1);

		try {
			verify(depEmpController, times(0)).deleteEmp(employee.getEdid(), employee.getEmpid());
			mockmvc.perform(delete("/deleteEmployee").contentType(MediaType.APPLICATION_JSON_VALUE))
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void updateEmployee() {
		Employee employee = new Employee(1, "Anusha", 24, 1);
		try {
			verify(depEmpController, times(0)).updateEmp(employee, employee.getEdid(), employee.getEmpid());
			String jasontype = obj.writeValueAsString(employee);
			mockmvc.perform(post("/updateEmployee").content(jasontype).contentType(MediaType.APPLICATION_JSON_VALUE))
					.andExpect(status().isOk());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}