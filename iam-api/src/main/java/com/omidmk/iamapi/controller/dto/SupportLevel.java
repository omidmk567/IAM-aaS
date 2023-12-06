package com.omidmk.iamapi.controller.dto;

import com.omidmk.iamapi.model.deployment.SupportLevelDV;

import java.util.Set;

public record SupportLevel(String title, Set<SupportLevelDV.Service> services) {
}
