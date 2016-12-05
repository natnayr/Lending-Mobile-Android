package com.crowdo.p2pmobile.helper;


import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by cwdsg05 on 5/12/16.
 */

public class CurrencyNumberFormatter {

    private static final String IDR = "IDR";

    public static String formatCurrency(String currency, String symbol, double amount, boolean wantSymbol){

        String output;
        NumberFormat nf;
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();

        switch(currency){
            case IDR:
                nf = NumberFormat.getCurrencyInstance(new Locale("en", "id-ID"));
                String curSymbol = (wantSymbol) ? symbol : "";
                dfs.setCurrencySymbol(curSymbol);
                dfs.setGroupingSeparator(',');
                dfs.setMonetaryDecimalSeparator('.');
                ((DecimalFormat) nf).setDecimalFormatSymbols(dfs);
                output = nf.format(amount);
            default:
                nf = NumberFormat.getCurrencyInstance(new Locale("en", "SG"));
                output = nf.format(amount);

        }

        //always removes trailing zeros and decimal point if empty
        return output.indexOf(".") < 0 ? output : output.replaceAll("0*$", "").replaceAll("\\.$", "");
    }
}
