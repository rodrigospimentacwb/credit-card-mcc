package br.com.pepper.credit_card_mcc.enums;

public enum TransactionStatusType {
    APPROVED("00"),
    DECLINED("51"),
    FAILED("07");

    private final String code;

    TransactionStatusType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
