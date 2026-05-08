package com.vivek.billing.controller;

import com.vivek.billing.entity.Invoice;
import com.vivek.billing.service.EmailService;
import com.vivek.billing.service.InvoiceService;
import com.vivek.billing.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invoice")
@CrossOrigin("*")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private EmailService emailService;

    // CREATE INVOICE
    @PostMapping
    public Invoice createInvoice(
            @RequestBody Invoice invoice
    ) {

        // SAVE INVOICE
        Invoice savedInvoice =
                invoiceService.createInvoice(invoice);

        // GENERATE PDF
        byte[] pdfBytes =
                pdfService.generatePdf(savedInvoice);

        // SEND EMAIL ONLY IF EMAIL EXISTS
        if (savedInvoice.getCustomerEmail() != null
                &&
                !savedInvoice.getCustomerEmail().trim().isEmpty()) {

            try {

                emailService.sendInvoiceEmail(
                        savedInvoice,
                        pdfBytes
                );

                System.out.println(
                        "Invoice email sent successfully"
                );

            } catch (Exception e) {

                // IMPORTANT:
                // EVEN IF EMAIL FAILS,
                // BILL GENERATION SHOULD CONTINUE

                System.out.println(
                        "Email sending failed : "
                                + e.getMessage()
                );
            }
        }

        // RETURN SAVED INVOICE
        return savedInvoice;
    }

    // DOWNLOAD PDF
    @GetMapping("/pdf/{id}")
    public ResponseEntity<byte[]> generatePdf(
            @PathVariable Long id
    ) {

        Invoice invoice =
                invoiceService.getById(id);

        byte[] pdf =
                pdfService.generatePdf(invoice);

        return ResponseEntity.ok()
                .header(
                        "Content-Disposition",
                        "attachment; filename=invoice.pdf"
                )
                .body(pdf);
    }
}