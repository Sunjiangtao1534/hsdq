package djjy.stlx;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hsdj.R;

import djjy.stlx.xtlx.PracticeActivity;

public class ModelSelectionActivity extends AppCompatActivity {
    private ImageButton btn_return;
    private Button btn_shijuan,btn_mryl;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_selection);
        btn_return=findViewById(R.id.btn_return);
        btn_shijuan=findViewById(R.id.btn_shijuan);
        btn_mryl=findViewById(R.id.btn_mryl);
        Onclick onclick=new Onclick();
        btn_return.setOnClickListener(onclick);
        btn_shijuan.setOnClickListener(onclick);
        btn_mryl.setOnClickListener(onclick);
    }

    private class Onclick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()){
                case R.id.btn_return:
                    finish();
                    break;
                case R.id.btn_shijuan:
                    intent=new Intent(ModelSelectionActivity.this, ExercisesActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_mryl:
                    intent=new Intent(ModelSelectionActivity.this, PracticeActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }
}