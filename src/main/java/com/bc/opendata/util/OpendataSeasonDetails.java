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

package com.bc.opendata.util;

import com.bc.opendata.caching.Cache;
import com.bc.opendata.servicenames.ServiceProperties;
import com.bc.opendata.services.impl.OpendataSoccer;
import java.io.Serializable;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Chinomso Bassey Ikwuagwu on Nov 15, 2018 9:13:12 PM
 */
public class OpendataSeasonDetails implements Serializable {

    private transient static final Logger LOG = Logger.getLogger(
            OpendataSeasonDetails.class.getName());
    
    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";
    private static final AtomicInteger lastSeason = new AtomicInteger();
    private static final AtomicInteger lastRound = new AtomicInteger(-1);
    
    private final boolean keepOnlyCurrentSeason = false;
    
    private final Cache<String, String> cache;
    
    private final OpendataSoccer service;
    
    public OpendataSeasonDetails(OpendataSoccer service) {
        this(new InMemoryCache(), service);
    }
    
    public OpendataSeasonDetails(Cache<String, String> cache, OpendataSoccer service) {
        this.cache = Objects.requireNonNull(cache);
        this.service = Objects.requireNonNull(service);
    }
    
    public int getCurrentRound(String league) {
        return this.getCurrentRound(league, this.getCurrentSeason(league));
    }

    public int getCurrentRound(String league, int season) {
        synchronized(lastRound) {
            int round = lastRound.get() < 1 ? 1 : lastRound.get();
            while(true) {
                final Optional<Boolean> started = this.isStarted(league, season, round);
                if(!started.isPresent()) {
                    lastRound.set(-1);
                    break;
                }
                if(!started.get()) {
                    if(round > 0) {
                        lastRound.set(round);
                    }
                    break;
                }
                final Optional<Boolean> ended = this.isEnded(league, season, round);
                if(!ended.isPresent()) {
                    lastRound.set(-1);
                    break;
                }
                if(!ended.get()) {
                    break;
                }
                
                lastRound.set(round);
                
                ++round;
            }
            final int result = lastRound.get();
            LOG.log(Level.FINE, "Current round: {0}", result);
            return result;
        }
    }
    
    public Optional<Boolean> isStarted(String league, int season, int round) {
        final Optional<Boolean> result = this.compare(league, season, round, 
                START_DATE, "<=", System.currentTimeMillis());
        if(LOG.isLoggable(Level.FINER)) {
            LOG.log(Level.FINER, "League: {0}, season: {1}, round: {2}, started: {3}", 
                    new Object[]{league, season, round, result});
        }
        return result;
    }

    public Optional<Boolean> isEnded(String league, int season, int round) {
        final Optional<Boolean> result = this.compare(league, season, round, 
                END_DATE, "<=", System.currentTimeMillis());
        if(LOG.isLoggable(Level.FINER)) {
            LOG.log(Level.FINER, "League: {0}, season: {1}, round: {2}, ended: {3}", 
                    new Object[]{league, season, round, result});
        }
        return result;
    }

    private Optional<Boolean> compare(String league, int season, int round, 
            String dateName, String operator, long other) {
        final long millis = this.getEpochMillis(league, season, round, dateName);
        final Boolean result;
        if(millis == -1) {
            result = null;
        }else{
            switch(operator) {
                case "==": result = millis == other; break;
                case "<": result = millis < other; break;
                case "<=": result = millis <= other; break;
                case ">": result = millis > other; break;
                case ">=": result = millis >= other; break;
                default:
                    throw new IllegalArgumentException(
                            "Unpected operator: " + operator + ". Supported: ==, <, <=, >, >=");
            }
        }
        return Optional.ofNullable(result);
    }

    public int getCurrentSeason(String league) {
        int result = -1;
        final int year = this.get2DigitYear();
        final int start = year - 1;
        final int end = year + 1;
        for(int season=start; season<end; season++) {
            final int round = this.getCurrentRound(league, season);
            if(round > 0) {
                result = season;
                break;
            }
        }
        if(result == -1) {
            throw new IllegalArgumentException();
        }
        LOG.log(Level.FINE, "Current season: {0}", result);
        return result;
    }
    
    public long getStartEpochMillis(String league, int season, int round) {
        return this.getEpochMillis(league, season, round, START_DATE);
    }
    
    public long getEndEpochMillis(String league, int season, int round) {
        return this.getEpochMillis(league, season, round, END_DATE);
    }

    public ZonedDateTime getStartDate(String league, int season, int round, ZonedDateTime outputIfNone) {
        return this.getDate(league, season, round, START_DATE, outputIfNone);
    }
    
    public ZonedDateTime getEndDate(String league, int season, int round, ZonedDateTime outputIfNone) {
        return this.getDate(league, season, round, END_DATE, outputIfNone);
    }

    private long getEpochMillis(String league, int season, int round, String dateName) {
        final ZonedDateTime date = this.getDate(league, season, round, dateName, null);
        return date == null ? -1 : TimeUnit.SECONDS.toMillis(date.toEpochSecond());
    }

    public ZonedDateTime getDate(String league, int season, int round, String dateName, ZonedDateTime outputIfNone) {
        final String data = this.getData(league, season);
        final String sval = this.getDateString(data, round, dateName, null);
        final String dateStr = sval == null ? null : new AddColonToIsoTimezoneOffset().apply(sval);
        final ZonedDateTime dateTime = dateStr == null ? null : 
                ZonedDateTime.parse(dateStr, DateTimeFormatter.ISO_DATE_TIME);
        return dateTime == null ? outputIfNone : dateTime;
    }
    
    public String getDateString(String data, int round, String dateName, String outputIfNone) {
        final Pattern pattern = this.buildDatePattern(round, dateName);
        final Matcher matcher = pattern.matcher(data);
        if(matcher.find()) {
            return matcher.group(1);
        }else{
            return outputIfNone;
        }
    }

    public Pattern buildDatePattern(int round, String dateName) {
        final String bridge = "[\\w\\W]+?";
        final String rounds_roundName = Pattern.quote("\"name\":\"Round "+round+"\"");
        final String rounds_roundDate = Pattern.quote("\""+dateName+"\":\"") + "([0-9-T:\\+]+?)" + Pattern.quote("\"");
        final Pattern pattern = Pattern.compile(rounds_roundName + bridge + rounds_roundDate);
        return pattern;
    }

    public String getData(String league, int season) {

        final String key = this.getKey(league, season);

        String result = cache.getOrDefault(key, null);

        if(result == null) {
            
            result = this.loadData(league, season);

            if(result != null) {
                cache.put(key, result);
            }
        }

        if(this.keepOnlyCurrentSeason && season != lastSeason.get()) {
            final String keyToRemove = this.getKey(league, lastSeason.get()); 
            LOG.log(Level.FINE, "Removing from cache. Key: {0}", keyToRemove);
            cache.remove(keyToRemove);
        }

        lastSeason.set(season);

        return result == null ? ServiceProperties.EMPTY_JSON : result;
    }
    
    public String loadData(String league, int season) {
        
        final String url = MessageFormat.format(
                "https://sportsop-soccer-sports-open-data-v1.p.mashape.com/v1/leagues/{0}/seasons/{1}-{2}", 
                league, season, season + 1);
        
        final String response = service.read(url);
        
        if(LOG.isLoggable(Level.FINE)) {
            LOG.log(Level.FINE, "   For: {0}\nLoaded: {1}", new Object[]{url, response});
        }
        
        return response == null ? ServiceProperties.EMPTY_JSON : response;
    }
    
    public String getKey(String league, int season) {
        return league + season;
    }

    private int get2DigitYear() {
        final String year2digitStr;
        if(true) {
            year2digitStr =new SimpleDateFormat("yy").format(new Date());
        }else{
            final String sval = String.valueOf(LocalDateTime.now().getYear());
            year2digitStr = sval.length() == 2 ? sval : sval.substring(sval.length()-2, sval.length());
        }
        return Integer.parseInt(year2digitStr);
    }
}
