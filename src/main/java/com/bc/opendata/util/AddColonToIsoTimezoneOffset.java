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

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Chinomso Bassey Ikwuagwu on Nov 18, 2018 6:24:01 PM
 */
public final class AddColonToIsoTimezoneOffset implements Serializable {

    private transient static final Logger LOG = Logger.getLogger(AddColonToIsoTimezoneOffset.class.getName());

    public String apply(String dateStr) {
        final int n = dateStr == null ? -1 : dateStr.lastIndexOf('+');
        final String result;
        if(n == -1) {
            result = dateStr;
        }else{
            final String timeZone = dateStr.substring(n + 1);
            final boolean fourDigits = new HasDigits().apply(timeZone, 4);
            if(fourDigits) {
                result = dateStr.substring(0, n + 1) + timeZone.substring(0, 2) + ':' + timeZone.substring(2);
            }else{
                result = dateStr;
            }
        }
        if(LOG.isLoggable(Level.FINER)) {
            LOG.log(Level.FINER, "Input: {0}, output: {1}", new Object[]{dateStr, result});
        }
        return result;
    }
}
