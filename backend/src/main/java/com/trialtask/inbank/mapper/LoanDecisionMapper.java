package com.trialtask.inbank.mapper;

import com.trialtask.inbank.domain.LoanDecisionResult;
import com.trialtask.inbank.dto.response.LoanDecisionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoanDecisionMapper {

    @Mapping(target = "decision", expression = "java(result.decision().name())")
    LoanDecisionResponse toResponse(LoanDecisionResult result);
}
