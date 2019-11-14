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
import com.bc.opendata.servicenames.SoccerNames;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

/**
 * @author Chinomso Bassey Ikwuagwu on Nov 1, 2018 4:58:01 PM
 */
public class SoccerTest extends ServiceTest {

    @Test
    public void testSoccer() {
        
        final StandardDataType dataType = StandardDataType.SOCCER;
        
        final Map parameters = new HashMap();
        parameters.put(SoccerNames.PROPERTY_OPENDATA_LEAGUE, SoccerNames.OPENDATA_EPL);
        parameters.put(SoccerNames.PROPERTY_FOOTBALLDATA_LEAGUE, SoccerNames.FOOTBALLDATA_EPL);
        final long half = TimeUnit.HOURS.toMillis(84);
        parameters.put(SoccerNames.PROPERTY_STARTDATE, new Date(System.currentTimeMillis() - half));
        parameters.put(SoccerNames.PROPERTY_ENDDATE, new Date(System.currentTimeMillis() + half));
        
//        final GetKey getKeyFromServiceName = new SimpleGetKey("$.data.matches[0].awayTeam.name");
        final GetKey getKeyFromServiceName = new GetKey() {
            @Override
            public String apply(String serviceName) {
                final String key;
                if(SoccerNames.SERVICE_NAME_OPENDATA.equals(serviceName)) {
                    key = "$.data.data.matches[0].away.team";
                }else if(SoccerNames.SERVICE_NAME_FOOTBALLDATA.equals(serviceName)) {
                    key = "$.data.matches[0].awayTeam.name";
                }else{
                    throw new IllegalArgumentException("Unexpected service name: " + serviceName);
                }
                return key;
            }
        };
        
        this.test(dataType, parameters, getKeyFromServiceName);
    }
}
