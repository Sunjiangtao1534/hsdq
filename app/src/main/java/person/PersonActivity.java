package person;

import android.annotation.SuppressLint;
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

public class PersonActivity extends AppCompatActivity{
    private ImageButton btn_return;
    private ListView list_view;
    //不同层级组织信息
    private List<HashMap<String, Object>> list_show = new ArrayList<HashMap<String,Object>>();//大冶有色级党委

    //下级组织信息
    private List<String> children=new ArrayList<>();
    private List<String> org_code=new ArrayList<>();
    private List<String> per_info=new ArrayList<>();

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        btn_return=findViewById(R.id.btn_return);
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish是取消当前页面
                finish();
            }
        });

        MyApplication myApplication= (MyApplication) getApplication().getApplicationContext();
        if(getIntent().getStringExtra("msg")==null){
            //第一次进页面查询组织树
            try{
                String accessKey = AccessKeyUtils.getAccessKey(myApplication.getMenuApplication(), 7);
                String json="{\"businessJson\":{},\"controlJson\":{\"menuId\":7,\"opt\":\"c\",\"accessKey\":\""+accessKey+"\",\"token\":\""+myApplication.getToken()+"\",\"opt_desc\":\"获取组织架构图\"}}";
                RequestBody body = RequestBody.create(JSON, json);
                Request request = new Request.Builder()
                        .url(UrlUtils.BASE_URL+"/app/hsdj/djzzmgr/views/getPartyOrgTree")
                        .post(body)
                        .build();
                OkHttpClient client = new OkHttpClient();
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    //处理数据
                    JSONArray array = new JSONArray(response.body().string());
                    JSONArray array1=new JSONArray(String.valueOf(array));
                    //中国有色集团党委
                    JSONObject obj = array1.getJSONObject(0);
                    JSONArray jsonArray =new JSONArray(obj.optString("children"));
                    initData(String.valueOf(jsonArray));
                }else{
                    Toast.makeText(myApplication, "组织列表获取失败", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            if (getIntent().getStringExtra("org_code")!=null){
                //最后一级查党员
                try {
                    String accessKey = AccessKeyUtils.getAccessKey(myApplication.getMenuApplication(), 5);
                    String json="{\"businessJson\":{\"org_code\":\""+getIntent().getStringExtra("org_code")+"\"},\"controlJson\":{\"menuId\":5,\"opt\":\"c\",\"accessKey\":\""+accessKey+"\",\"token\":\""+myApplication.getToken()+"\",\"opt_desc\":\"获取组织下所属人员\"}}";
                    RequestBody body = RequestBody.create(JSON, json);
                    Request request = new Request.Builder()
                            .url(UrlUtils.BASE_URL+"/app/hsdj/djzzmgr/views/showOrgData")
                            .post(body)
                            .build();
                    OkHttpClient client = new OkHttpClient();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        initPersonData(response.body().string());
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }else{
                try {
                    initData(getIntent().getStringExtra("msg"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        initView();
    }

    /*本页刷新组织信息*/
    public void flush(View view,String children,String org_code,String per_info) {
        if (!"".equals(per_info)){
            Intent intent=new Intent(PersonActivity.this, PerInfoActivity.class);
            intent.putExtra("per_info",per_info);
            startActivity(intent);
            return;
        }
        Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
        intent.putExtra("msg", children);
        if (children.length()<=2){
            intent.putExtra("org_code", org_code);
        }
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //避免重复打开页面时数据重复填写
    }

    /*处理当前页应该展示内容及告诉下一级页面的信息*/
    public void initData(String result) throws JSONException {
        //大冶有色党委
        JSONArray array=new JSONArray(result);
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject=array.getJSONObject(i);
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("org_name", jsonObject.optString("org_name"));
            list_show.add(item);
            org_code.add(jsonObject.optString("org_code"));
            if (jsonObject.optString("children").length()>0){
                children.add(jsonObject.optString("children"));
            }else{
                children.add("none");
            }
        }
    }

    /*处理党员信息*/
    public void initPersonData(String result) throws JSONException {
        JSONObject jsonObject=new JSONObject(result);
        JSONObject data=new JSONObject(jsonObject.optString("data"));
        JSONArray rows=new JSONArray(data.optString("rows"));
        if(rows.length()==0){
            Toast.makeText(PersonActivity.this,"该组织下暂无党员信息！",Toast.LENGTH_SHORT).show();
        }else{
            for (int i = 0; i < rows.length(); i++) {
                JSONObject person=rows.getJSONObject(i);
                HashMap<String, Object> item1 = new HashMap<String, Object>();
                item1.put("name",person.optString("name"));
                item1.put("party_duty",person.optString("party_duty"));
//                item1.put("party_branch",person.optString("party_branch"));
                list_show.add(item1);
                per_info.add(String.valueOf(person));
            }
        }
    }


    /**
     * 初始化view
     */
    private void initView() {
        //创建SimpleAdapter适配器将数据绑定到item显示控件上
        SimpleAdapter adapter = null;
        if (getIntent().getStringExtra("org_code")!=null){
            adapter = new SimpleAdapter(this, list_show, R.layout.per,
                    new String[]{"name","party_duty"}, new int[]{R.id.name,R.id.party_duty});
        }else{
            adapter = new SimpleAdapter(this, list_show, R.layout.org,
                    new String[]{"org_name"}, new int[]{R.id.org_name});
        }
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
            if(per_info.size()>0){
                flush(view,"","",per_info.get(position));
                return;
            }
            if (children.get(position).length()<=2){
                flush(view,children.get(position),org_code.get(position),"");
            }else{
                flush(view,children.get(position),"","");
            }
        }
    }
}