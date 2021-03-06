package ru.techmas.androidtemplate.api;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.techmas.androidtemplate.Const;
import ru.techmas.androidtemplate.api.endpoints.User;
import ru.techmas.androidtemplate.utils.presenter.TokenHelper;

/**
 * Created by Alex Bykov on 09.11.2016.
 * You can contact me at: me@alexbykov.ru.
 */

public class RestApi {

    public final User user;
    private TokenHelper tokenHelper;
    private Retrofit retrofit;

    public RestApi(TokenHelper preferenceHelper) {
        this.tokenHelper = preferenceHelper;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        TokenAppendingHeaderInterceptor tokenInterceptor = new TokenAppendingHeaderInterceptor();


        //if backend have cookies instead token,compile 'com.github.franmontiel:PersistentCookieJar:v1.0.1'
        // and add .cookieJar(cookieJar) into OkHttpClient;
        // ClearableCookieJar cookieJar = new PersistentCookieJar(dataControl.getCookieCache(), dataControl.getSharedPrefsCookiePersistor());

        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(tokenInterceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();


        retrofit = new Retrofit.Builder().baseUrl(Const.Url.API_PRODACTION)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        user = retrofit.create(User.class);
    }

    public final String getServer() {
        String url = retrofit.baseUrl().toString();
        if (url.equals(Const.Url.API_PRODACTION)) return "Prodaction";
        else return "Test";
    }


    private class TokenAppendingHeaderInterceptor implements Interceptor {

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {

            final int NO_AUTHORIZED = 401;

            Request request = chain.request();
            String token = tokenHelper.getToken();
            Request newRequest = request.newBuilder()
                    .addHeader(Const.Url.AUTHORIZATION, token)
                    .build();

            okhttp3.Response response = chain.proceed(newRequest);
            if (response.code() == NO_AUTHORIZED) {
                // TODO: 25.04.2017  reload Application, and clear token or update token

            }
            return response;
        }
    }

}
