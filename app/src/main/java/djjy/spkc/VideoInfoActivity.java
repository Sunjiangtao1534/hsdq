package djjy.spkc;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hsdj.MyApplication;
import com.example.hsdj.R;

import java.io.IOException;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import utils.AccessKeyUtils;
import utils.UrlUtils;

public class VideoInfoActivity extends AppCompatActivity {
    private ImageButton btn_return;
    private ListView list_view;

    private VideoListAdapter mAdapter;
    private SensorEventListener mSensorEventListener;
    private SensorManager mSensorManager;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_info);
        long startTime = SystemClock.elapsedRealtime();


        btn_return=findViewById(R.id.btn_return);
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long endTime = SystemClock.elapsedRealtime();
                long stayTime = endTime-startTime;
                if (stayTime>120000){
                    MyApplication myApplication= (MyApplication) getApplication().getApplicationContext();
                    String accessKey = AccessKeyUtils.getAccessKey(myApplication.getMenuApplication(), 49);
                    String json="{\"businessJson\":{\"user_id\":"+myApplication.getUserId()+",\"type\":\"learn_video\",\"plan_id\":\""+getIntent().getStringExtra("plan_id")+"\",\"learn_time_length\":\""+String.valueOf(stayTime/1000)+"\",\"score\":"+getIntent().getStringExtra("file_score")+",\"content_id\":\""+getIntent().getStringExtra("id")+"\"},\"controlJson\":{\"menuId\":49,\"opt\":\"c\",\"accessKey\":\""+accessKey+"\",\"token\":\""+myApplication.getToken()+"\",\"opt_desc\":\"上传视频的学习分数\"}}";
                    RequestBody body = RequestBody.create(JSON, json);
                    Request request = new Request.Builder()
                            .url(UrlUtils.BASE_URL+"/app/hsdj/xxjymgr/views/addScoreRecord")
                            .post(body)
                            .build();
                    OkHttpClient client = new OkHttpClient();
                    try {
                        Response response = client.newCall(request).execute();
                        if (response.isSuccessful()){
                            Log.d("response",response.body().string());
                            Toast.makeText(myApplication, "视频分数上传成功", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(VideoInfoActivity.this, "小于2分钟不计入学习积分", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });

        list_view = (ListView) findViewById(R.id.list_view);
        mAdapter = new VideoListAdapter(this,getIntent().getStringExtra("file_path"),getIntent().getStringExtra("remark"));
        list_view.setAdapter(mAdapter);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorEventListener = new JCVideoPlayer.JCAutoFullscreenListener();
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(mSensorEventListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mSensorEventListener);
        JCVideoPlayer.releaseAllVideos();
    }
}