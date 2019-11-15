## bcopendata
#### Built atop https://github.com/poshjosh/bcopendataapi for accessing data (e.g weather, exchange rates) openly available on the web

### Usage

```java

        // For our examples we implement a CacheProvider which returns a no-op Cache
        //    
        private static class CacheProviderImpl implements CacheProvider<Map, String>{
            @Override
            public Cache<Map, String> getCache(StandardDataType type) {
                return Cache.NO_OP;
            }
        }
```

#### To download freely available football data (English Premier League info)

```java            
        final ServiceFactoryImpl serviceFactory = new ServiceFactoryImpl(new CacheProviderImpl());
        
        final Service<Map, String> soccerInfoSvc = serviceFactory.get(StandardDataType.SOCCER);
        requestParams.clear();
        
        requestParams.putIfAbsent(SoccerNames.PROPERTY_OPENDATA_LEAGUE, SoccerNames.OPENDATA_EPL);
        requestParams.putIfAbsent(SoccerNames.PROPERTY_FOOTBALLDATA_LEAGUE, SoccerNames.FOOTBALLDATA_EPL);
        final long half = TimeUnit.DAYS.toMillis(7);
        requestParams.putIfAbsent(SoccerNames.PROPERTY_STARTDATE, new Date(System.currentTimeMillis() - half));
        requestParams.putIfAbsent(SoccerNames.PROPERTY_ENDDATE, new Date(System.currentTimeMillis() + half));

        responseJson = soccerInfoSvc.request(requestParams);
        
        System.out.println(responseJson);
            
```

#### To download freely available weather data 

```java
            
        final ServiceFactoryImpl serviceFactory = new ServiceFactoryImpl(new CacheProviderImpl());
        
        final Service<Map, String> weatherSvc = serviceFactory.get(StandardDataType.WEATHER);
        
        final BigDecimal lat = new BigDecimal("9.083329");
        final BigDecimal lon = new BigDecimal("7.444726");
        final Map requestParams = new HashMap();
        requestParams.put(WeatherNames.PROPERTY_LATITUDE, lat);
        requestParams.put(WeatherNames.PROPERTY_LONGITUDE, lon);

        String responseJson = weatherSvc.request(requestParams);
        
        System.out.println(responseJson);
            
```

#### To download freely available exchange rates data 

```java
            
        final ServiceFactoryImpl serviceFactory = new ServiceFactoryImpl(new CacheProviderImpl());
        
        final Service<Map, String> fxRateSvc = serviceFactory.get(StandardDataType.EXCHANGE_RATE);
        requestParams.clear();
        
        requestParams.put(ExchangeRateNames.PROPERTY_BASE_CURRENCY, "USD");

        responseJson = fxRateSvc.request(requestParams);
        
        System.out.println(responseJson);
            
```

### Note
#### For the above code to work, obtain api keys from the various remote services and save them in a properties file named 'com.bc.opendata.properties' in any 'META-INF' directory in your classpath.

```
            football-data.org.apikey=

            ForecastIOLib.apikey=

            ipstack.com.apikey=

            mashape.soccer-sports-open-data.default-application=
```
