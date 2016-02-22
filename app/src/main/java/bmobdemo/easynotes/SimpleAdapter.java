package bmobdemo.easynotes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2016/1/17.
 */
public class SimpleAdapter extends RecyclerView.Adapter<MyViewHolder> implements View.OnClickListener {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<note> list;
    private List<Integer> mHeights;
    private OnRecyclerViewItemClickListener mOnItemClickLitener;


    public SimpleAdapter(Context context, List<note> list) {
        this.mContext = context;
        this.list = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public void onClick(View v) {

    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, note note, int pos);

        void onItemLongClidc(View view, note note, int pos);
    }

    public void setmOnItemClickLitener(OnRecyclerViewItemClickListener litener) {
        this.mOnItemClickLitener = litener;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int pos) {
        final note note = list.get(pos);
        holder.list_title.setText(note.getTitle());
        holder.list_content.setText(note.getContent());
        holder.list_time.setText(note.getTime());
        holder.list_id.setText(note.getId());


        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int layoutPosition = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(v, note, layoutPosition);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int layoutPosition = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClidc(v, note, layoutPosition);

                    return false;
                }
            });
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_single_textview, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }
}

class MyViewHolder extends RecyclerView.ViewHolder {
    TextView list_title, list_content, list_time, list_id;


    public MyViewHolder(View itemView) {
        super(itemView);
        list_title = (TextView) itemView.findViewById(R.id.list_title);
        list_content = (TextView) itemView.findViewById(R.id.list_content);
        list_time = (TextView) itemView.findViewById(R.id.list_time);
        list_id = (TextView) itemView.findViewById(R.id.list_id);
    }
}