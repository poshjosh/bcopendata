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

package com.bc.opendata.response;

import java.util.Map;

/**
 * @author Chinomso Bassey Ikwuagwu on Nov 1, 2018 7:29:37 PM
 */
public interface ResponseBuilder<D, R> {

    R build();
    
    ResponseBuilder<D, R> withData(D data);
    
    ResponseBuilder<D, R> withMetadata(D metadata);
    
    ResponseBuilder<String, String> withServiceName(String arg);

    ResponseBuilder<String, String> withRequestParameters(Map arg);
    
    ResponseBuilder<String, String> withMetadataEntry(Object key, Object val);
}
