package djjy.stlx;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.example.hsdj.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Use the sjt
 */
public class SingleChoiseFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {
    private TextView tv_title;
    private RadioGroup rg_base;
    private RadioButton rb_option_a;
    private RadioButton rb_option_b;
    private RadioButton rb_option_c;
    private RadioButton rb_option_d;
    private RadioButton rb_option_e;
    private RadioButton rb_option_f;
    private RadioButton rb_option_g;
    private RadioButton rb_option_h;
    private AppCompatCheckBox check_show_analysis;
    private LinearLayout ll_single_analysis;
    private TextView tv_answer,tv_analysis;

    private String option = "";
    private int option_id;
    private int index;//第几题

    /*题目信息*/
    private Topic topic;
    Answer answer=new Answer();
    //题目解析是否可见
    boolean isAnalysisShow;

    public SingleChoiseFragment(Topic topic,int index,boolean isAnalysisShow) {
        this.topic=topic;
        this.index=index;
        this.isAnalysisShow=isAnalysisShow;
    }

    @Override
    public Answer getAnswer(){
        return this.answer;
    }

    @Override
    protected View initView() throws JSONException {
        View view = View.inflate(mActivity, R.layout.fragment_single_choise, null);
        tv_title = view.findViewById(R.id._tv_title);
        rg_base = view.findViewById(R.id._rg_base);
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
        /*选择题*/
        rb_option_a=view.findViewById(R.id._rb_option_a);rb_option_b=view.findViewById(R.id._rb_option_b);rb_option_c=view.findViewById(R.id._rb_option_c);
        rb_option_d=view.findViewById(R.id._rb_option_d);rb_option_e=view.findViewById(R.id._rb_option_e);rb_option_f=view.findViewById(R.id._rb_option_f);
        rb_option_g=view.findViewById(R.id._rb_option_g);rb_option_h=view.findViewById(R.id._rb_option_h);
        switch (this.topic.getOptions().size()){
            case 8:
                break;
            case 7:
                rb_option_h.setVisibility(View.GONE);
                break;
            case 6:
                rb_option_h.setVisibility(View.GONE);rb_option_g.setVisibility(View.GONE);
                break;
            case 5:
                rb_option_h.setVisibility(View.GONE);rb_option_g.setVisibility(View.GONE);rb_option_f.setVisibility(View.GONE);
                break;
            case 4:
                rb_option_h.setVisibility(View.GONE);rb_option_g.setVisibility(View.GONE);rb_option_f.setVisibility(View.GONE);
                rb_option_e.setVisibility(View.GONE);
                break;
            case 3:
                rb_option_h.setVisibility(View.GONE);rb_option_g.setVisibility(View.GONE);rb_option_f.setVisibility(View.GONE);
                rb_option_e.setVisibility(View.GONE);rb_option_d.setVisibility(View.GONE);
                break;
            case 2:
                rb_option_h.setVisibility(View.GONE);rb_option_g.setVisibility(View.GONE);rb_option_f.setVisibility(View.GONE);
                rb_option_e.setVisibility(View.GONE);rb_option_d.setVisibility(View.GONE);rb_option_c.setVisibility(View.GONE);
                break;
            default :
                break;
        }
        //监听事件
        rg_base.setOnCheckedChangeListener(this);

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
                        correctAnswer="A";
                        break;
                    case 1:
                        correctAnswer="B";
                        break;
                    case 2:
                        correctAnswer="C";
                        break;
                    case 3:
                        correctAnswer="D";
                        break;
                    case 4:
                        correctAnswer="E";
                        break;
                    case 5:
                        correctAnswer="F";
                        break;
                    case 6:
                        correctAnswer="G";
                        break;
                    case 7:
                        correctAnswer="H";
                        break;
                }
                break;
            }
        }
        tv_answer.setText("答案："+correctAnswer);
        tv_analysis.setText("解析："+this.topic.getAnalysis());
        tv_title.setText("" +(this.index+1)+"."+this.topic.getTopic_title());
        //单选题，对应选项赋值
        switch(this.topic.getOptions().size()){
            case 2:
                rb_option_a.setText("" +"A."+ this.topic.getOptions().get(0).get("value"));rb_option_b.setText("" +"B."+  this.topic.getOptions().get(1).get("value"));
                break;
            case 3:
                rb_option_a.setText("" +"A."+  this.topic.getOptions().get(0).get("value"));rb_option_b.setText("" +"B."+ this.topic.getOptions().get(1).get("value"));
                rb_option_c.setText("" +"C."+  this.topic.getOptions().get(2).get("value"));
                break;
            case 4:
                rb_option_a.setText("" +"A."+  this.topic.getOptions().get(0).get("value"));rb_option_b.setText("" +"B."+ this.topic.getOptions().get(1).get("value"));
                rb_option_c.setText("" +"C."+ this.topic.getOptions().get(2).get("value"));rb_option_d.setText("" +"D."+ this.topic.getOptions().get(3).get("value"));
                break;
            case 5:
                rb_option_a.setText("" +"A."+  this.topic.getOptions().get(0).get("value"));rb_option_b.setText("" +"B."+ this.topic.getOptions().get(1).get("value"));
                rb_option_c.setText("" +"C."+ this.topic.getOptions().get(2).get("value"));rb_option_d.setText("" +"D."+  this.topic.getOptions().get(3).get("value"));
                rb_option_e.setText("" +"E."+  this.topic.getOptions().get(4).get("value"));
                break;
            case 6:
                rb_option_a.setText("" +"A."+  this.topic.getOptions().get(0).get("value"));rb_option_b.setText("" +"B."+ this.topic.getOptions().get(1).get("value"));
                rb_option_c.setText("" +"C."+ this.topic.getOptions().get(2).get("value"));rb_option_d.setText("" +"D."+  this.topic.getOptions().get(3).get("value"));
                rb_option_e.setText("" +"E."+  this.topic.getOptions().get(4).get("value"));rb_option_f.setText("" +"F."+  this.topic.getOptions().get(5).get("value"));
                break;
            case 7:
                rb_option_a.setText("" +"A."+  this.topic.getOptions().get(0).get("value"));rb_option_b.setText("" +"B."+ this.topic.getOptions().get(1).get("value"));
                rb_option_c.setText("" +"C."+ this.topic.getOptions().get(2).get("value"));rb_option_d.setText("" +"D."+  this.topic.getOptions().get(3).get("value"));
                rb_option_e.setText("" +"E."+  this.topic.getOptions().get(4).get("value"));rb_option_f.setText("" +"F."+ this.topic.getOptions().get(5).get("value"));
                rb_option_g.setText("" +"G."+ this.topic.getOptions().get(6).get("value"));
                break;
            case 8:
                rb_option_a.setText("" +"A."+  this.topic.getOptions().get(0).get("value"));rb_option_b.setText("" +"B."+ this.topic.getOptions().get(1).get("value"));
                rb_option_c.setText("" +"C."+ this.topic.getOptions().get(2).get("value"));rb_option_d.setText("" +"D."+  this.topic.getOptions().get(3).get("value"));
                rb_option_e.setText("" +"E."+  this.topic.getOptions().get(4).get("value"));rb_option_f.setText("" +"F."+ this.topic.getOptions().get(5).get("value"));
                rb_option_g.setText("" +"G."+ this.topic.getOptions().get(6).get("value"));rb_option_h.setText("" +"H."+ this.topic.getOptions().get(7).get("value"));
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        if (checkedId == rb_option_a.getId()) {
            option = "A";option_id=0;
        } else if (checkedId == rb_option_b.getId()) {
            option = "B";option_id=1;
        } else if (checkedId == rb_option_c.getId()) {
            option = "C";option_id=2;
        } else if (checkedId == rb_option_d.getId()) {
            option = "D";option_id=3;
        }else if (checkedId == rb_option_e.getId()){
            option = "E";option_id=4;
        }
        else if (checkedId == rb_option_f.getId()){
            option = "F";option_id=5;
        }
        else if (checkedId == rb_option_g.getId()){
            option = "G";option_id=6;
        }else if (checkedId == rb_option_h.getId()){
            option = "H";option_id=7;
        }
        //设置答案
        answer.setId(index);
        answer.setOption(option);
        for (int i = 0; i < this.topic.getOptions().size(); i++) {
            if("True".equalsIgnoreCase(this.topic.getOptions().get(i).get("checkCurr"))){
                answer.setScore((i==option_id)?this.topic.getTopic_score():0);
                switch(i){
                    case 0:
                        answer.setCorrectOption("A");
                        break;
                    case 1:
                        answer.setCorrectOption("B");
                        break;
                    case 2:
                        answer.setCorrectOption("C");
                        break;
                    case 3:
                        answer.setCorrectOption("D");
                        break;
                    case 4:
                        answer.setCorrectOption("E");
                        break;
                    case 5:
                        answer.setCorrectOption("F");
                        break;
                    case 6:
                        answer.setCorrectOption("G");
                        break;
                    case 7:
                        answer.setCorrectOption("H");
                        break;
                }
            }
        }
        /*避免修改答案时的重复处理*/
        System.out.println(answer.getOption()+"   "+answer.getScore());
    }
}