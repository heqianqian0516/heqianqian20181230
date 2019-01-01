package adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bwei.heqianqian20181230.R;

import java.util.ArrayList;
import java.util.List;

import bean.ShoppingBean;

public class ShoppingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ShoppingBean.DataBean> mData=new ArrayList<>();
    private Context context;
    private Boolean boo;

    public ShoppingAdapter(Context context) {
        this.context = context;

    }
    public ShoppingAdapter(Context context,Boolean isliener) {
        this.context = context;
        boo=isliener;
    }
    public void delData(int i){
        mData.remove(i);
        notifyDataSetChanged();
    }

    public List<ShoppingBean.DataBean> getmData() {
        return mData;
    }

    public void setData(List<ShoppingBean.DataBean> datas) {
        mData.clear();
        if (datas!=null){
            mData.addAll(datas);
        }
        notifyDataSetChanged();
    }
    public void addData(List<ShoppingBean.DataBean> datas) {
        if (datas!=null){
            mData.addAll(datas);
        }
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder holder=null;
        if (boo){
            View view=LayoutInflater.from(context).inflate(R.layout.item_recycle_linear,viewGroup,false);
            holder= new ViewHolderLinear(view);
        }else {
            View view=LayoutInflater.from(context).inflate(R.layout.item_recycle_grid,viewGroup,false);
            holder= new ViewHolderGrid(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        String images = mData.get(i).getImages();
        String[] split = images.split("\\|");

        if (boo){
            ViewHolderLinear holderLinear= (ViewHolderLinear) viewHolder;
            holderLinear.tv1.setText(mData.get(i).getTitle());
            holderLinear.tv2.setText(mData.get(i).getPrice()+"");
           Glide.with(context).load(split[0]).into(holderLinear.img);

            holderLinear.ll_item_recycle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener!=null){
                        mClickListener.onItemClick(i);
                    }
                }
            });
            holderLinear.ll_item_recycle.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mLongItemClickListener!=null){
                        mLongItemClickListener.onItemLongClick(i);
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    public class ViewHolderLinear extends RecyclerView.ViewHolder{

        private ImageView img;
        private TextView tv1,tv2;
        private LinearLayout ll_item_recycle;
        public ViewHolderLinear(@NonNull View itemView) {
            super(itemView);

            img=itemView.findViewById(R.id.img_linear);
            tv1=itemView.findViewById(R.id.tv1_linear);
            tv2=itemView.findViewById(R.id.tv2_linear);
            ll_item_recycle=itemView.findViewById(R.id.ll_item_recycle);
        }
    }

    public class ViewHolderGrid extends RecyclerView.ViewHolder{

        private ImageView img;
        private TextView tv1,tv2;
        public ViewHolderGrid(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.img_grid);
            tv1=itemView.findViewById(R.id.tv1_grid);
            tv2=itemView.findViewById(R.id.tv2_grid);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnLongItemClickListener {
        void onItemLongClick(int position);
    }
    public interface ClickListener{
        void OnClickListener(int position);
    }

    private OnItemClickListener mClickListener;
    private OnLongItemClickListener mLongItemClickListener;

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    public void setOnLongItemClickListener(OnLongItemClickListener longItemClickListener) {
        this.mLongItemClickListener = longItemClickListener;
    }
}