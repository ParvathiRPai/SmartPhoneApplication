package ui;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.iClaim.R;
import com.codepath.iClaim.Test2Speech;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.Bill;

public class BillsRecyclerAdapter extends RecyclerView.Adapter<BillsRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Bill> billList;
    private Test2Speech t2s;

    public BillsRecyclerAdapter(Context context, List<Bill> billList) {
        this.context = context;
        this.billList = billList;
        this.t2s = new Test2Speech(context);
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

        viewHolder.bill_row_button.setOnClickListener(v -> {
            t2s.speakBillRow(context, bill.getTitle(), bill.getThought());
        });

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
        private final AppCompatImageButton bill_row_button;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context=ctx;
            title=itemView.findViewById(R.id.bills_title_list);
            thoughts=itemView.findViewById((R.id.bills_updated_list));
            dataAdded=itemView.findViewById(R.id.bills_timestamp_list);
            image=itemView.findViewById(R.id.bills_image_list);
            bill_row_button = itemView.findViewById(R.id.bill_row_speak);

        }
    }
}
