package com.acoes.fleetmanagement.workshop.execution.infractrusture.dto;

import java.time.LocalDate;

public record CreateExecutionRequest(

        LocalDate startDate

) {
}
