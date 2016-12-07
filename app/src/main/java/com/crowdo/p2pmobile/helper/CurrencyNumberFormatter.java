package com.crowdo.p2pmobile.helper;


import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * Created by cwdsg05 on 5/12/16.
 */

public class CurrencyNumberFormatter {

    private static final String IDR = "IDR";

    public static String formatCurrency(String currencyStr, String symbol, double amount, boolean wantSymbol){

        String output = "";
        NumberFormat nf;
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();

        switch(currencyStr){
            case IDR:
                nf = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
                nf.setMaximumFractionDigits(0);
                if(wantSymbol) {
                    dfs.setCurrencySymbol("Rp ");
                }else{
                    dfs.setCurrencySymbol("");
                }
                dfs.setGroupingSeparator(',');
                dfs.setMonetaryDecimalSeparator('.');
                ((DecimalFormat) nf).setDecimalFormatSymbols(dfs);
                output = nf.format(amount);
        }

        //always removes trailing zeros and decimal point if empty
        return output.indexOf(".") < 0 ? output : output.replaceAll("0*$", "").replaceAll("\\.$", "");
    }
}
