package com.example.fragementbestpractice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NewsTitleFragment extends Fragment {
    private boolean isTwoPane;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.news_title_frag,container,false); //加载 news_title_frag 界面
        RecyclerView newsTitleRecycleView=(RecyclerView) view.findViewById(R.id.news_title_recycler_view); //获取 RecyclerView 实例
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity()); //返回与当前片段相关联的活动，设置为线性布局控制
        newsTitleRecycleView.setLayoutManager(layoutManager); //将 news_title_frag 界面内的 RecyclerView 控件内部的控件设置为线性布局
        NewsAdapter adapter =new NewsAdapter(getNews()); //将自动生成的 50个 新闻列表内容 加载到声明的  adapter 对象中
        newsTitleRecycleView.setAdapter(adapter); //将adapter 适配器的内容加载到 news_title_frag 界面内部 的RecyclerView 控件内部
        return view; //返回当前界面的实例对象
    }
    private List<News> getNews(){ //产生50个 新闻实例 存入 List 列表内
        List<News> newsList=new ArrayList<>();
        for(int i=1;i<=50;i++){
            News news =new News();
            news.setTitle("This is news title"+i);
            news.setContent(getRandomLengthContent("This is news content"+i+"."));
            newsList.add(news);
        }
        return newsList; //返回产生的整个列表对象
    }
    private String getRandomLengthContent(String content){ //随机产生不定长度的新闻内容
        Random random =new Random();
        int length =random.nextInt(20)+1;
        StringBuilder builder=new StringBuilder();
        for(int i=0;i<length;i++){
            builder.append(content);
        }
        return builder.toString();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getActivity().findViewById(R.id.news_content_layout)!=null)
        {
            isTwoPane =true; //可以找到news_content_layout 为 双页布局
        }
        else
        {
            isTwoPane =false; //找不到news_content_layout 为 单页布局
        }
    }
    class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
        private List<News> mNewsList;
        class ViewHolder extends RecyclerView.ViewHolder{
            TextView newsTitleText;
            public ViewHolder(View view){
                super(view);
                newsTitleText =(TextView) view.findViewById(R.id.news_title);
            }
        }
        public NewsAdapter(List<News> newsList){
            mNewsList=newsList;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item,parent,false);
            final ViewHolder holder =new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    News news=mNewsList.get(holder.getAdapterPosition());
                    if(isTwoPane){
                        //如果是双页模式，则刷新NewsContentFragment 中的内容
                        NewsContentFragment newsContentFragment=(NewsContentFragment) getFragmentManager().findFragmentById(R.id.news_content_fragment);
                        newsContentFragment.refresh(news.getTitle(),news.getContent());
                    }
                    else
                    {
                        //如果是单页模式，则直接启动NewsContentActivity
                        NewsContentActivity.actionStart(getActivity(),news.getTitle(),news.getContent());
                    }
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            News news=mNewsList.get(position);
            holder.newsTitleText.setText(news.getTitle());
        }

        @Override
        public int getItemCount() {
            return mNewsList.size();
        }
    }

}
