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

import com.bc.opendata.services.impl.ServiceFactoryImpl;
import com.bc.opendata.servicenames.WeatherNames;
import com.bc.opendata.data.Data;
import com.bc.opendata.data.JsonDataBuilder;
import com.bc.opendata.StandardDataType;
import com.bc.opendata.TestBase;
import com.bc.opendata.util.JsonFormat;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import net.minidev.json.JSONValue;
import static org.junit.Assert.assertEquals;

/**
 * @author Chinomso Bassey Ikwuagwu on Nov 1, 2018 2:52:47 PM
 */
public class ServiceTest extends TestBase {
    
    public static interface GetKey {
        String apply(String serviceName); 
    }
    
    public static class SimpleGetKey implements GetKey{
        private final String key;
        public SimpleGetKey(String key) {
            this.key = Objects.requireNonNull(key);
        }
        @Override
        public String apply(String serviceName) {
            return this.key;
        }
    }

    private static final ServiceFactory<StandardDataType, ServiceHasCache<Map, String>> 
            serviceFactory = new ServiceFactoryImpl();

    public void test(StandardDataType type, Map properties, GetKey getKeyFromServiceName) {

        try{
            System.out.println("test("+type.getPath()+")");

            final Service<Map, String> service = serviceFactory.get(type);

            final String response = service.request(properties);

            final Data<String> data = new JsonDataBuilder().withData(response).build();

            try{
                
                final Object serviceName = ((Map)data.getValue("$.metadata", Collections.EMPTY_MAP))
                        .get(WeatherNames.PROPERTY_SERVICENAME);

                System.out.println(WeatherNames.PROPERTY_SERVICENAME + " = " + serviceName);

                final String key = getKeyFromServiceName.apply(serviceName.toString());

                final Object val = key == null ? null : data.getValue(key, null);

                System.out.println(key + " = " + val);

            }finally{

                final String raw = data.getRawValue(null);
                System.out.println("Raw: " + raw);

                final Map map = (Map)JSONValue.parse(response);
                System.out.println(new JsonFormat(true, true, "    ").toJSONString(map));

                assertEquals(response, raw);
            }
        }catch(RuntimeException e) {
            e.printStackTrace();
        }
    }
}
