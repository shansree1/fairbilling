package com.edstem.fairbilling.model;

import java.time.LocalTime;

/**
 * Billing detail stores the session details of a user.
 * @see com.edstem.fairbilling.service.BillingServiceImpl
 */
public class BillingDetail {

    /**
     * billing time.
     */
    private LocalTime billingTime;

    /**
     * user name.
     */
    private String userName;

    /**
     * session type (START/END).
     */
    private String type;

    public LocalTime getBillingTime() {
        return billingTime;
    }

    public void setBillingTime(LocalTime billingTime) {
        this.billingTime = billingTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * parameterised constructor with billing time, user name and session type.
     * @param billingTime billing time
     * @param userName user name
     * @param type session type
     */
    public BillingDetail(final LocalTime billingTime, final String userName, final String type) {
        this.billingTime = billingTime;
        this.userName = userName;
        this.type = type;
    }
}
