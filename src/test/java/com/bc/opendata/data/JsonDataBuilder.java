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

import static com.bc.opendata.servicenames.ServiceProperties.EMPTY_JSON;
import java.io.Serializable;

/**
 * @author Chinomso Bassey Ikwuagwu on Nov 1, 2018 1:12:57 PM
 */
public class JsonDataBuilder implements DataBuilder<String>, Serializable {
    
    private String data;
    
    public void clear() {
        this.data = null;
    }

    @Override
    public DataBuilder<String> withData(String data) {
        this.data = data;
        return this;
    }

    @Override
    public Data<String> build() {
        try{
            
            final Data<String> result;
            
            if(data == null || data.isEmpty() || EMPTY_JSON.equals(data)) {
                result = Data.EMPTY;
            }else{
                result = new JsonData(data);
            }
            
            return result;
            
        }finally{
            this.clear();
        }
    }
}
