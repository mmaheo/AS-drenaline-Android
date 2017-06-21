package fr.free.maheo.maxime.as_drenaline.data.network;

import fr.free.maheo.maxime.as_drenaline.data.source.category.CategoryDataSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mmaheo on 21/06/2017.
 */

public class NetworkProvider {

    private static NetworkProvider INSTANCE = null;

    private static final String BASE_URL = "http://151.80.57.80/api/";

    private CategoryDataSource categoryDataSource;

    public NetworkProvider() {
        categoryDataSource = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CategoryDataSource.class);
    }

    public static NetworkProvider getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NetworkProvider();
        }
        return INSTANCE;
    }

    public CategoryDataSource getManager() {
        return categoryDataSource;
    }
}
