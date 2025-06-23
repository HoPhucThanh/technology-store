package com.example.technologystore.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.technologystore.R;
import com.example.technologystore.models.CartItem;
import com.google.firebase.database.*;

import java.text.DecimalFormat;
import java.util.List;

public class AdapterCartItem extends RecyclerView.Adapter<AdapterCartItem.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItemList;
    private TextView txtTotal;
    private DatabaseReference cartRef;

    public AdapterCartItem(Context context, List<CartItem> cartItemList, TextView txtTotal) {
        this.context = context;
        this.cartItemList = cartItemList;
        this.txtTotal = txtTotal;

        SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String currentUserId = prefs.getString("current_user_id", "user1");

        cartRef = FirebaseDatabase.getInstance().getReference("cart").child(currentUserId);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItemList.get(position);

        holder.txtName.setText(item.getName());

        DecimalFormat formatter = new DecimalFormat("#,### đ");
        holder.txtPrice.setText(formatter.format(item.getPrice()));
        holder.txtQuantity.setText(String.valueOf(item.getQuantity()));

        Glide.with(context)
                .load(item.getImage_url())
                .placeholder(R.drawable.phone_laptop)
                .into(holder.imgProduct);

        holder.btnAdd.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            cartRef.child(item.getCartItemId()).child("quantity").setValue(item.getQuantity());
            updateTotal();
        });

        holder.btnRemove.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                cartRef.child(item.getCartItemId()).child("quantity").setValue(item.getQuantity());
                updateTotal();
            }
        });

        holder.btnDelete.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setTitle("Xoá sản phẩm")
                .setMessage("Bạn muốn xoá sản phẩm này?")
                .setPositiveButton("Xoá", (dialog, which) -> {
                    cartRef.child(item.getCartItemId()).removeValue();
                    cartItemList.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                    updateTotal();
                })
                .setNegativeButton("Huỷ", null)
                .show());
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public double calculateTotal() {
        double total = 0;
        for (CartItem item : cartItemList) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    public void updateTotal() {
        DecimalFormat formatter = new DecimalFormat("#,### đ");
        txtTotal.setText("Tổng: " + formatter.format(calculateTotal()));
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct, btnAdd, btnRemove, btnDelete;
        TextView txtName, txtPrice, txtQuantity;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgCartProduct);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnRemove = itemView.findViewById(R.id.btnRemove);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            txtName = itemView.findViewById(R.id.txtCartProductName);
            txtPrice = itemView.findViewById(R.id.txtCartProductPrice);
            txtQuantity = itemView.findViewById(R.id.txtCartQuantity);
        }
    }
}
