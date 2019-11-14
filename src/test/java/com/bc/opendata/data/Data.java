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

package com.bc.opendata.data;

/**
 * @author Chinomso Bassey Ikwuagwu on Oct 31, 2018 5:57:50 PM
 */
public interface Data<T> {
    
    Data EMPTY = new Data() {
        @Override
        public Object getOrException(String name) {
            throw new UnsupportedOperationException("Not supported.");
        }
        @Override
        public Object getValue(String name, Object outputIfNone) {
            return outputIfNone;
        }
        @Override
        public Object getRawValue(Object outputIfNone) {
            return outputIfNone;
        }
    };
    
    Object getOrException(String name);
    
    Object getValue(String name, Object outputIfNone);
    
    T getRawValue(T outputIfNone);
}
