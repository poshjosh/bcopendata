package com.bc.opendata;

import com.bc.opendata.caching.Cache;
import com.bc.opendata.caching.CacheProvider;
import com.bc.opendata.servicenames.ExchangeRateNames;
import com.bc.opendata.servicenames.SoccerNames;
import com.bc.opendata.servicenames.WeatherNames;
import com.bc.opendata.services.Service;
import com.bc.opendata.services.impl.ServiceFactoryImpl;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author USER
 */
public class ReadMe {
    
    private static class CacheProviderImpl implements CacheProvider<Map, String>{
        @Override
        public Cache<Map, String> getCache(StandardDataType type) {
            return Cache.NO_OP;
        }
    }
    
    public static void main(String... args){
    
        final ServiceFactoryImpl serviceFactory = new ServiceFactoryImpl(new CacheProviderImpl());
        
        final Service<Map, String> weatherSvc = serviceFactory.get(StandardDataType.WEATHER);
        
        final BigDecimal lat = new BigDecimal("9.083329");
        final BigDecimal lon = new BigDecimal("7.444726");
        final Map requestParams = new HashMap();
        requestParams.put(WeatherNames.PROPERTY_LATITUDE, lat);
        requestParams.put(WeatherNames.PROPERTY_LONGITUDE, lon);

        String responseJson = weatherSvc.request(requestParams);
        
        System.out.println(responseJson);
        
        final Service<Map, String> fxRateSvc = serviceFactory.get(StandardDataType.EXCHANGE_RATE);
        requestParams.clear();
        
        requestParams.put(ExchangeRateNames.PROPERTY_BASE_CURRENCY, "USD");

        responseJson = fxRateSvc.request(requestParams);
        
        System.out.println(responseJson);

        final Service<Map, String> soccerInfoSvc = serviceFactory.get(StandardDataType.SOCCER);
        requestParams.clear();
        
        requestParams.putIfAbsent(SoccerNames.PROPERTY_OPENDATA_LEAGUE, SoccerNames.OPENDATA_EPL);
        requestParams.putIfAbsent(SoccerNames.PROPERTY_FOOTBALLDATA_LEAGUE, SoccerNames.FOOTBALLDATA_EPL);
        final long half = TimeUnit.DAYS.toMillis(7);
        requestParams.putIfAbsent(SoccerNames.PROPERTY_STARTDATE, new Date(System.currentTimeMillis() - half));
        requestParams.putIfAbsent(SoccerNames.PROPERTY_ENDDATE, new Date(System.currentTimeMillis() + half));

        responseJson = soccerInfoSvc.request(requestParams);
        
        System.out.println(responseJson);
    }
}
