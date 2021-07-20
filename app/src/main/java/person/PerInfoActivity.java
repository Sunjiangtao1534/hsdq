package person;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hsdj.R;

import org.json.JSONException;
import org.json.JSONObject;

public class PerInfoActivity extends AppCompatActivity {
    private ImageButton btn_return;
    private TextView tv_name;
    private TextView tv_sex;
    private TextView tv_hometown;
    private TextView tv_category;
    private TextView tv_party_branch;
    private TextView tv_party_duty;
    private TextView tv_join_date;
    private TextView tv_work_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_per_info);

        btn_return=findViewById(R.id.btn_return);
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish是取消当前页面
                finish();
            }
        });

        ininView(getIntent().getStringExtra("per_info"));
    }

    private void ininView(String per_info){
        try {
            JSONObject per=new JSONObject(per_info);
            tv_name=findViewById(R.id.name);
            tv_name.setText(per.optString("name"));
            tv_sex=findViewById(R.id.sex);
            tv_sex.setText(per.optString("sex"));
            tv_hometown=findViewById(R.id.hometown);
            tv_hometown.setText(per.optString("hometown"));
            tv_category=findViewById(R.id.category);
            tv_category.setText(per.optString("category"));
            tv_party_branch=findViewById(R.id.party_branch);
            tv_party_branch.setText(per.optString("party_branch"));
            tv_party_duty=findViewById(R.id.party_duty);
            tv_party_duty.setText(per.optString("party_duty"));
            tv_join_date=findViewById(R.id.join_date);
            tv_join_date.setText(per.optString("join_date"));
            tv_work_date=findViewById(R.id.work_date);
            tv_work_date.setText(per.optString("work_date"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}