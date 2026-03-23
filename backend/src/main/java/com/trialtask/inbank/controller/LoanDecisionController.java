package com.trialtask.inbank.controller;

import com.trialtask.inbank.dto.request.LoanDecisionRequest;
import com.trialtask.inbank.dto.response.LoanDecisionResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loan")
public class LoanDecisionController {

    @PostMapping("/decision")
    public LoanDecisionResponse decide(@Valid @RequestBody LoanDecisionRequest request) {

        return new LoanDecisionResponse(
                "DECLINED",
                null,
                null,
                request.loanAmount(),
                request.loanPeriodMonths()
        );
    }
}
