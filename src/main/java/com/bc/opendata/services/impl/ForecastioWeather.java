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
import com.bc.opendata.servicenames.WeatherNames;
import com.bc.opendata.util.ApiKey;
import com.bc.opendata.response.JsonResponseBuilder;
import com.github.dvdme.ForecastIOLib.ForecastIO;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Chinomso Bassey Ikwuagwu on Nov 1, 2018 11:05:32 AM
 */
public class ForecastioWeather implements Service<Map, String>, WeatherNames {

    private transient static final Logger LOG = Logger.getLogger(ForecastioWeather.class.getName());

    private final ForecastIO service;
    
    public ForecastioWeather() {
        final String api_key = new ApiKey().getOrException(ApiKey.FORECAST_IO);
        this.service = new ForecastIO(api_key);
        this.service.setUnits(ForecastIO.UNITS_SI);
        this.service.setLang(ForecastIO.LANG_ENGLISH);
    }
    
    @Override
    public String request(Map parameters) {

        final String excluded = "hourly,minutely";
        
        //excluded reports from the reply
        service.setExcludeURL(excluded);          

        // Sets the latitude and longitude - not optional. Will fail if not set.
        // Method should be called after the options were set
        final String latitude = this.getRequiredPropery(parameters, PROPERTY_LATITUDE).toString();
        final String longitude = this.getRequiredPropery(parameters, PROPERTY_LONGITUDE).toString();
        
        if(LOG.isLoggable(Level.FINER)) {
            LOG.log(Level.FINER,"Lat: {0}, lon: {1}, excluded: {2}", 
                    new Object[]{latitude, longitude, excluded});
        }
        
        service.getForecast(latitude, longitude);
        
        final String rawResponse = service.getRawResponse();
        
        final String data = rawResponse == null || rawResponse.isEmpty() ? EMPTY_JSON : rawResponse;
        
        final String result = new JsonResponseBuilder()
                .withData(data)
                .withMetadataEntry(PROPERTY_ICON_LINK, "http://www.looseboxes.com/idisc/images/weather_icon.png")
                .withServiceName(SERVICE_NAME_FORECASTIO)
                .withRequestParameters(parameters)
                .withMetadataEntry(PROPERTY_EXCLUDED_URLS, excluded)
                .build();
        
        if(LOG.isLoggable(Level.FINE)) {
            LOG.log(Level.FINE, "Properties: {0}\nResponse: {1}", new Object[]{parameters, result});
        }
        
        return result;
    }

    public Object getRequiredPropery(Map properties, String name) {
        final Object value = properties.get(name);
        return Objects.requireNonNull(value, "Required property: " + name + " not found");
    }
}
