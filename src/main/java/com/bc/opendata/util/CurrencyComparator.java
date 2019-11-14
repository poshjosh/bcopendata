package com.bc.opendata.util;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Currency;
import java.util.Locale;
import java.util.Objects;

/**
 * @author Chinomso Bassey Ikwuagwu on Apr 10, 2018 11:41:34 AM
 */
public class CurrencyComparator implements Comparator<Currency>, Serializable {
    
    private final CurrencyDisplayNameProvider getCurrencyValue;
    
    public CurrencyComparator(){ 
        this(Locale.getDefault(Locale.Category.DISPLAY));
    }
    
    public CurrencyComparator(Locale locale){
        this(new CurrencyDisplayNameProvider(locale));
    }
    
    public CurrencyComparator(CurrencyDisplayNameProvider getCurrencyValue){ 
        this.getCurrencyValue = Objects.requireNonNull(getCurrencyValue);
    }

    @Override
    public int compare(Currency a, Currency b) {
        final String s1 = this.getCurrencyValue.apply(a);
        final String s2 = this.getCurrencyValue.apply(b);
        int output;
        if(s1 == null && s2 == null) {
            output = 0;
        }else if(s1 != null && s2 == null) {
            output = 1;
        }else if(s2 != null && s1 == null) {
            output = -1;
        }else{
            output = s1.compareTo(s2);
        }
        return output;
    }
}
