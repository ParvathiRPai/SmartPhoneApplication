package ui;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.journal.BillListActivity;
import com.codepath.journal.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.Bill;

public class BillsRecyclerAdapter extends RecyclerView.Adapter<BillsRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Bill> billList;

    public BillsRecyclerAdapter(Context context, List<Bill> billList) {
        this.context = context;
        this.billList = billList;
    }

    @NonNull
    @Override
    public BillsRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.bill_row,viewGroup, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull BillsRecyclerAdapter.ViewHolder  viewHolder, int position) {
        Bill bill= billList.get(position);
        String imageUrl;

        viewHolder.title.setText(bill.getTitle());
        viewHolder.thoughts.setText(bill.getThought());
        imageUrl=bill.getImageUrl();
        String timeAgo= (String) DateUtils.getRelativeTimeSpanString(bill.getTimeAdded().getSeconds()* 1000);
        viewHolder.dataAdded.setText(timeAgo);
        //Use picasso library to download and show image
        Picasso.get().load(imageUrl).placeholder(R.drawable.image_three).fit().into(viewHolder.image);

    }

    @Override
    public int getItemCount() {
        return billList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title,thoughts,dataAdded,name;
        public ImageView image;
        String userId;
        String username;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context=ctx;
            title=itemView.findViewById(R.id.journal_title_list);
            thoughts=itemView.findViewById((R.id.journal_thought_list));
            dataAdded=itemView.findViewById(R.id.journal_timestamp_list);
            image=itemView.findViewById(R.id.journal_image_list);

        }
    }
}
