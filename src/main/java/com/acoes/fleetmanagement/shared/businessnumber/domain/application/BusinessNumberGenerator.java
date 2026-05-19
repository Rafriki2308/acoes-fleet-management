package com.acoes.fleetmanagement.shared.businessnumber.domain.application;

import com.acoes.fleetmanagement.shared.businessnumber.domain.BusinessSequenceJpaEntity;
import com.acoes.fleetmanagement.shared.businessnumber.domain.model.BusinessSequenceKey;
import com.acoes.fleetmanagement.shared.businessnumber.domain.repository.BusinessSequenceJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BusinessNumberGenerator {

    private final BusinessSequenceJpaRepository repository;

    @Transactional
    public String generate(String prefix, BusinessSequenceKey sequenceKey) {
        LocalDate today = LocalDate.now();

        Integer year = today.getYear();
        Integer month = today.getMonthValue();

        BusinessSequenceJpaEntity sequence = repository
                .findBySequenceKeyAndYearAndMonth(sequenceKey, year, month)
                .orElseGet(() -> createSequence(sequenceKey, year, month));

        sequence.setCurrentValue(sequence.getCurrentValue() + 1);

        return prefix
                + "-"
                + year
                + "-"
                + String.format("%02d", month)
                + "-"
                + String.format("%06d", sequence.getCurrentValue());
    }

    private BusinessSequenceJpaEntity createSequence(
            BusinessSequenceKey sequenceKey,
            Integer year,
            Integer month
    ) {
        BusinessSequenceJpaEntity sequence = new BusinessSequenceJpaEntity();
        sequence.setSequenceKey(sequenceKey);
        sequence.setYear(year);
        sequence.setMonth(month);
        sequence.setCurrentValue(0L);

        return repository.save(sequence);
    }
}
