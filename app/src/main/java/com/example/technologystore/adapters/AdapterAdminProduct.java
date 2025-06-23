package com.example.technologystore.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.technologystore.R;
import com.example.technologystore.models.Product;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.List;

public class AdapterAdminProduct extends RecyclerView.Adapter<AdapterAdminProduct.ViewHolder> {

    private Context context;
    private List<Product> productList;

    public AdapterAdminProduct(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public AdapterAdminProduct.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAdminProduct.ViewHolder holder, int position) {
        Product p = productList.get(position);

        holder.txtName.setText(p.getName());
        holder.txtBrand.setText("Hãng: " + p.getBrand());
        holder.txtQuantity.setText("SL: " + p.getQuantity());

        DecimalFormat format = new DecimalFormat("#,### đ");
        holder.txtPrice.setText("Giá: " + format.format(p.getPrice()));

        Glide.with(context).load(p.getImage_url()).into(holder.imgProduct);

        // ✅ Xóa sản phẩm
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xóa sản phẩm")
                    .setMessage("Bạn chắc chắn muốn xóa sản phẩm này?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        DatabaseReference ref = FirebaseDatabase.getInstance()
                                .getReference("products")
                                .child(p.getProduct_id());

                        ref.removeValue().addOnSuccessListener(aVoid -> {
                            productList.remove(position);
                            notifyItemRemoved(position);
                            Toast.makeText(context, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
                        });
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        // ✅ Sửa sản phẩm
        holder.btnEdit.setOnClickListener(v -> showEditDialog(p, position));
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView txtName, txtBrand, txtPrice, txtQuantity;
        ImageButton btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtName = itemView.findViewById(R.id.txtProductName);
            txtBrand = itemView.findViewById(R.id.txtBrand);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            btnEdit = itemView.findViewById(R.id.btnEditProduct);
            btnDelete = itemView.findViewById(R.id.btnDeleteProduct);
        }
    }

    // ✅ Hàm hiển thị và xử lý dialog sửa sản phẩm
    private void showEditDialog(Product product, int position) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_product, null);

        EditText edtName = dialogView.findViewById(R.id.edtName);
        EditText edtPrice = dialogView.findViewById(R.id.edtPrice);
        EditText edtQuantity = dialogView.findViewById(R.id.edtQuantity);
        EditText edtBrand = dialogView.findViewById(R.id.edtBrand);
        EditText edtDescription = dialogView.findViewById(R.id.edtDescription);

        // Gán dữ liệu hiện tại vào dialog
        edtName.setText(product.getName());
        edtPrice.setText(String.valueOf(product.getPrice()));
        edtQuantity.setText(String.valueOf(product.getQuantity()));
        edtBrand.setText(product.getBrand());
        edtDescription.setText(product.getDescription());

        new AlertDialog.Builder(context)
                .setTitle("Chỉnh sửa sản phẩm")
                .setView(dialogView)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String name = edtName.getText().toString().trim();
                    long price = Long.parseLong(edtPrice.getText().toString().trim());
                    int quantity = Integer.parseInt(edtQuantity.getText().toString().trim());
                    String brand = edtBrand.getText().toString().trim();
                    String desc = edtDescription.getText().toString().trim();

                    // Cập nhật Firebase
                    DatabaseReference ref = FirebaseDatabase.getInstance()
                            .getReference("products")
                            .child(product.getProduct_id());

                    ref.child("name").setValue(name);
                    ref.child("price").setValue(price);
                    ref.child("quantity").setValue(quantity);
                    ref.child("brand").setValue(brand);
                    ref.child("description").setValue(desc);

                    // Cập nhật local list
                    product.setName(name);
                    product.setPrice(price);
                    product.setQuantity(quantity);
                    product.setBrand(brand);
                    product.setDescription(desc);
                    notifyItemChanged(position);

                    Toast.makeText(context, "Đã cập nhật sản phẩm", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
