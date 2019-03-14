package seyedabdollahi.ir.shop;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import seyedabdollahi.ir.shop.Keys.Keys;
import seyedabdollahi.ir.shop.Models.AddressList;
import seyedabdollahi.ir.shop.Models.Item;
import seyedabdollahi.ir.shop.Models.ItemsList;
import seyedabdollahi.ir.shop.Models.User;

public class MyPreferenceManager {

    private static MyPreferenceManager instance = null;
    private SharedPreferences sharedPreferences = null;
    private SharedPreferences.Editor editor = null;

    public static MyPreferenceManager getInstance(Context context){
        if (instance == null){
            instance = new MyPreferenceManager(context);
        }
        return instance;
    }

    private MyPreferenceManager(Context context){
        sharedPreferences = context.getSharedPreferences("my_preference" , Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void putRealSize(Point point){
        editor.putInt(Keys.WIDTH, point.x);
        editor.putInt(Keys.HEIGHT , point.y);
        editor.apply();
    }

    public Point getRealSize(){
        Point point = new Point();
        point.x = sharedPreferences.getInt(Keys.WIDTH , 1);
        point.y = sharedPreferences.getInt(Keys.HEIGHT , 1);
        return point;
    }

    public void putUser(User user){
        Gson gson = new Gson();
        String userJson = gson.toJson(user , User.class);
        editor.putString(Keys.USER , userJson);
        editor.apply();
    }

    public User getUser(){
        Gson gson = new Gson();
        String userJson = sharedPreferences.getString(Keys.USER , null);
        if(userJson == null){
            return new User();
        }
        return gson.fromJson(userJson , User.class);
    }

    public void putCartItems(ItemsList itemsList){
        Gson gson = new Gson();
        String cartItemsJson = gson.toJson(itemsList , ItemsList.class);
        editor.putString(Keys.Items , cartItemsJson);
        editor.apply();
    }

    public ItemsList getCartItems(){
        Gson gson = new Gson();
        String cartItemJson = sharedPreferences.getString(Keys.Items , null);
        if (cartItemJson == null){
            return new ItemsList();
        }
        return gson.fromJson(cartItemJson , ItemsList.class);
    }

    public void clearCartItems(){
        editor.remove(Keys.Items);
        editor.apply();
    }

    public void clearAddresses(){
        editor.remove(Keys.ADDRESSES);
        editor.apply();
    }

    public void clearUser(){
        editor.remove(Keys.USER);
        editor.apply();
        clearAddresses();
    }

    public void putAddresses(AddressList addressList){
        Gson gson = new Gson();
        String addressesJson = gson.toJson(addressList , AddressList.class);
        editor.putString(Keys.ADDRESSES , addressesJson);
        editor.apply();
    }

    public AddressList getAddresses(){
        Gson gson = new Gson();
        String addressesJson = sharedPreferences.getString(Keys.ADDRESSES , null);
        if (addressesJson == null){
            return new AddressList();
        }
        return gson.fromJson(addressesJson , AddressList.class);
    }
}
