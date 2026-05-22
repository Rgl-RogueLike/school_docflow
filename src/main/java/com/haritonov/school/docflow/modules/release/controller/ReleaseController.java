package com.haritonov.school.docflow.modules.release.controller;

import com.haritonov.school.docflow.modules.document.model.enums.DocumentPrefix;
import com.haritonov.school.docflow.modules.document.service.DocumentNumberGeneratedService;
import com.haritonov.school.docflow.modules.release.dto.ReleaseCreateRequest;
import com.haritonov.school.docflow.modules.release.dto.ReleaseResponse;
import com.haritonov.school.docflow.modules.release.dto.ReleaseUpdateRequest;
import com.haritonov.school.docflow.modules.release.service.ReleaseService;
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
@RequestMapping("/releases")
@RequiredArgsConstructor
public class ReleaseController {

    private final ReleaseService releaseService;
    private final StudentService studentService;
    private final CurrentUserService currentUserService;
    private final DocumentNumberGeneratedService documentNumberGeneratedService;

    @GetMapping
    public String listReleases(Model model) {
        model.addAttribute("releases", releaseService.getAll());
        model.addAttribute("username", currentUserService.getCurrentEmployeeFullName());
        return "releases/list";
    }

    @GetMapping("/{id}")
    public String viewRelease(@PathVariable Long id, Model model) {
        model.addAttribute("release", releaseService.getById(id));
        model.addAttribute("username", currentUserService.getCurrentEmployeeFullName());
        return "releases/card";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        ReleaseCreateRequest request = new ReleaseCreateRequest();
        request.setDocumentDate(LocalDate.now());
        request.setBasisDocumentDate(LocalDate.now());
        request.setDateFrom(LocalDate.now());
        request.setDateOn(LocalDate.now());
        String generatedNumber = documentNumberGeneratedService.generatedNumber(
                DocumentPrefix.RELEASE,
                LocalDate.now()
        );
        request.setDocumentNumber(generatedNumber);
        model.addAttribute("releaseDto", request);
        model.addAttribute("students", studentService.getAll());
        model.addAttribute("username", currentUserService.getCurrentEmployeeFullName());
        return "releases/new";
    }

    @PostMapping("/new")
    public String createRelease(@ModelAttribute("releaseDto") ReleaseCreateRequest request,
                                RedirectAttributes redirectAttributes) {
        try {
            request.setCreatorId(currentUserService.getCurrentEmployeeId());
            Long id = releaseService.create(request);
            redirectAttributes.addFlashAttribute("success", "Приказ об отчислении успешно создан");
            return "redirect:/releases/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка создания приказа: " + e.getMessage());
            return "redirect:/releases/new";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            ReleaseResponse release = releaseService.getById(id);
            ReleaseUpdateRequest updateRequest = getReleaseUpdateRequest(release);
            model.addAttribute("releaseDto", updateRequest);
            model.addAttribute("students", studentService.getAll());
            model.addAttribute("username", currentUserService.getCurrentEmployeeFullName());
            return "releases/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Приказ не найден");
            return "redirect:/releases";
        }
    }

    private static @NonNull ReleaseUpdateRequest getReleaseUpdateRequest(ReleaseResponse release) {
        ReleaseUpdateRequest updateRequest = new ReleaseUpdateRequest();
        updateRequest.setId(release.getId());
        updateRequest.setDocumentNumber(release.getDocumentNumber());
        updateRequest.setDocumentDate(release.getDocumentDate().toLocalDate());
        updateRequest.setBasis(release.getBasis());
        updateRequest.setBasisDocumentDate(release.getBasisDocumentDate());
        updateRequest.setDateFrom(release.getDateFrom());
        updateRequest.setDateOn(release.getDateOn());
        updateRequest.setStudentId(release.getStudentId());
        return updateRequest;
    }

    @PostMapping("/{id}/edit")
    public String updateRelease(@PathVariable Long id,
                                @ModelAttribute("releaseDto") ReleaseUpdateRequest request,
                                RedirectAttributes redirectAttributes) {
        try {
            request.setId(id);
            releaseService.update(request);
            redirectAttributes.addFlashAttribute("success", "Приказ об отчислении обновлён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка обновления: " + e.getMessage());
        }
        return "redirect:/releases/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteRelease(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            releaseService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Приказ об отчислении удалён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка удаления: " + e.getMessage());
        }
        return "redirect:/releases";
    }
}