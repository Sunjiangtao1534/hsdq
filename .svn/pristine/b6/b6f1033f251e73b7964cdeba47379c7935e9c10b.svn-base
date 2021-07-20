package djjy.stlx;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import org.json.JSONException;

/**
 * @ 描述：BaseFragment基类
 * @ 作者: sjt
 */

public abstract  class BaseFragment extends Fragment {

    public FragmentActivity mActivity;
    public Answer answer;
    public Topic topic;

    public Answer getAnswer(){
        return this.answer;
    }

    public Topic getTopic(){
        return this.topic;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        try {
            view = initView();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    //view的初始化
    protected abstract View initView() throws JSONException;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initListener();
    }
    /**
     * 初始化数据
     */
    public abstract void initData();
    /**
     * 初始化监听器
     */
    void initListener(){};

}
