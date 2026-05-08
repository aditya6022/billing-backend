package com.vivek.billing.service.impl;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.vivek.billing.entity.Invoice;
import com.vivek.billing.entity.Item;
import com.vivek.billing.service.PdfService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class PdfServiceImpl implements PdfService {

    @Override
    public byte[] generatePdf(Invoice invoice) {

        try {

            String html = loadHtmlTemplate();

            // REPLACE PLACEHOLDERS

            html = html.replace(
                    "{{invoiceNumber}}",
                    invoice.getInvoiceNumber()
            );

            html = html.replace(
                    "{{date}}",
                    invoice.getFormattedDateTime()
            );

            html = html.replace(
                    "{{customerName}}",
                    invoice.getCustomerName()
            );

            html = html.replace(
                    "{{customerAddress}}",
                    invoice.getCustomerAddress()
            );

            html = html.replace(
                    "{{customerEmail}}",
                    invoice.getCustomerEmail()
            );

            html = html.replace(
                    "{{customerMobile}}",
                    invoice.getCustomerMobile()
            );

            html = html.replace(
                    "{{total}}",
                    String.format("%.2f", invoice.getTotalAmount())
            );

            // ITEMS ROWS

            StringBuilder items = new StringBuilder();

            int srNo = 1;

            for (Item item : invoice.getItems()) {

                items.append("<tr>")

                        // SR NO
                        .append("<td style='border:1px solid #d6d6d6;'>")
                        .append(srNo++)
                        .append("</td>")

                        // ITEM NAME
                        .append("<td style='border:1px solid #d6d6d6; text-align:left;'>")
                        .append(item.getItemName())
                        .append("</td>")

                        // PRICE
                        .append("<td style='border:1px solid #d6d6d6;'>")
                        .append(String.format("%.2f", item.getPrice()))
                        .append("</td>")

                        // QUANTITY
                        .append("<td style='border:1px solid #d6d6d6;'>")
                        .append(item.getQuantity())
                        .append("</td>")

                        // TOTAL
                        .append("<td style='border:1px solid #d6d6d6;'>")
                        .append(String.format("%.2f", item.getTotal()))
                        .append("</td>")

                        .append("</tr>");
            }

            html = html.replace("{{items}}", items.toString());

            // PDF GENERATION

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfRendererBuilder builder = new PdfRendererBuilder();

            // FONT
            builder.useFont(
                    () -> getClass().getResourceAsStream(
                            "/fonts/NotoSans-Regular.ttf"
                    ),
                    "NotoSans"
            );

            // HTML
            builder.withHtmlContent(html, null);

            // OUTPUT
            builder.toStream(out);

            // BUILD PDF
            builder.run();

            return out.toByteArray();

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

    private String loadHtmlTemplate() throws IOException {

        return new String(
                Files.readAllBytes(
                        Paths.get(
                                "src/main/resources/templates/invoice.html"
                        )
                )
        );
    }
}