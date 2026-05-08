package com.vivek.billing.repository;

import com.vivek.billing.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query("SELECT COUNT(i) FROM Invoice i WHERE DATE(i.dateTime) = CURRENT_DATE")
    long countTodayInvoices();
}