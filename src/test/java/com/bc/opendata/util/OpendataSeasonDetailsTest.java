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

import com.bc.opendata.TestBase;
import com.bc.opendata.servicenames.SoccerNames;
import com.bc.opendata.services.impl.OpendataSoccer;
import java.time.ZonedDateTime;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Josh
 */
public class OpendataSeasonDetailsTest extends TestBase {
    
    private static OpendataSeasonDetails instance;
    private static final String league = SoccerNames.OPENDATA_EPL;
    private static int season;
    private static int round;
    
    public OpendataSeasonDetailsTest() { }
    
    @BeforeClass
    public static void setUpClass() {
        instance = new OpendataSoccer().getSeasonDetails();
        season = instance.getCurrentSeason(league);
        round = instance.getCurrentRound(league, season);
        System.out.println("league: "+league+", season: "+season+", current round: " + round);
    }
    
    @AfterClass
    public static void tearDownClass() { }

    /**
     * Test of getCurrentRound method, of class OpendataSeasonDetails.
     */
    @Test
    public void testGetCurrentRound_String() {
        System.out.println("getCurrentRound");
        final int result = instance.getCurrentRound(league);
        System.out.println("league: "+league+", current round: " + result);
    }

    /**
     * Test of getCurrentRound method, of class OpendataSeasonDetails.
     */
    @Test
    public void testGetCurrentRound_String_int() {
        System.out.println("getCurrentRound");
        final int result = instance.getCurrentRound(league, season);
        System.out.println("league: "+league+", season: "+season+", current round: " + result);
    }

    /**
     * Test of isStarted method, of class OpendataSeasonDetails.
     */
    @Test
    public void testIsStarted() {
        System.out.println("isStarted");
        final int crrentRound = instance.getCurrentRound(league, season);
        this.testIsStarted(crrentRound - 1);
        this.testIsStarted(crrentRound);
        this.testIsStarted(crrentRound + 1);
    }

    public void testIsStarted(int round) {
        final Object result = instance.isStarted(league, season, round);
        System.out.println("Started: " +result+ ", league: "+league+", season: "+season+", round: " + round);
    }

    /**
     * Test of isEnded method, of class OpendataSeasonDetails.
     */
    @Test
    public void testIsEnded() {
        System.out.println("isEnded");
        final int crrentRound = instance.getCurrentRound(league, season);
        this.testIsEnded(crrentRound - 1);
        this.testIsEnded(crrentRound);
        this.testIsEnded(crrentRound + 1);
    }

    public void testIsEnded(int round) {
        final Object result = instance.isEnded(league, season, round);
        System.out.println("Ended: " +result+ ", league: "+league+", season: "+season+", round: " + round);
    }

    /**
     * Test of getCurrentSeason method, of class OpendataSeasonDetails.
     */
    @Test
    public void testGetCurrentSeason() {
        System.out.println("getCurrentSeason");
        final int result = instance.getCurrentRound(league);
        System.out.println("league: "+league+", current season: " + result);
    }

    /**
     * Test of getStartEpochMillis method, of class OpendataSeasonDetails.
     */
    @Test
    public void testGetStartEpochMillis() {
        System.out.println("getStartEpochMillis");
        final long result = instance.getStartEpochMillis(league, season, round);
        System.out.println("Start epoch millis: " +result+ ", league: "+league+", season: "+season+", round: " + result);
    }

    /**
     * Test of getEndEpochMillis method, of class OpendataSeasonDetails.
     */
    @Test
    public void testGetEndEpochMillis() {
        System.out.println("getEndEpochMillis");
        final long result = instance.getEndEpochMillis(league, season, round);
        System.out.println("End epoch millis: " +result+ ", league: "+league+", season: "+season+", round: " + result);
    }

    /**
     * Test of getStartDate method, of class OpendataSeasonDetails.
     */
    @Test
    public void testGetStartDate() {
        System.out.println("getStartDate");
        final ZonedDateTime result = instance.getStartDate(league, season, round, null);
        System.out.println("Start date: " +result+ ", league: "+league+", season: "+season+", round: " + result);
    }

    /**
     * Test of getEndDate method, of class OpendataSeasonDetails.
     */
    @Test
    public void testGetEndDate() {
        System.out.println("getEndDate");
        final ZonedDateTime result = instance.getEndDate(league, season, round, null);
        System.out.println("End date: " +result+ ", league: "+league+", season: "+season+", round: " + result);
    }
    
    /**
     * Test of getData method, of class OpendataSeasonDetails.
     */
    @Test
    public void testGetData() {
        System.out.println("getData");
        final String result_1 = instance.getData(league, season);
        assertNotNull(result_1);
        final String result_2 = instance.getData(league, season);
        assertNotNull(result_2);
        assertEquals(result_1, result_2);
    }

    /**
     * Test of loadData method, of class OpendataSeasonDetails.
     */
    @Test
    public void testLoadData() {
        System.out.println("loadData");
        final String result = instance.loadData(league, season);
        assertNotNull(result);
    }

    /**
     * Test of getKey method, of class OpendataSeasonDetails.
     */
    @Test
    public void testGetKey() {
        System.out.println("getKey");
        final String result_1 = instance.getKey(league, 1);
        assertNotNull(result_1);
        final String result_1b = instance.getKey(league, 1);
        assertNotNull(result_1b);
        assertEquals(result_1, result_1b);
        final String result_2 = instance.getKey(league, 2);
        assertNotNull(result_2);
        assertNotEquals(result_1, result_2);
    }
}
