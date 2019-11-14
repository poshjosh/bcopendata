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
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Chinomso Bassey Ikwuagwu on Nov 16, 2018 10:32:16 PM
 */
public class InMemoryCache<K, V> implements Cache<K, V>, Serializable {

    private Map<K, V> cache = new HashMap<>();
    
    @Override
    public V getOrDefault(K key, V outputIfNone) {
        return cache.getOrDefault(key, outputIfNone);
    }

    @Override
    public boolean put(K key, V value) {
        cache.put(key, value);
        return true;
    }

    @Override
    public void flush() {}

    @Override
    public boolean isClosed() {
        return true;
    }

    @Override
    public void close() { }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public boolean remove(K key) {
        cache.remove(key);
        return true;
    }

    @Override
    public void delete() {
        this.clear();
        cache = null;
    }
}
