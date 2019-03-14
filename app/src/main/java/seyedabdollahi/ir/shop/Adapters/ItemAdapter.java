package seyedabdollahi.ir.shop.Adapters;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import seyedabdollahi.ir.shop.EventBus.DeleteItemClicked;
import seyedabdollahi.ir.shop.EventBus.TotalPriceChanged;
import seyedabdollahi.ir.shop.Models.Item;
import seyedabdollahi.ir.shop.Models.ItemsList;
import seyedabdollahi.ir.shop.Models.Product;
import seyedabdollahi.ir.shop.MyPreferenceManager;
import seyedabdollahi.ir.shop.R;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private ItemsList items;

    public ItemAdapter(ItemsList items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.template_item , viewGroup , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder , final int position) {

        holder.layout.setMinimumHeight(MyPreferenceManager.getInstance(holder.layout.getContext()).getRealSize().y / 4);
        holder.name.setText(items.getItemList().get(position).getName());
        holder.count.setText("تعداد: " + items.getItemList().get(position).getQuantity());
        holder.price.setText(items.getItemList().get(position).getPrice()+ " تومان");
        if (items.getItemList().get(position).getImage() != null){
            Glide.with(holder.image.getContext()).load(items.getItemList().get(position).getImage()).into(holder.image);
        }
        if (!items.getItemList().get(position).getVirtual().equals("true")){
            holder.virtual.setVisibility(View.GONE);
        }
        calculateTotalPrice();
        holder.deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new DeleteItemClicked(items.getItemList().get(position)));
            }
        });

        holder.countUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(items.getItemList().get(position).getQuantity()) + 1;
                holder.count.setText("تعداد: " + Integer.toString(count));
                items.getItemList().get(position).setQuantity(Integer.toString(count));
                items.getItemList().get(position).setTotalPrice(Integer.toString(count * Integer.parseInt(items.getItemList().get(position).getPrice())));
                holder.price.setText(items.getItemList().get(position).getTotalPrice() + " تومان");
                calculateTotalPrice();
                MyPreferenceManager.getInstance(holder.count.getContext()).putCartItems(items);
            }
        });

        holder.countDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!items.getItemList().get(position).getQuantity().equals("1")){
                    int count = Integer.parseInt(items.getItemList().get(position).getQuantity()) - 1;
                    holder.count.setText("تعداد: " + Integer.toString(count));
                    items.getItemList().get(position).setQuantity(Integer.toString(count));
                    items.getItemList().get(position).setTotalPrice(Integer.toString(count * Integer.parseInt(items.getItemList().get(position).getPrice())));
                    holder.price.setText(items.getItemList().get(position).getTotalPrice() + " تومان");
                    calculateTotalPrice();
                    MyPreferenceManager.getInstance(holder.count.getContext()).putCartItems(items);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.getItemList().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView price;
        private TextView count;
        private ImageView countUp;
        private ImageView countDown;
        private ImageView image;
        private ConstraintLayout deleteLayout;
        private LinearLayout layout;
        private ImageView virtual;

        private ViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.template_item_name);
            price = itemView.findViewById(R.id.template_item_price);
            count = itemView.findViewById(R.id.template_item_count);
            layout = itemView.findViewById(R.id.template_item_layout);
            image = itemView.findViewById(R.id.template_item_product_img);
            virtual = itemView.findViewById(R.id.template_item_virtual);
            deleteLayout = itemView.findViewById(R.id.template_item_delete_layout);
            countUp = itemView.findViewById(R.id.template_item_count_up);
            countDown = itemView.findViewById(R.id.template_item_count_down);
        }
    }

    private void calculateTotalPrice() {
        int totalPrice = 0;
        for (Item item : items.getItemList()) {
            totalPrice = totalPrice + Integer.parseInt(item.getTotalPrice());
        }
        EventBus.getDefault().post(new TotalPriceChanged(Integer.toString(totalPrice) + " تومان"));
    }
}
