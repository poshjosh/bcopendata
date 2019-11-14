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

package com.bc.opendata.data;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import java.io.Serializable;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Chinomso Bassey Ikwuagwu on Oct 31, 2018 5:59:35 PM
 */
public class JsonData implements Data<String>, Serializable {

    private transient static final Logger LOG = Logger.getLogger(JsonData.class.getName());

    private final String rawValue;
    
    public JsonData(String data) {
        this.rawValue = Objects.requireNonNull(data);
    }

    @Override
    public Object getOrException(String name) {
        return Objects.requireNonNull(this.getValue(name, null), name + " may not be null");
    }
    
    @Override
    public Object getValue(String name, Object outputIfNone) {
        Object result = null;
        try{
            result = this.getDocumentContext().read(name);
        }catch(com.jayway.jsonpath.JsonPathException e) {
            LOG.log(Level.WARNING, "{0}", e.toString());
            LOG.log(Level.FINER, "Exception reading: " + name, e);
        }
        Object logMe = result;
        
        if(LOG.isLoggable(Level.FINER)) {
            LOG.log(Level.FINER, "{0} = {1}", new Object[]{name, logMe});
        }
        
        return result == null ? outputIfNone : result;
    }

    @Override
    public String getRawValue(String outputIfNone) {
        return this.rawValue == null ? outputIfNone : rawValue;
    }
    
    private transient DocumentContext _dc;
    public DocumentContext getDocumentContext() {
        if(_dc == null) {
            _dc = Objects.requireNonNull(JsonPath.parse(Objects.requireNonNull(rawValue)));
            LOG.fine("Created document context");
        }
        return _dc;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.rawValue);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final JsonData other = (JsonData) obj;
        if (!Objects.equals(this.rawValue, other.rawValue)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return rawValue;
    }
}
