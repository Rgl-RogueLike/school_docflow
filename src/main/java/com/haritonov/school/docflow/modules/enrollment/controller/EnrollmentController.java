package com.haritonov.school.docflow.modules.enrollment.controller;

import com.haritonov.school.docflow.modules.document.model.enums.DocumentPrefix;
import com.haritonov.school.docflow.modules.document.service.DocumentNumberGeneratedService;
import com.haritonov.school.docflow.modules.enrollment.dto.EnrollmentCreateRequest;
import com.haritonov.school.docflow.modules.enrollment.dto.EnrollmentFilterDto;
import com.haritonov.school.docflow.modules.enrollment.dto.EnrollmentResponse;
import com.haritonov.school.docflow.modules.enrollment.dto.EnrollmentUpdateRequest;
import com.haritonov.school.docflow.modules.enrollment.service.EnrollmentService;
import com.haritonov.school.docflow.modules.printdocuments.service.DocumentTemplateService;
import com.haritonov.school.docflow.modules.student.service.EducationalClassService;
import com.haritonov.school.docflow.modules.user.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final EducationalClassService classService;
    private final CurrentUserService currentUserService;
    private final DocumentNumberGeneratedService documentNumberGeneratedService;
    private final DocumentTemplateService documentTemplateService;

    @GetMapping
    public String listEnrollments(@ModelAttribute EnrollmentFilterDto filter, Model model) {
        model.addAttribute("enrollments", enrollmentService.getAllWithFilter(filter));
        model.addAttribute("filter", filter);
        model.addAttribute("username", currentUserService.getCurrentEmployeeFullName());
        return "enrollments/list";
    }

    @GetMapping("/{id}")
    public String viewEnrollment(@PathVariable Long id, Model model) {
        model.addAttribute("enrollment", enrollmentService.getById(id));
        model.addAttribute("username", currentUserService.getCurrentEmployeeFullName());
        return "enrollments/card";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        EnrollmentCreateRequest request = new EnrollmentCreateRequest();
        request.setDocumentDate(LocalDate.now());
        request.setEnrollmentDate(LocalDate.now());
        String generatedNumber = documentNumberGeneratedService.generatedNumber(
                DocumentPrefix.ENROLLMENT,
                LocalDate.now()
        );
        request.setDocumentNumber(generatedNumber);
        model.addAttribute("enrollmentDto", request);
        model.addAttribute("classes", classService.getAll());
        model.addAttribute("username", currentUserService.getCurrentEmployeeFullName());
        return "enrollments/new";
    }

    @PostMapping("/new")
    public String createEnrollment(@ModelAttribute("enrollmentDto") EnrollmentCreateRequest request,
                                   RedirectAttributes redirectAttributes) {
        try {
            request.setCreatorId(currentUserService.getCurrentEmployeeId());
            Long id = enrollmentService.create(request);
            redirectAttributes.addFlashAttribute("success", "Приказ о зачислении успешно создан. Ученик добавлен в систему.");
            return "redirect:/enrollments/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка создания приказа: " + e.getMessage());
            return "redirect:/enrollments/new";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            EnrollmentResponse enrollment = enrollmentService.getById(id);
            EnrollmentUpdateRequest updateRequest = new EnrollmentUpdateRequest();
            updateRequest.setId(enrollment.getId());
            updateRequest.setDocumentNumber(enrollment.getDocumentNumber());
            updateRequest.setDocumentDate(enrollment.getDocumentDate().toLocalDate());
            updateRequest.setEnrollmentDate(enrollment.getEnrollmentDate());
            updateRequest.setReason(enrollment.getReason());

            updateRequest.setLastName(enrollment.getLastName());
            updateRequest.setFirstName(enrollment.getFirstName());
            updateRequest.setPatronymic(enrollment.getPatronymic());
            updateRequest.setGender(enrollment.getGender());
            updateRequest.setDateOfBirth(enrollment.getDateOfBirth());
            updateRequest.setClassId(enrollment.getClassId());

            model.addAttribute("enrollmentDto", updateRequest);
            model.addAttribute("classes", classService.getAll());
            model.addAttribute("username", currentUserService.getCurrentEmployeeFullName());
            return "enrollments/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Приказ не найден");
            return "redirect:/enrollments";
        }
    }

    @PostMapping("/{id}/edit")
    public String updateEnrollment(@PathVariable Long id,
                                   @ModelAttribute("enrollmentDto") EnrollmentUpdateRequest request,
                                   RedirectAttributes redirectAttributes) {
        try {
            request.setId(id);
            enrollmentService.update(request);
            redirectAttributes.addFlashAttribute("success", "Приказ обновлён. Данные ученика обновлены.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка обновления: " + e.getMessage());
        }
        return "redirect:/enrollments/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteEnrollment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            enrollmentService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Приказ и связанный ученик удалены");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка удаления: " + e.getMessage());
        }
        return "redirect:/enrollments";
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadEnrollment(@PathVariable Long id) {
        try {
            EnrollmentResponse enrollment = enrollmentService.getById(id);
            byte[] document = documentTemplateService.generateEnrollmentOrder(enrollment);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "prikaz_zachislenie_" + id + ".docx");

            return new ResponseEntity<>(document, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}