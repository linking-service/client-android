package com.example.d.linking.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.d.linking.Activity.Follow;
import com.example.d.linking.Activity.Fragment_following;
import com.example.d.linking.Data.FollowerResponse;
import com.example.d.linking.R;
import com.example.d.linking.Server.APIClient;
import com.example.d.linking.Server.APIInterface;
import com.google.gson.Gson;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class FollowingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private SharedPreferences preferences;
    private APIInterface service;
    String display_name;
    Context mContext;

    private ArrayList<FollowerResponse> following;
    public FollowingAdapter(ArrayList<FollowerResponse> following){
        this.following = following;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView following_display, following_name;
        Button following_delete;

        public MyViewHolder(View view){
            super(view);
            mContext = view.getContext();

            preferences = mContext.getSharedPreferences("user", MODE_PRIVATE);
            display_name = preferences.getString("display_name","");

            following_display = (TextView) view.findViewById(R.id.following_display);
            following_name = (TextView) view.findViewById(R.id.following_name);
            following_delete = (Button) view.findViewById(R.id.following_delete);

            service= APIClient.getClient().create(APIInterface.class);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_following, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final FollowingAdapter.MyViewHolder myViewHolder = (FollowingAdapter.MyViewHolder) holder;

        myViewHolder.following_display.setText(following.get(position).getDisplay_name());
        myViewHolder.following_name.setText(following.get(position).getName());

        myViewHolder.following_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followingDelete(display_name, following.get(position).getDisplay_name());
            }
        });

    }

    @Override
    public int getItemCount() {
        return following.size();
    }

    public void followingDelete(String display_name, String following_display){
        service.followingdelete(display_name,following_display).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("팔로잉 삭제 결과",""+new Gson().toJson(response.code()));
                Toast.makeText(mContext, "Delete completed", Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }
}