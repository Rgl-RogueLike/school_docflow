package com.haritonov.school.docflow.modules.employee.controller;

import com.haritonov.school.docflow.modules.employee.dto.PositionCreateRequest;
import com.haritonov.school.docflow.modules.employee.dto.PositionResponse;
import com.haritonov.school.docflow.modules.employee.dto.PositionUpdateRequest;
import com.haritonov.school.docflow.modules.employee.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/positions")
@RequiredArgsConstructor
public class PositionController {

    private final PositionService positionService;

    @GetMapping
    public String listPositions(Model model) {
        model.addAttribute("positions", positionService.getAll());
        model.addAttribute("username", "Секретарь");
        return "positions/list";
    }

    @GetMapping("/new")
    public String showCreatorForm(Model model) {
        model.addAttribute("positionDto", new PositionCreateRequest());
        model.addAttribute("username", "Секретарь");
        return "positions/new";
    }

    @PostMapping("/new")
    public String createPosition(@ModelAttribute("positionDto") PositionCreateRequest request,
                                 RedirectAttributes redirectAttributes) {
        try {
            positionService.create(request);
            redirectAttributes.addFlashAttribute("success", "Должность успешно добавлена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка добавления должности: " + e.getMessage());
        }
        return "redirect:/positions";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            PositionResponse position = positionService.getById(id);
            PositionUpdateRequest updateRequest = new PositionUpdateRequest();
            updateRequest.setId(position.getId());
            updateRequest.setName(position.getName());
            model.addAttribute("positionDto", updateRequest);
            model.addAttribute("username", "Секретарь");
            return "positions/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Должность не найдена");
            return "redirect:/positions";
        }
    }

    @PostMapping("/{id}/edit")
    public String updatePosition(@PathVariable Long id,
                                 @ModelAttribute("positionDto") PositionUpdateRequest request,
                                 RedirectAttributes redirectAttributes) {
        try {
            request.setId(id);
            positionService.update(request);
            redirectAttributes.addFlashAttribute("success", "Должность успешно обновлена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении должности: " + e.getMessage());
        }
        return "redirect:/positions";
    }

    @PostMapping("/{id}/delete")
    public String deletePosition(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            positionService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Должность успешно удалена");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", "Нельзя удалить должность: есть сотрудники с этой должностью");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении должности: " + e.getMessage());
        }
        return "redirect:/positions";
    }
}