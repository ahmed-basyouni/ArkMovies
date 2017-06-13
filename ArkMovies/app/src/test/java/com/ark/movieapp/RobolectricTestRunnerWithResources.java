package com.ark.movieapp;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Created by Ark on 6/13/2017.
 */

public class RobolectricTestRunnerWithResources extends RobolectricTestRunner {

    @Override
    protected Config buildGlobalConfig() {
        return Config.Builder.defaults()
                .setPackageName("com.ark.movieapp")
                .setManifest("build/intermediates/manifests/full/debug/AndroidManifest.xml")
                .setResourceDir("../../../res/merged/debug") // relative to manifest
                .setAssetDir("../../../assets/debug") // relative to manifest
                .build();
    }

    public RobolectricTestRunnerWithResources(Class<?> testClass) throws InitializationError {
        super(testClass);
    }
}
