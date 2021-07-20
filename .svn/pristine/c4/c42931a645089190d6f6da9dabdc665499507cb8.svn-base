package djjy.djzx;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hsdj.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConsInfoActivity extends AppCompatActivity {
    private ImageButton btn_return;
    private TextView news_title;
    private TextView news_content;
    private TextView news_score;
    private ImageView news_covering_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cons_info);
        //时间获取
        long startTime = SystemClock.elapsedRealtime();
        btn_return=findViewById(R.id.btn_return);
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish是取消当前页面
                finish();
                //获取页面结束时间
                long endTime = SystemClock.elapsedRealtime();
                //计算停留时长
                long stayTime = endTime - startTime;
            }
        });

        try {
            ininView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ininView() throws IOException {
        news_title=findViewById(R.id.news_title);
        news_title.setText(getIntent().getStringExtra("news_title"));
        news_content=findViewById(R.id.news_content);
        news_content.setText(getIntent().getStringExtra("news_content"));
        news_score=findViewById(R.id.news_score);
        news_score.setText(getIntent().getStringExtra("news_score"));
        news_covering_path=findViewById(R.id.news_covering_path);
//        news_covering_path.setImageURI(Uri.parse(getIntent().getExtras().getString("news_covering_path")));
        //Glide.with(this).load(getIntent().getExtras().getString("news_covering_path")).into(news_covering_path);
        //news_covering_path.setImageBitmap(getIntent().getExtras().getString("news_covering_path"));
        InputStream inputStream = getImageViewInputStream(getIntent().getExtras().getString("news_covering_path"));
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        news_covering_path.setImageBitmap(bitmap);
    }

    /*获取网络图片输入流*/
    public InputStream getImageViewInputStream(String url_path) throws IOException {
        InputStream inputStream = null;
        URL url = new URL(url_path);                    //服务器地址
        if (url != null) {
            //打开连接
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setConnectTimeout(3000);//设置网络连接超时的时间为3秒
            httpURLConnection.setRequestMethod("GET");        //设置请求方法为GET
            httpURLConnection.setDoInput(true);                //打开输入流
            int responseCode = httpURLConnection.getResponseCode();    // 获取服务器响应值
            if (responseCode == HttpURLConnection.HTTP_OK) {        //正常连接
                    inputStream = httpURLConnection.getInputStream();        //获取输入流
                }
            }
        return inputStream;
    }
}