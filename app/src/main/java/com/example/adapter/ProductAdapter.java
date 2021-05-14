package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kiemtra.MainActivity;
import com.example.kiemtra.R;
import com.example.model.Product;

import java.util.List;

public class ProductAdapter extends BaseAdapter {
    private MainActivity context;
    private int layout;
    private List<Product> listProduct;

    public ProductAdapter(MainActivity context, int layout, List<Product> listProduct) {
        this.context = context;
        this.layout = layout;
        this.listProduct = listProduct;
    }

    @Override
    public int getCount() {
        return listProduct.size();
    }

    @Override
    public Object getItem(int position) {
        return listProduct.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            viewHolder.productID = convertView.findViewById(R.id.txtProductID);
            viewHolder.productName = convertView.findViewById(R.id.txtProductName);
            viewHolder.productPrice = convertView.findViewById(R.id.txtGiaBan);
            viewHolder.productBrand = convertView.findViewById(R.id.txtBrand);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        final Product t = listProduct.get(position);
        viewHolder.productID.setText("Mã: " + t.getProductID());
        viewHolder.productName.setText("Tên sản phẩm: " + t.getProductName());
        viewHolder.productBrand.setText("Hãng: " + t.getProductBrand());
        viewHolder.productPrice.setText("Giá: " + t.getProductPrice() + " VNĐ");

        return convertView;
    }
    private static class ViewHolder{
        TextView productID, productName, productBrand, productPrice;
    }
}
