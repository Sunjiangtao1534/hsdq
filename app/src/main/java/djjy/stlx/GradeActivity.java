package djjy.stlx;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hsdj.R;

import java.util.ArrayList;

public class GradeActivity extends AppCompatActivity {
    private TextView tv_grade_score;
    private ListView lv_grade_score_detail;

    //题目及选项
    private ArrayList<Topic> problemList = new ArrayList<Topic>();
    //总分
    private String grade;
    //正确答案和错误答案
    static Answer[] answerArr=new Answer[500];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);

        tv_grade_score=findViewById(R.id.tv_grade_score);
        lv_grade_score_detail=findViewById(R.id.lv_grade_score_detail);

        //接受传来的数据
        problemList = (ArrayList<Topic>) getIntent().getExtras().get("problemList");
        grade = getIntent().getStringExtra("grade");
        answerArr = (Answer[]) getIntent().getExtras().get("answerArr");

        tv_grade_score.setText("您的成绩是： " + grade);
        lv_grade_score_detail.setAdapter(new MyAdapter());
    }

    /**
     * 题目列表适配器
     */
    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return problemList != null ? problemList.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return problemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
//            listview优化，复用布局以及id
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(GradeActivity.this).inflate(R.layout.list_item_activity_score, null);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_item_activty_score_title);
                holder.tvOptionA = (TextView) convertView.findViewById(R.id.tv_item_activty_score_optionA);
                holder.tvOptionB = (TextView) convertView.findViewById(R.id.tv_item_activty_score_optionB);
                holder.tvOptionC = (TextView) convertView.findViewById(R.id.tv_item_activty_score_optionC);
                holder.tvOptionD = (TextView) convertView.findViewById(R.id.tv_item_activty_score_optionD);
                holder.tvOptionE = (TextView) convertView.findViewById(R.id.tv_item_activty_score_optionE);
                holder.tvOptionF = (TextView) convertView.findViewById(R.id.tv_item_activty_score_optionF);
                holder.tvOptionG = (TextView) convertView.findViewById(R.id.tv_item_activty_score_optionG);
                holder.tvOptionH = (TextView) convertView.findViewById(R.id.tv_item_activty_score_optionH);
                holder.tvRightAnswer = (TextView) convertView.findViewById(R.id.tv_item_activty_score_right);
                holder.tvWrongAnswer = (TextView) convertView.findViewById(R.id.tv_item_activty_score_wrong);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvTitle.setText((position + 1) + "." + problemList.get(position).getTopic_title());
            //显示正确答案以及填写答案
            holder.tvRightAnswer.setText("正确答案：" + answerArr[position].getCorrectOption());
            holder.tvWrongAnswer.setText("你的答案：" + answerArr[position].getOption());
            //根据题目选项个数动态生成
            switch(answerArr[position].getOption().length()){
                case 1:
                    holder.tvOptionA.setText(problemList.get(position).getOptions().get(0).get("value"));
                    holder.tvOptionB.setVisibility(View.GONE);holder.tvOptionC.setVisibility(View.GONE);holder.tvOptionD.setVisibility(View.GONE);
                    holder.tvOptionE.setVisibility(View.GONE);holder.tvOptionF.setVisibility(View.GONE);holder.tvOptionG.setVisibility(View.GONE);
                    holder.tvOptionH.setVisibility(View.GONE);
                    break;
                case 2:
                    holder.tvOptionA.setText(problemList.get(position).getOptions().get(0).get("value"));
                    holder.tvOptionB.setText(problemList.get(position).getOptions().get(1).get("value"));
                    holder.tvOptionC.setVisibility(View.GONE);holder.tvOptionD.setVisibility(View.GONE);holder.tvOptionE.setVisibility(View.GONE);
                    holder.tvOptionF.setVisibility(View.GONE);holder.tvOptionG.setVisibility(View.GONE);holder.tvOptionH.setVisibility(View.GONE);
                    break;
                case 3:
                    holder.tvOptionA.setText(problemList.get(position).getOptions().get(0).get("value"));
                    holder.tvOptionB.setText(problemList.get(position).getOptions().get(1).get("value"));
                    holder.tvOptionC.setText(problemList.get(position).getOptions().get(2).get("value"));
                    holder.tvOptionD.setVisibility(View.GONE);holder.tvOptionE.setVisibility(View.GONE);holder.tvOptionF.setVisibility(View.GONE);
                    holder.tvOptionG.setVisibility(View.GONE);holder.tvOptionH.setVisibility(View.GONE);
                    break;
                case 4:
                    holder.tvOptionA.setText(problemList.get(position).getOptions().get(0).get("value"));
                    holder.tvOptionB.setText(problemList.get(position).getOptions().get(1).get("value"));
                    holder.tvOptionC.setText(problemList.get(position).getOptions().get(2).get("value"));
                    holder.tvOptionD.setText(problemList.get(position).getOptions().get(3).get("value"));
                    holder.tvOptionE.setVisibility(View.GONE);holder.tvOptionF.setVisibility(View.GONE);holder.tvOptionG.setVisibility(View.GONE);
                    holder.tvOptionH.setVisibility(View.GONE);
                    break;
                case 5:
                    holder.tvOptionA.setText(problemList.get(position).getOptions().get(0).get("value"));
                    holder.tvOptionB.setText(problemList.get(position).getOptions().get(1).get("value"));
                    holder.tvOptionC.setText(problemList.get(position).getOptions().get(2).get("value"));
                    holder.tvOptionD.setText(problemList.get(position).getOptions().get(3).get("value"));
                    holder.tvOptionE.setText(problemList.get(position).getOptions().get(4).get("value"));
                    holder.tvOptionF.setVisibility(View.GONE);holder.tvOptionG.setVisibility(View.GONE);holder.tvOptionH.setVisibility(View.GONE);
                    break;
                case 6:
                    holder.tvOptionA.setText(problemList.get(position).getOptions().get(0).get("value"));
                    holder.tvOptionB.setText(problemList.get(position).getOptions().get(1).get("value"));
                    holder.tvOptionC.setText(problemList.get(position).getOptions().get(2).get("value"));
                    holder.tvOptionD.setText(problemList.get(position).getOptions().get(3).get("value"));
                    holder.tvOptionE.setText(problemList.get(position).getOptions().get(4).get("value"));
                    holder.tvOptionF.setText(problemList.get(position).getOptions().get(5).get("value"));
                    holder.tvOptionG.setVisibility(View.GONE);holder.tvOptionH.setVisibility(View.GONE);
                    break;
                case 7:
                    holder.tvOptionA.setText(problemList.get(position).getOptions().get(0).get("value"));
                    holder.tvOptionB.setText(problemList.get(position).getOptions().get(1).get("value"));
                    holder.tvOptionC.setText(problemList.get(position).getOptions().get(2).get("value"));
                    holder.tvOptionD.setText(problemList.get(position).getOptions().get(3).get("value"));
                    holder.tvOptionE.setText(problemList.get(position).getOptions().get(4).get("value"));
                    holder.tvOptionF.setText(problemList.get(position).getOptions().get(5).get("value"));
                    holder.tvOptionG.setText(problemList.get(position).getOptions().get(6).get("value"));
                    holder.tvOptionH.setVisibility(View.GONE);
                    break;
                case 8:
                    holder.tvOptionA.setText(problemList.get(position).getOptions().get(0).get("value"));
                    holder.tvOptionB.setText(problemList.get(position).getOptions().get(1).get("value"));
                    holder.tvOptionC.setText(problemList.get(position).getOptions().get(2).get("value"));
                    holder.tvOptionD.setText(problemList.get(position).getOptions().get(3).get("value"));
                    holder.tvOptionE.setText(problemList.get(position).getOptions().get(4).get("value"));
                    holder.tvOptionF.setText(problemList.get(position).getOptions().get(5).get("value"));
                    holder.tvOptionG.setText(problemList.get(position).getOptions().get(6).get("value"));
                    holder.tvOptionH.setText(problemList.get(position).getOptions().get(7).get("value"));
                    break;
            }
            if (!answerArr[position].getCorrectOption().equals(answerArr[position].getOption())) {
                holder.tvTitle.setTextColor(Color.RED);
            }
            return convertView;
        }

        class ViewHolder {
            TextView tvTitle, tvRightAnswer, tvWrongAnswer, tvOptionA,
                    tvOptionB, tvOptionC, tvOptionD,tvOptionE,tvOptionF,tvOptionG,tvOptionH;
        }
    }
}