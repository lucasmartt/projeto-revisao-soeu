package com.amarelinha;

import java.text.Format;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {
    private NumberFormat currencyFormat;

    public CurrencyFormatter(String language, String country) {
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale(language, country));
    }

    public String format(double amount) {
        return currencyFormat.format(amount);
    }
}
