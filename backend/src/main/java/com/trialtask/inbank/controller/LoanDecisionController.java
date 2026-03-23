package com.trialtask.inbank.controller;

import com.trialtask.inbank.dto.request.LoanDecisionRequest;
import com.trialtask.inbank.dto.response.LoanDecisionResponse;
import com.trialtask.inbank.mapper.LoanDecisionMapper;
import com.trialtask.inbank.service.LoanDecisionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Loan Decision API", description = "Loan decision calculation endpoints")
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

    /**
     * Evaluates a loan application and returns the decision result.
     *
     * @param request the loan decision request containing personal code,
     *                requested amount, and loan period
     * @return loan decision response with approval status and approved offer if applicable
     */
    @Operation(summary = "Calculate loan decision based on personal code and request parameters")
    @PostMapping("/decision")
    public LoanDecisionResponse decide(@Valid @RequestBody LoanDecisionRequest request) {
        return loanDecisionMapper.toResponse(loanDecisionService.decide(request));
    }
}