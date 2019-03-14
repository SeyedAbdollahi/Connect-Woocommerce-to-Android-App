package seyedabdollahi.ir.shop.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import seyedabdollahi.ir.shop.Adapters.AddressAdapter;
import seyedabdollahi.ir.shop.Data.CreateCustomerController;
import seyedabdollahi.ir.shop.Data.CreateOrderController;
import seyedabdollahi.ir.shop.Data.GenerateAuthentication;
import seyedabdollahi.ir.shop.Data.WoocommerceAPI;
import seyedabdollahi.ir.shop.Dialogs.AddAddressDialog;
import seyedabdollahi.ir.shop.EventBus.ChangeAddressList;
import seyedabdollahi.ir.shop.EventBus.CompleteOrder;
import seyedabdollahi.ir.shop.Keys.Keys;
import seyedabdollahi.ir.shop.Keys.WoocommerceUrl;
import seyedabdollahi.ir.shop.Models.Address;
import seyedabdollahi.ir.shop.Models.AddressList;
import seyedabdollahi.ir.shop.Models.AuthenticationWoocommerce;
import seyedabdollahi.ir.shop.Models.Billing;
import seyedabdollahi.ir.shop.Models.CreateOrder;
import seyedabdollahi.ir.shop.Models.Item;
import seyedabdollahi.ir.shop.Models.ItemsList;
import seyedabdollahi.ir.shop.Models.Shipping;
import seyedabdollahi.ir.shop.Models.User;
import seyedabdollahi.ir.shop.MyPreferenceManager;
import seyedabdollahi.ir.shop.R;

public class FinalizeFragment extends Fragment {

    private User user;
    private EditText name;
    private EditText lastName;
    private EditText phone;
    private ConstraintLayout completeOrderLayout;
    private AddressList addressList;
    private Dialog dialog;
    private AddAddressDialog addAddressDialog;
    private int position;
    private RecyclerView addressRecycle;
    private ConstraintLayout addAddress;
    private Address sendAddress;
    private ProgressBar progressBar;
    private ItemsList itemsList;
    private LinearLayout sendAddressLayout;
    private ConstraintLayout virtualLayout;
    private boolean isVirtual;
    private Shipping shipping;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.finalize_cart_fragment , container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getArgum();
        if (user.getId() == null){
            Toast.makeText(getActivity() , "ابتدا وارد حساب کاربری بشوید" , Toast.LENGTH_LONG);
            getFragmentManager().popBackStack();
        }
        else {
            findViews(view);
            configPersonalProfile();
        }
        if (addressList.getAddressList().size() > 0){
            addressList.getAddressList().get(0).setChecked(true);
        }
        checkItemsIsVirtual();
    }

    private void getArgum(){
        user = MyPreferenceManager.getInstance(getActivity()).getUser();
        addressList = MyPreferenceManager.getInstance(getActivity()).getAddresses();
        itemsList = MyPreferenceManager.getInstance(getActivity()).getCartItems();
    }

    private void findViews(View view){
        name = view.findViewById(R.id.finalize_fragment_first_name);
        lastName = view.findViewById(R.id.finalize_fragment_last_name);
        phone = view.findViewById(R.id.finalize_fragment_phone);
        completeOrderLayout = view.findViewById(R.id.finalize_complete_layout);
        addressRecycle = view.findViewById(R.id.finalize_fragment_location_recycle);
        addAddress = view.findViewById(R.id.finalize_fragment_add_location_layout);
        progressBar = view.findViewById(R.id.finalize_fragment_progress);
        sendAddressLayout = view.findViewById(R.id.finalize_send_address_layout);
        virtualLayout = view.findViewById(R.id.finalize_virtual_layout);
    }

    private void configPersonalProfile(){
        if (user.getFirstName() != null){
            name.setText(user.getFirstName());
        }
        if (user.getLastName() != null){
            lastName.setText(user.getLastName());
        }
        if (user.getPhone() != null){
            phone.setText(user.getPhone());
        }
        completeOrderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createOrder();
            }
        });
        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAddressDialog = new AddAddressDialog(getActivity() , null);
                addAddressDialog.show();
            }
        });
    }

    private void createOrder(){
        if (setSendAddress() || isVirtual){
            startCreateOrder();
        }else {
            Toast.makeText(getActivity() , "آدرس ارسال انتخاب کنید" , Toast.LENGTH_SHORT).show();
        }
    }

    private void startCreateOrder(){
        ItemsList itemsList = MyPreferenceManager.getInstance(getActivity()).getCartItems();
        List<Item> listItem = new ArrayList<Item>();
        for (Item item: itemsList.getItemList()){
            listItem.add(item);
        }
        Billing billing = new Billing(name.getText().toString() , lastName.getText().toString() , user.getEmail() , "09217780617");
        if (isVirtual){
            shipping = new Shipping(null  , null , null , null);
        }else {
            shipping = new Shipping(sendAddress.getAddress() , sendAddress.getCity() , sendAddress.getState() , sendAddress.getPostCode());
        }
        CreateOrder createOrder = new CreateOrder(billing , user.getId() , shipping , listItem);
        GenerateAuthentication generateAuthentication = new GenerateAuthentication();
        AuthenticationWoocommerce authenticationWoocommerce = generateAuthentication.woocommerce(WoocommerceUrl.POST , WoocommerceUrl.ORDERS);

        CreateOrderController createOrderController = new CreateOrderController(new WoocommerceAPI.createOrderCallback() {
            @Override
            public void onResponse(boolean successful) {
                progressBar.setVisibility(View.INVISIBLE);
                if (successful){
                    EventBus.getDefault().post(new CompleteOrder());
                }
                else {
                    Toast.makeText(getActivity() , "خطا در ارتباط با سرور" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure() {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity() , "خطا در ارتباط با سرور" , Toast.LENGTH_SHORT).show();
            }
        });
        progressBar.setVisibility(View.VISIBLE);
        createOrderController.start(authenticationWoocommerce, createOrder);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeAddressList(final ChangeAddressList event){
        switch (event.getAction()){
            case "ADD":
                addressList.getAddressList().add(event.getAddress());
                MyPreferenceManager.getInstance(getActivity()).putAddresses(addressList);
                addAddressDialog.dismiss();
                showAddresses();
                Toast.makeText(getActivity() , "آدرس اضافه شد" , Toast.LENGTH_SHORT).show();
                break;
            case "EDIT_CLICKED":
                position = event.getPosition();
                addAddressDialog = new AddAddressDialog(getActivity() , event.getAddress());
                addAddressDialog.show();
                break;
            case "EDITED":
                addressList.getAddressList().set(position , event.getAddress());
                MyPreferenceManager.getInstance(getActivity()).putAddresses(addressList);
                addAddressDialog.dismiss();
                showAddresses();
                Toast.makeText(getActivity() , "آدرس ویرایش شد" , Toast.LENGTH_SHORT).show();
                break;
            case "DELETE_CLICKED":
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setMessage("آیا آدرس حذف بشود؟");
                alertDialog.setNegativeButton("بله", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        addressList.getAddressList().remove(event.getPosition());
                        MyPreferenceManager.getInstance(getActivity()).putAddresses(addressList);
                        showAddresses();
                        Toast.makeText(getActivity() , "آدرس حدف شد" , Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }).setPositiveButton("خیر", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                    }
                });
                dialog = alertDialog.create();
                dialog.show();
                break;
            case "CHECKED":
                position = event.getPosition();
                for (int i=0 ; i<addressList.getAddressList().size() ; i++){
                    if (i == position){
                        addressList.getAddressList().get(i).setChecked(true);
                    }
                    else {
                        addressList.getAddressList().get(i).setChecked(false);
                    }
                }
                //MyPreferenceManager.getInstance(getActivity()).putAddresses(addressList);
                showAddresses();
                break;
        }
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

    private void showAddresses(){
        AddressAdapter addressAdapter = new AddressAdapter(addressList , true);
        addressRecycle.setLayoutManager( new LinearLayoutManager(getActivity() , LinearLayout.HORIZONTAL , true));
        addressRecycle.setAdapter(addressAdapter);
    }

    private boolean setSendAddress(){
        for (Address address : addressList.getAddressList()){
            if (address.getChecked()){
                sendAddress = address;
                return true;
            }
        }
        return false;
    }

    private void checkItemsIsVirtual(){
        for (Item item : itemsList.getItemList()){
            if (item.getVirtual().equals("false")){
                virtualLayout.setVisibility(View.GONE);
                showAddresses();
                isVirtual = false;
                return;
            }
        }
        isVirtual = true;
        sendAddressLayout.setVisibility(View.GONE);
    }
}
