package seyedabdollahi.ir.shop.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import seyedabdollahi.ir.shop.Data.GenerateAuthentication;
import seyedabdollahi.ir.shop.Data.GetProductWithIdController;
import seyedabdollahi.ir.shop.Data.WoocommerceAPI;
import seyedabdollahi.ir.shop.Keys.WoocommerceUrl;
import seyedabdollahi.ir.shop.Models.AuthenticationWoocommerce;
import seyedabdollahi.ir.shop.Models.ItemsList;
import seyedabdollahi.ir.shop.Models.Product;
import seyedabdollahi.ir.shop.MyPreferenceManager;
import seyedabdollahi.ir.shop.R;

public class DetailOrderAdapter extends RecyclerView.Adapter<DetailOrderAdapter.ViewHolder> {

    private ItemsList items;

    public DetailOrderAdapter(ItemsList items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.template_order_items , viewGroup , false);
        return new DetailOrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.detailLayout.setMinimumHeight(MyPreferenceManager.getInstance(holder.detailLayout.getContext()).getRealSize().y / 4);
        holder.name.setText(items.getItemList().get(position).getName());
        holder.count.setText(items.getItemList().get(position).getQuantity());
        holder.price.setText(items.getItemList().get(position).getPrice() + " تومان");

        GenerateAuthentication generateAuthentication = new GenerateAuthentication();
        AuthenticationWoocommerce authenticationWoocommerce = generateAuthentication.woocommerce(WoocommerceUrl.GET , WoocommerceUrl.PRODUCTS + items.getItemList().get(position).getProductId());
        GetProductWithIdController getProductWithIdController = new GetProductWithIdController(new WoocommerceAPI.getProductWithIdCallback() {
            @Override
            public void onResponse(boolean successful, Product product) {
                if (successful){
                    if (product.getImages().get(0).getSrc() != null) {
                        Glide.with(holder.image.getContext()).load(product.getImages().get(0).getSrc()).into(holder.image);
                    }
                }
            }

            @Override
            public void onFailure() {

            }
        });
        getProductWithIdController.start(authenticationWoocommerce , items.getItemList().get(position).getProductId());
    }

    @Override
    public int getItemCount() {
        return items.getItemList().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView count;
        private TextView price;
        private LinearLayout detailLayout;
        private ImageView image;

        private ViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.template_order_items_name);
            count = itemView.findViewById(R.id.template_order_items_count);
            price = itemView.findViewById(R.id.template_order_items_price);
            detailLayout = itemView.findViewById(R.id.template_order_items_layout);
            image = itemView.findViewById(R.id.template_order_items_image);
        }
    }
}
