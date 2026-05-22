package com.haritonov.school.docflow.modules.certificate.controller;

import com.haritonov.school.docflow.modules.certificate.dto.CertificateCreateRequest;
import com.haritonov.school.docflow.modules.certificate.dto.CertificateResponse;
import com.haritonov.school.docflow.modules.certificate.dto.CertificateUpdateRequest;
import com.haritonov.school.docflow.modules.certificate.service.CertificateService;
import com.haritonov.school.docflow.modules.document.model.enums.DocumentPrefix;
import com.haritonov.school.docflow.modules.document.service.DocumentNumberGeneratedService;
import com.haritonov.school.docflow.modules.employee.service.EmployeeService;
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
@RequestMapping("/certificates")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;
    private final StudentService studentService;
    private final EmployeeService employeeService;
    private final CurrentUserService currentUserService;
    private final DocumentNumberGeneratedService documentNumberGeneratedService;

    @GetMapping
    public String listCertificates(Model model) {
        model.addAttribute("certificates", certificateService.getAll());
        model.addAttribute("username", currentUserService.getCurrentEmployeeFullName());
        return "certificates/list";
    }

    @GetMapping("/{id}")
    public String viewCertificate(@PathVariable Long id, Model model) {
        model.addAttribute("certificate", certificateService.getById(id));
        model.addAttribute("username", currentUserService.getCurrentEmployeeFullName());
        return "certificates/card";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        CertificateCreateRequest request = new CertificateCreateRequest();
        request.setDocumentDate(LocalDate.now());
        request.setIssueDate(LocalDate.now());
        request.setDateFrom(LocalDate.now());
        String generatedNumber = documentNumberGeneratedService.generatedNumber(
                DocumentPrefix.CERTIFICATE,
                LocalDate.now()
        );
        request.setDocumentNumber(generatedNumber);
        model.addAttribute("certificateDto", request);
        model.addAttribute("students", studentService.getAll());
        model.addAttribute("employees", employeeService.getAll());
        model.addAttribute("username", currentUserService.getCurrentEmployeeFullName());
        return "certificates/new";
    }

    @PostMapping("/new")
    public String createCertificate(@ModelAttribute("certificateDto") CertificateCreateRequest request,
                                    RedirectAttributes redirectAttributes) {
        try {
            Long id = certificateService.create(request);
            redirectAttributes.addFlashAttribute("success", "Справка успешно создана");
            return "redirect:/certificates/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка создания справки: " + e.getMessage());
            return "redirect:/certificates/new";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            CertificateResponse certificate = certificateService.getById(id);
            CertificateUpdateRequest updateRequest = getCertificateUpdateRequest(certificate);
            model.addAttribute("certificateDto", updateRequest);
            model.addAttribute("students", studentService.getAll());
            model.addAttribute("employees", employeeService.getAll());
            model.addAttribute("username", currentUserService.getCurrentEmployeeFullName());
            return "certificates/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Справка не найдена");
            return "redirect:/certificates";
        }
    }

    private static @NonNull CertificateUpdateRequest getCertificateUpdateRequest(CertificateResponse certificate) {
        CertificateUpdateRequest updateRequest = new CertificateUpdateRequest();
        updateRequest.setId(certificate.getId());
        updateRequest.setDocumentNumber(certificate.getDocumentNumber());
        updateRequest.setDocumentDate(certificate.getDocumentDate().toLocalDate());
        updateRequest.setPurpose(certificate.getPurpose());
        updateRequest.setDateFrom(certificate.getDateFrom());
        updateRequest.setIssueDate(certificate.getIssueDate());
        updateRequest.setStudentId(certificate.getStudentId());
        return updateRequest;
    }

    @PostMapping("/{id}/edit")
    public String updateCertificate(@PathVariable Long id,
                                    @ModelAttribute("certificateDto") CertificateUpdateRequest request,
                                    RedirectAttributes redirectAttributes) {
        try {
            request.setId(id);
            certificateService.update(request);
            redirectAttributes.addFlashAttribute("success", "Справка обновлена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка обновления: " + e.getMessage());
        }
        return "redirect:/certificates/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteCertificate(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            certificateService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Справка удалена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка удаления: " + e.getMessage());
        }
        return "redirect:/certificates";
    }
}
