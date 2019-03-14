package seyedabdollahi.ir.shop.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import seyedabdollahi.ir.shop.Adapters.OrderAdapter;
import seyedabdollahi.ir.shop.Data.GenerateAuthentication;
import seyedabdollahi.ir.shop.Data.GetAllOrderController;
import seyedabdollahi.ir.shop.Data.WoocommerceAPI;
import seyedabdollahi.ir.shop.EventBus.DetailOrderClicked;
import seyedabdollahi.ir.shop.Keys.WoocommerceUrl;
import seyedabdollahi.ir.shop.Models.AuthenticationWoocommerce;
import seyedabdollahi.ir.shop.Models.Order;
import seyedabdollahi.ir.shop.MyPreferenceManager;
import seyedabdollahi.ir.shop.R;

public class OrdersFragment extends Fragment {

    private CoordinatorLayout orderLayout;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private TextView txt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.orders_fragment , container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        getAllOrders();
    }

    private void findViews(View view){
        orderLayout = view.findViewById(R.id.orders_fragment_layout);
        progressBar = view.findViewById(R.id.orders_fragment_progress);
        recyclerView = view.findViewById(R.id.orders_fragment_recycler);
        txt = view.findViewById(R.id.orders_fragment_txt);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void detailOrderClicked(DetailOrderClicked event){
        Bundle bundle = new Bundle();
        bundle.putSerializable("items" , event.getItemsList());
        bundle.putString("totalPrice" , event.getTotalPrice());
        DetailOrderFragment detailOrderFragment = new DetailOrderFragment();
        detailOrderFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .add(R.id.orders_fragment_full_frame , detailOrderFragment)
                .addToBackStack(null)
                .commit();
    }

    private void getAllOrders(){
        progressBar.setVisibility(View.VISIBLE);
        GetAllOrderController getAllOrderController = new GetAllOrderController(MyPreferenceManager.getInstance(getActivity()).getUser().getId(), new WoocommerceAPI.getAllOrdersCallback() {
            @Override
            public void onResponse(boolean successful, List<Order> orders) {
                if(successful){
                    if (orders.size() > 0){
                        showOrders(orders);
                    }
                    else {
                        txt.setVisibility(View.VISIBLE);
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                }
                else {
                    showSnackBar(false , "خطا در ارتباط با سرور");
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure() {
                showSnackBar(false , "خطا در ارتباط با سرور");
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
        GenerateAuthentication generateAuthentication = new GenerateAuthentication();
        AuthenticationWoocommerce authenticationWoocommerce = generateAuthentication.woocommerce(WoocommerceUrl.GET , WoocommerceUrl.ORDERS);
        getAllOrderController.start(authenticationWoocommerce);
    }

    private void showOrders(List<Order> orders){
        List<Order> userOrders = new ArrayList<Order>();
        String userId = MyPreferenceManager.getInstance(getActivity()).getUser().getId();
        for (Order order: orders){
            if (userId.equals(order.getCustomerId())){
                userOrders.add(order);
            }
        }
        if (userOrders.size() > 0) {
            txt.setVisibility(View.INVISIBLE);
            OrderAdapter orderAdapter = new OrderAdapter(userOrders);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(orderAdapter);
        }
        else {
            txt.setVisibility(View.VISIBLE);
        }
    }

    private void showSnackBar(boolean successful , String text){
        TSnackbar snackbar = TSnackbar.make(orderLayout, text , TSnackbar.LENGTH_LONG);
        View view = snackbar.getView();
        TextView textView = view.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.setActionTextColor(Color.WHITE);
        if(successful){
            view.setBackgroundColor(getResources().getColor(R.color.green));
        }else {
            snackbar.setDuration(TSnackbar.LENGTH_INDEFINITE);
            view.setBackgroundColor(getResources().getColor(R.color.red));
            snackbar.setAction("try Again", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAllOrders();
                }
            });
        }
        snackbar.show();
    }
}
