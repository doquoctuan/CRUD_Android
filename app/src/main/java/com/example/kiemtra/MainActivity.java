package com.example.kiemtra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.adapter.ProductAdapter;
import com.example.model.Product;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Databases databases;
    ListView lvData;
    ArrayList<Product> listProduct;
    ProductAdapter productAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PrepareDB();
        AddViews();
        GetData();
        AddEvent();
    }

    private void AddEvent() {
        lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Dialog dialogDetail = new Dialog(MainActivity.this);
                dialogDetail.setContentView(R.layout.layout_edit_delete);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialogDetail.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                final Product t = listProduct.get(position);

                final EditText editName = dialogDetail.findViewById(R.id.edtDetailName);
                final EditText editBrand = dialogDetail.findViewById(R.id.edtDetailBrand);
                final EditText editPrice = dialogDetail.findViewById(R.id.edtDetailPrice);
                Button btnOk = dialogDetail.findViewById(R.id.btnDetailOk);
                Button btnCancel = dialogDetail.findViewById(R.id.btnDetailCancel);
                Button btnDelete = dialogDetail.findViewById(R.id.btnDetailDelete);
                editName.setText(t.getProductName());
                editBrand.setText(t.getProductBrand());
                editPrice.setText(t.getProductPrice() + "");
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newName = editName.getText().toString();
                        String newBrand = editBrand.getText().toString();
                        Float newPrice = Float.parseFloat(editPrice.getText().toString());
                        databases.QueryData("UPDATE PRODUCTS SET PRODUCTNAME='"+newBrand+"', PRODUCTBRAND = '"+newBrand+"', PRODUCTPRICE="+newPrice+" WHERE ID=" + t.getProductID());
                        Toast.makeText(MainActivity.this, "Edit successlly", Toast.LENGTH_LONG).show();
                        dialogDetail.dismiss();
                        GetData();
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogDetail.dismiss();
                    }
                });
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                        dialog.setMessage("Bạn chắc chắn muốn xóa?");
                        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                databases.QueryData("DELETE FROM PRODUCTS WHERE ID = " + t.getProductID());
                                Toast.makeText(MainActivity.this, "Delete successlly!", Toast.LENGTH_SHORT).show();
                                GetData();
                                dialogDetail.dismiss();
                            }
                        });
                        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        dialog.show();
                    }
                });
                dialogDetail.show();
                dialogDetail.getWindow().setAttributes(lp);
            }
        });
    }

    private void PrepareDB(){
        // create databases
        databases = new Databases(this, "products_db.db", null, 1);
        // create table
        databases.QueryData("CREATE TABLE IF NOT EXISTS PRODUCTS(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "PRODUCTNAME VARCHAR(200), PRODUCTBRAND VARCHAR(200), PRODUCTPRICE FLOAT)");
        // insert data
//        databases.QueryData("INSERT INTO PRODUCTS VALUES(null,'Iphone X', 'Apple', 1900000)");
//        databases.QueryData("INSERT INTO PRODUCTS VALUES(null,'Iphone 11', 'Apple', 1500000)");
//        databases.QueryData("INSERT INTO PRODUCTS VALUES(null,'Iphone 12', 'Apple', 2200000)");
//        databases.QueryData("INSERT INTO PRODUCTS VALUES(null,'Iphone 12 XS Max', 'Apple', 3000000)");
    }

    private void AddViews() {
        lvData = findViewById(R.id.lvData);
        listProduct = new ArrayList<>();
        productAdapter = new ProductAdapter(this, R.layout.item_row, listProduct);
        lvData.setAdapter(productAdapter);
    }

    private void GetData() {
        String query = "SELECT * FROM PRODUCTS";
        Cursor c = databases.GetData(query);
        listProduct.clear();
        while(c.moveToNext()){
            int productID = c.getInt(0);
            String productName = c.getString(1);
            String productBrand = c.getString(2);
            Float productPrice = c.getFloat(3);
            Product product = new Product(productID, productName, productBrand, productPrice);
            listProduct.add(product);
        }
        productAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.mnAddTask){
            openDialogAddTask();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openDialogAddTask() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_add);
        dialog.setCanceledOnTouchOutside(false);
        final EditText txtName = dialog.findViewById(R.id.edtAddName);
        final EditText txtBrand = dialog.findViewById(R.id.edtAddBrand);
        final EditText txtPrice = dialog.findViewById(R.id.edtAddPrice);
        Button btnOk = dialog.findViewById(R.id.btnAddOk);
        Button btnCancel = dialog.findViewById(R.id.btnAddCancel);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtName.getText().toString() == "" || txtBrand.getText().toString() == "" || txtPrice.getText().toString() == ""){
                    Toast.makeText(MainActivity.this, "Không được để trống thông tin!", Toast.LENGTH_LONG).show();
                } else {
                    String name = txtName.getText().toString();
                    String brand = txtBrand.getText().toString();
                    Float price = Float.parseFloat(txtPrice.getText().toString());
                    databases.QueryData("INSERT INTO PRODUCTS VALUES(null, '" + name + "', '"+brand+"', "+price+")");
                    Toast.makeText(MainActivity.this, "Add Product Successlly!", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    GetData();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}