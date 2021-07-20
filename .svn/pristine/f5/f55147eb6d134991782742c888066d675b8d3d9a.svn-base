package djjy.stlx.xtlx;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.hsdj.MyApplication;
import com.example.hsdj.R;
import com.google.gson.Gson;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import djjy.stlx.DoubleChoiseFragment;
import djjy.stlx.ExercisesInfo;
import djjy.stlx.SingleChoiseFragment;
import djjy.stlx.Topic;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import utils.AccessKeyUtils;
import utils.UrlUtils;

public class PracticeActivity extends AppCompatActivity {
    private ImageButton btn_return;
    private ViewPager vp_answer;
    private Button _btn_previous,_btn_next;

    private ArrayList<Fragment> fragmentlists = new ArrayList<>();
    /*题目信息*/
    private List<Topic> problemList = new ArrayList<Topic>();
    private int beforePager = -1;
    private int nowpager = 0;
    //记录上一次滑动的positionOffsetPixels值
    private int lastValue = -1;
    private Handler handler = new Handler(Looper.getMainLooper());
    //是否左滑
    private boolean isLeft=true;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        btn_return=findViewById(R.id.btn_return);
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        _btn_previous=findViewById(R.id._btn_previous);
        _btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nowpager == 0) {
                    Toast.makeText(PracticeActivity.this, "已经是第一题", Toast.LENGTH_SHORT).show();
                } else {
                    vp_answer.setCurrentItem(--nowpager);
                }
            }
        });
        _btn_next=findViewById(R.id._btn_next);
        _btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nowpager == fragmentlists.size()) {
                    Toast.makeText(PracticeActivity.this, "已经是最后一题", Toast.LENGTH_SHORT).show();
                } else {
                    vp_answer.setCurrentItem(++nowpager);
                }
            }
        });

        vp_answer=findViewById(R.id.vp_answer);
        try {
            initView();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        vp_answer.setOnPageChangeListener(new PracticeActivity.MyOnPageChangeListener());
    }

    private void initView() throws IOException, JSONException {
        //进度条对话框
        final ProgressDialog progressDialog = new ProgressDialog(PracticeActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("获取题目中...");
        progressDialog.show();
        /*查找试卷列表: 异步post请求*/
        MyApplication myApplication= (MyApplication) getApplication().getApplicationContext();
        String accessKey = AccessKeyUtils.getAccessKey(myApplication.getMenuApplication(), 53);
        String json="{\"businessJson\":{\"page\":1,\"rows\":10},\"controlJson\":{\"menuId\":53,\"opt\":\"c\",\"accessKey\":\""+accessKey+"\",\"token\":\""+myApplication.getToken()+"\",\"opt_desc\":\"获得习题\"}}";
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(UrlUtils.BASE_URL+"/app/hsdj/xxjymgr/views/queryTopics")
                .post(body)
                .build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            //递归解析menu
            Gson gson=new Gson();
            ExercisesInfo exercisesInfo = gson.fromJson(response.body().string(), ExercisesInfo.class);
            for (int i = 0; i < exercisesInfo.getData().getRows().size(); i++) {
                Topic topic=new Topic();
                topic.setTopic_title(exercisesInfo.getData().getRows().get(i).getTopic_title());
                topic.setTopic_score(exercisesInfo.getData().getRows().get(i).getTopic_score());
                topic.setOption_def(exercisesInfo.getData().getRows().get(i).getOption_def());
                topic.setAnalysis(exercisesInfo.getData().getRows().get(i).getAnalysis());
                if ("单选题".equals(exercisesInfo.getData().getRows().get(i).getTopic_types())){
                    topic.setType(0);
                }else if ("多选题".equals(exercisesInfo.getData().getRows().get(i).getTopic_types())){
                    topic.setType(1);
                }
                problemList.add(topic);
            }
            //调用不同的布局来展示试题
            for (int i = 0; i < problemList.size(); i++) {
                switch(problemList.get(i).getType()){
                    case 0 :
                        fragmentlists.add(new SingleChoiseFragment(problemList.get(i),i,true));
                        break;
                    case 1 :
                        fragmentlists.add(new DoubleChoiseFragment(problemList.get(i),i,true));
                        break;
                    default :
                        break;
                }
            }
            //设置适配器
            handler.post(new MyThread());
            progressDialog.dismiss();
            progressDialog.dismiss();
        } else {
            Toast.makeText(myApplication, "题目获取失败！", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            finish();
        }

        /*OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("page","1");
        formBody.add("rows","10");
        Request okRequest = new Request.Builder().url("http://58.51.240.150:8000/app/hsdj/xxjymgr/views/queryTopics").post(formBody.build()).build();
        okHttpClient.newCall(okRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                progressDialog.dismiss();
                Toast.makeText(PracticeActivity.this,"查询数据失败！",Toast.LENGTH_SHORT).show();
                finish();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson=new Gson();
                ExercisesInfo exercisesInfo=gson.fromJson(response.body().string(),ExercisesInfo.class);
                if (exercisesInfo.getCode()==0){
                    //查询成功
                    for (int i = 0; i < exercisesInfo.getData().getRows().size(); i++) {
                        Topic topic=new Topic();
                        topic.setTopic_title(exercisesInfo.getData().getRows().get(i).getTopic_title());
                        topic.setTopic_score(exercisesInfo.getData().getRows().get(i).getTopic_score());
                        topic.setOption_def(exercisesInfo.getData().getRows().get(i).getOption_def());
                        topic.setAnalysis(exercisesInfo.getData().getRows().get(i).getAnalysis());
                        if ("单选题".equals(exercisesInfo.getData().getRows().get(i).getTopic_types())){
                            topic.setType(0);
                        }else if ("多选题".equals(exercisesInfo.getData().getRows().get(i).getTopic_types())){
                            topic.setType(1);
                        }
                        problemList.add(topic);
                    }
                    //调用不同的布局来展示试题
                    for (int i = 0; i < problemList.size(); i++) {
                        switch(problemList.get(i).getType()){
                            case 0 :
                                fragmentlists.add(new SingleChoiseFragment(problemList.get(i),i,true));
                                break;
                            case 1 :
                                fragmentlists.add(new DoubleChoiseFragment(problemList.get(i),i,true));
                                break;
                            default :
                                break;
                        }
                    }
                    //设置适配器
                    handler.post(new MyThread());
                    progressDialog.dismiss();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(PracticeActivity.this,"查询数据失败！",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });*/
    }

    /**
     * viewpager适配器
     */
    class MainAdapter extends FragmentPagerAdapter {

        public MainAdapter(FragmentManager fm) {
            super(fm);
        }


        //获取条目
        @Override
        public Fragment getItem(int position) {
            return fragmentlists.get(position);
        }

        //数目
        @Override
        public int getCount() {
            return fragmentlists.size();
        }
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (positionOffset != 0) {
                if (lastValue >= positionOffsetPixels) {
                    //右滑
                    isLeft=false;
                } else if (lastValue < positionOffsetPixels) {
                    //左滑
                    isLeft=true;
                }
            }
            lastValue = positionOffsetPixels;
        }

        @Override
        public void onPageSelected(int position) {
            nowpager = position;
            if (isLeft==false){//右滑
                beforePager=nowpager+1;
            }else if (isLeft==true){//左滑
                beforePager=nowpager-1;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    class MyThread implements Runnable {
        public void run() {
            //原来想要执行的代码
            vp_answer.setAdapter(new MainAdapter(getSupportFragmentManager()));
        }
    }
}