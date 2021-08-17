package com.edstem.fairbilling.model;

/**
 * Billing response.
 */
public class BillingResponse {

    /**
     * user Id.
     */
    private String userId;

    /**
     * Session count - The maximum count of start or end sessions.
     */
    private int sessionCount;

    /**
     * duration of user in seconds.
     */
    private long duration;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getSessionCount() {
        return sessionCount;
    }

    public void setSessionCount(int sessionCount) {
        this.sessionCount = sessionCount;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public BillingResponse(String userId, int sessionCount, long duration) {
        this.userId = userId;
        this.sessionCount = sessionCount;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "userId='" + userId + '\'' + ", sessionCount=" + sessionCount + ", duration=" + duration;
    }
}
