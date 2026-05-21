package com.haritonov.school.docflow.modules.employee.controller;

import com.haritonov.school.docflow.modules.employee.dto.EmployeeCreateRequest;
import com.haritonov.school.docflow.modules.employee.dto.EmployeeResponse;
import com.haritonov.school.docflow.modules.employee.dto.EmployeeUpdateRequest;
import com.haritonov.school.docflow.modules.employee.service.EmployeeService;
import com.haritonov.school.docflow.modules.employee.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final PositionService positionService;

    @GetMapping
    public String listEmployees(Model model) {
        model.addAttribute("employees", employeeService.getAll());
        model.addAttribute("username", "Секретарь");
        return "employees/list";
    }

    @GetMapping("/{id}")
    public String viewEmployee(@PathVariable Long id, Model model) {
        model.addAttribute("employee", employeeService.getById(id));
        model.addAttribute("username", "Секретарь");
        return "employees/card";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("employeeDto", new EmployeeCreateRequest());
        model.addAttribute("positions", positionService.getAll());
        model.addAttribute("username", "Секретарь");
        return "employees/new";
    }

    @PostMapping("/new")
    public String createEmployee(@ModelAttribute("employeeDto") EmployeeCreateRequest request,
                                 RedirectAttributes redirectAttributes) {
        try {
            employeeService.create(request);
            redirectAttributes.addFlashAttribute("success", "Сотрудник успешно добавлен");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка добавления сотрудника: " + e.getMessage());
        }
        return "redirect:/employees";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            EmployeeResponse employee = employeeService.getById(id);
            EmployeeUpdateRequest updateRequest = getEmployeeUpdateRequest(employee);
            model.addAttribute("employeeDto", updateRequest);
            model.addAttribute("positions", positionService.getAll());
            model.addAttribute("username", "Секретарь");
            return "employees/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Сотрудник не найден");
            return "redirect:/employees";
        }
    }

    private static @NonNull EmployeeUpdateRequest getEmployeeUpdateRequest(EmployeeResponse employee) {
        EmployeeUpdateRequest updateRequest = new EmployeeUpdateRequest();
        updateRequest.setId(employee.getId());
        updateRequest.setFirstName(employee.getFirstName());
        updateRequest.setLastName(employee.getLastName());
        updateRequest.setPatronymic(employee.getPatronymic());
        updateRequest.setPhoneNumber(employee.getPhoneNumber());
        updateRequest.setDateOfEmployment(employee.getDateOfEmployment());
        updateRequest.setPositionId(employee.getPositionId());
        return updateRequest;
    }

    @PostMapping("/{id}/edit")
    public String updateEmployee(@PathVariable Long id,
                                 @ModelAttribute("employeeDto") EmployeeUpdateRequest request,
                                 RedirectAttributes redirectAttributes) {
        try {
            request.setId(id);
            employeeService.update(request);
            redirectAttributes.addFlashAttribute("success", "Сотрудник успешно обновлён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении сотрудника: " + e.getMessage());
        }
        return "redirect:/employees/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            employeeService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Сотрудник успешно удалён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении сотрудника: " + e.getMessage());
        }
        return "redirect:/employees";
    }
}