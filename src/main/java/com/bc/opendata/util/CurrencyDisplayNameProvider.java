/*
 * Copyright 2018 NUROX Ltd.
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

import java.util.Currency;
import java.util.Locale;
import java.util.Objects;

/**
 * @author Chinomso Bassey Ikwuagwu on Apr 10, 2018 12:00:17 PM
 */
public class CurrencyDisplayNameProvider {

    private final Locale locale;
    
    public CurrencyDisplayNameProvider() {
        this(Locale.getDefault(Locale.Category.DISPLAY));
    }
    
    public CurrencyDisplayNameProvider(Locale locale) {
        this.locale = Objects.requireNonNull(locale);
    }

    public String apply(Currency currency) {
        try{
            return currency.getDisplayName(locale);
        }catch(RuntimeException java6AndBelowWillFailHere) {
            return currency.toString();
        }
    }
}
