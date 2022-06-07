package com.example.etransect;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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
import java.util.Collection;
import java.util.List;

public class RecyclerAdaptor2 extends RecyclerView.Adapter<RecyclerAdaptor2.ViewHolder> implements Filterable {
    ArrayList<String> pro_namelist;
    ArrayList<String>pro_pricelist;
    ArrayList<Integer>imageslist;
    ArrayList<String>all_products;
    ArrayList<String>pro_descri;
    ArrayList<Uri>uri;
    List<Upload> uploads;
    Context context;
    public RecyclerAdaptor2(Context context,ArrayList<String>pro_namelist,ArrayList<String>pro_pricelist,ArrayList<Integer>imageslist,ArrayList<String>pro_descri,ArrayList<Uri>uri,List<Upload> uploads){
        this.context=context;
        this.pro_namelist=pro_namelist;
        this.pro_pricelist=pro_pricelist;
        this.imageslist=imageslist;
        this.pro_descri=pro_descri;
        this.all_products= new ArrayList<>(pro_namelist);
        this.uri = uri;
        this.uploads = uploads;
    }

    @NonNull
    @Override
    public RecyclerAdaptor2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View nameView=layoutInflater.inflate(R.layout.gridmyitem,parent,false);
        ViewHolder viewHolder = new ViewHolder(nameView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdaptor2.ViewHolder holder, int position) {
        String image = "1";
        int likes = 1;
        String seller = "mosismdlalose@gmail.com";
        if(position>10){
            Upload upload = uploads.get(position-11);
            holder.grid_name.setText(upload.getmName());
            holder.grid_price.setText(upload.getMprice());
            Picasso.get().
                    load(upload.getmImage_url()).
                    fit().
                    into(holder.grid_image);
            image = upload.getmImage_url();
            seller = upload.getSeller();
            likes = upload.getLikes();
        }
        else {
            holder.grid_name.setText(pro_namelist.get(position));
            holder.grid_price.setText(pro_pricelist.get(position));
            holder.grid_image.setImageResource(imageslist.get(position));
            image = imageslist.get(position).toString();
        }
        String finalImage = image;
        String finalSeller = seller;
        String finalLikes = Integer.toString(likes);
        holder.grid_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String confirmed_name_grid = pro_namelist.get(position);
                String confirmed_price_grid = pro_pricelist.get(position);
                String confirmed_descrip_grid =pro_descri.get(position);
                Integer confirmed_images_grid = imageslist.get(position);
                String image_ = finalImage;

                Intent intent = new Intent(context, Item_details.class);
                intent.putExtra("confirmed_name", confirmed_name_grid);
                intent.putExtra("confirmed_price", confirmed_price_grid);
                intent.putExtra("confirmed_descrip",confirmed_descrip_grid);
                intent.putExtra("image_",image_);
                intent.putExtra("image",confirmed_images_grid);
                intent.putExtra("seller", finalSeller);
                intent.putExtra("likes", finalLikes);

                context.startActivity(intent);
            }
        });

        holder.grid_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Click on "+pro_namelist.get(position),Toast.LENGTH_SHORT).show();
            }
        });
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Click on like, You can only like this item once!",Toast.LENGTH_SHORT).show();

                if(position>10){
                    Upload upload = uploads.get(position-11);
                    int cr_like = upload.getLikes();
                    cr_like+=1;
                    FirebaseDatabase firebaseDatabase =FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference().child("uploads");
                    int finalCr_like = cr_like;
                    String str []= new String[1] ;
                    databaseReference.orderByChild("mName").equalTo(upload.getmName()).addValueEventListener(new ValueEventListener() {
                        @Override

                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                String key = dataSnapshot.getKey();
                                str[0] =  key;
                            }
                            FirebaseDatabase firebaseDatabase =FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference = firebaseDatabase.getReference();
                            databaseReference.child("uploads").child(str[0]).child("likes").setValue(finalCr_like);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                holder.grid_image.performClick();
            }
        });
    }

    @Override
    public int getItemCount() {
        return pro_namelist.size();
    }

    @Override
    public Filter getFilter() {

        return filter;
    }
    Filter filter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<String> filteredList = new ArrayList<>();
            if(constraint.toString().isEmpty()){
                filteredList.addAll(all_products);
            }else{
                for(String products:all_products){
                    if(products.toLowerCase().contains(constraint.toString().toLowerCase().trim())){
                        filteredList.add(products);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            pro_namelist.clear();
            pro_namelist.addAll((Collection<? extends String>) results.values);
            notifyDataSetChanged();
            pro_descri.clear();
            pro_descri.addAll((Collection<? extends String>) results.values);
            notifyDataSetChanged();
//            pro_pricelist.clear();
//            pro_pricelist.addAll((Collection<? extends String>) results.values);
//            notifyDataSetChanged();
        }

    };


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView grid_image,like;
        TextView grid_name, grid_price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            grid_name =itemView.findViewById(R.id.p_name_grid);
            grid_price =itemView.findViewById(R.id.p_price_grid);
            grid_image =itemView.findViewById(R.id.imageId_grid);
            like=itemView.findViewById(R.id.like_button);
        }
    }
}
