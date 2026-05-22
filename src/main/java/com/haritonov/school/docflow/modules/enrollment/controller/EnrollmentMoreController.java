package com.haritonov.school.docflow.modules.enrollment.controller;

import com.haritonov.school.docflow.modules.enrollment.dto.EnrollmentMoreCreateRequest;
import com.haritonov.school.docflow.modules.enrollment.dto.EnrollmentMoreItemDto;
import com.haritonov.school.docflow.modules.enrollment.dto.EnrollmentMoreResponse;
import com.haritonov.school.docflow.modules.enrollment.dto.EnrollmentMoreUpdateRequest;
import com.haritonov.school.docflow.modules.enrollment.service.EnrollmentMoreService;
import com.haritonov.school.docflow.modules.student.service.EducationalClassService;
import com.haritonov.school.docflow.modules.user.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/enrollments-more")
@RequiredArgsConstructor
public class EnrollmentMoreController {

    private final EnrollmentMoreService enrollmentMoreService;
    private final EducationalClassService classService;
    private final CurrentUserService currentUserService;

    @GetMapping
    public String listEnrollments(Model model) {
        model.addAttribute("enrollments", enrollmentMoreService.getAll());
        model.addAttribute("username", currentUserService.getCurrentEmployeeFullName());
        return "enrollments-more/list";
    }

    @GetMapping("/{id}")
    public String viewEnrollment(@PathVariable Long id, Model model) {
        model.addAttribute("enrollment", enrollmentMoreService.getById(id));
        model.addAttribute("username", currentUserService.getCurrentEmployeeFullName());
        return "enrollments-more/card";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        EnrollmentMoreCreateRequest request = new EnrollmentMoreCreateRequest();
        request.setDocumentDate(LocalDate.now());
        request.setDateFrom(LocalDate.now());

        List<EnrollmentMoreItemDto> items = new ArrayList<>();
        items.add(new EnrollmentMoreItemDto());
        request.setItems(items);

        model.addAttribute("enrollmentDto", request);
        model.addAttribute("classes", classService.getAll());
        model.addAttribute("username", currentUserService.getCurrentEmployeeFullName());
        return "enrollments-more/new";
    }

    @PostMapping("/new")
    public String createEnrollment(@ModelAttribute("enrollmentDto") EnrollmentMoreCreateRequest request,
                                   RedirectAttributes redirectAttributes) {
        try {
            System.out.println("Received items count: " + (request.getItems() != null ? request.getItems().size() : 0));
            request.setCreatorId(currentUserService.getCurrentEmployeeId());
            Long id = enrollmentMoreService.create(request);
            redirectAttributes.addFlashAttribute("success", "Приказ о зачислении успешно создан. " +
                    (request.getItems() != null ? request.getItems().size() : 0) + " учеников добавлено в систему.");
            return "redirect:/enrollments-more/" + id;
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Ошибка создания приказа: " + e.getMessage());
            return "redirect:/enrollments-more/new";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            EnrollmentMoreResponse enrollment = enrollmentMoreService.getById(id);
            EnrollmentMoreUpdateRequest updateRequest = new EnrollmentMoreUpdateRequest();
            updateRequest.setId(enrollment.getId());
            updateRequest.setDocumentNumber(enrollment.getDocumentNumber());
            updateRequest.setDocumentDate(enrollment.getDocumentDate().toLocalDate());
            updateRequest.setBasis(enrollment.getBasis());
            updateRequest.setDateFrom(enrollment.getDateFrom());
            updateRequest.setClassId(enrollment.getClassId());

            List<EnrollmentMoreItemDto> items = new ArrayList<>();
            for (var item : enrollment.getItems()) {
                EnrollmentMoreItemDto itemDto = new EnrollmentMoreItemDto();
                itemDto.setId(item.getStudentId());
                itemDto.setLastName(item.getLastName());
                itemDto.setFirstName(item.getFirstName());
                itemDto.setPatronymic(item.getPatronymic());
                itemDto.setGender(item.getGender());
                itemDto.setDateOfBirth(item.getDateOfBirth());
                itemDto.setEnrollmentDate(item.getEnrollmentDate());
                items.add(itemDto);
            }
            updateRequest.setItems(items);

            model.addAttribute("enrollmentDto", updateRequest);
            model.addAttribute("classes", classService.getAll());
            model.addAttribute("username", currentUserService.getCurrentEmployeeFullName());
            return "enrollments-more/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Приказ не найден");
            return "redirect:/enrollments-more";
        }
    }

    @PostMapping("/{id}/edit")
    public String updateEnrollment(@PathVariable Long id,
                                   @ModelAttribute("enrollmentDto") EnrollmentMoreUpdateRequest request,
                                   RedirectAttributes redirectAttributes) {
        try {
            request.setId(id);
            enrollmentMoreService.update(request);
            redirectAttributes.addFlashAttribute("success", "Приказ обновлён. Данные учеников обновлены.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка обновления: " + e.getMessage());
        }
        return "redirect:/enrollments-more/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteEnrollment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            enrollmentMoreService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Приказ и все связанные ученики удалены");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка удаления: " + e.getMessage());
        }
        return "redirect:/enrollments-more";
    }
}