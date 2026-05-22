package com.haritonov.school.docflow.modules.student.controller;

import com.haritonov.school.docflow.modules.student.dto.StudentResponse;
import com.haritonov.school.docflow.modules.student.dto.StudentUpdateRequest;
import com.haritonov.school.docflow.modules.student.service.EducationalClassService;
import com.haritonov.school.docflow.modules.student.service.StudentService;
import com.haritonov.school.docflow.modules.user.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final EducationalClassService classService;
    private final CurrentUserService currentUserService;

    @GetMapping
    public String listStudents(Model model, @RequestParam(required = false) Long classId) {
        model.addAttribute("classes", classService.getAll());
        if (classId != null) {
            List<StudentResponse> students = studentService.getByClassId(classId);
            model.addAttribute("students", students);
            model.addAttribute("selectedClassId", classId);
            model.addAttribute("selectedClass", classService.getById(classId));
            model.addAttribute("title", "Список учеников");
        } else {
            model.addAttribute("students", null);
            model.addAttribute("title", "Выберите учебный класс");
        }
        model.addAttribute("username", currentUserService.getCurrentEmployeeFullName());
        return "students/list";
    }

    @GetMapping("/{id}")
    public String viewStudent(@PathVariable Long id, Model model) {
        StudentResponse student = studentService.getById(id);
        model.addAttribute("student", student);
        model.addAttribute("title", "Карточка ученика");
        model.addAttribute("username", currentUserService.getCurrentEmployeeFullName());
        return "students/card";
    }

    @GetMapping("/{id}/edit")
    public String editStudentForm(@PathVariable Long id, Model model) {
        StudentResponse student = studentService.getById(id);
        model.addAttribute("student", student);
        model.addAttribute("classes", classService.getAll());
        model.addAttribute("title", "Редактирование ученика");
        model.addAttribute("username", currentUserService.getCurrentEmployeeFullName());
        return "students/edit";
    }

    @PostMapping("/{id}/edit")
    public String updateStudent(@PathVariable Long id,
                                @ModelAttribute StudentUpdateRequest request,
                                RedirectAttributes redirectAttributes) {
        request.setId(id);
        studentService.update(request);
        redirectAttributes.addFlashAttribute("success", "Ученик успешно обновлен");
        return "redirect:/students/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteStudent(@PathVariable Long id,
                                @RequestParam(required = false) Long classId,
                                RedirectAttributes redirectAttributes) {
        try {
            studentService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Ученик успешно удален");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении ученика: " + e.getMessage());
        }

        if (classId != null) {
            return "redirect:/students?classId=" + classId;
        }
        return "redirect:/students";
    }
}
