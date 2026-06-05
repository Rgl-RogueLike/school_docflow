package com.haritonov.school.docflow.modules.certificate.controller;

import com.haritonov.school.docflow.modules.certificate.dto.*;
import com.haritonov.school.docflow.modules.certificate.service.CertificateService;
import com.haritonov.school.docflow.modules.document.model.enums.DocumentPrefix;
import com.haritonov.school.docflow.modules.document.service.DocumentNumberGeneratedService;
import com.haritonov.school.docflow.modules.printdocuments.service.DocumentTemplateService;
import com.haritonov.school.docflow.modules.student.service.StudentService;
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
@RequestMapping("/certificates")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;
    private final StudentService studentService;
    private final CurrentUserService currentUserService;
    private final DocumentNumberGeneratedService documentNumberGeneratedService;
    private final DocumentTemplateService documentTemplateService;

    @GetMapping
    public String listCertificates(@ModelAttribute CertificateFilterDto filter, Model model) {
        model.addAttribute("certificates", certificateService.getAllWithFilter(filter));
        model.addAttribute("filter", filter);
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
        // Предзаполним учебный год (текущий год - следующий год)
        int year = LocalDate.now().getYear();
        request.setAcademicYear(year + "-" + (year + 1));
        String generatedNumber = documentNumberGeneratedService.generateNumberWithFullYear(
                DocumentPrefix.CERTIFICATE, // нужно добавить префикс CERTIFICATE в DocumentPrefix
                LocalDate.now()
        );
        request.setDocumentNumber(generatedNumber);
        model.addAttribute("certificateDto", request);
        model.addAttribute("students", studentService.getAll());
        model.addAttribute("username", currentUserService.getCurrentEmployeeFullName());
        return "certificates/new";
    }

    @PostMapping("/new")
    public String createCertificate(@ModelAttribute("certificateDto") CertificateCreateRequest request,
                                    RedirectAttributes redirectAttributes) {
        try {
            request.setCreatorId(currentUserService.getCurrentEmployeeId());
            Long id = certificateService.create(request);
            redirectAttributes.addFlashAttribute("success", "Справка об обучении успешно создана");
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
            CertificateUpdateRequest updateRequest = new CertificateUpdateRequest();
            updateRequest.setId(certificate.getId());
            updateRequest.setDocumentNumber(certificate.getDocumentNumber());
            updateRequest.setDocumentDate(certificate.getDocumentDate().toLocalDate());
            updateRequest.setAcademicYear(certificate.getAcademicYear());
            updateRequest.setStudentId(certificate.getStudentId());
            model.addAttribute("certificateDto", updateRequest);
            model.addAttribute("students", studentService.getAll());
            model.addAttribute("username", currentUserService.getCurrentEmployeeFullName());
            return "certificates/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Справка не найдена");
            return "redirect:/certificates";
        }
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

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadCertificate(@PathVariable Long id) {
        try {
            CertificateResponse certificate = certificateService.getById(id);
            byte[] document = documentTemplateService.generateCertificate(certificate);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "spravka_obuchenie_" + id + ".docx");
            return new ResponseEntity<>(document, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}