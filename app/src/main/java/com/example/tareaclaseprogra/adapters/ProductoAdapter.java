package com.example.tareaclaseprogra.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tareaclaseprogra.R;
import com.example.tareaclaseprogra.models.Producto;

import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private List<Producto> productos;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Producto producto);
    }

    public ProductoAdapter(List<Producto> productos, OnItemClickListener listener) {
        this.productos = productos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = productos.get(position);
        holder.bind(producto, listener);
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
        notifyDataSetChanged();
    }

    static class ProductoViewHolder extends RecyclerView.ViewHolder {
        private TextView textNombre;
        private TextView textEstado;
        private TextView textFecha;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            textNombre = itemView.findViewById(R.id.text_nombre);
            textEstado = itemView.findViewById(R.id.text_estado);
            textFecha = itemView.findViewById(R.id.text_fecha);
        }

        public void bind(final Producto producto, final OnItemClickListener listener) {
            textNombre.setText(producto.getNombre());
            textEstado.setText(producto.getEstado());
            textFecha.setText(producto.getFecha());
            itemView.setOnClickListener(v -> listener.onItemClick(producto));
        }
    }
}

