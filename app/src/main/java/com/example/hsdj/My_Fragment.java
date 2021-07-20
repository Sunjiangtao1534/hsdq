package com.example.hsdj;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import login.LoginActivity;

public class My_Fragment extends Fragment {
    private ImageView h_back;
    private ImageView h_head;
    private TextView user_name;
    private LinearLayout edit_password;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.my_fragment,null);
        h_back=view.findViewById(R.id.h_back);
        h_head=view.findViewById(R.id.h_head);
        user_name=view.findViewById(R.id.user_name);
        edit_password=view.findViewById(R.id.edit_password);

        //设置背景磨砂效果
        Glide.with(getActivity()).load(R.drawable.user)
                .bitmapTransform(new BlurTransformation(getActivity(), 25), new CenterCrop(getActivity()))
                .into(h_back);
        //设置圆形图像
        Glide.with(getActivity()).load(R.drawable.user)
                .bitmapTransform(new CropCircleTransformation(getActivity()))
                .into(h_head);

        //验证是否登录
        MyApplication application = (MyApplication) getActivity().getApplicationContext();
        if (application.getUsername()==null||"".equals(application.getUsername())){
            Toast.makeText(application, "请先登录！", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }else{
            user_name.setText(application.getUserdesc());
        }

        edit_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }
}
