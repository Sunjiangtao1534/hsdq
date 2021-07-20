package djjy.spkc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.hsdj.R;
import com.squareup.picasso.Picasso;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by RANLEGERAN on 2018/5/13.
 */

public class VideoListAdapter extends BaseAdapter {
    Context mContext;
    int mPager = -1;
    private String file_path;
    private String remark;

    public VideoListAdapter(Context context,String file_path, String remark) {
        this.mContext = context;
        this.file_path = file_path;
        this.remark = remark;
    }

    public VideoListAdapter(Context context, int pager) {
        this.mContext = context;
        this.mPager = pager;
    }

    @Override
    public int getCount() {
        return mPager == -1 ? 1: 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            holder = new ViewHolder();
            LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
            convertView = mLayoutInflater.inflate(R.layout.activity_video_item, null);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mJCVideoPlayerStandard = (JCVideoPlayerStandard) convertView.findViewById(R.id.videoplayer);
        if (mPager == -1) {
            holder.mJCVideoPlayerStandard.setUp(
                    file_path, JCVideoPlayer.SCREEN_LAYOUT_LIST, remark);
            Picasso.with(convertView.getContext())
                    .load(R.drawable.a)
                    .into(holder.mJCVideoPlayerStandard.thumbImageView);
        } else {
            holder.mJCVideoPlayerStandard.setUp(
                    file_path, JCVideoPlayer.SCREEN_LAYOUT_LIST, remark);
            Picasso.with(convertView.getContext())
                    .load(R.drawable.a)
                    .into(holder.mJCVideoPlayerStandard.thumbImageView);
        }
        return convertView;
    }

    /*获取视频第一帧的图片*/
   /* public void getBitmap(String videoUrl){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoUrl, new HashMap());
        //获得第1帧图片 这里的第一个参数 以微秒为单位
        Bitmap bitmap = retriever.getFrameAtTime();
        retriever.release();
        //图片转存本地文件
        File file=new File(String.valueOf(R.drawable.temp));//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    class ViewHolder {
        JCVideoPlayerStandard mJCVideoPlayerStandard;
    }
}
