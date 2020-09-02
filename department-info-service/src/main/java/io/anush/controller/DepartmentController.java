package io.anush.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import io.anush.model.Department;
import io.anush.model.DepartmentList;
import io.anush.model.Employee;
import io.anush.model.EmployeeList;
import io.anush.service.DepartmentService;
import io.anush.service.EmployeeService;

@RestController
@RequestMapping("/Department")
public class DepartmentController {

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private EmployeeService employeeService;

	/*
	 * @HystrixCommand(fallbackMethod = "saveFallBackMethod")
	 * 
	 * @PostMapping("/saveDept") public Department saveDept(@RequestBody Department
	 * dept) { return departmentService.createDepartmentServ(dept); }
	 * 
	 * public Department saveFallBackMethod(@RequestBody Department dept) { return
	 * new Department(0, "no dept is available as of now");
	 * 
	 * }
	 */

	@RequestMapping("/GetAll")
	public DepartmentList getAllDepartments() {
		List<Department> lst = departmentService.getAllDepartments();
		DepartmentList departmentList = new DepartmentList();
		departmentList.setDeptList(lst);
		return departmentList;
	}

	@RequestMapping("/{deptid}")
	public Optional<Department> getDepartment(@PathVariable("deptid") int deptid) {
		return departmentService.getDepartment(deptid);
	}

	@PostMapping("/addDept")
	public void addDepartment(@RequestBody Department department) {
		departmentService.addDepartment(department);
	}

	@PutMapping("/updateDepartment/{deptid}")
	public void updateDepartment(@RequestBody Department department, @PathVariable int deptid) {
		department.setDeptId(deptid);
		departmentService.updateDepartment(department);
	}

	@DeleteMapping("/DeleteDepartment/{deptid}")
	public void deleteDepartment(@PathVariable int deptid) {
		departmentService.deleteDepartment(deptid);
		employeeService.deleteEmployee(deptid);
	}

	@GetMapping("{edid}/employees")
	public EmployeeList getAllEmployees(@PathVariable int edid) {
		EmployeeList lst = employeeService.getAllEmployees(edid);
		System.out.println(lst.getEmployeeList().size());
		return lst;
	}

	@GetMapping("/employess/{empid}")
	public Employee getEmployee(@PathVariable int empid) {
		return employeeService.getEmployee(empid);
	}

	@PostMapping("/employees/{edid}/addEmployee")
	public void addEmployee(@RequestBody Employee employee, @PathVariable int edid) {
		employee.setEdid(edid);
		employeeService.addEmployee(employee, edid);
	}

	@PutMapping("/employees/{edid}/updateEmployee/{empid}")
	public void updateEmployee(@RequestBody Employee employee, @PathVariable int edid, @PathVariable int empid) {
		employee.setEdid(edid);
		employee.setEmpid(empid);
		employeeService.updateEmployee(employee, edid, empid);
	}

	@DeleteMapping("/employees/deleteEmployee/{edid}")
	public void deleteEmployee(@PathVariable int edid) {
		employeeService.deleteEmployee(edid);
	}

	@DeleteMapping("/employees/{edid}/deleteEmployee/{empid}")
	public void deleteSingleEmployee(@PathVariable int edid, @PathVariable int empid) {
		employeeService.deleteSingleEmployee(edid, empid);
	}

}
