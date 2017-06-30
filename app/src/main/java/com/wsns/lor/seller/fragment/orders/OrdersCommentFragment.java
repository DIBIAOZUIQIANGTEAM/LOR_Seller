package com.wsns.lor.seller.fragment.orders;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.wsns.lor.seller.R;
import com.wsns.lor.seller.activity.order.CommentReplyEditActivity;
import com.wsns.lor.seller.adapter.CommentListAdapter;
import com.wsns.lor.seller.entity.OrdersComment;
import com.wsns.lor.seller.http.HttpMethods;
import com.wsns.lor.seller.http.subscribers.ProgressSubscriber;
import com.wsns.lor.seller.http.subscribers.SubscriberOnNextListener;
import com.wsns.lor.seller.view.layout.VRefreshLayout;

import java.util.ArrayList;
import java.util.List;

//评价列表
public class OrdersCommentFragment extends Fragment {

    View view;
    ListView listView;
    int page = 0;
    int NOT_MORE_PAGE = -1;
    View LoadMore;
    TextView textLoadMore;
    CommentListAdapter listAdapter;
    Activity activity;
    VRefreshLayout mRefreshLayout;
    List<OrdersComment> mComments = new ArrayList<OrdersComment>();
    private SubscriberOnNextListener getCommentsOnNext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {

            activity = getActivity();

            view = inflater.inflate(R.layout.fragment_records_consumption, null);
            LoadMore = inflater.inflate(R.layout.list_foot, null);
            textLoadMore = (TextView) LoadMore.findViewById(R.id.loadmore);

            listView = (ListView) view.findViewById(R.id.consumption_list);
//            listView.addFooterView(LoadMore);
            listAdapter = new CommentListAdapter(activity, mComments);
            listView.setAdapter(listAdapter);

            LoadMore.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (page != NOT_MORE_PAGE) {
                        load();
                    }
                }
            });
            initHeaderView();
            getCommentsOnNext = new SubscriberOnNextListener<List<OrdersComment>>() {
                @Override
                public void onNext(List<OrdersComment> comments) {
                    mRefreshLayout.refreshComplete();
                    setDate(comments);
                }
            };
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    System.out.println("mComments.get(i).getReply()"+mComments.get(i).getReply());
                    if (mComments.get(i).getReply()!=null)
                        return;
                    Intent intent=new Intent(activity, CommentReplyEditActivity.class);
                    intent.putExtra("orders_id",mComments.get(i).getOrders().getId()+"");
                    startActivity(intent);

                }
            });
        }

        return view;
    }

    private void setDate(List<OrdersComment> comments) {
        mComments.clear();
        mComments.addAll(comments);
        listAdapter.notifyDataSetChanged();

    }

    private void initHeaderView() {
        mRefreshLayout = (VRefreshLayout) view.findViewById(R.id.refresh_layout);
        if (mRefreshLayout != null) {
            mRefreshLayout.setBackgroundColor(Color.DKGRAY);
            mRefreshLayout.setAutoRefreshDuration(400);
            mRefreshLayout.setRatioOfHeaderHeightToReach(1.5f);
            mRefreshLayout.addOnRefreshListener(new VRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    page = 0;
                    load();
                }
            });
        }

        mRefreshLayout.setHeaderView(mRefreshLayout.getDefaultHeaderView());
        mRefreshLayout.setBackgroundColor(Color.WHITE);
    }


    @Override
    public void onResume() {
        super.onResume();
        load();
    }

    void load() {

        HttpMethods.getInstance().getComments(new ProgressSubscriber(getCommentsOnNext, activity, false));

    }
}