package seyedabdollahi.ir.shop.Adapters;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import seyedabdollahi.ir.shop.EventBus.DetailOrderClicked;
import seyedabdollahi.ir.shop.Models.ItemsList;
import seyedabdollahi.ir.shop.Models.Order;
import seyedabdollahi.ir.shop.R;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private List<Order> items;

    public OrderAdapter(List<Order> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.template_order , viewGroup , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.orderId.setText(items.get(position).getId());
        holder.orderPrice.setText(items.get(position).getTotalPrice() + " تومان");
        holder.orderDate.setText(items.get(position).getDate());
        switch (items.get(position).getStatus()){
            case "completed":
                holder.orderStatus.setText("تکمیل شده");
                break;
            case "processing":
                holder.orderStatus.setText("در حال انجام");
                break;
            case "pending":
                holder.orderStatus.setText("در انتظار پرداخت");
                break;
            case "on-hold":
                holder.orderStatus.setText("در انتظار بررسی");
                break;
            case "cancelled":
                holder.orderStatus.setText("لغو شده");
                break;
            case "refunded":
                holder.orderStatus.setText("مسترد شده");
                break;
            case "failed":
                holder.orderStatus.setText("ناموفق");
                break;
            default:
                holder.orderStatus.setText(items.get(position).getStatus());
        }
        holder.detailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemsList itemsList = new ItemsList();
                itemsList.setItemList(items.get(position).getItems());
                EventBus.getDefault().post(new DetailOrderClicked(itemsList , items.get(position).getTotalPrice()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private ConstraintLayout detailLayout;
        private TextView orderId;
        private TextView orderDate;
        private TextView orderPrice;
        private TextView orderStatus;

        private ViewHolder(View itemView){
            super(itemView);
            detailLayout = itemView.findViewById(R.id.template_order_detail_order);
            orderId = itemView.findViewById(R.id.template_order_id);
            orderDate = itemView.findViewById(R.id.template_order_date);
            orderPrice = itemView.findViewById(R.id.template_order_price);
            orderStatus = itemView.findViewById(R.id.template_order_status);
        }
    }
}
