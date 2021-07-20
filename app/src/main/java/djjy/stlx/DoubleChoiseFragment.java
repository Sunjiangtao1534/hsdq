package djjy.stlx;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatCheckBox;

import com.example.hsdj.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * sjt
 */
public class DoubleChoiseFragment extends BaseFragment implements CheckBox.OnCheckedChangeListener{
    private TextView tv_title;
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private CheckBox checkBox4;
    private CheckBox checkBox5;
    private CheckBox checkBox6;
    private CheckBox checkBox7;
    private CheckBox checkBox8;
    private AppCompatCheckBox check_show_analysis;
    private LinearLayout ll_single_analysis;
    private TextView tv_answer,tv_analysis;

    /*题目信息*/
    private Topic topic;
    private String option = "";
    private List<String> optionId=new ArrayList<>();
    Answer answer=new Answer();
    private int index;//第几题
    //题目解析是否可见
    boolean isAnalysisShow;

    public DoubleChoiseFragment(Topic topic,int index,boolean isAnalysisShow){
        this.topic=topic;
        this.index=index;
        this.isAnalysisShow=isAnalysisShow;
    }

    @Override
    public Answer getAnswer(){
        return this.answer;
    }

    @Override
    public Topic getTopic(){
        return this.topic;
    }

    @Override
    protected View initView() throws JSONException {
        View view = View.inflate(mActivity, R.layout.fragment_double_choise, null);
        tv_title = view.findViewById(R.id._tv_title);
        check_show_analysis=view.findViewById(R.id.check_show_analysis);
        ll_single_analysis=view.findViewById(R.id.ll_single_analysis);
        tv_answer=view.findViewById(R.id.tv_answer);
        tv_analysis=view.findViewById(R.id.tv_analysis);
        check_show_analysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_single_analysis.getVisibility()==View.GONE){
                    ll_single_analysis.setVisibility(View.VISIBLE);
                }else{
                    ll_single_analysis.setVisibility(View.GONE);
                }
            }
        });

        //解析选项字符串，找出有几个选项（不大于8个）
        JSONArray option_def = new JSONArray(topic.getOption_def());
        ArrayList<HashMap<String,String>> optionList=new ArrayList<>();
        for (int i = 0; i < option_def.length(); i++) {
            JSONObject option=option_def.getJSONObject(i);
            HashMap<String,String> item=new HashMap<>();
            item.put("value",  option.optString("value"));
            item.put("checkCurr", option.optString("checkCurr"));
            optionList.add(item);
        }
        this.topic.setOptions(optionList);
        /*多选题*/
        checkBox1=view.findViewById(R.id.checkBox1);checkBox2=view.findViewById(R.id.checkBox2);checkBox3=view.findViewById(R.id.checkBox3);
        checkBox4=view.findViewById(R.id.checkBox4);checkBox5=view.findViewById(R.id.checkBox5);checkBox6=view.findViewById(R.id.checkBox6);
        checkBox7=view.findViewById(R.id.checkBox7);checkBox8=view.findViewById(R.id.checkBox8);
        switch (this.topic.getOptions().size()){
            case 8:
                break;
            case 7:
                checkBox8.setVisibility(View.GONE);
                break;
            case 6:
                checkBox8.setVisibility(View.GONE);checkBox7.setVisibility(View.GONE);
                break;
            case 5:
                checkBox8.setVisibility(View.GONE);checkBox7.setVisibility(View.GONE);checkBox6.setVisibility(View.GONE);
                break;
            case 4:
                checkBox8.setVisibility(View.GONE);checkBox7.setVisibility(View.GONE);checkBox6.setVisibility(View.GONE);
                checkBox5.setVisibility(View.GONE);
                break;
            case 3:
                checkBox8.setVisibility(View.GONE);checkBox7.setVisibility(View.GONE);checkBox6.setVisibility(View.GONE);
                checkBox5.setVisibility(View.GONE);checkBox4.setVisibility(View.GONE);
                break;
            case 2:
                checkBox8.setVisibility(View.GONE);checkBox7.setVisibility(View.GONE);checkBox6.setVisibility(View.GONE);
                checkBox5.setVisibility(View.GONE);checkBox4.setVisibility(View.GONE);checkBox3.setVisibility(View.GONE);
                break;
            default :
                break;
        }
        //监听事件
        checkBox1.setOnCheckedChangeListener(this);checkBox2.setOnCheckedChangeListener(this);checkBox3.setOnCheckedChangeListener(this);
        checkBox4.setOnCheckedChangeListener(this);checkBox5.setOnCheckedChangeListener(this);checkBox6.setOnCheckedChangeListener(this);
        checkBox7.setOnCheckedChangeListener(this);checkBox8.setOnCheckedChangeListener(this);

        return view;
    }

    @Override
    public void initData() {
        //如果没有传递数据，则退出
        if (this.topic == null) {
            return;
        }
        if(!this.isAnalysisShow){
            check_show_analysis.setVisibility(View.GONE);
        }
        ll_single_analysis.setVisibility(View.GONE);
        String correctAnswer="";
        for (int i = 0; i < this.topic.getOptions().size(); i++) {
            if ("True".equalsIgnoreCase(this.topic.getOptions().get(i).get("checkCurr"))){
                switch(i){
                    case 0:
                        correctAnswer+="A";
                        break;
                    case 1:
                        correctAnswer+="B";
                        break;
                    case 2:
                        correctAnswer+="C";
                        break;
                    case 3:
                        correctAnswer+="D";
                        break;
                    case 4:
                        correctAnswer+="E";
                        break;
                    case 5:
                        correctAnswer+="F";
                        break;
                    case 6:
                        correctAnswer+="G";
                        break;
                    case 7:
                        correctAnswer+="H";
                        break;
                }
            }
        }
        tv_answer.setText("答案："+correctAnswer);
        tv_analysis.setText("解析："+this.topic.getAnalysis());
        tv_title.setText("" +(this.index+1)+"."+this.topic.getTopic_title());
        //多选题，对应选项赋值
        switch(this.topic.getOptions().size()){
            case 2:
                checkBox1.setText("" +"A."+ this.topic.getOptions().get(0).get("value"));checkBox2.setText("" +"B."+ this.topic.getOptions().get(1).get("value"));
                break;
            case 3:
                checkBox1.setText("" +"A."+ this.topic.getOptions().get(0).get("value"));checkBox2.setText("" +"B."+ this.topic.getOptions().get(1).get("value"));
                checkBox3.setText("" +"C."+ this.topic.getOptions().get(2).get("value"));
                break;
            case 4:
                checkBox1.setText("" +"A."+this.topic.getOptions().get(0).get("value"));checkBox2.setText("" +"B."+ this.topic.getOptions().get(1).get("value"));
                checkBox3.setText("" +"C."+ this.topic.getOptions().get(2).get("value"));checkBox4.setText("" +"D."+ this.topic.getOptions().get(3).get("value"));
                break;
            case 5:
                checkBox1.setText("" +"A."+ this.topic.getOptions().get(0).get("value"));checkBox2.setText("" +"B."+ this.topic.getOptions().get(1).get("value"));
                checkBox3.setText("" +"C."+ this.topic.getOptions().get(2).get("value"));checkBox4.setText("" +"D."+ this.topic.getOptions().get(3).get("value"));
                checkBox5.setText("" +"E."+ this.topic.getOptions().get(4).get("value"));
                break;
            case 6:
                checkBox1.setText("" +"A."+ this.topic.getOptions().get(0).get("value"));checkBox2.setText("" +"B."+ this.topic.getOptions().get(1).get("value"));
                checkBox3.setText("" +"C."+ this.topic.getOptions().get(2).get("value"));checkBox4.setText("" +"D."+ this.topic.getOptions().get(3).get("value"));
                checkBox5.setText("" +"E."+ this.topic.getOptions().get(4).get("value"));checkBox6.setText("" +"F."+ this.topic.getOptions().get(5).get("value"));
                break;
            case 7:
                checkBox1.setText("" +"A."+ this.topic.getOptions().get(0).get("value"));checkBox2.setText("" +"B."+ this.topic.getOptions().get(1).get("value"));
                checkBox3.setText("" +"C."+ this.topic.getOptions().get(2).get("value"));checkBox4.setText("" +"D."+ this.topic.getOptions().get(3).get("value"));
                checkBox5.setText("" +"E."+ this.topic.getOptions().get(4).get("value"));checkBox6.setText("" +"F."+ this.topic.getOptions().get(5).get("value"));
                checkBox7.setText("" +"G."+ this.topic.getOptions().get(6).get("value"));
                break;
            case 8:
                checkBox1.setText("" +"A."+ this.topic.getOptions().get(0).get("value"));checkBox2.setText("" +"B."+ this.topic.getOptions().get(1).get("value"));
                checkBox3.setText("" +"C."+ this.topic.getOptions().get(2).get("value"));checkBox4.setText("" +"D."+ this.topic.getOptions().get(3).get("value"));
                checkBox5.setText("" +"E."+ this.topic.getOptions().get(4).get("value"));checkBox6.setText("" +"F."+ this.topic.getOptions().get(5).get("value"));
                checkBox7.setText("" +"G."+ this.topic.getOptions().get(6).get("value"));checkBox8.setText("" +"H."+ this.topic.getOptions().get(7).get("value"));
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
}