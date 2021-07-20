package djjy.spkc;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hsdj.MyApplication;
import com.example.hsdj.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import utils.AccessKeyUtils;
import utils.UrlUtils;

public class VideoActivity extends AppCompatActivity {
    private ImageButton btn_return;
    private ListView list_view;

    private List<HashMap<String, Object>> list_show = new ArrayList<HashMap<String,Object>>();
    private VideoListAdapter adapterVideoList;
    /*传感器*/
    private SensorManager sensorManager;
    private SensorEventListener sensorEventListener;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        MyApplication application = (MyApplication) getApplicationContext();

        btn_return=findViewById(R.id.btn_return);
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*获取视频课程列表*/
        final ProgressDialog progressDialog = new ProgressDialog(VideoActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("获取视频列表中...");
        progressDialog.show();
        try{
            MyApplication myApplication= (MyApplication) getApplication().getApplicationContext();
            String accessKey = AccessKeyUtils.getAccessKey(myApplication.getMenuApplication(), 49);
            String json="{\"businessJson\":{\"ob.id\":"+myApplication.getUserId()+"},\"controlJson\":{\"menuId\":49,\"opt\":\"c\",\"accessKey\":\""+accessKey+"\",\"token\":\""+myApplication.getToken()+"\",\"opt_desc\":\"获取视频列表\"}}";
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(UrlUtils.BASE_URL+"/app/hsdj/xxjymgr/views/getPlanByVideo")
                    .post(body)
                    .build();
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                initData(response.body().string());
                progressDialog.dismiss();
            }else{
                Toast.makeText(myApplication, "获取视频列表失败", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                finish();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        initView();
    }

    private void initData(String result){
        Gson gson = new Gson();
        Video b = gson.fromJson(result,Video.class);
        for (int i = 0; i < b.getData().getRows().size(); i++) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("remark",b.getData().getRows().get(i).getFile_name());
            item.put("file_path",UrlUtils.BASE_URL+"/media/"+b.getData().getRows().get(i).getFile_path()+
                    "/"+b.getData().getRows().get(i).getFile_name()+"."+b.getData().getRows().get(i).getFile_type());
            item.put("id", b.getData().getRows().get(i).getId());
            item.put("file_score", b.getData().getRows().get(i).getFile_score());
            item.put("plan_id",b.getData().getRows().get(i).getPlan_id());
            list_show.add(item);
        }
    }

    private void initView(){
        list_view = findViewById(R.id.list_view);
        SimpleAdapter adapter= new SimpleAdapter(this, list_show, R.layout.video,
                new String[]{"remark"}, new int[]{R.id.remark});
        list_view.setAdapter(adapter);
        //条目点击事件
        list_view.setOnItemClickListener(new VideoActivity.ItemClickListener());
    }

    //获取点击事件
    private final class ItemClickListener implements AdapterView.OnItemClickListener {
        @SuppressLint("WrongConstant")
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent=new Intent(VideoActivity.this, VideoInfoActivity.class);
            intent.putExtra("id", String.valueOf(list_show.get(position).get("id")));
            intent.putExtra("file_score", String.valueOf(list_show.get(position).get("file_score")));
            intent.putExtra("file_path", String.valueOf(list_show.get(position).get("file_path")));
            intent.putExtra("remark", String.valueOf(list_show.get(position).get("remark")));
            MyApplication application = (MyApplication) getApplicationContext();
            intent.putExtra("user_id",application.getUserId());
            intent.putExtra("plan_id",String.valueOf(list_show.get(position).get("plan_id")));
            startActivity(intent);
        }
    }
}