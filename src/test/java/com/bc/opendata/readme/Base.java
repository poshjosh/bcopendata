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

package com.bc.opendata.readme;

import com.bc.opendata.util.JsonFormat;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * @author Chinomso Bassey Ikwuagwu on Nov 16, 2018 7:38:03 PM
 */
public class Base {

    public JSONObject toJson(String raw_response, JSONObject outputIfNone) {
        
        final JSONObject json = raw_response == null ? null : (JSONObject)JSONValue.parse(raw_response);
        
        System.out.println("JSON Response:\n" +  json == null ? null : new JsonFormat(true, true, "  ").toJSONString(json));
        
        if(json == null) {
            System.out.println("RAW Response:\n" + raw_response);
        }
        
        return json == null ? outputIfNone : json;
    }
}
