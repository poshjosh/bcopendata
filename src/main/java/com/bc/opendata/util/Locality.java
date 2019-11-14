/*
 * Copyright 2016 NUROX Ltd.
 *
 * Licensed under the NUROX Ltd Software License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.looseboxes.com/legal/licenses/software.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bc.opendata.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Currency;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Chinomso Bassey Ikwuagwu on Sep 30, 2016 8:59:54 PM
 */
public class Locality implements Serializable {
    
    private transient static final Logger LOG = Logger.getLogger(Locality.class.getName());
    
    public static final Locale NGN_ENG = new Locale("en","NG","");
    
    private final CurrencyDisplayNameProvider currencyDisplayNameProvider;
    
    public Locality() {
        this.currencyDisplayNameProvider = new CurrencyDisplayNameProvider();
    }
    
    public String getDisplayName(Currency currency) {
        return currencyDisplayNameProvider.apply(currency);
    }

    public Set<String> getCurrencyCodes() {
        Set<Currency> currencies = this.getCurrencies();
        Set<String> codes = new LinkedHashSet<>();
        for(Currency currency:currencies) {
            String code = currency.getCurrencyCode();
            if(code != null) {
                codes.add(code);
            }
        }
        return codes;
    }

    private Locale [] _locs;
    public Locale [] getLocales() {
        if(_locs == null) {
            Locale [] locs = Locale.getAvailableLocales();
            LinkedList<Locale> list = new LinkedList<>(Arrays.asList(locs));
            if(!list.contains(NGN_ENG)) {
                list.add(0, NGN_ENG);
            }
            _locs = list.toArray(new Locale[list.size()]);
        }
        return _locs;
    }

    private Set<Currency> _curr;
    public Set<Currency> getCurrencies() {
        if(_curr == null) {
            try{

                Locale [] locales = this.getLocales();

                LOG.log(Level.FINE, "Available Locales: {0}", locales==null?null:locales.length);

                _curr = new HashSet<>(locales.length);

                for(Locale locale:locales) {

                    final Currency currency = this.getCurrencyOrDefault(locale, null);
                    if(currency == null) {
                        continue;
                    }
                    if(currency.getCurrencyCode() == null) {
                        continue;
                    }
                    _curr.add(currency);
                }

                ArrayList<Currency> list = new ArrayList<>(_curr);

                Collections.sort(list, new CurrencyComparator());

                _curr = Collections.unmodifiableSet(new LinkedHashSet<>(list));

            }catch(Exception e) {
                LOG.log(Level.WARNING, null, e);
            }
        }

        return _curr;
    }

    public Currency getCurrencyOrDefault(Locale locale, Currency outputIfNone) {
        Currency output;
        try {
            output = Currency.getInstance(locale);
        }catch(Exception e) {
            // Currency.getInstance(Locale) may throw java.lang.IllegalArgumentException
//            Logx.getInstance().log(this.getClass(), e);
            output = null;
        }
        return output == null ? outputIfNone : output;
    }
}
