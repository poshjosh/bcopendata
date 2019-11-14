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

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

/**
 * @author Chinomso Bassey Ikwuagwu on Oct 31, 2018 1:12:57 PM
 */
public final class ApiKey {
    
    public static final String FOOTBAL_DATA = "football-data.org.apikey";

    public static final String FORECAST_IO = "ForecastIOLib.apikey";
    
    public static final String IPSTACK = "ipstack.com.apikey";
    
    public static final String MASHAPE_SPORTS_OPENDATA_DEFAULTAPP = "mashape.soccer-sports-open-data.default-application";
    
    public static final String MASHAPE_SPORTS_OPENDATA_NEWSMINUTE = "mashape.soccer-sports-open-data.newsminute";
    
    private transient final Properties properties;
    
    public ApiKey() {
        final ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try(InputStream in = cl.getResourceAsStream("META-INF/com.bc.opendata.properties")) {
            properties = new Properties();
            properties.load(in);
        }catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public String getOrException(String name) {
        return Objects.requireNonNull(this.get(name, null), name + " cannot be null");
    }
    
    public String get(String name, String outputIfNone) {
        return properties.getProperty(name, outputIfNone);
    }
}
