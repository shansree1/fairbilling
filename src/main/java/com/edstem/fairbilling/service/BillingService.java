package com.edstem.fairbilling.service;

import com.edstem.fairbilling.model.BillingDetail;
import com.edstem.fairbilling.model.BillingResponse;

import java.util.List;

/**
 * Billing service.
 */
public interface BillingService {

    /**
     * Parser billing details from the file path.
     *
     * @param filePath file Path
     * @return billing details
     */
    List<BillingDetail> parseBillingDetails(String filePath);

    /**
     * Get billing time and active session count per users.
     *
     * @param billingDetails billing details
     * @return billing response
     */
    List<BillingResponse> getUserBillingTime(List<BillingDetail> billingDetails);

}
