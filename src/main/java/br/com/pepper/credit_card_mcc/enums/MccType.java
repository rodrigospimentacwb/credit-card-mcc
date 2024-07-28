package br.com.pepper.credit_card_mcc.enums;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum MccType {
    FOOD("5411", "5412"),
    MEAL("5811", "5812"),
    CASH("DEFAULT");

    private final Set<String> mccCodes;

    MccType(String... mccCodes) {
        this.mccCodes = new HashSet<>(Arrays.asList(mccCodes));
    }

    public static MccType fromMcc(String mcc) {
        for (MccType type : values()) {
            if (type.mccCodes.contains(mcc)) {
                return type;
            }
        }
        return CASH;
    }
}

