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

import com.bc.opendata.TestBase;
import java.util.Currency;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Josh
 */
public class LocalityTest extends TestBase {
    
    private final Locality instance = new Locality();
    public LocalityTest() { }

    /**
     * Test of getCurrencyCodes method, of class Locality.
     */
    @Test
    public void testGetCurrencyCodes() {
        System.out.println("getCurrencyCodes");
        Set<String> result = instance.getCurrencyCodes();
        Assert.assertNotNull("No currency codes were returned", result);
        Assert.assertFalse("No currency codes were returned", result.isEmpty());
        System.out.println("Currency codes: " + result);
    }

    /**
     * Test of getLocales method, of class Locality.
     */
    @Test
    public void testGetLocales() {
        System.out.println("getLocales");
        Locale[] result = instance.getLocales();
        Assert.assertNotNull("No locales were returned", result);
        Assert.assertFalse("No locales were returned", result.length == 0);
        final Set displayNames = new TreeSet();
        for(Locale locale : result) {
            displayNames.add(locale.getDisplayName());
        }
        System.out.println("Locales: " + displayNames);
    }

    /**
     * Test of getCurrencyOrDefault method, of class Locality.
     */
    @Test
    public void testGetCurrencyOrDefault() {
        System.out.println("getCurrencyOrDefault");
        Locale defaultLocale = Locale.getDefault();
        printLocale(instance, defaultLocale);
        
        final Locale [] locales = instance.getLocales();
        
        for(Locale locale : locales) {
            printLocale(instance, locale);
        }
    }
    
    private void printLocale(Locality locality, Locale locale) {
        Currency result = locality.getCurrencyOrDefault(locale, null);
        System.out.println("\nLocale: " + locale.getDisplayName() + ", currency: " + (result==null?null:locality.getDisplayName(result)));
        System.out.println("Locale.country: " + locale.getCountry() + ", Locale.displayCountry: " + locale.getDisplayCountry());
    }
}
