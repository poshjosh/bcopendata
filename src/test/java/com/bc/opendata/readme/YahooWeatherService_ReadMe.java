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

import com.bc.opendata.util.YahooWeatherChannelConverter;
import com.bc.opendata.util.JsonFormat;
import com.github.fedy2.weather.YahooWeatherService;
import com.github.fedy2.weather.YahooWeatherService.LimitDeclaration;
import com.github.fedy2.weather.data.Atmosphere;
import com.github.fedy2.weather.data.Channel;
import com.github.fedy2.weather.data.Condition;
import com.github.fedy2.weather.data.Forecast;
import com.github.fedy2.weather.data.Image;
import com.github.fedy2.weather.data.Item;
import com.github.fedy2.weather.data.unit.DegreeUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Chinomso Bassey Ikwuagwu on Oct 30, 2018 11:18:56 AM
 */
public class YahooWeatherService_ReadMe {

    public static void main(String... args) {
  
        try {
            final boolean detailed = false;
            final YahooWeatherService service = new YahooWeatherService();
//            final Channel channel = service.getForecast("2502265", DegreeUnit.CELSIUS);
            final LimitDeclaration ld = service.getForecastForLocation("Abuja", DegreeUnit.CELSIUS);
            
            final List<Channel> channelList = ld.first(1);
            System.out.println("Printing first " + channelList.size() + " result(s)");

            final YahooWeatherService_ReadMe yws = new YahooWeatherService_ReadMe();
            
            final YahooWeatherChannelConverter converter = new YahooWeatherChannelConverter();

            for(Channel channel : channelList) {
               
                yws.print(channel, detailed);

                final Map map = converter.toMap(channel);

                System.out.println("=================================================");
                System.out.println(new JsonFormat(true, true, "  ").toJSONString(map));
                System.out.println("=================================================");
                
//                Channel update = c.toChannel(map);
                
//                yws.print(update, detailed);

//                assertTrue(yws.channelEquals(channel, update, true));
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void print(Channel channel, boolean detailed) {
        if(detailed) System.out.println("Channel: " + channel);
        System.out.println("Description: " + channel.getDescription());
        System.out.println("Title: " + channel.getTitle());
        final Atmosphere atm = channel.getAtmosphere();
        if(detailed) System.out.println("\tAtomosphere: " + atm);
        if(atm != null) {
            System.out.println("\tAtmosphere.humidity: " + atm.getHumidity());
            System.out.println("\tAtmosphere.pressure: " + atm.getPressure());
            System.out.println("\tAtmosphere.visibility: " + atm.getVisibility());
        }
        final Image image = channel.getImage();
        if(detailed) System.out.println("\tImage: " + image);
        if(image != null) {
            System.out.println("\tImage.link: " + image.getLink());
            System.out.println("\tImage.URL: " + image.getUrl());
            System.out.println("\tImage.title: " + image.getTitle());
        }
        final Item item = channel.getItem();
        if(detailed) System.out.println("\tItem: " + item);
        if(item != null) {
            final List<Forecast> forecasts = item.getForecasts();
            if(forecasts != null) {
                for(Forecast forecast : forecasts) {
                    if(detailed)System.out.println("\t\tForecast: " + forecast);
                    System.out.println("\t\tItem.Forecast.code: " + forecast.getCode());
                    System.out.println("\t\tItem.Forecast.low: " + forecast.getHigh());
                    System.out.println("\t\tItem.Forecast.low: " + forecast.getLow());
                    System.out.println("\t\tItem.Forecast.text: " + forecast.getText());
                }
            }
            final Condition condition = item.getCondition();
            if(detailed) System.out.println("\t\tCondition: " + condition);
            if(condition != null) {
                System.out.println("\t\tItem.Condition.text: " + condition.getText());
                System.out.println("\t\tItem.Condition.code: " + condition.getCode());
                System.out.println("\t\tItem.Condition.temperature: " + condition.getTemp());
                System.out.println("\t\tItem.Condition.date: " + condition.getDate());
            }
        }
    }
    
    public boolean channelEquals(Channel lhs, Channel rhs, boolean log) {
        if(this.stringEquals(lhs.getAstronomy(), rhs.getAstronomy())) {
            if(log) System.out.println("Astronomy equals");
            if(this.stringEquals(lhs.getAtmosphere(), rhs.getAtmosphere())) {
                if(log) System.out.println("Atmosphere equals");
                if(this.stringEquals(lhs.getImage(), rhs.getImage())) {
                    if(log) System.out.println("Image equals");
                    if(this.itemEquals(lhs.getItem(), rhs.getItem(), log)) {
                        if(log) System.out.println("Item equals");
                        if(Objects.equals(lhs.getLanguage(), rhs.getLanguage())) {
                            if(log) System.out.println("Language equals");
                            if(Objects.equals(lhs.getLastBuildDate(), rhs.getLastBuildDate())) {
                                if(log) System.out.println("Last build date equals");
                                if(Objects.equals(lhs.getLink(), rhs.getLink())) {
                                    if(log) System.out.println("Link equals");
                                    if(this.stringEquals(lhs.getLocation(), rhs.getLocation())) {
                                        if(log) System.out.println("Location equals");
                                        if(Objects.equals(lhs.getTitle(), rhs.getTitle())) {
                                            if(log) System.out.println("Title equals");
                                            if(lhs.getTtl() == rhs.getTtl()) {
                                                if(log) System.out.println("Ttl equals");
                                                if(this.stringEquals(lhs.getUnits(), rhs.getUnits())) {
                                                    if(log) System.out.println("Units equals");
                                                    if(this.stringEquals(lhs.getWind(), rhs.getWind())) {
                                                        if(log) System.out.println("Wind equals");
                                                        return true;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return false;
    }
    
    public boolean itemEquals(Item lhs, Item rhs, boolean log) {
        if(this.stringEquals(lhs.getCondition(), rhs.getCondition())) {
            if(Objects.equals(lhs.getDescription(), rhs.getDescription())) {
                if(this.forecastListEquals(lhs.getForecasts(), rhs.getForecasts())) {
                    if(lhs.getGeoLat() == rhs.getGeoLat()) {
                        if(lhs.getGeoLong() == rhs.getGeoLong()) {
                            if(Objects.equals(lhs.getGuid(), rhs.getGuid())) {
                                if(Objects.equals(lhs.getLink(), rhs.getLink())) {
                                    if(Objects.equals(lhs.getPubDate(), rhs.getPubDate())) {
                                        if(Objects.equals(lhs.getTitle(), rhs.getTitle())) {
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public boolean forecastListEquals(List<Forecast> lhs, List<Forecast> rhs) {
        if(lhs == null && rhs == null) {
            return true;
        }else if(lhs != null && rhs != null) {
            if(lhs.size() != rhs.size()) {
                return false;
            }
            for(int i=0; i<lhs.size(); i++) {
                Forecast l = lhs.get(i);
                Forecast r = rhs.get(i);
                if(!this.stringEquals(l, r)) {
                    return false;
                }
            }
            return true;
        }else{
            return false;
        }
    }
    
    public boolean stringEquals(Object lhs, Object rhs) {
        return Objects.equals(lhs==null?null:lhs.toString(), rhs==null?null:rhs.toString());
    }
}
