package com.vivek.billing.service.impl;

import com.vivek.billing.entity.Invoice;
import com.vivek.billing.entity.Item;
import com.vivek.billing.repository.InvoiceRepository;
import com.vivek.billing.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Override
    public Invoice createInvoice(Invoice invoice) {

        // IST Time
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Kolkata"))
                .withNano(0);

        invoice.setDateTime(now);

        // Display Date
        DateTimeFormatter displayFormatter =
                DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");

        String formattedDateTime = now.format(displayFormatter);
        invoice.setFormattedDateTime(formattedDateTime);

        // Daily Count
        long count = invoiceRepository.countTodayInvoices() + 1;

        String serial = String.format("%02d", count);

        // 🔥 CHANGED HERE (yyyy → yy)
        DateTimeFormatter dateFormatter =
                DateTimeFormatter.ofPattern("ddMMyy");

        String datePart = now.format(dateFormatter);

        // Final Invoice Number
        String invoiceNumber = "VE" + datePart + serial;

        invoice.setInvoiceNumber(invoiceNumber);

        double totalAmount = 0;

        for (Item item : invoice.getItems()) {

            double itemTotal = item.getQuantity() * item.getPrice();

            item.setTotal(itemTotal);

            item.setInvoice(invoice);

            totalAmount += itemTotal;
        }

        invoice.setTotalAmount(totalAmount);

        return invoiceRepository.save(invoice);
    }

    @Override
    public Invoice getById(Long id) {
        return invoiceRepository.findById(id).orElseThrow();
    }
}