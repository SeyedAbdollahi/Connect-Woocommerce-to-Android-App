package seyedabdollahi.ir.shop.Adapters;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import seyedabdollahi.ir.shop.Models.CommentList;
import seyedabdollahi.ir.shop.R;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private CommentList items;

    public CommentAdapter(CommentList items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.template_comment , viewGroup , false);
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.webView.loadData("<body dir=\"rtl\">" + items.getCommentList().get(position).getText() + "</body>" , "text/html" , "UTF-8");
        holder.name.setText(items.getCommentList().get(position).getName());
        holder.date.setText(items.getCommentList().get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return items.getCommentList().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private WebView webView;
        private TextView name;
        private TextView date;

        private ViewHolder(View itemView){
            super(itemView);
            webView = itemView.findViewById(R.id.template_comment_web_view);
            name = itemView.findViewById(R.id.template_comment_name);
            date = itemView.findViewById(R.id.template_comment_date);
        }
    }
}
