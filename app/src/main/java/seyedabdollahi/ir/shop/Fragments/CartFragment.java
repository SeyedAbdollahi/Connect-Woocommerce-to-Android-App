package seyedabdollahi.ir.shop.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import seyedabdollahi.ir.shop.Adapters.ItemAdapter;
import seyedabdollahi.ir.shop.EventBus.CompleteOrder;
import seyedabdollahi.ir.shop.EventBus.DeleteItemClicked;
import seyedabdollahi.ir.shop.EventBus.TotalPriceChanged;
import seyedabdollahi.ir.shop.Models.Item;
import seyedabdollahi.ir.shop.Models.ItemsList;
import seyedabdollahi.ir.shop.MyPreferenceManager;
import seyedabdollahi.ir.shop.R;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView txt;
    private Button openListOrdersBtn;
    private ItemsList itemsList;
    private TextView totalPrice;
    private ConstraintLayout priceLayout;
    private ConstraintLayout finalizeLayout;
    private AlertDialog dialog;
    private int position;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cart_fragment , container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        configViews();
        itemsList = MyPreferenceManager.getInstance(getActivity()).getCartItems();
        showRecycle();
    }

    private void findViews(View view){
        recyclerView = view.findViewById(R.id.cart_fragment_recycle);
        txt = view.findViewById(R.id.cart_fragment_txt);
        openListOrdersBtn = view.findViewById(R.id.cart_fragment_btn);
        totalPrice = view.findViewById(R.id.cart_fragment_total_price);
        priceLayout = view.findViewById(R.id.cart_fragment_price_layout);
        finalizeLayout = view.findViewById(R.id.cart_fragment_finalize_layout);
    }

    private void configViews(){
        openListOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrdersFragment ordersFragment = new OrdersFragment();
                getFragmentManager().beginTransaction()
                        .add(R.id.cart_fragment_full_frame , ordersFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        finalizeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FinalizeFragment finalizeFragment = new FinalizeFragment();
                getFragmentManager().beginTransaction()
                        .add(R.id.cart_fragment_full_frame , finalizeFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void showRecycle(){
        ItemAdapter itemAdapter = new ItemAdapter(itemsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(itemAdapter);
        if (itemsList.getItemList().size() != 0){
            recyclerView.setVisibility(View.VISIBLE);
            txt.setVisibility(View.INVISIBLE);
            openListOrdersBtn.setVisibility(View.INVISIBLE);
            priceLayout.setVisibility(View.VISIBLE);
            finalizeLayout.setVisibility(View.VISIBLE);
        }else {
            recyclerView.setVisibility(View.INVISIBLE);
            txt.setVisibility(View.VISIBLE);
            openListOrdersBtn.setVisibility(View.VISIBLE);
            priceLayout.setVisibility(View.INVISIBLE);
            finalizeLayout.setVisibility(View.INVISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void deleteItem(final DeleteItemClicked event){
        position = 0;
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("آیا مایل به حذف محصول از سبد خرید هستید؟");
        builder.setPositiveButton("بله", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for(Item item: itemsList.getItemList()){
                    if (item.getProductId().equals(event.getItem().getProductId())){
                        itemsList.getItemList().remove(position);
                        MyPreferenceManager.getInstance(getActivity()).putCartItems(itemsList);
                        showRecycle();
                    }
                    position++;
                }
            }
        }).setNegativeButton("خیر", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void totalPriceChanged(TotalPriceChanged event){
        totalPrice.setText(event.getTotalPrice());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void completeOrder(CompleteOrder event){
        MyPreferenceManager.getInstance(getActivity()).clearCartItems();
        itemsList.getItemList().clear();
        showRecycle();
        getFragmentManager().popBackStack();
        Toast.makeText(getActivity() , "خرید با موفقیت انجام شد"  , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
