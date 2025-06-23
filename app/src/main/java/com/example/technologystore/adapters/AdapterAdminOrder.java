package com.example.technologystore.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;

import com.bumptech.glide.Glide;
import com.example.technologystore.OrderDetailActivity;
import com.example.technologystore.R;
import com.example.technologystore.models.Order;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.List;

public class AdapterAdminOrder extends RecyclerView.Adapter<AdapterAdminOrder.ViewHolder> {

    private Context context;
    private List<Order> orderList;

    public AdapterAdminOrder(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_manage_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.txtOrderId.setText("Mã đơn: " + order.getOrder_id());
        holder.txtUserId.setText("Người đặt: " + order.getUser_id());
        holder.txtDate.setText("Ngày tạo: " + order.getCreated_at());
        holder.txtMethod.setText("Phương thức: " + order.getPayment_method());

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

        // Gán dữ liệu spinner trạng thái
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.order_status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinnerStatus.setAdapter(adapter);
        holder.spinnerStatus.setSelection(getStatusIndex(order.getStatus()));

        holder.btnViewDetail.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderDetailActivity.class);
            intent.putExtra("order_id", order.getOrder_id());
            context.startActivity(intent);
        });


        // Cập nhật khi chọn trạng thái mới
        holder.spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean first = true;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (first) {
                    first = false;
                    return;
                }

                String newStatus = parent.getItemAtPosition(pos).toString();

                // Cập nhật Firebase
                DatabaseReference ref = FirebaseDatabase.getInstance()
                        .getReference("orders")
                        .child(order.getOrder_id());

                ref.child("status").setValue(newStatus);

                Toast.makeText(context, "Trạng thái đơn hàng đã cập nhật: " + newStatus, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private int getStatusIndex(String status) {
        String[] statusArray = {"chờ xác nhận", "đã xác nhận", "đang giao", "đã giao", "đã hủy"};
        for (int i = 0; i < statusArray.length; i++) {
            if (statusArray[i].equalsIgnoreCase(status)) return i;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return orderList != null ? orderList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderId, txtUserId, txtTotal, txtMethod, txtDate;
        Spinner spinnerStatus;
        ImageView imgQr;
        Button btnViewDetail; // thêm nút

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            txtUserId = itemView.findViewById(R.id.txtUserId);
            txtTotal = itemView.findViewById(R.id.txtTotalAmount);
            txtMethod = itemView.findViewById(R.id.txtPaymentMethod);
            txtDate = itemView.findViewById(R.id.txtCreatedAt);
            spinnerStatus = itemView.findViewById(R.id.spinnerStatus);
            imgQr = itemView.findViewById(R.id.imgQrCode);
            btnViewDetail = itemView.findViewById(R.id.btnViewDetail); // gán
        }
    }
}
