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

import com.bc.opendata.servicenames.LocationNames;
import com.bc.opendata.StandardDataType;
import java.util.Collections;
import java.util.Map;
import org.junit.Test;

/**
 * @author Chinomso Bassey Ikwuagwu on Nov 5, 2018 3:39:17 PM
 */
public class LocationTest extends ServiceTest {

    @Test
    public void testLocation() {
        
        final StandardDataType dataType = StandardDataType.LOCATION;
        
        final Map properties = Collections.singletonMap(LocationNames.PROPERTY_IPADDRESS, "105.112.23.9");
        
        final GetKey getKeyFromServiceName = new SimpleGetKey("$.data.location.calling_code");
        
        this.test(dataType, properties, getKeyFromServiceName);
    }
}