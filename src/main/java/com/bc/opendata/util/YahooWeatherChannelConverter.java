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

import com.bc.objectgraph.MapBuilder;
import com.bc.objectgraph.MapBuilderImpl;
import com.bc.objectgraph.ObjectFromMapBuilderImpl;
import com.github.fedy2.weather.data.Channel;
import java.io.Serializable;
import java.util.Map;

/**
 * @author Chinomso Bassey Ikwuagwu on Oct 31, 2018 10:02:46 PM
 */
public class YahooWeatherChannelConverter implements Serializable {

    public Channel toChannel(Map map) {
        final Channel channel = (Channel)new ObjectFromMapBuilderImpl()
//                .formatter(ObjectFromMapBuilder.Formatter.NO_OP)
//                .lenient(true)
//                .resultBuffer(map)
                .source(map)
                .targetType(Channel.class)
                .build();
        return channel;        
    }
    
    public Map toMap(Channel channel) {
        final Map map = new MapBuilderImpl()
//                .filter(MapBuilder.Filter.ACCEPT_ALL)
                .recursionFilter(MapBuilder.RecursionFilter.DEFAULT)
                .maxCollectionSize(Integer.MAX_VALUE)
                .maxDepth(Integer.MAX_VALUE)
                .nullsAllowed(false)
                .source(channel)
                .build();
        return map;        
    }
}
