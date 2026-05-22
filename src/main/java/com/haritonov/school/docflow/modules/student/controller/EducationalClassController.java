package com.haritonov.school.docflow.modules.student.controller;

import com.haritonov.school.docflow.modules.student.dto.EducationalClassCreateRequest;
import com.haritonov.school.docflow.modules.student.dto.EducationalClassResponse;
import com.haritonov.school.docflow.modules.student.dto.EducationalClassUpdateRequest;
import com.haritonov.school.docflow.modules.student.service.EducationalClassService;
import com.haritonov.school.docflow.modules.user.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/classes")
@RequiredArgsConstructor
public class EducationalClassController {

    private final EducationalClassService classService;
    private final CurrentUserService currentUserService;

    @GetMapping
    public String listClasses(Model model) {
        List<EducationalClassResponse> classes = classService.getAll();
        model.addAttribute("classes", classes);
        model.addAttribute("username", currentUserService.getCurrentEmployeeFullName());
        return "classes/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("classDto", new EducationalClassCreateRequest());
        model.addAttribute("username", currentUserService.getCurrentEmployeeFullName());
        return "classes/new";
    }

    @PostMapping("/new")
    public String createClass(@ModelAttribute("classDto") EducationalClassCreateRequest request) {
        classService.create(request);
        return "redirect:/classes";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            EducationalClassResponse classResponse = classService.getById(id);
            EducationalClassUpdateRequest updateRequest = new EducationalClassUpdateRequest();
            updateRequest.setId(classResponse.getId());
            updateRequest.setName(classResponse.getName());
            updateRequest.setYear(classResponse.getYear());

            model.addAttribute("classDto", updateRequest);
            model.addAttribute("username", currentUserService.getCurrentEmployeeFullName());
            return "classes/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Класс не найден");
            return "redirect:/classes";
        }
    }

    @PostMapping("/{id}/edit")
    public String updateClass(@PathVariable Long id,
                              @ModelAttribute("classDto") EducationalClassUpdateRequest request,
                              RedirectAttributes redirectAttributes) {
        try {
            request.setId(id);
            classService.update(request);
            redirectAttributes.addFlashAttribute("success", "Класс успешно обновлен");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении класса: " + e.getMessage());
        }
        return "redirect:/classes";
    }

    @PostMapping("/{id}/delete")
    public String deleteClass(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            classService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Класс успешно удален");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", "Нельзя удалить класс: в нем есть ученики");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении класса: " + e.getMessage());
        }
        return "redirect:/classes";
    }
}
