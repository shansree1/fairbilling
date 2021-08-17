package com.edstem.fairbilling;

import com.edstem.fairbilling.model.BillingDetail;
import com.edstem.fairbilling.service.BillingService;
import com.edstem.fairbilling.service.BillingServiceImpl;

import java.util.List;

/**
 * Application main runner.
 */
public class BillingServiceApplication {

    /**
     * Runner for the Billing respone.
     *
     * @param args file path
     */
    public static void main(String[] args) {
        BillingService billingService = new BillingServiceImpl();
        List<BillingDetail> billingDetails = billingService.parseBillingDetails(args[0]);
        var billingTime = billingService.getUserBillingTime(billingDetails);
        billingTime.forEach(System.out::println);
    }
}
