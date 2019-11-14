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

package com.bc.opendata.readme;

import com.bc.opendata.util.ApiKey;
import com.github.dvdme.ForecastIOLib.FIOAlerts;
import com.github.dvdme.ForecastIOLib.FIOCurrently;
import com.github.dvdme.ForecastIOLib.FIODataPoint;
import com.github.dvdme.ForecastIOLib.ForecastIO;

/**
 * @author Chinomso Bassey Ikwuagwu on Oct 30, 2018 1:15:42 PM
 */
public class Forecastio_ReadMe extends Base {

    public static void main(String... args) {
        new Forecastio_ReadMe().run();
    }

    public void run() {
        //daily#data[0]#temperatureMax
        final String api_key = new ApiKey().getOrException(ApiKey.FORECAST_IO);
        ForecastIO fio = new ForecastIO(api_key);
        fio.setUnits(ForecastIO.UNITS_SI);
        fio.setLang(ForecastIO.LANG_ENGLISH);
        
        //excluded the minutely and hourly reports from the reply
        fio.setExcludeURL("hourly,minutely");          
        // Sets the latitude and longitude - not optional. Will fail if not set.
        // Method should be called after the options were set
//        fio.getForecast("38.7252993", "-9.1500364");  
        //Jaji, Command And Staff College, 101241, Jaji
        fio.getForecast("10.824816", "7.559508");

        FIOCurrently currently = new FIOCurrently(fio);
        FIODataPoint dp = currently.get();
        System.out.println("Summary: " + dp.summary());
        System.out.println("Aparent temperature: " + dp.apparentTemperature());
        System.out.println("Cloud cover: " + dp.cloudCover());
        System.out.println("Dew point: " + dp.dewPoint());
        System.out.println("Humidity: " + dp.humidity());
        System.out.println("Icon: " + dp.icon());
        System.out.println("Storm Distance: " + dp.nearestStormDistance());
        System.out.println("Pressure: " + dp.pressure());
        System.out.println("Temperature: " + dp.temperature());
        System.out.println("Time: " + dp.time());
        System.out.println("Visibility: " + dp.visibility());
        System.out.println("Wind bearing: " + dp.windBearing());
        System.out.println("Wind speed: " + dp.windSpeed());

        FIOAlerts alerts = new FIOAlerts(fio);
	if(alerts.NumberOfAlerts() <= 0){
            System.out.println("No alerts for this location.");
	}else {
            for(int i=0; i<alerts.NumberOfAlerts(); i++) {
		System.out.println(alerts.getAlert(i));
            }    
	}
        
        final String raw_response = fio.getRawResponse();
        
        this.toJson(raw_response, null);
    }
}
