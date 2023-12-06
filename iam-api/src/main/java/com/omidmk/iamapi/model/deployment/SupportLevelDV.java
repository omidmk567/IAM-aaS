package com.omidmk.iamapi.model.deployment;

import lombok.Getter;

import java.util.Set;

import static com.omidmk.iamapi.model.deployment.SupportLevelDV.Service.*;

@Getter
public enum SupportLevelDV {
    STANDARD("Standard", Set.of(MAILING_CHANNEL, WEEKLY_RESPONSE_TIME, NO_UPTIME_COMMITMENT)),
    PROFESSIONAL("Professional", Set.of(MAILING_CHANNEL, TICKETING_CHANNEL, DAILY_RESPONSE_TIME, UPTIME_90_PERCENT_COMMITMENT)),
    EXPERT("Expert", Set.of(MAILING_CHANNEL, TICKETING_CHANNEL, ON_CALLING_CHANNEL, DEDICATED_INSTANCE, HOURLY_RESPONSE_TIME, UPTIME_99_PERCENT_COMMITMENT));

    private final String title;

    private final Set<Service> services;

    SupportLevelDV(String title, Set<Service> services) {
        this.title = title;
        this.services = services;
    }

    public enum Service {
        MAILING_CHANNEL,
        TICKETING_CHANNEL,
        ON_CALLING_CHANNEL,
        DEDICATED_INSTANCE,
        HOURLY_RESPONSE_TIME,
        DAILY_RESPONSE_TIME,
        WEEKLY_RESPONSE_TIME,
        NO_UPTIME_COMMITMENT,
        UPTIME_90_PERCENT_COMMITMENT,
        UPTIME_99_PERCENT_COMMITMENT
    }
}
