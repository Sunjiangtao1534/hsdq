package login;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hsdj.MenuApplication;
import com.example.hsdj.MyApplication;
import com.example.hsdj.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import utils.UrlUtils;

public class LoginActivity extends AppCompatActivity {
    Button button1; //点击的LinearLayout
    private EditText et_data_uname;
    private EditText et_data_upass;
    private Map<String, String> stringHashMap = new HashMap<String, String>();
    private String username;
    private String token;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MyApplication myApplication= (MyApplication) getApplication();

        button1= findViewById(R.id.button1);
        /*用户登录验证接口*/
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_data_uname = (EditText) findViewById(R.id.et_data_uname);
                et_data_upass = (EditText) findViewById(R.id.et_data_upass);

                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("用户登陆中...");
                progressDialog.show();
                try {
                    //用户登录接口：异步post请求
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    String json="{\"username\":\""+et_data_uname.getText().toString()+"\",\"userpwd\":\""+et_data_upass.getText().toString()+"\"}";
                    RequestBody body = RequestBody.create(JSON, json);
                    Request request = new Request.Builder()
                            .url(UrlUtils.BASE_URL+"/app/hsdj/authmgr/views/userLogin")
                            .post(body)
                            .build();
                    OkHttpClient client = new OkHttpClient();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        JSONObject jsonObject=new JSONObject(response.body().string());
                        if("OK".equalsIgnoreCase(jsonObject.optString("message"))){
                            Toast.makeText(getApplication(), "登录成功！", Toast.LENGTH_SHORT).show();
                            JSONObject data=new JSONObject(jsonObject.optString("data"));
                            token=data.optString("token");
                            myApplication.setToken(token);

                            //用户信息接口：同步post请求
                            String json1="{\"businessJson\":{\"username\":\""+et_data_uname.getText().toString()+"\"},\"controlJson\":{\"menuId\":0,\"token\":\""+token+"\",\"opt_desc\":\"移动端登录\"}}";
                            RequestBody body1 = RequestBody.create(JSON, json1);
                            Request request1 = new Request.Builder()
                                    .url(UrlUtils.BASE_URL+"/app/hsdj/authmgr/views/getUsers")
                                    .post(body1)
                                    .build();
                            OkHttpClient client1 = new OkHttpClient();
                            Response response1 = client1.newCall(request1).execute();
                            if (response1.isSuccessful()) {
                                Gson gson=new Gson();
                                UserInfo userInfo = gson.fromJson(response1.body().string(), UserInfo.class);
                                myApplication.setUsername(userInfo.getData().getRows().get(0).getUsername());
                                myApplication.setUserId(String.valueOf(userInfo.getData().getRows().get(0).getId()));
                                myApplication.setUserdesc(userInfo.getData().getRows().get(0).getUserdesc());
                                finish();
                            } else {
                                Toast.makeText(myApplication, "获取用户信息成功", Toast.LENGTH_SHORT).show();
                            }

                            //获取菜单的accessKey
                            String json2="{\"businessJson\":{\"role_id\":3335,\"token\":\""+token+"\"},\"controlJson\":{\"menuId\":0,\"token\":\""+token+"\",\"opt_desc\":\"获得角色的菜单树\"}}";
                            RequestBody body2 = RequestBody.create(JSON, json2);
                            Request request2 = new Request.Builder()
                                    .url(UrlUtils.BASE_URL+"/app/hsdj/authmgr/views/getRoleMenuTree")
                                    .post(body2)
                                    .build();
                            OkHttpClient client2 = new OkHttpClient();
                            Response response2 = client2.newCall(request2).execute();
                            if (response2.isSuccessful()) {
                                //递归解析menu
                                JSONArray jsonArray=new JSONArray(response2.body().string());
                                JSONObject jsonObject1=new JSONObject(jsonArray.get(0).toString());
                                myApplication.setMenuApplication(digui(jsonObject1));
//                                Log.d("menuApplication",myApplication.getMenuApplication().toString());
                                progressDialog.dismiss();
                                finish();
                            } else {
                                Toast.makeText(myApplication, "获取用户信息成功", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getApplication(), "用户名或密码错误！", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(myApplication, "登录失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //递归：解析menuTree
    private MenuApplication digui(JSONObject json) throws JSONException {
        MenuApplication menuApplication=new MenuApplication();
        menuApplication.setMenu_id(json.optInt("menu_id"));
        menuApplication.setMenu_seq(json.optString("menu_seq"));
        menuApplication.setParent_id(json.optInt("parent_id"));
        menuApplication.setText(json.optString("text"));
        menuApplication.setUrl(json.optString("url"));
        menuApplication.setAuthcode(json.optString("authcode"));
        menuApplication.setOptcode(json.optString("optcode"));
        menuApplication.setAccessKey(json.optString("accessKey"));
        menuApplication.setAppname(json.optString("appname"));
        if(json.optString("children")!=null){
            JSONArray jsonArray= new JSONArray(json.optString("children"));
            List<MenuApplication> childrens=new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject=new JSONObject(jsonArray.get(i).toString());
                childrens.add(digui(jsonObject));
            }
            menuApplication.setChildren(childrens);
        }
        return menuApplication;
    }

    //禁用返回键，此时用户点击返回键时会没登陆进入系统
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) { //监控/拦截/屏蔽返回键
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    class BusinessJson{
        String username;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        @Override
        public String toString() {
            return "businessJson"+":{" +
                    "username:'" + username + '\'' +
                    '}';
        }
    }

    class ControlJson{
       private int menuId;
       private String token;
       private String opt_desc;

        public int getMenuId() {
            return menuId;
        }

        public void setMenuId(int menuId) {
            this.menuId = menuId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getOpt_desc() {
            return opt_desc;
        }

        public void setOpt_desc(String opt_desc) {
            this.opt_desc = opt_desc;
        }

        @Override
        public String toString() {
            return "'controlJson'"+":{" +
                    "menuId:" + menuId +
                    ", token:'" + token + '\'' +
                    ", opt_desc:'" + opt_desc + '\'' +
                    '}';
        }
    }
}