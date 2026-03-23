package com.trialtask.inbank.controller;

import com.trialtask.inbank.dto.request.LoanDecisionRequest;
import com.trialtask.inbank.dto.response.LoanDecisionResponse;
import com.trialtask.inbank.mapper.LoanDecisionMapper;
import com.trialtask.inbank.service.LoanDecisionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loan")
public class LoanDecisionController {

    private final LoanDecisionService loanDecisionService;
    private final LoanDecisionMapper loanDecisionMapper;

    public LoanDecisionController(
            LoanDecisionService loanDecisionService,
            LoanDecisionMapper loanDecisionMapper
    ) {
        this.loanDecisionService = loanDecisionService;
        this.loanDecisionMapper = loanDecisionMapper;
    }

    @PostMapping("/decision")
    public LoanDecisionResponse decide(@Valid @RequestBody LoanDecisionRequest request) {
        return loanDecisionMapper.toResponse(loanDecisionService.decide(request));
    }
}
