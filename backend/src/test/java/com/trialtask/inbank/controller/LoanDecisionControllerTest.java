package com.trialtask.inbank.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LoanDecisionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("should return positive decision for valid request")
    void decide_whenRequestIsValid_shouldReturnPositiveDecision() throws Exception {
        String requestBody = """
                {
                  "personalCode": "49002010976",
                  "loanAmount": 4000,
                  "loanPeriodMonths": 24
                }
                """;

        mockMvc.perform(post("/api/loan/decision")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.decision").value("POSITIVE"))
                .andExpect(jsonPath("$.approvedAmount").value(2400))
                .andExpect(jsonPath("$.approvedPeriodMonths").value(24))
                .andExpect(jsonPath("$.requestedAmount").value(4000))
                .andExpect(jsonPath("$.requestedPeriodMonths").value(24));
    }

    @Test
    @DisplayName("should return bad request for unknown personal code")
    void decide_whenPersonalCodeIsUnknown_shouldReturnBadRequest() throws Exception {
        String requestBody = """
                {
                  "personalCode": "12345678901",
                  "loanAmount": 4000,
                  "loanPeriodMonths": 24
                }
                """;

        mockMvc.perform(post("/api/loan/decision")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.detail").value("Unknown personal code: 12345678901"));
    }

    @Test
    @DisplayName("should return bad request for invalid input")
    void decide_whenRequestIsInvalid_shouldReturnBadRequest() throws Exception {
        String requestBody = """
                {
                  "personalCode": "",
                  "loanAmount": 1000,
                  "loanPeriodMonths": 6
                }
                """;

        mockMvc.perform(post("/api/loan/decision")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.detail").value("Validation failed."));
    }
}
