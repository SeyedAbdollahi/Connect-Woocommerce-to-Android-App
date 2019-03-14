package seyedabdollahi.ir.shop.Adapters;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import org.greenrobot.eventbus.EventBus;
import java.util.List;
import seyedabdollahi.ir.shop.EventBus.ProductClicked;
import seyedabdollahi.ir.shop.Models.Image;
import seyedabdollahi.ir.shop.Models.Product;
import seyedabdollahi.ir.shop.MyPreferenceManager;
import seyedabdollahi.ir.shop.R;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> items;

    public ProductAdapter(List<Product> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.template_product , viewGroup , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.layout.setMaxHeight(MyPreferenceManager.getInstance(holder.layout.getContext()).getRealSize().y / 4);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new ProductClicked(items.get(position)));
            }
        });
        if (items.get(position).getName() != null){
            holder.name.setText(items.get(position).getName());
        }else{
            Log.d("TAG" , "[Name] position " + position + " is null");
        }
        List<Image> images = items.get(position).getImages();
        if (images.get(0).getSrc() != null) {
            Glide.with(holder.image.getContext()).load(images.get(0).getSrc()).into(holder.image);
        }else {
            Log.d("TAG" , "[Image] position " + position + " is null");
        }
        if (items.get(position).getPrice() != null) {
            holder.price.setText(items.get(position).getPrice() + " تومان");
        }else {
            Log.d("TAG" , "[price] position " + position + " is null");
        }
        if (!items.get(position).getVirtual().equals("true")){
            holder.virtual.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class   ViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private ImageView image;
        private TextView price;
        private ConstraintLayout layout;
        private ImageView virtual;

        private ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.template_product_name);
            image = itemView.findViewById(R.id.template_product_image);
            price = itemView.findViewById(R.id.template_product_price);
            layout = itemView.findViewById(R.id.template_product_layout);
            virtual = itemView.findViewById(R.id.template_product_virtual);
        }
    }
}
