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
import com.bc.opendata.servicenames.LocationNames;
import com.bc.opendata.util.ApiKey;
import com.bc.opendata.response.JsonResponseBuilder;
import com.bc.opendata.util.UrlContentReader;
import com.bc.opendata.util.UrlContentReaderOkhttp3;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Chinomso Bassey Ikwuagwu on Nov 4, 2018 11:53:25 PM
 */
public class Ipstack implements Service<Map, String>, LocationNames, Serializable {

    private transient static final Logger LOG = Logger.getLogger(Ipstack.class.getName());

    private final UrlContentReader urlReader;

    public Ipstack() {
        this(new UrlContentReaderOkhttp3());
    }
    
    public Ipstack(UrlContentReader urlReader) {
        this.urlReader = Objects.requireNonNull(urlReader);
    }
    
    @Override
    public String request(Map parameters) {
        
        final String ipaddress = (String)this.getRequiredPropery(parameters, PROPERTY_IPADDRESS);
        
        final String apikey = new ApiKey().getOrException(ApiKey.IPSTACK);
        
        final String url = "http://api.ipstack.com/"+ipaddress+"?access_key="+apikey;
        
        final String content = this.urlReader.request(url);
        
        final String data = content == null || content.isEmpty() ? EMPTY_JSON : content;

        final String result = new JsonResponseBuilder()
                .withData(data)
                .withServiceName(SERVICE_NAME_IPSTACK)
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
