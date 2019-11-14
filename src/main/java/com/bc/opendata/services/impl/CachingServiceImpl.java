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

import com.bc.opendata.services.ServiceHasCache;
import com.bc.opendata.services.Service;
import com.bc.opendata.caching.Cache;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Chinomso Bassey Ikwuagwu on Nov 2, 2018 9:58:42 AM
 */
public class CachingServiceImpl<K, V> implements ServiceHasCache<K, V> {

    private transient static final Logger LOG = Logger.getLogger(CachingServiceImpl.class.getName());

    private final Service<K, V> service;
    
    private final Cache<K, V> cache;

    public CachingServiceImpl(Service<K, V> service, Cache<K, V> cache) {
        this.service = Objects.requireNonNull(service);
        this.cache = Objects.requireNonNull(cache);
    }

    @Override
    public V request(K request) {
        
        V response = cache.getOrDefault(request, null);
        
        if(response == null) {
            
            response = service.request(request);
            
            if(response != null) {
                
                cache.put(request, response);
                
                try{
                    cache.flush();
                }catch(IOException e) {
                    LOG.log(Level.WARNING, "Exception flushing cache", e);
                }
            }
        }
        
        return response;
    }

    @Override
    public Cache<K, V> getCache() {
        return cache;
    }
}
