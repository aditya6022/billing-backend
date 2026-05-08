package com.vivek.billing.service;

import com.vivek.billing.entity.Invoice;

public interface InvoiceService {

    Invoice createInvoice(Invoice invoice);
    Invoice getById(Long id);
}