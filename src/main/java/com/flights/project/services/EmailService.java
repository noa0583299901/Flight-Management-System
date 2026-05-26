package com.flights.project.services;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage; 
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendBookingConfirmation(String toEmail, String passengerName, String flightNumber, String seat) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("project.ans1414@gmail.com"); 
            message.setTo(toEmail);
            message.setSubject("אישור הזמנת טיסה - Flight " + flightNumber);
            
            String emailText = "שלום " + passengerName + ",\n\n" +
                    "איזה כיף! ההזמנה שלך לטיסה " + flightNumber + " אושרה בהצלחה! 🎉\n" +
                    "מספר המושב שלך הוא: " + seat + ".\n\n" +
                    "תודה שטסת איתנו,\n" +
                    "סוכנות הנסיעות של נועה ✈️";
                    
            message.setText(emailText);
            mailSender.send(message);
            System.out.println("📬 מייל אישור הזמנה נשלח בהצלחה ל-" + toEmail);
        } catch (Exception e) {
            System.out.println("❌ שגיאה בשליחת המייל: " + e.getMessage());
        }
    }

    public void sendWaitingListNotification(String toEmail, String flightNumber) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("study0583299901@gmail.com");
            message.setTo(toEmail);
            message.setSubject("חדשות טובות! התפנה מקום בטיסה " + flightNumber);
            
            String emailText = "שלום,\n\n" +
                    "נרשמת בעבר לרשימת ההמתנה עבור טיסה " + flightNumber + ".\n" +
                    "ברגע זה התפנה מקום במטוס בעקבות ביטול! 🔔\n\n" +
                    "מהרו להיכנס לאתר ולהזמין את הכרטיס לפני שהמקום ייתפס שוב.\n\n" +
                    "בברכה,\n" +
                    "סוכנות הנסיעות של נועה ✈️";
                    
            message.setText(emailText);
            mailSender.send(message);
            System.out.println("📬 מייל עדכון רשימת המתנה נשלח בהצלחה ל-" + toEmail);
        } catch (Exception e) {
            System.out.println("❌ שגיאה בשליחת מייל רשימת המתנה: " + e.getMessage());
        }
    }
}