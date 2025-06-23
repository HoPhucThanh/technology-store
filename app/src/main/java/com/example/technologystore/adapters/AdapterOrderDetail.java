package com.example.technologystore.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.technologystore.R;
import com.example.technologystore.models.OrderDetail;

import java.text.DecimalFormat;
import java.util.List;

public class AdapterOrderDetail extends RecyclerView.Adapter<AdapterOrderDetail.ViewHolder> {

    private Context context;
    private List<OrderDetail> detailList;

    public AdapterOrderDetail(Context context, List<OrderDetail> detailList) {
        this.context = context;
        this.detailList = detailList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderDetail detail = detailList.get(position);

        holder.txtName.setText(detail.getName());
        holder.txtQuantity.setText("Số lượng: " + detail.getQuantity());

        DecimalFormat formatter = new DecimalFormat("#,### đ");
        holder.txtPrice.setText("Giá: " + formatter.format(detail.getPrice()));

        Glide.with(context)
                .load(detail.getImage_url())
                .placeholder(R.drawable.phone_laptop)  // ảnh mặc định nếu load lỗi
                .into(holder.imgProduct);
    }

    @Override
    public int getItemCount() {
        return detailList != null ? detailList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView txtName, txtQuantity, txtPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtName = itemView.findViewById(R.id.txtProductName);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            txtPrice = itemView.findViewById(R.id.txtPrice);
        }
    }
}
