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

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Josh
 */
public class HasDigitsTest {
    
    public HasDigitsTest() { }

    /**
     * Test of apply method, of class HasDigits.
     */
    @Test
    public void testApply() {
        System.out.println("apply");
        HasDigits instance = new HasDigits();
        boolean result = instance.apply("01234", 5);
        boolean expRes = true;
        assertEquals(result, expRes);
        result = instance.apply("01234", 2);
        expRes = false;
        assertEquals(result, expRes);
        result = instance.apply("01W34", 5);
        expRes = false;
        assertEquals(result, expRes);
    }
}
