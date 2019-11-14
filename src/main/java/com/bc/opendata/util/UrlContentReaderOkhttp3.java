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

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author Chinomso Bassey Ikwuagwu on Nov 16, 2018 1:35:35 PM
 */
public class UrlContentReaderOkhttp3 implements UrlContentReader, Serializable {

    private transient static final Logger LOG = Logger.getLogger(UrlContentReaderOkhttp3.class.getName());

    @Override
    public UrlContentReader addHeader(String name, String value) {
        this.getRequestBuilder().addHeader(name, value);
        return this;
    }

    @Override
    public String request(String urlString) {
        try{
            final Request request = this.getRequestBuilder()
                .url(urlString)
                .build();
        
            try(final Response response = this.getHttpClient()
                    .newCall(request)
                    .execute()) {

                final ResponseBody resBody = response.body();
                
                final String bodyText = resBody == null ? null : resBody.string();

                return bodyText;
            }
        }catch(IOException e) {
            
            LOG.log(Level.WARNING, null, e);
            
            return null;
        }finally{
            this.reset();
        }
    }
    
    public void reset() {
        _rb = null;
    }

    private Request.Builder _rb;
    public Request.Builder getRequestBuilder() {
        if(_rb == null) {
            _rb = new Request.Builder();
        }
        return _rb;
    }
    
    private static OkHttpClient _oc;
    public OkHttpClient getHttpClient() {
        if(_oc == null) {
            _oc = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build();
        }
        return _oc;
    }
}
