package com.bwei.heqianqian20181230;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.HashMap;

import adapter.ShoppingAdapter;
import api.Apis;
import bean.ShoppingBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import constants.Constants;
import presenter.IPresenterImpl;
import view.IView;

public class MainActivity extends AppCompatActivity implements IView {

    @BindView(R.id.left)
    ImageView left;
    @BindView(R.id.edit_query)
    EditText editQuery;
    @BindView(R.id.right)
    ImageView right;
    @BindView(R.id.comprehensive)
    TextView comprehensive;
    @BindView(R.id.salesvolume)
    TextView salesvolume;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.screen)
    TextView screen;
    @BindView(R.id.contents)
    XRecyclerView contents;
    private ShoppingAdapter adapter;
    private IPresenterImpl iPresenter;
    private int mPage = 1;
    private int mSort = 0;
    private boolean b = false;
    private boolean mLinear = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initPresenter();
        //initView();
        initRecyclerView();
    }


    private void initPresenter() {
        iPresenter = new IPresenterImpl(this);
    }

    private void initView() {


    }

    private void initRecyclerView() {
        contents.setLoadingMoreEnabled(true);
        contents.setPullRefreshEnabled(true);
        contents.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                loadData();
            }

            @Override
            public void onLoadMore() {
                loadData();
            }
        });
        changeRecyclerView();
    }

    private void changeRecyclerView() {
        if (b == false) {
            contents.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
            right.setBackgroundResource(R.drawable.ic_action_right);
            b = true;
        } else if (b = true) {
            contents.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            right.setBackgroundResource(R.drawable.ic_action_shu);
            b = false;
        }
        adapter = new ShoppingAdapter(this, mLinear);
        adapter.setOnItemClickListener(new ShoppingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent=new Intent(MainActivity.this,ShoppingCarActivity.class);
                startActivity(intent);
            }
        });
        contents.setAdapter(adapter);
        mLinear = !mLinear;

    }

    private void loadData() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(Constants.MAP_KEY_SEARCH_PRODUCTS_KEYWORDS, editQuery.getText().toString());
        hashMap.put(Constants.MAP_KEY_SEARCH_PRODUCES_PAGE, String.valueOf(mPage));
        hashMap.put(Constants.MAP_KEY_SEARCH_PRODUCES_SORT, String.valueOf(mSort));
        iPresenter.startRequestPost(Apis.URL_SEARCH_PRODUCTS, hashMap, ShoppingBean.class);
    }

    @OnClick({R.id.left, R.id.right, R.id.comprehensive, R.id.salesvolume, R.id.price, R.id.screen, R.id.contents})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left:
                loadData();
                break;
            case R.id.right:
                if (b == false) {
                    contents.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
                    right.setBackgroundResource(R.drawable.ic_action_right);
                    b = true;
                } else if (b = true) {
                    contents.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                    right.setBackgroundResource(R.drawable.ic_action_shu);
                    b = false;
                }
                break;
            case R.id.comprehensive:
                if (!comprehensive.isSelected()) {
                    mPage = 1;
                    mSort = 0;
                    loadData();
                    comprehensive.setSelected(true);
                    salesvolume.setSelected(false);
                    price.setSelected(false);
                }
                break;
            case R.id.salesvolume:
                if (!salesvolume.isSelected()) {
                    mPage = 1;
                    mSort = 1;
                    loadData();
                    comprehensive.setSelected(false);
                    salesvolume.setSelected(true);
                    price.setSelected(false);
                }
                break;
            case R.id.price:
                if (!price.isSelected()) {
                    mPage = 1;
                    mSort = 2;
                    loadData();
                    comprehensive.setSelected(false);
                    salesvolume.setSelected(false);
                    price.setSelected(true);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void getDataSuccess(Object data) {
        if (data instanceof ShoppingBean) {
            ShoppingBean bean = (ShoppingBean) data;
            if (bean == null || !bean.isSuccess()) {
                Toast.makeText(MainActivity.this, bean.getMsg(), Toast.LENGTH_LONG).show();
            } else {
                if (mPage == 1) {
                    adapter.setData(bean.getData());
                } else {
                    adapter.addData(bean.getData());
                }
                mPage++;
                contents.loadMoreComplete();
                contents.refreshComplete();
            }
        }
    }

    @Override
    public void getDataFail(String error) {
        Toast.makeText(MainActivity.this,error,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        iPresenter.onDetach();
    }
}
