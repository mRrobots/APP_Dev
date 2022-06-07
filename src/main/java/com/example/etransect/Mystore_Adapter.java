package com.example.etransect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Mystore_Adapter extends RecyclerView.Adapter<Mystore_Adapter.ViewHolder> {
    ArrayList<String> data;
    ArrayList<String>price_data;
    Context context;
    List<Upload> uploads;
    List<Upload> mystore = new ArrayList<>();;
    String seller;

    public Mystore_Adapter(Context context, ArrayList<String>data, ArrayList<String>price_data,List<Upload> uploads,String seller) {
        this.data=data;
        this.price_data=price_data;
        this.context=context;
       this.uploads = uploads;
       this.seller = seller;


       for(int i=0;i<uploads.size();i++){
           Upload cr = uploads.get(i);
           String seller_ = cr.getSeller();
           if(seller_.equals(Global.name)){
               mystore.add(cr);
           }
       }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View nameView=layoutInflater.inflate(R.layout.seller_items,parent,false);
        ViewHolder viewHolder = new ViewHolder(nameView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

//        if(position>2){
            Upload current = mystore.get(position);
            String seller_ = current.getSeller();
            if(seller_.equals(Global.name)){
                holder.check_name.setText(current.getmName());
                holder.check_price.setText(current.getMprice());
                Picasso.get().
                        load(current.getmImage_url()).
                        fit().
                        into(holder.thirdImage);
            }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "You have deleted this items", Toast.LENGTH_SHORT).show();


                FirebaseDatabase firebaseDatabase =FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference().child("uploads");
                String str []= new String[1] ;
                databaseReference.orderByChild("mName").equalTo(current.getmName()).addValueEventListener(new ValueEventListener() {
                    @Override

                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                            String key = dataSnapshot.getKey();
                            str[0] =  key;

                        }
                        FirebaseDatabase firebaseDatabase =FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = firebaseDatabase.getReference();
                        databaseReference.child("uploads").child(str[0]).removeValue();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return mystore.size() ;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView thirdImage;
        TextView check_name,check_price;
        Button delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            check_name=itemView.findViewById(R.id.saved_name);
            check_price=itemView.findViewById(R.id.saved_price);
            thirdImage=itemView.findViewById(R.id.imageView3);
             delete=itemView.findViewById(R.id.delete);
        }
    }
}
