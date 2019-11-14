/*
 * Copyright 2018 NUROX Ltd.
 *
 * Licensed under the NUROX Ltd Software License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy get the License at
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

import com.bc.opendata.services.ServiceHasCache;
import com.bc.opendata.services.Service;
import com.bc.opendata.services.ServiceFactory;
import com.bc.opendata.caching.CacheProvider;
import com.bc.opendata.caching.Cache;
import com.bc.opendata.StandardDataType;
import java.io.IOException;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;

/**
 * @author Chinomso Bassey Ikwuagwu on Oct 31, 2018 8:37:44 PM
 */
public class ServiceFactoryImpl implements ServiceFactory<StandardDataType, ServiceHasCache<Map, String>> {

    private transient static final Logger LOG = Logger.getLogger(ServiceFactoryImpl.class.getName());
    
    private final Lock cacheLock = new ReentrantLock();

    private final EnumMap<StandardDataType, ServiceHasCache<Map, String>> cache;
    
    private final CacheProvider<Map, String> cacheProvider;

    public ServiceFactoryImpl() {
        this(new CacheProvider() {
            @Override
            public Cache getCache(StandardDataType type) { return Cache.NO_OP; }
        });
    }
    
    public ServiceFactoryImpl(CacheProvider<Map, String> cacheProvider) {
        this.cache = new EnumMap<>(StandardDataType.class);
        this.cacheProvider = Objects.requireNonNull(cacheProvider);
    }
    
    @Override
    public boolean isClosed() {
        try{
            cacheLock.lock();
            final Collection<ServiceHasCache<Map, String>> services = this.cache.values();
            for(ServiceHasCache<Map, String> service : services) {
                if(!service.getCache().isClosed()) {
                    return false;
                }
            }
            return true;
        }finally{
            cacheLock.unlock();
        }
    }
    
    @Override
    public void close() throws IOException {
        try{
            cacheLock.lock();
            IOException exception = null;
            final Collection<ServiceHasCache<Map, String>> services = this.cache.values();
            for(ServiceHasCache<Map, String> service : services) {
                try{
                    service.getCache().close();
                }catch(IOException e) {
                    if(exception == null) {
                        exception = e;
                    }else{
                        exception.addSuppressed(e);
                    }
                }
            }
            if(exception != null) {
                throw exception;
            }
        }finally{
            cacheLock.unlock();
        }
    }
    
    @Override
    public Set<StandardDataType> getServiceIds() {
        try{
            cacheLock.lock();
            return this.cache.keySet();
        }finally{
            cacheLock.unlock();
        }
    }
    
    @Override
    public ServiceHasCache<Map, String> get(StandardDataType type) {
        try {
            cacheLock.lock();
            ServiceHasCache<Map, String> result = cache.get(type);
            if(result == null) {
                result = this.create(type);
                cache.put(type, result);
            }
            if(LOG.isLoggable(Level.FINE)) {
                LOG.log(Level.FINE, "For {0}, returning: {1}", new Object[]{type, result});
            }
            return result;
        }finally{
            cacheLock.unlock();
        }
    }
        
    public ServiceHasCache<Map, String> create(StandardDataType type) {

        final Service<Map, String> result;

        switch(type) {
            case EXCHANGE_RATE:
                final boolean useExternalCache = true;
                final long updateIntervalMillis = useExternalCache ? -1 : TimeUnit.HOURS.toMillis(6);
                result = new ExchangeRate(updateIntervalMillis);
                break;
            case LOCATION:
                result = new Ipstack();
                break;
            case SOCCER:
                result = new ServiceWithFallbacks(new OpendataSoccer(), new FootballdataSoccer());
                break;
            case WEATHER:
                result = this.getWeatherService();
                break;
            default:
                throw StandardDataType.getExceptionForUnsupported(type);
        }
        
        if(LOG.isLoggable(Level.FINE)) {
            LOG.log(Level.FINE, "For {0}, created: {1}", new Object[]{type, result});
        }
        
        final Cache<Map, String> serviceCache = cacheProvider.getCache(type);

        return new CachingServiceImpl(result, serviceCache);
    } 
    
    protected Service<Map, String> getWeatherService() {
        try{
            return new ServiceWithFallbacks(new YahooWeather(), new ForecastioWeather());
        }catch(JAXBException e) {
            LOG.log(Level.WARNING, "Failed to instantiate " + YahooWeather.class.getName(), e);
            return new ForecastioWeather();
        }
    }
}
