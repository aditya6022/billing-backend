package com.vivek.billing.service.impl;

import com.vivek.billing.entity.Invoice;
import com.vivek.billing.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendInvoiceEmail(
            Invoice invoice,
            byte[] pdfBytes
    ) {

        try {

            MimeMessage message =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            true,
                            "UTF-8"
                    );

            // CUSTOMER EMAIL
            helper.setTo(
                    invoice.getCustomerEmail()
            );

            // SUBJECT
            helper.setSubject(
                    "आपका बिल - Vivek Traders"
            );

            // EMAIL BODY
            helper.setText(

                    "प्रिय ग्राहक,\n\n" +

                            "Vivek Traders की तरफ से आपका धन्यवाद।\n\n" +

                            "आपकी खरीदारी का बिल इस ईमेल के साथ PDF के रूप में भेज दिया गया है।\n\n" +

                            "Invoice Number : "
                            + invoice.getInvoiceNumber() +

                            "\nकुल राशि : ₹ "
                            + invoice.getTotalAmount() +

                            "\n\nकृपया भविष्य के उपयोग हेतु इस बिल को सुरक्षित रखें।" +

                            "\n\nकिसी भी सहायता या जानकारी के लिए हमसे संपर्क करें।" +

                            "\n\nधन्यवाद," +

                            "\nVivek Traders"
            );

            // PDF ATTACHMENT
            helper.addAttachment(
                    invoice.getInvoiceNumber() + ".pdf",
                    new ByteArrayResource(pdfBytes)
            );

            // SEND MAIL
            mailSender.send(message);

            System.out.println(
                    "Invoice email sent successfully"
            );

        } catch (MessagingException e) {

            throw new RuntimeException(e);
        }
    }
}