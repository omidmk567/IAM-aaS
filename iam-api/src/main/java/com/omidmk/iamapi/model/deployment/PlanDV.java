package com.omidmk.iamapi.model.deployment;

import lombok.Getter;

@Getter
public enum PlanDV {
    BEGINNER("plan.beginner", 100, 5, 2, 5, SupportLevelDV.STANDARD, 7_000L),
    NORMAL("plan.normal", 1000, 8, 5, 8, SupportLevelDV.PROFESSIONAL, 10_000L),
    ADVANCED("plan.advanced", 10_000, 15, 10, 15, SupportLevelDV.PROFESSIONAL, 15_000L),
    PROFESSIONAL("plan.professional", 100_000, 20, 15, 20, SupportLevelDV.EXPERT, 24_000L),
    IMAGINARY("plan.imaginary", 1_000_000, 50, 20, 50, SupportLevelDV.EXPERT, 39_000L),
    ;

    private final String name;
    private final Integer usersCount;
    private final Integer clientsCount;
    private final Integer groupsCount;
    private final Integer rolesCount;
    private final SupportLevelDV defaultSupportLevel;
    private final Long costPerHour;

    PlanDV(String name, Integer usersCount, Integer clientsCount, Integer groupsCount, Integer rolesCount, SupportLevelDV defaultSupportLevel, Long costPerHour) {
        this.name = name;
        this.usersCount = usersCount;
        this.clientsCount = clientsCount;
        this.groupsCount = groupsCount;
        this.rolesCount = rolesCount;
        this.costPerHour = costPerHour;
        this.defaultSupportLevel = defaultSupportLevel;
    }
}
