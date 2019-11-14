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

package com.bc.opendata.services;

import com.bc.opendata.StandardDataType;
import com.bc.opendata.servicenames.WeatherNames;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 * @author Chinomso Bassey Ikwuagwu on Nov 1, 2018 4:56:13 PM
 */
public class WeatherTest extends ServiceTest {

    @Test
    public void testWeather() {
        
        final StandardDataType dataType = StandardDataType.WEATHER;
        
//        final Map properties = Collections.singletonMap(WeatherNames.PROPERTY_LOCATION, "Jaji, Kaduna, Nigeria");
        final Map properties = new HashMap();
        properties.put(WeatherNames.PROPERTY_LATITUDE, 10.82603);
        properties.put(WeatherNames.PROPERTY_LONGITUDE, 7.56912);
        
        final GetKey getKeyFromServiceName = new GetKey() {
            @Override
            public String apply(String serviceName) {
                return WeatherNames.SERVICE_NAME_FORECASTIO.equals(serviceName) ? 
                "$.data.currently.windGust" : 
                WeatherNames.SERVICE_NAME_YAHOO.equals(serviceName) ? 
                "$.data.item.forecasts[0].low" : null;
            }
        };
        
        this.test(dataType, properties, getKeyFromServiceName);
    }
    
}
