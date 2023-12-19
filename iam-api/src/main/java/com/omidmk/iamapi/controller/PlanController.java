package com.omidmk.iamapi.controller;

import com.omidmk.iamapi.controller.dto.Plan;
import com.omidmk.iamapi.exception.ApplicationException;
import com.omidmk.iamapi.exception.PlanNotFoundException;
import com.omidmk.iamapi.model.deployment.PlanDV;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.stream.Stream;

@RestController
@RequestMapping("/v1/plans")
public class PlanController {
    @GetMapping("")
    public Stream<Plan> getAvailablePlans() {
        return Arrays.stream(PlanDV.values()).map(Plan::new);
    }

    @GetMapping("/{name}")
    public Plan getPlan(@PathVariable String name) throws ApplicationException {
        try {
            PlanDV planDV = PlanDV.valueOf(name);
            return new Plan(planDV);
        } catch (IllegalArgumentException ex) {
            throw new PlanNotFoundException();
        }
    }
}
