package com.haritonov.school.docflow.modules.dislocation.controller;

import com.haritonov.school.docflow.modules.dislocation.dto.DislocationCreateRequest;
import com.haritonov.school.docflow.modules.dislocation.dto.DislocationResponse;
import com.haritonov.school.docflow.modules.dislocation.dto.DislocationUpdateRequest;
import com.haritonov.school.docflow.modules.dislocation.service.DislocationService;
import com.haritonov.school.docflow.modules.document.model.enums.DocumentPrefix;
import com.haritonov.school.docflow.modules.document.service.DocumentNumberGeneratedService;
import com.haritonov.school.docflow.modules.student.service.StudentService;
import com.haritonov.school.docflow.modules.user.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/dislocations")
@RequiredArgsConstructor
public class DislocationController {

    private final DislocationService dislocationService;
    private final StudentService studentService;
    private final CurrentUserService currentUserService;
    private final DocumentNumberGeneratedService documentNumberGeneratedService;

    @GetMapping
    public String listDislocations(Model model) {
        model.addAttribute("dislocations", dislocationService.getAll());
        model.addAttribute("username", currentUserService.getCurrentEmployeeFullName());
        return "dislocations/list";
    }

    @GetMapping("/{id}")
    public String viewDislocation(@PathVariable Long id, Model model) {
        model.addAttribute("dislocation", dislocationService.getById(id));
        model.addAttribute("username", currentUserService.getCurrentEmployeeFullName());
        return "dislocations/card";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        DislocationCreateRequest request = new DislocationCreateRequest();
        request.setDocumentDate(LocalDate.now());
        request.setDateFrom(LocalDate.now());
        request.setEffectiveStartDate(LocalDate.now());
        request.setEffectiveEndDate(LocalDate.now());
        String generatedNumber = documentNumberGeneratedService.generatedNumber(
                DocumentPrefix.DISLOCATION,
                LocalDate.now()
        );
        request.setDocumentNumber(generatedNumber);
        model.addAttribute("dislocationDto", request);
        model.addAttribute("students", studentService.getAll());
        model.addAttribute("username", currentUserService.getCurrentEmployeeFullName());
        return "dislocations/new";
    }

    @PostMapping("/new")
    public String createDislocation(@ModelAttribute("dislocationDto") DislocationCreateRequest request,
                                    RedirectAttributes redirectAttributes) {
        try {
            // TODO: взять реальный ID текущего сотрудника (например, из SecurityContext)
            Long currentEmployeeId = 1L;
            request.setCreatorId(currentEmployeeId);
            Long id = dislocationService.create(request);
            redirectAttributes.addFlashAttribute("success", "Приказ о перемещении успешно создан");
            return "redirect:/dislocations/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка создания приказа: " + e.getMessage());
            return "redirect:/dislocations/new";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            DislocationResponse dislocation = dislocationService.getById(id);
            DislocationUpdateRequest updateRequest = getDislocationUpdateRequest(dislocation);
            model.addAttribute("dislocationDto", updateRequest);
            model.addAttribute("students", studentService.getAll());
            model.addAttribute("username", currentUserService.getCurrentEmployeeFullName());
            return "dislocations/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Документ не найден");
            return "redirect:/dislocations";
        }
    }

    private static @NonNull DislocationUpdateRequest getDislocationUpdateRequest(DislocationResponse dislocation) {
        DislocationUpdateRequest updateRequest = new DislocationUpdateRequest();
        updateRequest.setId(dislocation.getId());
        updateRequest.setDocumentNumber(dislocation.getDocumentNumber());
        updateRequest.setDocumentDate(dislocation.getDocumentDate().toLocalDate());
        updateRequest.setRelation(dislocation.getRelation());
        updateRequest.setBasis(dislocation.getBasis());
        updateRequest.setDateFrom(dislocation.getDateFrom());
        updateRequest.setEffectiveStartDate(dislocation.getEffectiveStartDate());
        updateRequest.setEffectiveEndDate(dislocation.getEffectiveEndDate());
        return updateRequest;
    }

    @PostMapping("/{id}/edit")
    public String updateDislocation(@PathVariable Long id,
                                    @ModelAttribute("dislocationDto") DislocationUpdateRequest request,
                                    RedirectAttributes redirectAttributes) {
        try {
            request.setId(id);
            dislocationService.update(request);
            redirectAttributes.addFlashAttribute("success", "Приказ обновлён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка обновления: " + e.getMessage());
        }
        return "redirect:/dislocations/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteDislocation(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            dislocationService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Приказ удалён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка удаления: " + e.getMessage());
        }
        return "redirect:/dislocations";
    }
}