package com.example.d.linking.Adapter;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.d.linking.Data.LinkListResponse;
import com.example.d.linking.R;
import com.example.d.linking.Server.APIClient;
import com.example.d.linking.Server.APIInterface;
import com.google.gson.Gson;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LinkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Bitmap bitmap;
    private String url;
    private APIInterface service ;
    int[] link_id = new int[1000];

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView read_status;
        TextView meta_title, meta_desc, link_url, link_tag, desc;
        ImageView meta_imgUrl, img_favorite;

        public MyViewHolder(View view){
            super(view);
            read_status = (CircleImageView) view.findViewById(R.id.read_status);
            meta_title = (TextView) view.findViewById(R.id.meta_title);
            meta_desc = (TextView) view.findViewById(R.id.meta_desc);
            link_url = (TextView) view.findViewById(R.id.link_url);
            link_tag = (TextView) view.findViewById(R.id.link_tag);
            meta_imgUrl = (ImageView) view.findViewById(R.id.meta_imgUrl);
            desc = (TextView) view.findViewById(R.id.desc);
            img_favorite = (ImageView) view.findViewById(R.id.img_favorite);

            service= APIClient.getClient().create(APIInterface.class);
        }
    }

    private ArrayList<LinkListResponse> linkList;
    public LinkAdapter(ArrayList<LinkListResponse> linkList){
        this.linkList = linkList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_link, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;

        myViewHolder.link_url.setText(linkList.get(position).getLink());
        myViewHolder.meta_desc.setText(linkList.get(position).getMeta_desc());
        myViewHolder.meta_title.setText(linkList.get(position).getMeta_title());
        myViewHolder.desc.setText(linkList.get(position).getDesc());
        myViewHolder.link_tag.setText(linkList.get(position).getLink_tag());
        if(linkList.get(position).getFavorite_status() == 0){
            myViewHolder.img_favorite.setVisibility(View.INVISIBLE);
        }
        link_id[position] = linkList.get(position).getLink_id();
        if(linkList.get(position).getRead_status() == 1){
            myViewHolder.read_status.setImageResource(R.drawable.read_1);
        }else{
            myViewHolder.read_status.setImageResource(R.drawable.read_0);
        }
        url = linkList.get(position).getMeta_imgUrl();

        LoadImage loadImage = new LoadImage(url);
        bitmap = loadImage.getBitmap();
        myViewHolder.meta_imgUrl.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return linkList.size();
    }

    public void remove(int swipedPosition) {
        linkList.remove(swipedPosition);
        notifyItemRemoved(swipedPosition);

        service.linkdelete(link_id[swipedPosition]).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("링크 삭제 결과",""+new Gson().toJson(response.code()));
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });

    }

    public void favorite(int swipedPosition){

    }
}
