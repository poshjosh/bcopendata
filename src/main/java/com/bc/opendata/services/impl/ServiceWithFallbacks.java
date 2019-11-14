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

import com.bc.opendata.services.Service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Chinomso Bassey Ikwuagwu on Nov 1, 2018 11:25:41 AM
 */
public class ServiceWithFallbacks<K, T> implements Service<K, T> {

    private transient static final Logger LOG = Logger.getLogger(ServiceWithFallbacks.class.getName());

    private final List<Service<K, T>> serviceList;
    
    private Service<K, T> activeService;
    
    public ServiceWithFallbacks(Service<K, T> preferred, Service<K, T>... fallbacks) {
        final List<Service<K, T>> list = new ArrayList();
        list.add(preferred);
        if(fallbacks != null && fallbacks.length > 0) {
            list.addAll(Arrays.asList(fallbacks));
        }
        this.serviceList = Collections.unmodifiableList(list);
        if(serviceList.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.activeService = serviceList.get(0);
    }

    @Override
    public T request(K properties) {
        RuntimeException exception = null;
        T result = null;
        for(Service<K, T> service : serviceList) {
            try{
                result = service.request(properties);
            }catch(RuntimeException e) {
                if(exception == null) {
                    exception = e;
                }else{
                    exception.addSuppressed(e);
                }
            }
            if(result != null) {
                activeService = service;
                break;
            }
        }        
        
        if(result == null && exception != null) {
            throw exception;
        }
        
        return result;
    }

    public List<Service<K, T>> getServiceList() {
        return Collections.unmodifiableList(serviceList);
    }

    public Service<K, T> getActiveService() {
        return activeService;
    }

    @Override
    public String toString() {
        return activeService.toString();
    }
}
