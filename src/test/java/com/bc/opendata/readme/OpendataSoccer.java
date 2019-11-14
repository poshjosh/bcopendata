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
import java.text.MessageFormat;

/**
 * @author Chinomso Bassey Ikwuagwu on Nov 15, 2018 8:46:38 PM
 */
public class OpendataSoccer extends Base {

    public static void main(String... args) {
        new OpendataSoccer().run();
    }

    public void run() {
        
// https://sportsop-soccer-sports-open-data-v1.p.mashape.com/v1/leagues/{league_slug}/seasons/{season_slug}/rounds/{round_slug}/matches
// https://sportsop-soccer-sports-open-data-v1.p.mashape.com/v1/leagues/premier-league/seasons/18-19/rounds/round-11/matches
    
        final String url = MessageFormat.format(
                "https://sportsop-soccer-sports-open-data-v1.p.mashape.com/v1/leagues/{0}/seasons/{1}/rounds/{2}/matches", 
                "premier-league", "18-19", "round-11");
        
        System.out.println("URL: " + url);
        
        final String raw_response = new UrlContentReaderOkhttp3()
//            .addHeader("X-Mashape-Host", "sportsop-soccer-sports-open-data-v1.p.rapidapi.com")
            .addHeader("X-Mashape-Key", new ApiKey().getOrException(ApiKey.MASHAPE_SPORTS_OPENDATA_NEWSMINUTE))
            .addHeader("Accept", "application/json")
            .request(url);
        
        this.toJson(raw_response, null);
    }
}
