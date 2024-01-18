package com.tangerine.virtualaccount.enums;

import lombok.Getter;

@Getter
public enum ProductType {

    EDUCATION_PLAN("EDP"), FAMILY_WELFARE_PLAN("FWP"), PROTECTION_PLAN("PRP"), RETIREMENT_SAVINGS_PLAN("RTS"), SAVINGS_PLAN("SAP"), SAVINGS_PLUS_PLAN("SPP"), TANG_BETTA_PLAN("TBP"), TANG_FLEX_PLAN("TFP"), TANG_VIP_PLAN("TVP"), TRIPLE_PLAN("TPP");


    ProductType(String product_Type) {
        this.product_type = product_Type;
    }

    private final String product_type;


    public String value() {
        return product_type;
    }
}
