package application;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Lru
 * 算法：
 * 最近最多使用----回收的对象，是指的图片
 */

public class MyApp extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
//        初始化ImageLoader
        intImageLoader();
    }

    public static Context getContext() {
        return context;
    }

    private void intImageLoader() {
        ImageLoader.getInstance().init(getConifg1(this));
    }

//    options  配置加载图片时候的东西

    public static DisplayImageOptions getOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()

//                .showImageOnLoading(R.drawable.ic_stub)// 设置图片下载期间显示的图片
//
//                .showImageForEmptyUri(R.drawable.ic_empty)// 设置图片Uri为空或是错误的时候显示的图片
//
//                .showImageOnFail(R.drawable.ic_error)// 设置图片加载或解码过程中发生错误显示的图片
//
//                .resetViewBeforeLoading(false) // default 设置图片在加载前是否重置、复位
//
//                .delayBeforeLoading(1000) // 下载前的延迟时间

                .cacheInMemory(true)// default  设置下载的图片是否缓存在内存中

                .cacheOnDisk(true)// default  设置下载的图片是否缓存在SD卡中
//
//                .preProcessor(...)
//
//        .postProcessor(...)
//
//        .extraForDownloader(...)
//
//        .considerExifParams(false)// default
//
//                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)// default 设置图片以如何的编码方式显示
//
//                .bitmapConfig(Bitmap.Config.ARGB_8888)// default 设置图片的解码类型
//
//                .decodingOptions(...) // 图片的解码设置
//
//        .displayer(newSimpleBitmapDisplayer()) // default  还可以设置圆角图片new RoundedBitmapDisplayer(20)
//
//                .handler(newHandler()) // default

                .build();
        return options;
    }


    //    自己定义ImageLoader的配置信息
    public ImageLoaderConfiguration config(Context context) {
        File cacheDir = StorageUtils.getCacheDirectory(context);  //缓存文件夹路径
//
//        File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/myCache");

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)

//                .memoryCacheExtraOptions(480, 800)// default = device screen dimensions 内存缓存文件的最大长宽

//                .diskCacheExtraOptions(480, 800, null) // 本地缓存的详细信息(缓存的最大长宽)，最好不要设置这个

//                .taskExecutor(...)

//        .taskExecutorForCachedImages(...)

//        .threadPoolSize(3)// default  线程池内加载的数量

//                .threadPriority(Thread.NORM_PRIORITY - 2)// default 设置当前线程的优先级

//                .tasksProcessingOrder(QueueProcessingType.FIFO)// default

//                .denyCacheImageMultipleSizesInMemory()

                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))//可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024) // 内存缓存的最大值
                .memoryCacheSizePercentage(13)// default
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default 可以自定义缓存路径
                .diskCacheSize(50 * 1024 * 1024)// 50 Mb sd卡(本地)缓存的最大值
                .diskCacheFileCount(100) // 可以缓存的文件数量
                // default为使用HASHCODE对UIL进行加密命名， 还可以用MD5(new Md5FileNameGenerator())加密
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .imageDownloader(new BaseImageDownloader(context)) // default
//                .imageDecoder(new BaseImageDecoder()) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())// default
                .writeDebugLogs()// 打印debug log
                .build();//开始构建
        return config;
    }

    //简化版
    public ImageLoaderConfiguration getConifg1(Context context) {
        File cacheDir = StorageUtils.getCacheDirectory(context);  //缓存文件夹路径
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))//可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024) // 内存缓存的最大值
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default 可以自定义缓存路径
                .diskCacheSize(50 * 1024 * 1024)// 50 Mb sd卡(本地)缓存的最大值
                .build();//开始构建
        return config;
    }
}
