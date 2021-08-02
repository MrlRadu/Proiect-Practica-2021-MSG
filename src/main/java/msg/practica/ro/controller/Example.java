package msg.practica.ro.controller;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import java.io.IOException;

public class Example {
    public static void main(String[] args) throws IOException {
        Email from = new Email("rares.dan.g@gmail.com");
        String subject = "I'm replacing the subject tag";
        Email to = new Email("rares.dan.g@outlook.com");
        Content content = new Content("text/html", "I'm replacing the <strong>body tag</strong>");
        Mail mail = new Mail(from, subject, to, content);
        mail.personalization.get(0).addSubstitution("-name-", "Example User");
        mail.personalization.get(0).addSubstitution("-city-", "Denver");
        mail.setTemplateId("13b8f94f-bcae-4ec6-b752-70d6cb59f932");

        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            throw ex;
        }
    }
}