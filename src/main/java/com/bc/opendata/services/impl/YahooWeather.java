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
import com.bc.opendata.util.YahooWeatherChannelConverter;
import com.bc.opendata.util.JsonFormat;
import com.bc.opendata.response.JsonResponseBuilder;
import com.github.fedy2.weather.YahooWeatherService;
import com.github.fedy2.weather.data.Channel;
import com.github.fedy2.weather.data.unit.DegreeUnit;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;

/**
 * @author Chinomso Bassey Ikwuagwu on Nov 1, 2018 11:00:32 AM
 */
public class YahooWeather implements Service<Map, String>, WeatherNames {

    private transient static final Logger LOG = Logger.getLogger(YahooWeather.class.getName());

    private final YahooWeatherService service;
    
    public YahooWeather() throws JAXBException { 
        this.service = new YahooWeatherService();
    }

    @Override
    public String request(Map parameters) {
        
        final Object woeid = parameters.get(PROPERTY_WOEID);
        
        Channel channel;
        try{
            if(woeid != null) {
                channel = service.getForecast(woeid.toString(), DegreeUnit.CELSIUS);
            }else{
                final String location = this.getRequiredPropery(parameters, PROPERTY_LOCATION).toString();
                final YahooWeatherService.LimitDeclaration ld = service.getForecastForLocation(location, DegreeUnit.CELSIUS);
                final List<Channel> channelList = ld.first(1);
                channel = channelList == null || channelList.isEmpty() ? null : channelList.get(0);
            }
        }catch(IOException | JAXBException e) {
            LOG.log(Level.WARNING, "Failed to retrieve current weather from service: " + service, e);
            channel = null;
        }
        
        final JsonFormat jsonFormat = new JsonFormat(false, true, "");
        
        final Map dataMap = channel == null ? null : new YahooWeatherChannelConverter().toMap(channel);
        
        final String data = dataMap == null ? EMPTY_JSON : jsonFormat.toJSONString(dataMap);
        
        final String result = new JsonResponseBuilder()
                .withData(data)
                .withMetadataEntry(PROPERTY_ICON_LINK, "http://www.looseboxes.com/idisc/images/weather_icon.png")
                .withServiceName(SERVICE_NAME_YAHOO)
                .withRequestParameters(parameters)
                .build();
        
        if(LOG.isLoggable(Level.FINE)) {
            LOG.log(Level.FINE, "Properties: {0}\nResponse {1}", new Object[]{parameters, result});
        }
        return result;
    }
    
    public Object getRequiredPropery(Map properties, String name) {
        final Object value = properties.get(name);
        return Objects.requireNonNull(value, "Required property: " + name + " not found");
    }
}
