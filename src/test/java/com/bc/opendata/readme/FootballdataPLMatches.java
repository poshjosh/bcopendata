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
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author Chinomso Bassey Ikwuagwu on Oct 30, 2018 5:02:39 PM
 */
public class FootballdataPLMatches extends Base {

    public static void main(String... args) {
        new FootballdataPLMatches().run();
    }

    public void run() {
        try{
            // https://www.football-data.org/documentation/quickstart/
            // https://www.football-data.org/documentation/api
  
            final long periodDays = 7;

            final Date startTime = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(periodDays));

            final Date endTime = new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(periodDays));

            System.out.println("Start: " + startTime + ", end: " + endTime);

            final DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

            final String start = fmt.format(startTime);

            final String end = fmt.format(endTime);

            System.out.println("Start: " + start + ", end: " + end);

            final String query = MessageFormat.format(
                    "https://api.football-data.org/v2/competitions/PL/matches?dateFrom={0}&dateTo={1}", 
                    start, end);
            
            final String raw_response = new UrlContentReaderOkhttp3()
                    .addHeader("X-Auth-Token", new ApiKey().getOrException(ApiKey.FOOTBAL_DATA))
                    .request(query);
            
//            final URL url = new URL(query);
//            final HttpURLConnection conn = (HttpURLConnection)url.openConnection();
//            conn.setRequestMethod("GET");
//            conn.setRequestProperty("X-Auth-Token", [YOUR API KEY HERE]);
//            conn.setUseCaches(false);
            
//            final InputStream in = conn.getInputStream();
            
//            final Object json = org.json.simple.JSONValue.parse(new InputStreamReader(in, StandardCharsets.UTF_8));

//            System.out.println("JSON Response:\n" +  json);
            
            this.toJson(raw_response, null);
            
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
