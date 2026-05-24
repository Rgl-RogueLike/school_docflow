package com.haritonov.school.docflow.modules.certificate.controller;

import com.haritonov.school.docflow.modules.certificate.dto.ExemptionOrderCreateRequest;
import com.haritonov.school.docflow.modules.certificate.dto.ExemptionOrderFilterDto;
import com.haritonov.school.docflow.modules.certificate.dto.ExemptionOrderResponse;
import com.haritonov.school.docflow.modules.certificate.dto.ExemptionOrderUpdateRequest;
import com.haritonov.school.docflow.modules.certificate.service.ExemptionOrderService;
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
@RequestMapping("/exemption-orders")
@RequiredArgsConstructor
public class ExemptionOrderController {

    private final ExemptionOrderService certificateService;
    private final StudentService studentService;
    private final EmployeeService employeeService;
    private final CurrentUserService currentUserService;
    private final DocumentNumberGeneratedService documentNumberGeneratedService;

    @GetMapping
    public String listCertificates(@ModelAttribute ExemptionOrderFilterDto filter, Model model) {
        model.addAttribute("certificates", certificateService.getAllWithFilter(filter));
        model.addAttribute("filter", filter);
        model.addAttribute("username", currentUserService.getCurrentEmployeeFullName());
        return "exemption-orders/list";
    }

    @GetMapping("/{id}")
    public String viewCertificate(@PathVariable Long id, Model model) {
        model.addAttribute("certificate", certificateService.getById(id));
        model.addAttribute("username", currentUserService.getCurrentEmployeeFullName());
        return "exemption-orders/card";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        ExemptionOrderCreateRequest request = new ExemptionOrderCreateRequest();
        request.setDocumentDate(LocalDate.now());
        request.setDateFrom(LocalDate.now());
        request.setIssueDate(LocalDate.now());

        String generatedNumber = documentNumberGeneratedService.generateNumberWithFullYear(
                DocumentPrefix.EXEMPTION_ORDER,
                LocalDate.now()
        );
        request.setDocumentNumber(generatedNumber);

        model.addAttribute("certificateDto", request);
        model.addAttribute("students", studentService.getAll());
        model.addAttribute("username", currentUserService.getCurrentEmployeeFullName());
        return "exemption-orders/new";
    }

    @PostMapping("/new")
    public String createCertificate(@ModelAttribute("certificateDto") ExemptionOrderCreateRequest request,
                                    RedirectAttributes redirectAttributes) {
        System.out.println("=== СОЗДАНИЕ ПРИКАЗА ОБ ОСВОБОЖДЕНИИ ===");
        System.out.println("Номер: " + request.getDocumentNumber());
        System.out.println("Дата приказа: " + request.getDocumentDate());
        System.out.println("Ученик ID: " + request.getStudentId());
        System.out.println("Основание: " + request.getPurpose());
        System.out.println("Дата начала: " + request.getDateFrom());
        System.out.println("Дата окончания: " + request.getIssueDate());

        try {
            request.setCreatorId(currentUserService.getCurrentEmployeeId());

            Long id = certificateService.create(request);
            redirectAttributes.addFlashAttribute("success", "Справка успешно создана");
            return "redirect:/exemption-orders/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка создания справки: " + e.getMessage());
            return "redirect:/exemption-orders/new";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            ExemptionOrderResponse certificate = certificateService.getById(id);
            ExemptionOrderUpdateRequest updateRequest = getCertificateUpdateRequest(certificate);
            model.addAttribute("certificateDto", updateRequest);
            model.addAttribute("students", studentService.getAll());
            model.addAttribute("employees", employeeService.getAll());
            model.addAttribute("username", currentUserService.getCurrentEmployeeFullName());
            return "exemption-orders/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Справка не найдена");
            return "redirect:/exemption-orders";
        }
    }

    private static @NonNull ExemptionOrderUpdateRequest getCertificateUpdateRequest(ExemptionOrderResponse certificate) {
        ExemptionOrderUpdateRequest updateRequest = new ExemptionOrderUpdateRequest();
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
                                    @ModelAttribute("certificateDto") ExemptionOrderUpdateRequest request,
                                    RedirectAttributes redirectAttributes) {
        try {
            request.setId(id);
            certificateService.update(request);
            redirectAttributes.addFlashAttribute("success", "Справка обновлена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка обновления: " + e.getMessage());
        }
        return "redirect:/exemption-orders/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteCertificate(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            certificateService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Справка удалена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка удаления: " + e.getMessage());
        }
        return "redirect:/exemption-orders";
    }
}
