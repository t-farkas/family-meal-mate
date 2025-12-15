package com.farkas.familymealmate.util;

import com.farkas.familymealmate.repository.HouseholdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@RequiredArgsConstructor
public class JoinIdGenerator {
    private static final String ALPHANUM = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int LENGTH = 8;

    private final SecureRandom random = new SecureRandom();
    private final HouseholdRepository householdRepository;

    public String generateUniqueJoinId() {
        String joinId;
        do {
            joinId = generateRandomId();
        } while (householdRepository.existsByJoinId(joinId));
        return joinId;
    }

    private String generateRandomId() {
        StringBuilder sb = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            int index = random.nextInt(ALPHANUM.length());
            sb.append(ALPHANUM.charAt(index));
        }
        return sb.toString();
    }

}
