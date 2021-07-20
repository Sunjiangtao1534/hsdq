package djjy.djzx;

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

public class ConsActivity extends AppCompatActivity {
    private ImageButton btn_return;
    private ListView cons_list_view;
    private List<String> cons_info=new ArrayList<>();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    //多条资讯标题
    private List<HashMap<String, Object>> list_show = new ArrayList<HashMap<String,Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cons);

        btn_return=findViewById(R.id.btn_return);
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish是取消当前页面
                finish();
            }
        });

        try {
            final ProgressDialog progressDialog = new ProgressDialog(ConsActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("内容获取中...");
            progressDialog.show();
            /*查找党建资讯列表: 异步post请求*/
            MyApplication myApplication= (MyApplication) getApplication().getApplicationContext();
            String accessKey = AccessKeyUtils.getAccessKey(myApplication.getMenuApplication(), 55);
            String json="{\"businessJson\":{\"page\":1,\"rows\":10},\"controlJson\":{\"menuId\":55,\"opt\":\"c\",\"accessKey\":\""+accessKey+"\",\"token\":\""+myApplication.getToken()+"\",\"opt_desc\":\"资讯获取\"}}";
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url("http://58.51.240.150:8000/app/hsdj/xxjymgr/views/queryInformation")
                    .post(body)
                    .build();
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//            OkHttpClient okHttpClient = new OkHttpClient();
//            //创建表单请求体
//            FormBody.Builder formBody = new FormBody.Builder();
//            Request okRequest = new Request.Builder().url("http://58.51.240.150:8000/app/hsdj/xxjymgr/views/queryInformation").post(formBody.build()).build();
//            Response response = okHttpClient.newCall(okRequest).execute();
            /*json串和Cons对象数据绑定*/
            if (response.isSuccessful()) {
                Gson gson = new Gson();
                Cons b = gson.fromJson(response.body().string(),Cons.class);
                for (int i = 0; i < b.getData().getRows().size(); i++) {
                    HashMap<String, Object> item = new HashMap<String, Object>();
                    item.put("news_title", b.getData().getRows().get(i).getNews_title());
                    item.put("news_covering_path", b.getData().getRows().get(i).getNews_covering_path());
                    item.put("news_content", b.getData().getRows().get(i).getNews_content());
                    item.put("news_score", b.getData().getRows().get(i).getNews_score());
                    list_show.add(item);
                }
                progressDialog.dismiss();
            }else{
                progressDialog.dismiss();
                Toast.makeText(myApplication, "资讯列表获取失败", Toast.LENGTH_SHORT).show();
                finish();
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        initView();
    }
    public void flush(String news_title,String news_covering_path,String news_content,String news_score) {
        Intent intent=new Intent(ConsActivity.this, ConsInfoActivity.class);
        intent.putExtra("news_title",news_title);
        intent.putExtra("news_covering_path",news_covering_path);
        intent.putExtra("news_content",news_content);
        intent.putExtra("news_score",news_score);
        startActivity(intent);
        return;
    }

     private void initView(){
         SimpleAdapter adapter= new SimpleAdapter(this, list_show, R.layout.cons,
                 new String[]{"news_title"}, new int[]{R.id.news_title});
         //实现列表的显示
         cons_list_view=findViewById(R.id.cons_list_view);
         cons_list_view.setAdapter(adapter);
         //条目点击事件
         cons_list_view.setOnItemClickListener(new ItemClickListener());
     }

    //获取点击事件
    private final class ItemClickListener implements AdapterView.OnItemClickListener {
        @SuppressLint("WrongConstant")
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //news_title news_covering_path news_content news_score
            flush(String.valueOf(list_show.get(position).get("news_title")),String.valueOf(list_show.get(position).get("news_covering_path")),
                    String.valueOf(list_show.get(position).get("news_content")),String.valueOf(list_show.get(position).get("news_score")));
        }
    }
}