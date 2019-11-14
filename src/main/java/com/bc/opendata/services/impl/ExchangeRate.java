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

package com.bc.opendata.services.impl;

import com.bc.opendata.services.Service;
import com.bc.opendata.servicenames.ExchangeRateNames;
import com.bc.opendata.util.Locality;
import com.bc.fxrateservice.FxRate;
import com.bc.fxrateservice.FxRateService;
import com.bc.fxrateservice.impl.DefaultFxRateService;
import com.bc.fxrateservice.util.UrlReader;
import com.bc.opendata.util.JsonFormat;
import com.bc.opendata.response.JsonResponseBuilder;
import com.bc.opendata.util.UrlContentReaderOkhttp3;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Chinomso Bassey Ikwuagwu on Oct 31, 2018 7:59:10 PM
 */
public class ExchangeRate implements Service<Map, String>, ExchangeRateNames {

    private transient static final Logger LOG = Logger.getLogger(ExchangeRate.class.getName());
    
    public static class UrlReaderImpl implements UrlReader{
        @Override
        public String read(String url) throws IOException {
            return new UrlContentReaderOkhttp3().request(url);
        }
    }

    private final FxRateService fxRateService;
    
    public ExchangeRate() {
        this(TimeUnit.HOURS.toMillis(6));
    }
    
    public ExchangeRate(long updateIntervalMillis) {
        this(new UrlReaderImpl(), updateIntervalMillis);
    }
    public ExchangeRate(UrlReader urlReader, long updateIntervalMillis) {
        this.fxRateService = new DefaultFxRateService(urlReader, updateIntervalMillis);
    }

    @Override
    public String request(Map parameters) {
        
        final String baseCurrency = parameters.get(PROPERTY_BASE_CURRENCY).toString();
        
        final Set<String> otherCurrencies = this.getOtherCurrencies(parameters);
        
        final Map<String, Object> rates = new TreeMap<>();
        
        final FxRate [] fxRates = this.fxRateService.getRates(baseCurrency, otherCurrencies.toArray(new String[0]));
        
        for(FxRate fxRate : fxRates) {
            
            if(FxRate.NONE.equals(fxRate)) {
                continue;
            }
            
            final float rateValue = fxRate.getRateOrDefault(-1.0f);
            if(rateValue == -1.0f) {
                continue;
            }

            rates.put(fxRate.getToCode(), rateValue);
        }

        final String data = rates.isEmpty() ? EMPTY_JSON : new JsonFormat(false, true, "").toJSONString(rates);
        
        final String result = new JsonResponseBuilder()
                .withData(data)
                .withMetadataEntry(PROPERTY_ICON_LINK, "http://www.looseboxes.com/idisc/images/exchagerates_icon.png")
                .withServiceName(SERVICE_NAME_BCFXRATESERVICE)
                .withRequestParameters(parameters)
                .build();
        
        if(LOG.isLoggable(Level.FINE)) {
            LOG.log(Level.FINE, "Base rate: {0}, other rates: {1}\nResponse: {2}", 
                    new Object[]{baseCurrency, otherCurrencies, result});
        }
        
        return result;
    }
    
    public Set<String> getOtherCurrencies(Map properties) {
        
        final String baseCurrency = properties.get(PROPERTY_BASE_CURRENCY).toString();
        
        final Object others = properties.get(PROPERTY_OTHER_CURRENCIES);
        
        final Set<String> otherCurrencies;
        if(others == null) {
            final Set<String> currencyCodes = new Locality().getCurrencyCodes();
            final Set<String> set = new HashSet(currencyCodes);
            set.remove(baseCurrency);
            otherCurrencies = set;
        }else{
            otherCurrencies = this.toStringSet(others);
        }
        
        return otherCurrencies;
    }

    public Set<String> toStringSet(Object val) {
        final Collection c;
        if(val instanceof Collection) {
            c = (Collection)val;
        }else if(val instanceof Object[]) {
            c = Arrays.asList((Object[])val);
        }else{
            final String [] parts = val.toString().split(",");
            c = Arrays.asList(parts);
        }
        final Set<String> result = new TreeSet<>();
        for(Object e : c) {
            if(e != null) {
                c.add(e.toString());
            }
        }
        return result;
    }
}
