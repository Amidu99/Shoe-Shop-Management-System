package lk.ijse.HelloShoesBE.util;

import lk.ijse.HelloShoesBE.entity.Customer;
import lk.ijse.HelloShoesBE.repo.CustomerRepo;
import lk.ijse.HelloShoesBE.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class BirthdayScheduler {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private MailService mailService;

    final static Logger logger = LoggerFactory.getLogger(BirthdayScheduler.class);

    @Scheduled(cron = "0 30 07 * * ?") // set to run at 7:30 AM every day
    public void sendBirthdayWishes() {
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();
        logger.info("Today's date: {}-{}", month, day);
        List<Customer> customers = customerRepo.findByMonthAndDay(month, day);

        if (customers.isEmpty()) {
            logger.info("No customers found with birthday on {}-{}", month, day);
        } else {
            logger.info("Found {} customers with birthday on {}-{}", customers.size(), month, day);
        }

        for (Customer customer : customers) {
            String subject = "Happy Birthday, " + customer.getCustomerName() + "!";
            String body = "Happy Birthday, " + customer.getCustomerName() + "!\n\n"
                    + "Dear " + customer.getCustomerName() + ",\n"
                    + "Wishing you a fantastic day filled with joy and happiness.\n\n"
                    + "Best wishes,\n"
                    + "Hello Shoes PVT Ltd.";
            mailService.sendBirthdayEmail(customer.getEmail(), subject, body);
            logger.info("Sent wish to "+customer.getCustomerName());
        }
    }
}