package com.example.popo.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    class newsReq{
        String accoundID;
        int lastIndex;
        int count;
        int[] type;

         newsReq(String accoundID,int lastIndex,int count,int[] type){
            this.accoundID=accoundID;
            this.count=count;
            this.lastIndex=lastIndex;
            this.type=type;
        }
    }
    class newsRes{
        int status;
        String[] errMsgs;
        result_newsRes results;
        class result_newsRes{
            int[] count;
            content_newsRes[] content;
            class content_newsRes{
                int type;
                String url;
                String imageUrl;
                String title;
                String content;
                int index;
                String publisher;
            }
        }
    }
     class myAdaper extends BaseAdapter{
        newsRes.result_newsRes.content_newsRes[] news;
        int view;

        myAdaper( newsRes.result_newsRes.content_newsRes[] news,int view ){
            this.news=news;
            this.view=view;
        }

         @Override
         public int getCount() {
             return news.length;
         }

         @Override
         public Object getItem(int position) {
             return news[position];
         }

         @Override
         public long getItemId(int position) {
             return 0;
         }

         @Override
         public View getView(int position, View convertView, ViewGroup parent) {
             convertView =getLayoutInflater().inflate(view,parent,false);
             TextView title=(TextView)convertView.findViewById(R.id.title);
             title.setText(news[position].title);
             return  convertView;
         }
     }
    Gson gson=new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //init news req
        int[] type={3,4};
        newsReq req=new newsReq("ddd",-1,20,type);
        //do api

        String reqJson=gson.toJson(req);
        OkHttpClient myOkHttpClient=new OkHttpClient();
        RequestBody body=RequestBody.create(MediaType.parse("application/json; charset=utf-8"),reqJson);
        Request request=new Request.Builder()
                .url("https://api-dev.bluenet-ride.com/v2_0/lineBot/news/get")
                .post(body)
                .build();
        Call call=myOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String resJson=response.body().string();
                newsRes res=gson.fromJson(resJson,newsRes.class);
                if(res.status==0){
                    ListView newsListView=(ListView)findViewById(R.id.newsListView);
                    myAdaper adaper=new myAdaper(res.results.content,R.layout.newsview);
                    newsListView.setAdapter(adaper);
                }

            }
        });

    }
}
