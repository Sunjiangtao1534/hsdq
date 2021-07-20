package djjy.filemange;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.hsdj.MyApplication;
import com.example.hsdj.R;
import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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


public class FileActivity extends AppCompatActivity {
    private ImageButton btn_return;
    private ListView file_list_view;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    //多条文件标题
    private List<HashMap<String, Object>> list_show = new ArrayList<HashMap<String, Object>>();
    public static final int EXTERNAL_STORAGE_REQ_CODE = 10 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        btn_return = findViewById(R.id.btn_return);
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish是取消当前页面
                finish();
            }
        });

        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // 请求权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    EXTERNAL_STORAGE_REQ_CODE);
        }

        try {
            final ProgressDialog progressDialog = new ProgressDialog(FileActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("内容获取中...");
            progressDialog.show();
            /*查找党建资讯列表: 异步post请求*/
            MyApplication myApplication= (MyApplication) getApplication().getApplicationContext();
            String accessKey = AccessKeyUtils.getAccessKey(myApplication.getMenuApplication(), 54);
            String json="{\"businessJson\":{\"page\":1,\"rows\":10},\"controlJson\":{\"menuId\":54,\"opt\":\"c\",\"accessKey\":\""+accessKey+"\",\"token\":\""+myApplication.getToken()+"\",\"opt_desc\":\"文件获取\"}}";
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url("http://58.51.240.150:8000/app/hsdj/xxjymgr/views/getFiles")
                    .post(body)
                    .build();
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            /*json串和File对象数据绑定*/
            if (response.isSuccessful()) {
                Gson gson = new Gson();
                File b = gson.fromJson(response.body().string(), File.class);
                for (int i = 0; i < b.getData().getRows().size(); i++) {
                    HashMap<String, Object> item = new HashMap<String, Object>();
                    item.put("file_name", b.getData().getRows().get(i).getFile_name());
                    item.put("file_show", b.getData().getRows().get(i).getFile_name()+"."+b.getData().getRows().get(i).getFile_type());
                    item.put("file_type", b.getData().getRows().get(i).getFile_type());
                    item.put("file_size", b.getData().getRows().get(i).getFile_size());
                    item.put("file_path", b.getData().getRows().get(i).getFile_path());
                    list_show.add(item);
                }
                progressDialog.dismiss();
            }else{
                progressDialog.dismiss();
                Toast.makeText(myApplication, "资讯列表获取失败", Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        initView();
    }

    @SuppressLint("WrongViewCast")
    private void initView() {
        SimpleAdapter adapter = new SimpleAdapter(this, list_show, R.layout.file,
                new String[]{"file_show", "file_size"}, new int[]{R.id.file_name, R.id.file_size});
        //实现列表的显示
        file_list_view = findViewById(R.id.file_list_view);
        file_list_view.setAdapter(adapter);
        //条目点击事件V
        file_list_view.setOnItemClickListener(new FileActivity.ItemClickListener());

    }

    //获取点击事件
    private final class ItemClickListener implements AdapterView.OnItemClickListener {
        @SuppressLint("WrongConstant")
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            ProgressDialog progressDialog = new ProgressDialog(FileActivity.this);
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            progressDialog.setTitle("正在下载");
//            progressDialog.setMessage("请稍后...");
//            progressDialog.setProgress(0);
//            progressDialog.setMax(100);
//            progressDialog.show();
//            progressDialog.setCancelable(false);
            MyApplication myApplication= (MyApplication) getApplication().getApplicationContext();
            String accessKey = AccessKeyUtils.getAccessKey(myApplication.getMenuApplication(), 54);
            String json="{\"businessJson\":{\"file_type\": \""+list_show.get(position).get("file_type")+"\",\"file_path\": \""+list_show.get(position).get("file_path")+"\",\"file_name\": \""+list_show.get(position).get("file_name")+"\"},\"controlJson\":{\"menuId\":54,\"opt\":\"c\",\"accessKey\":\""+accessKey+"\",\"token\":\""+myApplication.getToken()+"\",\"opt_desc\":\"下载文件\"}}";
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(UrlUtils.BASE_URL+"/app/hsdj/xxjymgr/views/downloadFile")
                    .post(body)
                    .build();
            OkHttpClient client = new OkHttpClient();
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    final long startTime = System.currentTimeMillis();
                    InputStream is = null;
                    byte[] buf = new byte[2048];
                    int len = 0;
                    FileOutputStream fos = null;
                    // 储存下载文件的目录
                    ContextWrapper cw = new ContextWrapper(getApplicationContext());
//                    java.io.File directory = cw.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
                    java.io.File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    try {
                        is = response.body().byteStream();
                        long total = response.body().contentLength();
                        java.io.File file = new java.io.File(directory,list_show.get(position).get("file_show").toString());
                        fos = new FileOutputStream(file);
                        long sum = 0;
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                            sum += len;
                            int progress = (int) (sum * 1.0f / total * 100);
                            // 下载中
//                        listener.onDownloading(progress);
                        }
                        fos.flush();
                        // 下载完成
//                    listener.onDownloadSuccess();
                        Log.i("DOWNLOAD","download success");
                        Log.i("DOWNLOAD","totalTime="+ (System.currentTimeMillis() - startTime));
                    } catch (Exception e) {
                        e.printStackTrace();
//                    listener.onDownloadFailed();
                        Log.i("DOWNLOAD","download failed");
                    } finally {
                        try {
                            if (is != null)
                                is.close();
                        } catch (IOException e) {
                        }
                        try {
                            if (fos != null)
                                fos.close();
                        } catch (IOException e) {
                        }
                    }
//                    progressDialog.dismiss();
                    Toast.makeText(myApplication, "文件下载成功！", Toast.LENGTH_SHORT).show();
                }else{
                    Log.i("DOWNLOAD","download failed");
//                    progressDialog.dismiss();
                    Toast.makeText(myApplication, "文件获取失败！", Toast.LENGTH_SHORT).show();
                }
            }catch (IOException e){
//                progressDialog.dismiss();
                Toast.makeText(myApplication, "文件获取失败！", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
