package djjy.stlx;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

public class ExercisesActivity extends AppCompatActivity {
    private ImageButton btn_return;
    private ListView list_view;

    private List<HashMap<String, Object>> list_show = new ArrayList<HashMap<String,Object>>();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercises_activity);

        MyApplication application = (MyApplication) getApplicationContext();

        btn_return=findViewById(R.id.btn_return);
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish是取消当前页面
                finish();
            }
        });

        try {
            final ProgressDialog progressDialog = new ProgressDialog(ExercisesActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("获取试卷列表中...");
            progressDialog.show();
            /*查找试卷列表*/
            MyApplication myApplication= (MyApplication) getApplication().getApplicationContext();
            String accessKey = AccessKeyUtils.getAccessKey(myApplication.getMenuApplication(), 53);
            String json="{\"businessJson\":{\"ob.id\":"+myApplication.getUserId()+"},\"controlJson\":{\"menuId\":53,\"opt\":\"c\",\"accessKey\":\""+accessKey+"\",\"token\":\""+myApplication.getToken()+"\",\"opt_desc\":\"获得该用户下分配的试卷列表\"}}";
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(UrlUtils.BASE_URL+"/app/hsdj/xxjymgr/views/getPlanByExam")
                    .post(body)
                    .build();
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                progressDialog.dismiss();
                JSONObject jsonObject=new JSONObject(response.body().string());
                JSONObject data=new JSONObject(jsonObject.optString("data"));
                JSONArray rows=new JSONArray(data.optString("rows"));
                for (int i = 0; i < rows.length(); i++) {
                    JSONObject exam=rows.getJSONObject(i);
                    HashMap<String, Object> item = new HashMap<String, Object>();
                    item.put("exam_title",exam.optString("exam_title"));
                    item.put("exam_id",exam.optString("id"));
                    item.put("plan_id",exam.optString("plan_id"));
                    list_show.add(item);
                }
            }else{
                Toast.makeText(myApplication, "试卷列表获取失败", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                finish();
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        initView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //避免重复打开页面时数据重复填写
    }

    /**
     * 初始化view
     */
    private void initView() {
        //创建SimpleAdapter适配器将数据绑定到item显示控件上
        SimpleAdapter adapter = new SimpleAdapter(this, list_show, R.layout.exercise,
                    new String[]{"exam_title"}, new int[]{R.id.exam_title});
        //实现列表的显示
        list_view=findViewById(R.id.list_view);
        list_view.setAdapter(adapter);
        //条目点击事件
        list_view.setOnItemClickListener(new ItemClickListener());
    }

    //获取点击事件
    private final class ItemClickListener implements AdapterView.OnItemClickListener {
        @SuppressLint("WrongConstant")
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent=new Intent(ExercisesActivity.this, ExercisesInfoActivity.class);
            intent.putExtra("exam_id", String.valueOf(list_show.get(position).get("exam_id")));
            intent.putExtra("exam_title",String.valueOf(list_show.get(position).get("exam_title")));
            intent.putExtra("plan_id",String.valueOf(list_show.get(position).get("plan_id")));
            startActivity(intent);
        }
    }
}