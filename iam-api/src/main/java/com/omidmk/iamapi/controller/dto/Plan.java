package com.omidmk.iamapi.controller.dto;

import com.omidmk.iamapi.model.deployment.PlanDV;

public record Plan(String name, String title, Integer usersCount, Integer clientsCount, Integer groupsCount, Integer rolesCount, SupportLevel defaultSupportLevel, Long costPerHour) {
    public Plan(PlanDV planDV) {
        this(
                planDV.name(),
                planDV.getTitle(),
                planDV.getUsersCount(),
                planDV.getClientsCount(),
                planDV.getGroupsCount(),
                planDV.getRolesCount(),
                new SupportLevel(planDV.getDefaultSupportLevel().getTitle(), planDV.getDefaultSupportLevel().getServices()),
                planDV.getCostPerHour()
        );
    }
}
