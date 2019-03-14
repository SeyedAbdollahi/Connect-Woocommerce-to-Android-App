package seyedabdollahi.ir.shop.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.List;
import seyedabdollahi.ir.shop.Adapters.DetailOrderAdapter;
import seyedabdollahi.ir.shop.EventBus.DetailOrderClicked;
import seyedabdollahi.ir.shop.Models.Item;
import seyedabdollahi.ir.shop.Models.ItemsList;
import seyedabdollahi.ir.shop.R;

public class DetailOrderFragment extends Fragment {

    private RecyclerView recyclerView;
    private ItemsList itemsList;
    private TextView price;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.detail_order_fragment , container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        itemsList = (ItemsList) getArguments().get("items");
        findViews(view);
        configViews();
    }

    private void findViews(View view){
        recyclerView = view.findViewById(R.id.detail_order_recycle);
        price = view.findViewById(R.id.detail_order_total_price);
    }

    private void configViews(){
        price.setText(getArguments().getString("totalPrice") + " تومان");
        DetailOrderAdapter detailOrderAdapter = new DetailOrderAdapter(itemsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(detailOrderAdapter);
    }
}
