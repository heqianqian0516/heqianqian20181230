package adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCarAdapter extends PagerAdapter {
    private List<String> mDatas;
    private Context mContext;

    public ShoppingCarAdapter(Context mContext) {
        this.mContext = mContext;
        mDatas=new ArrayList<>();
    }

    public void setData(List<String> datas) {
        mDatas.clear();
        if (datas!=null){
            mDatas.addAll(datas);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas.size()>0?5000:0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        ImageView imageView=new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        container.addView(imageView);
        ImageLoader.getInstance().displayImage(mDatas.get(position%mDatas.size()),imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
