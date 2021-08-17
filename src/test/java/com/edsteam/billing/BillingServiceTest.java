package com.edsteam.billing;

import com.edstem.fairbilling.model.BillingDetail;
import com.edstem.fairbilling.model.BillingResponse;
import com.edstem.fairbilling.service.BillingService;
import com.edstem.fairbilling.service.BillingServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BillingServiceTest {

    private BillingService billingService;

    @Before
    public void setUp() {
        billingService = new BillingServiceImpl();
    }

    /**
     * Testing with a start and end pair exists.
     */
    @Test
    public void getUserBillingTimeTest() {
        List<BillingDetail> billingDetails = List.of(new BillingDetail(LocalTime.of(10, 15, 0), "TESTUSER1", "START"),
                new BillingDetail(LocalTime.of(10, 15, 5), "TESTUSER1", "END"),
                new BillingDetail(LocalTime.of(10, 15, 0), "TESTUSER2", "START"),
                new BillingDetail(LocalTime.of(10, 15, 15), "TESTUSER2", "END"));

        Map<String, BillingResponse> expectedResponses =
                Map.of("TESTUSER1", new BillingResponse("TESTUSER1", 1, 5),
                        "TESTUSER2", new BillingResponse("TESTUSER2", 1, 15));
        List<BillingResponse> userBillingTime = billingService.getUserBillingTime(billingDetails);
        userBillingTime.forEach(billingResponse -> {
            assertEquals(expectedResponses.get(billingResponse.getUserId()).getDuration(), billingResponse.getDuration());
            assertEquals(expectedResponses.get(billingResponse.getUserId()).getSessionCount(), billingResponse.getSessionCount());
        });
    }

    /**
     * Testing with a start only exists for a user.
     */
    @Test
    public void getUserBillingTimeTest1() {
        List<BillingDetail> billingDetails = List.of(new BillingDetail(LocalTime.of(10, 15, 0), "TESTUSER1", "START"),
                new BillingDetail(LocalTime.of(10, 15, 20), "TESTUSER1", "END"),
                new BillingDetail(LocalTime.of(10, 15, 0), "TESTUSER2", "START"),
                new BillingDetail(LocalTime.of(10, 15, 15), "TESTUSER2", "START"));

        Map<String, BillingResponse> expectedResponses =
                Map.of("TESTUSER1", new BillingResponse("TESTUSER1", 1, 20),
                        "TESTUSER2", new BillingResponse("TESTUSER2", 2, 20));
        List<BillingResponse> userBillingTime = billingService.getUserBillingTime(billingDetails);
        userBillingTime.forEach(billingResponse -> {
            assertEquals(expectedResponses.get(billingResponse.getUserId()).getDuration(), billingResponse.getDuration());
            assertEquals(expectedResponses.get(billingResponse.getUserId()).getSessionCount(), billingResponse.getSessionCount());
        });
    }

    /**
     * Testing with a End only exists for a user.
     */
    @Test
    public void getUserBillingTimeTest2() {
        List<BillingDetail> billingDetails = List.of(new BillingDetail(LocalTime.of(10, 15, 0), "TESTUSER1", "START"),
                new BillingDetail(LocalTime.of(10, 15, 20), "TESTUSER1", "END"),
                new BillingDetail(LocalTime.of(10, 15, 0), "TESTUSER2", "END"),
                new BillingDetail(LocalTime.of(10, 15, 15), "TESTUSER2", "END"));

        Map<String, BillingResponse> expectedResponses =
                Map.of("TESTUSER1", new BillingResponse("TESTUSER1", 1, 20),
                        "TESTUSER2", new BillingResponse("TESTUSER2", 2, 15));
        List<BillingResponse> userBillingTime = billingService.getUserBillingTime(billingDetails);
        userBillingTime.forEach(billingResponse -> {
            assertEquals(expectedResponses.get(billingResponse.getUserId()).getDuration(), billingResponse.getDuration());
            assertEquals(expectedResponses.get(billingResponse.getUserId()).getSessionCount(), billingResponse.getSessionCount());
        });
    }

    /**
     * Testing with a Multiple active session a user.
     */
    @Test
    public void getUserBillingTimeTest3() {
        List<BillingDetail> billingDetails = List.of(new BillingDetail(LocalTime.of(10, 15, 0), "TESTUSER1", "START"),
                new BillingDetail(LocalTime.of(10, 15, 20), "TESTUSER1", "START"),
                new BillingDetail(LocalTime.of(10, 15, 25), "TESTUSER1", "END"),
                new BillingDetail(LocalTime.of(10, 15, 30), "TESTUSER1", "END"));

        Map<String, BillingResponse> expectedResponses =
                Map.of("TESTUSER1", new BillingResponse("TESTUSER1", 2, 30));
        List<BillingResponse> userBillingTime = billingService.getUserBillingTime(billingDetails);
        userBillingTime.forEach(billingResponse -> {
            assertEquals(expectedResponses.get(billingResponse.getUserId()).getDuration(), billingResponse.getDuration());
            assertEquals(expectedResponses.get(billingResponse.getUserId()).getSessionCount(), billingResponse.getSessionCount());
        });
    }
}
