package com.bank.app.utils;

import com.bank.app.common.Currency;

import static com.bank.app.common.Currency.*;

public class CurrencyUtils {

    public static Currency convertStringToCurrency (String currency) {
        switch(currency.toLowerCase()) {
            case "swedishkrona":
                return SWEDISHKRONA;
            case "pound":
                return POUND;
            default:
                return EURO;

        }
    }
}
