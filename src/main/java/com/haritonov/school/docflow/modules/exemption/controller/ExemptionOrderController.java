package com.haritonov.school.docflow.modules.exemption.controller;

import com.haritonov.school.docflow.modules.exemption.dto.ExemptionOrderCreateRequest;
import com.haritonov.school.docflow.modules.exemption.dto.ExemptionOrderFilterDto;
import com.haritonov.school.docflow.modules.exemption.dto.ExemptionOrderResponse;
import com.haritonov.school.docflow.modules.exemption.dto.ExemptionOrderUpdateRequest;
import com.haritonov.school.docflow.modules.exemption.service.ExemptionOrderService;
import com.haritonov.school.docflow.modules.document.model.enums.DocumentPrefix;
import com.haritonov.school.docflow.modules.document.service.DocumentNumberGeneratedService;
import com.haritonov.school.docflow.modules.employee.service.EmployeeService;
import com.haritonov.school.docflow.modules.printdocuments.service.DocumentTemplateService;
import com.haritonov.school.docflow.modules.student.service.StudentService;
import com.haritonov.school.docflow.modules.user.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
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
@RequestMapping("/exemption-orders")
@RequiredArgsConstructor
public class ExemptionOrderController {

    private final ExemptionOrderService exemptionOrderService;
    private final StudentService studentService;
    private final EmployeeService employeeService;
    private final CurrentUserService currentUserService;
    private final DocumentNumberGeneratedService documentNumberGeneratedService;
    private final DocumentTemplateService documentTemplateService;

    @GetMapping
    public String listExemptionOrders(@ModelAttribute ExemptionOrderFilterDto filter, Model model) {
        model.addAttribute("exemptionOrders", exemptionOrderService.getAllWithFilter(filter));
        model.addAttribute("filter", filter);
        model.addAttribute("username", currentUserService.getCurrentEmployeeFullName());
        return "exemption-orders/list";
    }

    @GetMapping("/{id}")
    public String viewExemptionOrder(@PathVariable Long id, Model model) {
        model.addAttribute("exemptionOrder", exemptionOrderService.getById(id));
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

        model.addAttribute("exemptionDto", request);
        model.addAttribute("students", studentService.getAll());
        model.addAttribute("username", currentUserService.getCurrentEmployeeFullName());
        return "exemption-orders/new";
    }

    @PostMapping("/new")
    public String createExemptionOrder(@ModelAttribute("exemptionDto") ExemptionOrderCreateRequest request,
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
            Long id = exemptionOrderService.create(request);
            redirectAttributes.addFlashAttribute("success", "Приказ об освобождении успешно создан");
            return "redirect:/exemption-orders/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка создания приказа: " + e.getMessage());
            return "redirect:/exemption-orders/new";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            ExemptionOrderResponse exemption = exemptionOrderService.getById(id);
            ExemptionOrderUpdateRequest updateRequest = new ExemptionOrderUpdateRequest();
            updateRequest.setId(exemption.getId());
            updateRequest.setDocumentNumber(exemption.getDocumentNumber());
            updateRequest.setDocumentDate(exemption.getDocumentDate().toLocalDate());
            updateRequest.setPurpose(exemption.getPurpose());
            updateRequest.setDateFrom(exemption.getDateFrom());
            updateRequest.setIssueDate(exemption.getIssueDate());
            updateRequest.setStudentId(exemption.getStudentId());

            model.addAttribute("exemptionDto", updateRequest);
            model.addAttribute("students", studentService.getAll());
            model.addAttribute("username", currentUserService.getCurrentEmployeeFullName());
            return "exemption-orders/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Приказ не найден");
            return "redirect:/exemption-orders";
        }
    }

    @PostMapping("/{id}/edit")
    public String updateExemptionOrder(@PathVariable Long id,
                                       @ModelAttribute("exemptionDto") ExemptionOrderUpdateRequest request,
                                       RedirectAttributes redirectAttributes) {
        try {
            request.setId(id);
            exemptionOrderService.update(request);
            redirectAttributes.addFlashAttribute("success", "Приказ об освобождении обновлён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка обновления: " + e.getMessage());
        }
        return "redirect:/exemption-orders/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteExemptionOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            exemptionOrderService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Приказ об освобождении удалён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка удаления: " + e.getMessage());
        }
        return "redirect:/exemption-orders";
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadExemptionOrder(@PathVariable Long id) {
        try {
            ExemptionOrderResponse exemption = exemptionOrderService.getById(id);
            byte[] document = documentTemplateService.generateExemptionOrder(exemption);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "exemption_order_" + id + ".docx");

            return new ResponseEntity<>(document, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}