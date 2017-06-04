package com.ark.movieapp.app;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import com.ark.movieapp.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ahmedb on 12/1/16.
 */

public class MovieAppApplicationClass extends Application{

    public static MovieAppApplicationClass instance;

    //get MAx number of threads the device can handle (taken from AsyncTask.java)
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;

    private ExecutorService executorService;
    private Handler handler = new Handler(Looper.getMainLooper());

    //init the executor service and make a static context instance
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initImageLoader(getApplicationContext());
        executorService = Executors.newFixedThreadPool(CORE_POOL_SIZE);
    }

    public static void initImageLoader(Context context) {
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

    public ImageLoader getImageLoader(){
        return ImageLoader.getInstance();
    }

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

    /**
     *  a method to run any runnable in background from the pool in {@link #executorService}
     * @param runnable
     */
    public void runInBackGround(Runnable runnable){
        executorService.submit(runnable);
    }

    /**
     * a method to run any runnable on UI thread using {@link #handler}
     * @param runnable
     */
    public void runOnUI(Runnable runnable){
        handler.post(runnable);
    }

    /**
     * A method to return the static context
     * @return MovieAppApplicationClass
     */
    public static MovieAppApplicationClass getInstance(){
        return instance;
    }

}
