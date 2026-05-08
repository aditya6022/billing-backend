package com.vivek.billing.service;

import com.vivek.billing.entity.Invoice;

public interface PdfService {
    byte[] generatePdf(Invoice invoice);
}