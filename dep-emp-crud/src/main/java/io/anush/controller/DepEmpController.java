package io.anush.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import io.anush.model.Department;
import io.anush.model.DepartmentList;
import io.anush.model.Employee;
import io.anush.model.EmployeeList;

@RestController
public class DepEmpController {

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "/DeptList", method = RequestMethod.GET)
	public ModelAndView getAllDepartments(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("In Controller");
		DepartmentList deptlist = getDepartmentList();
		System.out.println(deptlist.getDeptList().get(0));
		List<Department> listdept = new ArrayList<>();

		for (int i = 0; i < deptlist.getDeptList().size(); i++) {
			listdept.add(deptlist.getDeptList().get(i));
		}
		for (Department department : listdept) {
			System.out.println(department.getDeptId());
		}
		HttpSession session = request.getSession();
		session.setAttribute("DeptList", listdept);
		ModelAndView modelAndView = new ModelAndView("home");
		modelAndView.addObject("DeptList", listdept);
		modelAndView.addObject("homepage", "main");
		return modelAndView;
	}

	public DepartmentList getDepartmentList() {
		return restTemplate.getForObject("http://gateway-service/Department/GetAll", DepartmentList.class);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/NewDepartment", method = RequestMethod.GET)
	public ModelAndView newDepartment(HttpServletRequest request) {
		String Register = "newform";
		HttpSession session1 = request.getSession();
		List<Department> list = (List<Department>) session1.getAttribute("DeptList");
		session1.setAttribute("DeptList", list);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("Register", Register);
		modelAndView.addObject("createdept", "newdept");
		modelAndView.setViewName("home");
		Department department = new Department();
		modelAndView.addObject("department", department);
		return modelAndView;
	}

	@RequestMapping(value = "/CreateDepartment", method = RequestMethod.POST)
	public ModelAndView insertDepartment(@ModelAttribute Department department) {
		addDepartment(department);
		return new ModelAndView("redirect:/DeptList");
	}

	public Department addDepartment(Department department) {
		return restTemplate.postForObject("http://gateway-service/Department/AddDepartment", department,
				Department.class);
	}

	@RequestMapping(value = "/UpdateDepartment")
	public ModelAndView updateDepartment(@ModelAttribute Department department, HttpServletRequest request) {
		int deptId = Integer.parseInt(request.getParameter("deptId"));
		upadateDept(department, deptId);
		return new ModelAndView("redirect:/DeptList");
	}

	public void upadateDept(Department department, int deptId) {
		restTemplate.put("http://gateway-service/Department/updateDepartment/" + deptId, department);
	}

	@RequestMapping(value = "/DeleteDepartment")
	public ModelAndView deleteDepartment(HttpServletRequest request) {
		int deptid = Integer.parseInt(request.getParameter("deptId"));
		deleteDept(deptid);
		return new ModelAndView("redirect:/DeptList");
	}

	public void deleteDept(int deptId) {
		restTemplate.delete("http://gateway-service/Department/DeleteDepartment/" + deptId);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/GetDepartment", method = RequestMethod.GET)
	public ModelAndView getDepartmentId(HttpServletRequest request) {
		int deptId = Integer.parseInt(request.getParameter("deptId"));
		HttpSession session2 = request.getSession();
		List<Department> list = (List<Department>) session2.getAttribute("DeptList");
		ModelAndView modelAndView = new ModelAndView("home");
		modelAndView.addObject("DeptList", list);
		modelAndView.addObject("departmentId", deptId);
		return modelAndView;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/showdepartments", method = RequestMethod.GET)
	public ModelAndView showDepartments(HttpServletRequest request) {
		HttpSession session3 = request.getSession();
		List<Department> listdept = (List<Department>) session3.getAttribute("DeptList");
		session3.setAttribute("DeptListemp", listdept);
		ModelAndView modelAndView = new ModelAndView("home");
		modelAndView.addObject("DeptListemp", listdept);
		int deptId = listdept.get(0).getDeptId();
		modelAndView.addObject("name", "names");
		return new ModelAndView("redirect:/emplist?deptId=" + deptId);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/emplist")
	public ModelAndView getAllEmployees(HttpServletRequest request) {
		int deptid = Integer.parseInt(request.getParameter("deptid"));
		List<Employee> listemp = new ArrayList<>();
		EmployeeList list = getAllDepartments(deptid);
		for (int i = 0; i < list.getEmployeeList().size(); i++) {
			listemp.add(list.getEmployeeList().get(i));
		}
		HttpSession httpSession = request.getSession();
		httpSession.setAttribute("EmpList", listemp);
		List<Department> lstdept1 = (List<Department>) httpSession.getAttribute("DeptList");
		ModelAndView modelAndView = new ModelAndView("home");
		modelAndView.addObject("DeptListemp", lstdept1);
		modelAndView.addObject("EmpList", listemp);
		modelAndView.addObject("homepage", "emppage");
		modelAndView.addObject("name", "names");
		return modelAndView;
	}

	public EmployeeList getAllDepartments(int deptid) {
		return restTemplate.getForObject("http://gateway-service/Department/" + deptid + "/employees",
				EmployeeList.class);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/newEmployee", method = RequestMethod.GET)
	public ModelAndView newContact(HttpServletRequest request) {
		String Register = "NewForm";
		HttpSession session1 = request.getSession();
		List<Employee> list = (List<Employee>) session1.getAttribute("EmpList");
		ModelAndView model = new ModelAndView("home");
		model.addObject("EmpList", list);
		model.addObject("Register", Register);
		model.addObject("insertEmployee", "newemployee");
		model.addObject("homepage", "emppage");
		return model;
	}

	@RequestMapping(value = "/saveEmployee", method = RequestMethod.POST)
	public ModelAndView saveEmployee(@ModelAttribute Employee employee, HttpServletRequest request) {
		int edid = Integer.parseInt(request.getParameter("edid"));
		addEmployee(employee, edid);
		return new ModelAndView("redirect:/emplist?deptid=" + edid);
	}

	public Employee addEmployee(Employee employee, int edid) {
		return restTemplate.postForObject("http://gateway-service/Department/employees/" + edid + "/addEmployee",
				employee, Employee.class);
	}

	@RequestMapping(value = "/deleteEmployee")
	public ModelAndView deleteEmployee(HttpServletRequest request) {
		int employeeId = Integer.parseInt(request.getParameter("id"));
		int edid = Integer.parseInt(request.getParameter("did"));
		deleteEmp(employeeId, edid);
		return new ModelAndView("redirect:/emplist?deptid=" + edid);
	}

	public void deleteEmp(int employeeId, int edid) {
		restTemplate.delete("http://gateway-service/Department/employees/" + edid + "/deleteEmployee/" + employeeId);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getEmployee", method = RequestMethod.GET)
	public ModelAndView editContact(HttpServletRequest request) {
		int employeeId = Integer.parseInt(request.getParameter("id"));
		int did = Integer.parseInt(request.getParameter("did"));
		HttpSession session2 = request.getSession();
		List<Employee> list = (List<Employee>) session2.getAttribute("EmpList");
		session2.setAttribute("EmpList", list);
		ModelAndView model = new ModelAndView("home");
		model.addObject("homepage", "emppage");
		model.addObject("EmpList", list);
		model.addObject("employeeid", employeeId);
		model.addObject("Did", did);
		return model;
	}

	@RequestMapping(value = "/updateEmployee")
	public ModelAndView updateEmployee(@ModelAttribute Employee employee, HttpServletRequest request) {
		int employeeId = Integer.parseInt(request.getParameter("empid"));
		int did = Integer.parseInt(request.getParameter("edid"));
	
		System.out.println("In Method Update");
		updateEmp(employee, employeeId, did);
		System.out.println("In Method Update");
		return new ModelAndView("redirect:/emplist?deptid=" + did);
	}

	public void updateEmp(Employee employee, int employeeId, int did) {
		System.out.println("From Test Case");
		restTemplate.put("http://gateway-service/Department/employees/" + did + "/updateEmployee/" + employeeId,
				employee);
	}

}