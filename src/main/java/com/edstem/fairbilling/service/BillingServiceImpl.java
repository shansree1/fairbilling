package com.edstem.fairbilling.service;

import com.edstem.fairbilling.model.BillingDetail;
import com.edstem.fairbilling.model.BillingResponse;
import com.edstem.fairbilling.model.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.max;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;

/**
 * Billing service implementation.
 */
public class BillingServiceImpl implements BillingService {

    /**
     * Parser billing details from the file path.
     *
     * @param filePath file Path
     * @return billing details
     */
    @Override
    public List<BillingDetail> parseBillingDetails(final String filePath) {

        Path path = Paths.get(filePath);
        try (Stream<String> streams = Files.lines(path)) {
            return streams.map(String::toUpperCase)
                    .filter(s -> s.matches("\\d{2}:\\d{2}:\\d{2} (.*) (START|END)"))
                    .map(BillingServiceImpl::billingDetailMapper)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Get the minimum and maximum time entered in the billing log.
     *
     * @param billingDetails billing details
     * @return max and minimum time in the log
     */
    public Pair<LocalTime> getMinAndMaxTime(final List<BillingDetail> billingDetails) {
        LocalTime max = billingDetails.stream()
                .max(comparing(BillingDetail::getBillingTime))
                .map(BillingDetail::getBillingTime)
                .orElse(LocalTime.MIN);
        LocalTime min = billingDetails.stream()
                .min(comparing(BillingDetail::getBillingTime))
                .map(BillingDetail::getBillingTime)
                .orElse(LocalTime.MIN);
        return new Pair<>(min, max);
    }

    /**
     * Get billing time and active session count per users.
     *
     * @param billingDetails billing details
     * @return billing response
     */
    @Override
    public List<BillingResponse> getUserBillingTime(final List<BillingDetail> billingDetails) {
        Map<String, List<BillingDetail>> userTimeDetails = billingDetails.stream()
                .collect(groupingBy(BillingDetail::getUserName, toList()));
        Pair<LocalTime> minAndMaxTime = getMinAndMaxTime(billingDetails);
        List<BillingResponse> billingResponses = new ArrayList<>();
        userTimeDetails.forEach((user, details) -> {
            List<Pair<BillingDetail>> pairBilling = new ArrayList<>();
            int startSession = 0;
            int endSession = 0;
            Pair<BillingDetail> p = new Pair<>();
            String previousStatus = "END";
            for (BillingDetail bill : details) {
                if ("START".equals(bill.getType())) {
                    if (Objects.isNull(p.getStart())) {
                        p.setStart(bill);
                    } else if ("END".equals(previousStatus)) {
                        pairBilling.add(p);
                        p = new Pair<>();
                    }
                    startSession++;
                } else if ("END".equals(bill.getType())) {
                    p.setEnd(bill);
                    endSession++;
                }
                previousStatus = bill.getType();
            }
            if (Objects.nonNull(p.getStart()) || Objects.nonNull(p.getEnd())) {
                pairBilling.add(p);
            }

            long sum = pairBilling.stream()
                    .map(Optional::ofNullable)
                    .mapToLong(pair -> {
                        var startTime = pair.map(Pair::getStart)
                                .map(BillingDetail::getBillingTime)
                                .orElse(minAndMaxTime.getStart());
                        var endTime = pair.map(Pair::getEnd)
                                .map(BillingDetail::getBillingTime)
                                .orElse(minAndMaxTime.getEnd());
                        Duration diff = Duration.between(startTime, endTime);
                        return diff.getSeconds();
                    }).sum();
            var maxSession = max(startSession, endSession);
            billingResponses.add(new BillingResponse(user, maxSession, sum));
        });
        return billingResponses;
    }

    /**
     * Billing detail mapper from the string
     *
     * @param detailStr log detail string
     * @return billing detail
     */
    private static BillingDetail billingDetailMapper(final String detailStr) {
        String[] details = detailStr.toUpperCase().split("\\s");
        if (details.length == 3) {
            String userName = details[1];
            String type = details[2];
            try {
                LocalTime time = LocalTime.parse(details[0]);
                return new BillingDetail(time, userName, type);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
