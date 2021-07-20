package djjy.stlx;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import utils.AccessKeyUtils;
import utils.UrlUtils;

public class ExercisesInfoActivity extends AppCompatActivity implements Chronometer.OnChronometerTickListener{
    private ImageButton btn_return;
    private ViewPager vp_answer;
    private Chronometer chronometer;
    private Button _btn_previous;
    private Button _btn_submit;
    private Button _btn_next;

    private ArrayList<Fragment> fragmentlists = new ArrayList<>();
    /*题目信息*/
    private ArrayList<Topic> problemList = new ArrayList<Topic>();
    /*用户答题信息:最多500题*/
    static Answer[] answerArr=new Answer[500];
    private AlertDialog.Builder builder;
    private int beforePager = -1;
    private int nowpager = 0;
    private int minute = 0;
    private int second = 0;
    private Handler handler = new Handler(Looper.getMainLooper());
    //记录上一次滑动的positionOffsetPixels值
    private int lastValue = -1;
    //是否左滑
    private boolean isLeft=true;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises_info);

        btn_return=findViewById(R.id.btn_return);
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish是取消当前页面
                finish();
            }
        });

        _btn_previous=findViewById(R.id._btn_previous);
        _btn_submit=findViewById(R.id._btn_submit);
        _btn_next=findViewById(R.id._btn_next);
        _btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nowpager == 0) {
                    Toast.makeText(ExercisesInfoActivity.this, "已经是第一题", Toast.LENGTH_SHORT).show();
                } else {
                    vp_answer.setCurrentItem(--nowpager);
                }
            }
        });
        _btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*判断试卷是否做完*/
                beforePager=nowpager;
                setData();
                //判断试卷是否做完
                /*for (int i = 0; i < answerArr.length; i++) {
                    if ((answerArr[i]==null)||answerArr[i].getOption()==null){
                        System.out.println("第"+(i+1)+"题未完成！");
                        Toast.makeText(ExercisesInfoActivity.this, "第"+(i+1)+"题未完成！", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }*/
                //  否则初始化并展示提交对话框
                initAlertDialog();
                builder.show();
            }
        });
        _btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nowpager == fragmentlists.size()) {
                    Toast.makeText(ExercisesInfoActivity.this, "已经是最后一题", Toast.LENGTH_SHORT).show();
                } else {
                    vp_answer.setCurrentItem(++nowpager);
                }
            }
        });

        vp_answer=findViewById(R.id.vp_answer);
        try {
            initData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        vp_answer.setOnPageChangeListener(new MyOnPageChangeListener());
        setChronometer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //避免重复打开页面时数据重复填写
    }


    /*设置定时器*/
    private void setChronometer() {
        chronometer=findViewById(R.id._chro_exam);
        chronometer.setText(nowtime());
        chronometer.start();
        chronometer.setOnChronometerTickListener(this);
    }

    /*定时器规则*/
    @Override
    public void onChronometerTick(Chronometer chronometer) {
        second++;
        if (second == 59) {
            minute++;
            second = 00;
        }
    }

    /**
     * 现在时间
     *
     * @return
     */
    private String nowtime() {
        if (second < 10) {
            return (minute + ":0" + second);
        } else {
            return (minute + ":" + second);
        }
    }

    // 弹出是否确认交卷的对话框
    private void initAlertDialog() {
        //新建对话框
        builder = new AlertDialog.Builder(ExercisesInfoActivity.this);
        boolean isCompled=true;
        int index = 0;
        for (int i = 0; i < fragmentlists.size(); i++) {
            if ((answerArr[i]==null)||answerArr[i].getOption()==null){
                isCompled=false;
                index=i;
                break;
            }
        }
        if (isCompled){
            builder.setTitle("提示");
            builder.setMessage("是否确定交卷?");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final ProgressDialog progressDialog = new ProgressDialog(ExercisesInfoActivity.this);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("分数上传中...");
                    progressDialog.show();
                    //计算分值
                    float score = 0;
                    for (int i = 0; i < fragmentlists.size(); i++) {
                        score += answerArr[i].getScore();
                    }
                    //异步post：上传分值
                    MyApplication myApplication= (MyApplication) getApplication().getApplicationContext();
                    String accessKey = AccessKeyUtils.getAccessKey(myApplication.getMenuApplication(), 53);
                    String json="{\"businessJson\":{\"user_id\":"+myApplication.getUserId()+",\"type\":\"learn_exam\",\"plan_id\":\""+getIntent().getExtras().get("plan_id").toString()+"\",\"learn_time_length\":\""+String.valueOf(minute * 60 + second)+"\",\"score\":"+String.valueOf(score)+",\"content_id\":\""+getIntent().getExtras().get("exam_id").toString()+"\"},\"controlJson\":{\"menuId\":53,\"opt\":\"c\",\"accessKey\":\""+accessKey+"\",\"token\":\""+myApplication.getToken()+"\",\"opt_desc\":\"上传试卷的学习分数\"}}";
                    RequestBody body = RequestBody.create(JSON, json);
                    Request request = new Request.Builder()
                            .url(UrlUtils.BASE_URL+"/app/hsdj/xxjymgr/views/addScoreRecord")
                            .post(body)
                            .build();
                    OkHttpClient client = new OkHttpClient();
                    try {
                        Response response = client.newCall(request).execute();
                        if (response.isSuccessful()) {
                            Log.d("response",response.body().string());
                            Toast.makeText(myApplication, "试卷分数上传成功", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            finish();
//                            Intent intent=new Intent(ExercisesInfoActivity.this,GradeActivity.class);
//                            intent.putExtra("problemList",problemList);
//                            intent.putExtra("grade",score);
//                            intent.putExtra("answerArr",answerArr);
//                            startActivity(intent);
                        }else{
                            Toast.makeText(myApplication, "试卷分数上传失败", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            finish();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).setNegativeButton("取消", null);
        }else{
            builder.setTitle("提示");
            builder.setMessage("第"+(index+1)+"题未完成");
            builder.setNegativeButton("确定", null);
        }

    }

    private void initData() throws IOException {
        //进度条对话框
        final ProgressDialog progressDialog = new ProgressDialog(ExercisesInfoActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("获取题目中...");
        progressDialog.show();

        /*查找试卷下的习题: 异步post请求*/
        MyApplication myApplication= (MyApplication) getApplication().getApplicationContext();
        String accessKey = AccessKeyUtils.getAccessKey(myApplication.getMenuApplication(), 53);
        String json="{\"businessJson\":{\"exam_id\":"+getIntent().getExtras().get("exam_id").toString().trim()+"},\"controlJson\":{\"menuId\":53,\"opt\":\"c\",\"accessKey\":\""+accessKey+"\",\"token\":\""+myApplication.getToken()+"\",\"opt_desc\":\"获取该试卷所属题目\"}}";
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(UrlUtils.BASE_URL+"/app/hsdj/xxjymgr/views/getTopicsByExam")
                .post(body)
                .build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
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
                            fragmentlists.add(new SingleChoiseFragment(problemList.get(i),i,false));
                            break;
                        case 1 :
                            fragmentlists.add(new DoubleChoiseFragment(problemList.get(i),i,false));
                            break;
                        default :
                            break;
                    }
                }
                //设置适配器
                handler.post(new MyThread());
                progressDialog.dismiss();
        }else{
            Toast.makeText(myApplication, "试卷列表获取失败", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            finish();
        }
    }
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

    /**
     * viewpager监听事件
     */
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
            System.out.println("nowPage"+nowpager+"  beforePage"+beforePager);
            setData();
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    private void setData(){
        System.out.println(beforePager);
        BaseFragment fragment = (BaseFragment) fragmentlists.get(beforePager);
        if (fragment instanceof SingleChoiseFragment){
            answerArr[beforePager]=fragment.getAnswer();
        }else if (fragment instanceof DoubleChoiseFragment){
            String option="";
            CheckBox checkBox1= fragment.getActivity().findViewById(R.id.checkBox1);
            CheckBox checkBox2= fragment.getActivity().findViewById(R.id.checkBox2);
            CheckBox checkBox3= fragment.getActivity().findViewById(R.id.checkBox3);
            CheckBox checkBox4= fragment.getActivity().findViewById(R.id.checkBox4);
            CheckBox checkBox5= fragment.getActivity().findViewById(R.id.checkBox5);
            CheckBox checkBox6= fragment.getActivity().findViewById(R.id.checkBox6);
            CheckBox checkBox7= fragment.getActivity().findViewById(R.id.checkBox7);
            CheckBox checkBox8= fragment.getActivity().findViewById(R.id.checkBox8);
            List<String> optionId=new ArrayList<>();
            if (checkBox1.isChecked()){
                option += "A";
                optionId.add("0");
            }if (checkBox2.isChecked()){
                option += "B";
                optionId.add("1");
            }if (checkBox3.isChecked()){
                option += "C";
                optionId.add("2");
            }if (checkBox4.isChecked()){
                option += "D";
                optionId.add("3");
            }if (checkBox5.isChecked()){
                option += "E";
                optionId.add("4");
            }if (checkBox6.isChecked()){
                option += "F";
                optionId.add("5");
            }if (checkBox7.isChecked()){
                option += "G";
                optionId.add("6");
            }if (checkBox8.isChecked()){
                option += "H";
                optionId.add("7");
            }
            Answer answer=new Answer();
            //设置答案
            answer.setOption(option);
            String correctOption="";
            for (int i = 0; i < optionId.size(); i++) {
                if("False".equalsIgnoreCase(fragment.getTopic().getOptions().get(Integer.parseInt(optionId.get(i))).get("checkCurr"))){
                    answer.setScore(0);
                    break;
                }
                if(i!=optionId.size()-1&&"True".equalsIgnoreCase(fragment.getTopic().getOptions().get(Integer.parseInt(optionId.get(i))).get("checkCurr"))){
                    answer.setScore(fragment.getTopic().getTopic_score()/2);
                    continue;
                }
                if (i==optionId.size()-1&&"True".equalsIgnoreCase(fragment.getTopic().getOptions().get(Integer.parseInt(optionId.get(i))).get("checkCurr"))){
                    answer.setScore(fragment.getTopic().getTopic_score());
                }
            }
            Topic topic = ((DoubleChoiseFragment) fragmentlists.get(beforePager)).getTopic();
            for (int i = 0; i < topic.getOptions().size(); i++) {
                if ("True".equalsIgnoreCase(topic.getOptions().get(i).get("checkCurr"))){
                    switch(i){
                        case 0:
                            correctOption+="A";
                            break;
                        case 1:
                            correctOption+="B";
                            break;
                        case 2:
                            correctOption+="C";
                            break;
                        case 3:
                            correctOption+="D";
                            break;
                        case 4:
                            correctOption+="E";
                            break;
                        case 5:
                            correctOption+="F";
                            break;
                        case 6:
                            correctOption+="G";
                            break;
                        case 7:
                            correctOption+="H";
                            break;
                    }
                }
            }
            answer.setCorrectOption(correctOption);
            answerArr[beforePager]=answer;
        }
    }

    class MyThread implements Runnable {
        public void run() {
            //原来想要执行的代码
            vp_answer.setAdapter(new MainAdapter(getSupportFragmentManager()));
        }
    }
}