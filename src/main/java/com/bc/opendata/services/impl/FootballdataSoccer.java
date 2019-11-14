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
import com.bc.opendata.servicenames.SoccerNames;
import com.bc.opendata.util.ApiKey;
import com.bc.opendata.response.JsonResponseBuilder;
import com.bc.opendata.util.UrlContentReader;
import com.bc.opendata.util.UrlContentReaderOkhttp3;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Chinomso Bassey Ikwuagwu on Oct 31, 2018 10:09:51 PM
 */
public class FootballdataSoccer implements Service<Map, String>, SoccerNames, Serializable {

    private transient static final Logger LOG = Logger.getLogger(FootballdataSoccer.class.getName());
    
    private final UrlContentReader urlReader;

    public FootballdataSoccer() {
        this(new UrlContentReaderOkhttp3());
    }
    
    public FootballdataSoccer(UrlContentReader urlReader) {
        this.urlReader = Objects.requireNonNull(urlReader);
    }

    @Override
    public String request(Map parameters) {
        
        final String result;
        
        final String request = this.getRequiredPropery(parameters, PROPERTY_FOOTBALLDATA_LEAGUE).toString();
        
        if(FOOTBALLDATA_EPL.equals(request)) {
            result = this.requestLeagueMatches(request, parameters);
        }else{
            throw new UnsupportedOperationException("Not supported yet. Parameters: " + 
                    PROPERTY_FOOTBALLDATA_LEAGUE + " = " + request);
        }
        
        return result;
    }

    public String requestLeagueMatches(String league, Map parameters) {
        
        final DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        
        final String start = this.getRequiredDatePropery(parameters, PROPERTY_STARTDATE, fmt);
        final String end = this.getRequiredDatePropery(parameters, PROPERTY_ENDDATE, fmt);
        
        if(LOG.isLoggable(Level.FINER)) {
            LOG.log(Level.FINER, "Start: {0}, end: {1}", new Object[]{start, end});
        }
        
        final String urlString = MessageFormat.format(
                "https://api.football-data.org/v2/competitions/{0}/matches?dateFrom={1}&dateTo={2}", 
                league, start, end);
        
        LOG.log(Level.FINE, "URL: {0}", urlString);
        
        final String rawResponse = urlReader
                .addHeader("X-Auth-Token", new ApiKey().getOrException(ApiKey.FOOTBAL_DATA))
                .request(urlString);
        
        final String data = rawResponse == null || rawResponse.isEmpty() ? EMPTY_JSON : rawResponse;
        
        final String result = new JsonResponseBuilder()
                .withData(data)
                .withMetadataEntry(PROPERTY_ICON_LINK, "http://www.looseboxes.com/idisc/images/soccer_icon.png")
                .withServiceName(SERVICE_NAME_FOOTBALLDATA)
                .withRequestParameters(parameters)
                .build();
        
        if(LOG.isLoggable(Level.FINE)) {
            LOG.log(Level.FINE, "Query: {0}\nResponse: {1}", new Object[]{urlString, result});
        }
        
        return result;
    }
    
    public String getRequiredDatePropery(Map properties, String name, DateFormat fmt) {
        final String result;
        final Object oval = this.getRequiredPropery(properties, name);
        if(oval instanceof Long) {
            result = fmt.format(new Date((Long)oval)); 
        }else if(oval instanceof java.util.Date) {
            final java.util.Date date = (java.util.Date)oval;
            result = fmt.format(date);
        }else{
            result = oval.toString();
        }
        return result;
    }
    
    public Object getRequiredPropery(Map properties, String name) {
        final Object value = properties.get(name);
        return Objects.requireNonNull(value, "Required property: " + name + " not found");
    }
}
