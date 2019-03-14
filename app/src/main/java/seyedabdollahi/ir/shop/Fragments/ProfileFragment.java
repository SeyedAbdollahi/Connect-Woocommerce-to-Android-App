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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import seyedabdollahi.ir.shop.Adapters.AddressAdapter;
import seyedabdollahi.ir.shop.Dialogs.AddAddressDialog;
import seyedabdollahi.ir.shop.EventBus.ChangeAddressList;
import seyedabdollahi.ir.shop.EventBus.EditUserSuccessful;
import seyedabdollahi.ir.shop.EventBus.UserLogout;
import seyedabdollahi.ir.shop.Models.Address;
import seyedabdollahi.ir.shop.Models.AddressList;
import seyedabdollahi.ir.shop.Models.User;
import seyedabdollahi.ir.shop.MyPreferenceManager;
import seyedabdollahi.ir.shop.R;

public class ProfileFragment extends Fragment {

    private ConstraintLayout editLayout;
    private ConstraintLayout addAddress;
    private ConstraintLayout logoutLayout;
    private RecyclerView addressRecycle;
    private AddressList addressList;
    private AddAddressDialog addAddressDialog;
    private TextView name;
    private TextView email;
    private TextView phone;
    private User user;
    private int position;
    private Dialog deleteDialog;
    private Dialog logoutDialog;
    private ConstraintLayout ordersLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment , container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        getArgum();
        configViews();
        showAddresses();
    }

    private void findViews(View view){
        editLayout = view.findViewById(R.id.profile_edit_user_layout);
        name = view.findViewById(R.id.profile_user_name);
        email = view.findViewById(R.id.profile_user_email);
        phone = view.findViewById(R.id.profile_user_phone);
        addAddress = view.findViewById(R.id.profile_add_location_layout);
        addressRecycle = view.findViewById(R.id.profile_location_recycle);
        ordersLayout = view.findViewById(R.id.profile_orders_layout);
        logoutLayout = view.findViewById(R.id.profile_logout_layout);
    }

    private void getArgum(){
       user = (User) getArguments().getSerializable("user");
       addressList = MyPreferenceManager.getInstance(getActivity()).getAddresses();
    }

    private void configViews(){
        name.setText(user.getFirstName() + user.getLastName());
        email.setText(user.getEmail());
        editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("user" , user);
                EditUserFragment editUserFragment = new EditUserFragment();
                editUserFragment.setArguments(bundle);
                getFragmentManager().beginTransaction()
                        .add(R.id.profile_frame, editUserFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAddressDialog = new AddAddressDialog(getActivity() , null);
                addAddressDialog.show();
            }
        });
        ordersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrdersFragment ordersFragment = new OrdersFragment();
                getFragmentManager().beginTransaction()
                        .add(R.id.profile_frame , ordersFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("آیا مایل به خروج از حساب کاربری هستید؟");
                builder.setPositiveButton("بله", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MyPreferenceManager.getInstance(getActivity()).clearUser();
                        logoutDialog.dismiss();
                        EventBus.getDefault().post(new UserLogout());
                    }
                }).setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        logoutDialog.dismiss();
                    }
                });
                logoutDialog = builder.create();
                logoutDialog.show();
            }
        });
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

    @Subscribe (threadMode = ThreadMode.MAIN)
    public void editUser(EditUserSuccessful event){
        MyPreferenceManager.getInstance(getActivity()).putUser(event.getUser());
        user = event.getUser();
        configViews();
        getFragmentManager().popBackStack();
    }

    @Subscribe (threadMode = ThreadMode.MAIN)
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
                        deleteDialog.dismiss();
                    }
                }).setPositiveButton("خیر", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteDialog.dismiss();
                    }
                });
                deleteDialog = alertDialog.create();
                deleteDialog.show();
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

    private void showAddresses(){
        AddressAdapter addressAdapter = new AddressAdapter(addressList , false);
        addressRecycle.setLayoutManager( new LinearLayoutManager(getActivity() , LinearLayout.HORIZONTAL , true));
        addressRecycle.setAdapter(addressAdapter);
    }
}
