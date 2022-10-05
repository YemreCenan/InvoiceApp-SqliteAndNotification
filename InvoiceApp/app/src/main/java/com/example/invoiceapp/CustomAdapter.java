package com.example.invoiceapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList invoice_id, invoice_title, invoice_price, invoice_date;

    CustomAdapter(Activity activity, Context context, ArrayList invoice_id, ArrayList invoice_title, ArrayList invoice_price,
                  ArrayList invoice_date){
        this.activity = activity;
        this.context = context;
        this.invoice_id = invoice_id;
        this.invoice_title = invoice_title;
        this.invoice_price = invoice_price;
        this.invoice_date = invoice_date;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.invoice_id_txt.setText(String.valueOf(invoice_id.get(position)));
        holder.invoice_title_txt.setText(String.valueOf(invoice_title.get(position)));
        holder.invoice_price_txt.setText(String.valueOf(invoice_price.get(position)));
        holder.invoice_date_txt.setText(String.valueOf(invoice_date.get(position)));
        //Recyclerview onClickListener
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", String.valueOf(invoice_id.get(position)));
                intent.putExtra("title", String.valueOf(invoice_title.get(position)));
                intent.putExtra("price", String.valueOf(invoice_price.get(position)));
                intent.putExtra("date", String.valueOf(invoice_date.get(position)));
                activity.startActivityForResult(intent, 1);
            }
        });


    }

    @Override
    public int getItemCount() {
        return invoice_id.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView invoice_id_txt, invoice_title_txt, invoice_price_txt, invoice_date_txt;
        LinearLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            invoice_id_txt = itemView.findViewById(R.id.invoice_id_txt);
            invoice_title_txt = itemView.findViewById(R.id.invoice_title_txt);
            invoice_price_txt = itemView.findViewById(R.id.invoice_price_txt);
            invoice_date_txt = itemView.findViewById(R.id.invoice_date_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            //Animate Recyclerview
            Animation translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            mainLayout.setAnimation(translate_anim);
        }

    }

}
