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

package com.bc.opendata.services.impl;

import com.bc.opendata.util.OpendataSeasonDetails;
import com.bc.opendata.caching.Cache;
import static com.bc.opendata.servicenames.ServiceProperties.EMPTY_JSON;
import com.bc.opendata.servicenames.SoccerNames;
import com.bc.opendata.services.Service;
import com.bc.opendata.util.ApiKey;
import com.bc.opendata.util.InMemoryCache;
import com.bc.opendata.response.JsonResponseBuilder;
import com.bc.opendata.util.UrlContentReader;
import com.bc.opendata.util.UrlContentReaderOkhttp3;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Chinomso Bassey Ikwuagwu on Nov 15, 2018 8:56:27 PM
 */
public class OpendataSoccer implements Service<Map, String>, SoccerNames, Serializable {

    private transient static final Logger LOG = Logger.getLogger(OpendataSoccer.class.getName());

    private static final Cache<String, String> seasonDetailsCache = new InMemoryCache<>();
    
    private final UrlContentReader urlReader;
    
    private final OpendataSeasonDetails seasonDetails;
    
    public OpendataSoccer() {
        this(new UrlContentReaderOkhttp3());
    }

    public OpendataSoccer(UrlContentReader urlReader) {
        this.urlReader = Objects.requireNonNull(urlReader);
        this.seasonDetails = new OpendataSeasonDetails(seasonDetailsCache, this);
    }

    @Override
    public String request(Map parameters) {
        
        final String league = this.getRequiredPropery(parameters, PROPERTY_OPENDATA_LEAGUE).toString();
        final Integer season = (Integer)parameters.getOrDefault(PROPERTY_SEASON, 
                seasonDetails.getCurrentSeason(league));
        Objects.requireNonNull(season);
        final Integer round = (Integer)parameters.getOrDefault(PROPERTY_ROUND, null);
        
        final String rawResponse;
        if(round != null) {
            rawResponse = this.read(league, season, round);
        }else{
            final Integer [] rounds = this.getRounds(league, season);
            if(rounds.length == 1) {
                rawResponse = this.read(league, season, rounds[0]);
            }else{
                final String [] responses = new String[rounds.length];
                for(int i=0; i<rounds.length; i++) {
                    responses[i] = this.read(league, season, rounds[i]);
                }
                final StringBuilder appendTo = new StringBuilder();
                final int merged = this.merge(appendTo, responses);
                if(merged < 1 || appendTo.length() < 1) {
                    rawResponse = this.read(league, season, seasonDetails.getCurrentRound(league, season));
                }else{
                    rawResponse = appendTo.toString();
                }
            }
        }
        
        return this.buildResult(parameters, rawResponse);
    }
    
    public String buildResult(Map parameters, String rawResponse) {
        
        final String data = rawResponse == null || rawResponse.isEmpty() ? EMPTY_JSON : rawResponse;
        
        final String result = new JsonResponseBuilder()
                .withData(data)
                .withMetadataEntry(PROPERTY_ICON_LINK, "http://www.looseboxes.com/idisc/images/soccer_icon.png")
                .withServiceName(SERVICE_NAME_OPENDATA)
                .withRequestParameters(parameters)
                .build();
        
        LOG.log(Level.FINE, "Response: {0}", result);
        
        return result;
    }
    
    public String read(String league, Integer season, Integer round) {
        Objects.requireNonNull(league);
        Objects.requireNonNull(season);
        Objects.requireNonNull(round);
        final String urlString = MessageFormat.format(
                "https://sportsop-soccer-sports-open-data-v1.p.mashape.com/v1/leagues/{0}/seasons/{1}-{2}/rounds/round-{3}/matches", 
                league, season, (season + 1), round);
        final String rawResponse = this.read(urlString);
        return rawResponse;
    }
    
    public String read(String urlString) {
        LOG.log(Level.FINE, "Query: {0}", urlString);
        final String rawResponse = this.urlReader
//                .addHeader("X-Mashape-Host", "sportsop-soccer-sports-open-data-v1.p.rapidapi.com")
                .addHeader("X-Mashape-Key", new ApiKey().getOrException(ApiKey.MASHAPE_SPORTS_OPENDATA_DEFAULTAPP))
                .addHeader("Accept", "application/json")
                .request(urlString);
        return rawResponse;
    }
    
    public int merge(StringBuilder appendTo, String... results) {
        int merged = 0;
        final String prefix = "{\"data\":{\"matches\":[";
        final String suffixPrefix = "],";
        String suffix = null;
        boolean doneFirst = false;
        
        appendTo.append(prefix);
        for(String result : results) {
            final int n = result.indexOf(prefix);
            if(n == -1) {
                LOG.log(Level.WARNING, "Skipping. Required prefix not found in: {0}", result);
                continue;
            }
            final int suffixStart = result.indexOf(suffixPrefix, n);
            if(suffixStart == -1) {
                LOG.log(Level.WARNING, "Skipping. Required suffix not found in: {0}", result);
                continue;
            }
            if(result.indexOf(suffixPrefix, suffixStart + 1) != -1) {
                LOG.log(Level.WARNING, "Skipping. Found > 1 suffix in: {0}", result);
                continue;
            }
            
            if(!doneFirst) {
                doneFirst = true;
            }else{
                appendTo.append(',');
            }
            appendTo.append(result.substring(n + prefix.length(), suffixStart));
            suffix = result.substring(suffixStart);
            ++merged;
        }
        appendTo.append(suffix);
        
        if(merged == 0 || suffix == null || suffix.isEmpty()) {
            merged = 0;
            appendTo.setLength(0);
        }
        
        LOG.log(Level.FINE, "Merge result: {0}", appendTo);
        
        return merged;
    }
    
    public Integer [] getRounds(String league, Integer season) {
        final int currentRound = seasonDetails.getCurrentRound(league, season);
        final Integer [] rounds;
        if(currentRound < 1) {
            rounds = new Integer[]{1};
        }else if(currentRound == 1){
            rounds = new Integer[]{1, 2};
        }else {  
            rounds = new Integer[]{currentRound-1, currentRound};
        }
        return rounds;
    }
    
    public Object getRequiredPropery(Map properties, String name) {
        final Object value = properties.get(name);
        return Objects.requireNonNull(value, "Required property: " + name + " not found");
    }

    public OpendataSeasonDetails getSeasonDetails() {
        return this.seasonDetails;
    }
}
