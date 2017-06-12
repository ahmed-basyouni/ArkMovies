package com.ark.movieapp.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.test.espresso.IdlingResource;

import com.ark.movieapp.R;
import com.ark.movieapp.idleresources.SimpleIdlingResource;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 *
 * Created by Ark on 6/10/2017.
 */

@Module
public class AppModule {

    private MovieAppApplicationClass applicationClass;

    public AppModule(MovieAppApplicationClass aClass){
        this.applicationClass = aClass;
        initImageLoader(this.applicationClass);
    }

    @Singleton
    @Provides
    public MovieAppApplicationClass provideApplicationClass(){
        return applicationClass;
    }

    @Singleton
    @Provides
    public Context provideContext(){
        return applicationClass;
    }

    private void initImageLoader(MovieAppApplicationClass context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    @Singleton
    @Provides
    public ImageLoader getImageLoader(){
        return ImageLoader.getInstance();
    }

    @Singleton
    @Provides
    public DisplayImageOptions getDisplayOptions(){
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }
}
