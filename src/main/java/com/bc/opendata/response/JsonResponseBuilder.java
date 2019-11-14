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

package com.bc.opendata.response;

import com.bc.opendata.servicenames.ServiceProperties;
import com.bc.opendata.util.JsonFormat;
import static com.bc.opendata.servicenames.ServiceProperties.PROPERTY_SERVICENAME;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Chinomso Bassey Ikwuagwu on Nov 15, 2018 11:21:30 AM
 */
public class JsonResponseBuilder implements ResponseBuilder<String, String>, Serializable {

    private transient static final Logger LOG = Logger.getLogger(JsonResponseBuilder.class.getName());
    
    private String data;
    
    private String metadata;
    
    private Map metadataMap;

    public JsonResponseBuilder() { }
    
    public void clear() {
        this.data = null;
        this.metadata = null;
    }

    @Override
    public ResponseBuilder<String, String> withData(String data) {
        this.data = data;
        return this;
    }

    @Override
    public ResponseBuilder<String, String> withMetadata(String metaData) {
        this.metadata = metaData;
        return this;
    }

    @Override
    public ResponseBuilder<String, String> withServiceName(String arg) {
        return this.withMetadataEntry(PROPERTY_SERVICENAME, arg);
    }

    @Override
    public ResponseBuilder<String, String> withRequestParameters(Map arg) {
        return this.withMetadataEntry(
                "request", Collections.singletonMap("parameters", arg));
    }

    @Override
    public ResponseBuilder<String, String> withMetadataEntry(Object key, Object val) {
        if(this.metadataMap == null) {
            this.metadataMap = new LinkedHashMap();
        }
        this.metadataMap.put(key, val);
        return this;
    }

    @Override
    public String build() {
        
        final String result;
        
        final boolean hasMetaMap = (this.metadataMap != null && ! this.metadataMap.isEmpty());
        
        if(this.isNullOrEmpty(metadata) && hasMetaMap) {
            metadata = new JsonFormat(false, true, "").toJSONString(this.metadataMap);
        }
        
        final boolean hasData = ! this.isNullOrEmpty(data);
        final boolean hasMeta = ! this.isNullOrEmpty(metadata);

        if( ! hasData && ! hasMeta) {
            
            result = ServiceProperties.EMPTY_JSON;
            
        }else{
            
            final StringBuilder appendTo = new StringBuilder();

            appendTo.append('{');
            if(hasMeta) {
                appendTo.append("\"metadata\":").append(metadata);
            }
            if(hasData) {
                if(hasMeta) {
                    appendTo.append(',');
                }
                appendTo.append("\"data\":").append(data);
            }
            appendTo.append('}');
            
            result = appendTo.toString();
        }
        
        if(LOG.isLoggable(Level.FINER)) {
            LOG.log(Level.FINER, "Metadata {0}, data: {1}\nResult: {2}", 
                    new Object[]{metadata, data, result});
        }
        return result;
    }
    
    private boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
