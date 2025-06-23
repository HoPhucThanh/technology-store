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
import com.example.technologystore.models.Order;

import java.text.DecimalFormat;
import java.util.List;

public class AdapterOrder extends RecyclerView.Adapter<AdapterOrder.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;

    public AdapterOrder(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.txtOrderId.setText("Mã đơn: " + order.getOrder_id());
        holder.txtStatus.setText("Trạng thái: " + order.getStatus());
        holder.txtMethod.setText("Phương thức: " + order.getPayment_method());
        holder.txtDate.setText("Ngày tạo: " + order.getCreated_at());

        DecimalFormat formatter = new DecimalFormat("#,### đ");
        holder.txtTotal.setText("Tổng tiền: " + formatter.format(order.getTotal_amount()));

        // ✅ Luôn hiển thị ảnh QR nếu đơn thanh toán bằng QR
        if ("qr".equalsIgnoreCase(order.getPayment_method())) {
            Glide.with(context)
                    .load("https://png.pngtree.com/png-clipart/20190924/original/pngtree-qr-code-free-png-png-image_4863862.jpg")
                    .into(holder.imgQr);
        } else {
            holder.imgQr.setImageResource(0); // hoặc bạn có thể set ảnh rỗng nếu không phải QR
        }
    }


    @Override
    public int getItemCount() {
        return orderList != null ? orderList.size() : 0;
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderId, txtStatus, txtMethod, txtTotal, txtDate;
        ImageView imgQr;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtMethod = itemView.findViewById(R.id.txtMethod);
            txtTotal = itemView.findViewById(R.id.txtTotalAmount);
            txtDate = itemView.findViewById(R.id.txtCreatedAt);
            imgQr = itemView.findViewById(R.id.imgQrCode);
        }
    }
}