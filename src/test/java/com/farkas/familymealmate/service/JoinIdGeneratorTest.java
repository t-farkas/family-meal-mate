package com.farkas.familymealmate.service;

import com.farkas.familymealmate.repository.HouseholdRepository;
import com.farkas.familymealmate.util.JoinIdGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JoinIdGeneratorTest {

    @Mock
    HouseholdRepository householdRepository;

    @InjectMocks
    JoinIdGenerator generator;

    @Test
    void generatesValidJoinId() {

        when(householdRepository.existsByJoinId(anyString())).thenReturn(false);

        String joinId = generator.generateUniqueJoinId();

        assertThat(joinId).isNotNull();
        assertThat(joinId).hasSize(8);
        assertThat(joinId).matches("[A-Z0-9]+");
    }

    @Test
    void retriesWhenJoinIdAlreadyExists() {
        when(householdRepository.existsByJoinId(anyString()))
                .thenReturn(true, false);

        String joinId = generator.generateUniqueJoinId();

        assertThat(joinId).isNotNull();
        verify(householdRepository, atLeast(2)).existsByJoinId(anyString());
    }

}
