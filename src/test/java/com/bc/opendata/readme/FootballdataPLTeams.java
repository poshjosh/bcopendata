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

import com.bc.opendata.util.ApiKey;
import com.bc.opendata.util.UrlContentReaderOkhttp3;

/**
 * @author Chinomso Bassey Ikwuagwu on Oct 30, 2018 5:06:28 PM
 */
public class FootballdataPLTeams extends Base {

    public static void main(String... args) {
        new FootballdataPLTeams().run();
    }

    public void run() {
        try{
            final String query = "https://api.football-data.org/v2/competitions/PL/teams";
            
            final String raw_response = new UrlContentReaderOkhttp3()
                    .addHeader("X-Auth-Token", new ApiKey().getOrException(ApiKey.FOOTBAL_DATA))
                    .request(query);
            
            this.toJson(raw_response, null);
            
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
