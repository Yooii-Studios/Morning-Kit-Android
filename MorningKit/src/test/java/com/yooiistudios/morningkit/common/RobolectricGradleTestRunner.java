package com.yooiistudios.morningkit.common;

import org.junit.runners.model.InitializationError;
import org.robolectric.AndroidManifest;
import org.robolectric.AndroidManifestExt;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.bytecode.Setup;
import org.robolectric.res.Fs;

/**
 * Created by StevenKim on 2013. 10. 31..
 *
 * RobolectricGradleTestRunner
 *  Robolectric을 커스터마이징해 플러그인에 대응
 */
public class RobolectricGradleTestRunner extends RobolectricTestRunner {
    public RobolectricGradleTestRunner(final Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    public Setup createSetup() {
        return new EnhancedSetup();
    }

    @Override
    protected AndroidManifest getAppManifest(final Config config) {
        final String manifestProperty = System.getProperty("android.manifest");
        if (config.manifest().equals(Config.DEFAULT) && manifestProperty != null) {
            final String resProperty = System.getProperty("android.resources");
            final String assetsProperty = System.getProperty("android.assets");
            final String packageProperty = System.getProperty("android.package");
            final AndroidManifestExt a = new AndroidManifestExt(Fs.fileFromPath(manifestProperty), Fs.fileFromPath(resProperty), Fs.fileFromPath(assetsProperty));
            a.setPackageName(packageProperty);
            return a;
        }
        return super.getAppManifest(config);
    }
}
