package com.omidmk.iamapi.model.deployment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlanDV {
    BEGINNER("plan.beginner", 100, 5, 2, 5, SupportLevelDV.STANDARD, 7L),
    NORMAL("plan.normal", 1000, 8, 5, 8, SupportLevelDV.PROFESSIONAL, 10L),
    ADVANCED("plan.advanced", 10_000, 15, 10, 15, SupportLevelDV.PROFESSIONAL, 15L),
    PROFESSIONAL("plan.professional", 100_000, 20, 15, 20, SupportLevelDV.EXPERT, 24L),
    IMAGINARY("plan.imaginary", 1_000_000, 50, 20, 50, SupportLevelDV.EXPERT, 39L),
    ;

    private final String title;
    private final Integer usersCount;
    private final Integer clientsCount;
    private final Integer groupsCount;
    private final Integer rolesCount;
    private final SupportLevelDV defaultSupportLevel;
    private final Long costPerHour;
}
