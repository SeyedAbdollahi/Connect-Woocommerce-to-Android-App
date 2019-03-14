package seyedabdollahi.ir.shop.Adapters;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import seyedabdollahi.ir.shop.EventBus.ChangeAddressList;
import seyedabdollahi.ir.shop.Models.Address;
import seyedabdollahi.ir.shop.Models.AddressList;
import seyedabdollahi.ir.shop.MyPreferenceManager;
import seyedabdollahi.ir.shop.R;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private AddressList items;
    private boolean radioButton;

    public AddressAdapter(AddressList items, boolean radioButton) {
        this.items = items;
        this.radioButton = radioButton;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.template_address , viewGroup , false);
        return new AddressAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.layout.setMaxWidth((MyPreferenceManager.getInstance(holder.layout.getContext()).getRealSize().x / 10) * 6);
        holder.state.setText(items.getAddressList().get(position).getState());
        holder.city.setText(items.getAddressList().get(position).getCity());
        holder.postCode.setText(items.getAddressList().get(position).getPostCode());
        holder.address.setText(items.getAddressList().get(position).getAddress());

        holder.deleteAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new ChangeAddressList("DELETE_CLICKED" , null , position));
            }
        });
        holder.editAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new ChangeAddressList("EDIT_CLICKED" , items.getAddressList().get(position) , position));
            }
        });

        if (radioButton){
            holder.checkAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new ChangeAddressList("CHECKED" , null , position));
                }
            });
            holder.checkAddress.setChecked(items.getAddressList().get(position).getChecked());
        }
        else {
            holder.checkAddress.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items.getAddressList().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView state;
        private TextView city;
        private TextView postCode;
        private TextView address;
        private ImageView deleteAddress;
        private ImageView editAddress;
        private ConstraintLayout layout;
        private RadioButton checkAddress;

        private ViewHolder(View itemView){
            super(itemView);
            layout = itemView.findViewById(R.id.template_address_layout);
            state = itemView.findViewById(R.id.template_address_state);
            city = itemView.findViewById(R.id.template_address_city);
            postCode = itemView.findViewById(R.id.template_address_post_code);
            address = itemView.findViewById(R.id.template_address);
            deleteAddress = itemView.findViewById(R.id.template_address_delete_img);
            editAddress = itemView.findViewById(R.id.template_address_edit_img);
            checkAddress = itemView.findViewById(R.id.template_address_radio_btn);
        }
    }
}
