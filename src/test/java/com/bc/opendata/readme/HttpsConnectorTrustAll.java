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

import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * @author Chinomso Bassey Ikwuagwu on Nov 15, 2018 4:45:55 PM
 */
public class HttpsConnectorTrustAll {

    private static class TrustAll implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) {}
        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) {}
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
    
    public HttpsURLConnection connect(URL url) throws IOException {
        SSLContext ctx = null;
        try{
            ctx = SSLContext.getDefault();
            SSLContext.setDefault(this.getTrustAllSslContext());
            final HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
            conn.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            }); 
            return conn;
        }catch(NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }finally{
            if(ctx != null) {
                SSLContext.setDefault(ctx);
            }
        }
    }
    
    private transient static SSLContext _sc;
    private SSLContext getTrustAllSslContext(){
        if(_sc == null) {
            try{
                // configure the SSLContext with a TrustManager
                SSLContext ctx = SSLContext.getInstance("TLS");
                ctx.init(new KeyManager[0], new TrustManager[] {new TrustAll()}, new SecureRandom());
                _sc = ctx;
            }catch(NoSuchAlgorithmException | KeyManagementException e) {
                throw new RuntimeException(e);
            }
        }
        return _sc;
    }
}
