package com.vivek.billing.controller;

import com.vivek.billing.entity.Invoice;
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

    // CREATE INVOICE
    @PostMapping
    public Invoice createInvoice(
            @RequestBody Invoice invoice
    ) {

        // SAVE INVOICE
        Invoice savedInvoice =
                invoiceService.createInvoice(invoice);

        // RETURN SAVED INVOICE
        return savedInvoice;
    }

    // DOWNLOAD PDF
    @GetMapping("/pdf/{id}")
    public ResponseEntity<byte[]> generatePdf(
            @PathVariable Long id
    ) {

        // GET INVOICE
        Invoice invoice =
                invoiceService.getById(id);

        // GENERATE PDF
        byte[] pdf =
                pdfService.generatePdf(invoice);

        // RETURN PDF
        return ResponseEntity.ok()
                .header(
                        "Content-Disposition",
                        "attachment; filename=invoice.pdf"
                )
                .body(pdf);
    }
}