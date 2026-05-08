package com.vivek.billing.service;

import com.vivek.billing.entity.Invoice;

public interface EmailService {

    void sendInvoiceEmail(
            Invoice invoice,
            byte[] pdfBytes
    );
}