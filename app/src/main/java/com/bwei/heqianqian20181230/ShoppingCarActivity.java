package com.bwei.heqianqian20181230;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.ShoppingCarAdapter;
import api.Apis;
import bean.ShoppingCarBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import constants.Constants;
import presenter.IPresenterImpl;
import view.IView;

public class ShoppingCarActivity extends AppCompatActivity implements IView {

    @BindView(R.id.bannerViewPager)
    ViewPager bannerViewPager;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.original)
    TextView original;
    @BindView(R.id.discounts)
    TextView discounts;
    private IPresenterImpl iPresenter;
    private ShoppingCarAdapter adapter;
    private int mPage;
    private int pid;
    private List<String> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_car);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mPage = 1;
        pid=10;
        iPresenter = new IPresenterImpl(this);
        adapter = new ShoppingCarAdapter(this);
        bannerViewPager.setAdapter(adapter);
        initData();
    }

    private void initData() {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(Constants.MAP_KEY_SEARCH_PRODUCES_PAGE,String.valueOf(mPage));
        hashMap.put(Constants.MAP_KEY_PRODUCTS_DETAIL_PID,String.valueOf(pid));
        iPresenter.startRequestPost(Apis.URL_LOGIN_IMAGE,hashMap, ShoppingCarBean.class);
    }
    @Override
    public void getDataSuccess(Object data) {
        if (data instanceof ShoppingCarBean) {
            ShoppingCarBean bean = (ShoppingCarBean) data;
            if (bean == null || !bean.isSuccess()) {
                Toast.makeText(ShoppingCarActivity.this, bean.getMsg(), Toast.LENGTH_LONG).show();
            } else {
                sub(bean.getData().getImages());
                adapter.setData(list);
                title.setText(bean.getData().getTitle());
                original.setText("原价：" + bean.getData().getPrice());
                discounts.setText("优惠价：" + bean.getData().getBargainPrice());
                int center = adapter.getCount() / 2;
                center = center - center % (list.size());
                bannerViewPager.setCurrentItem(center);
                startLooper();
            }
        }
    }

    @Override
    public void getDataFail(String error) {
        Toast.makeText(ShoppingCarActivity.this,error,Toast.LENGTH_LONG).show();
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            bannerViewPager.setCurrentItem(bannerViewPager.getCurrentItem() + 1);
            handler.sendEmptyMessageDelayed(0, 2000);
        }
    };

    private void startLooper() {
        handler.removeCallbacksAndMessages(null);
        handler.sendEmptyMessageDelayed(0, 2000);
    }

    private void sub(String str) {
        int index = str.indexOf("|");
        if (index >= 0) {
            String strLeft = str.substring(0, index);
            list.add(strLeft);
            sub(str.substring(index + 1, str.length()));
        } else {
            list.add(str);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        iPresenter.onDetach();
    }
}