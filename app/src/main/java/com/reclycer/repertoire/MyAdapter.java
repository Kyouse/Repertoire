package com.reclycer.repertoire;
/**
 * Created by kyouse on 14/11/16.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<Contact> contact = new ArrayList<>();
    private DatabaseManager databaseManager;

    public void refreshContact(Context context){
        databaseManager = new DatabaseManager(context);
        contact = databaseManager.readContact();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(contact != null)
         return contact.size();
        else
            return 0;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_cell, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Contact fouet = contact.get(position);
        holder.display(fouet);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name) TextView name;
        @BindView(R.id.description) TextView description;
        @BindView(R.id.imageView) ImageView imageView;

        private Contact currentContact;

        public MyViewHolder(final View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent detail_intent = new Intent(itemView.getContext(), DetailContactActivity.class);
                    detail_intent.putExtra("ContactID", currentContact.getIdContact());
                    itemView.getContext().startActivity(detail_intent);
                }
            });
        }

        public void display(Contact personne) {
            currentContact = personne;
            name.setText(currentContact.getNom()+ ' ' + currentContact.getPrenom());
            description.setText(currentContact.getNumero());
            if(currentContact.getPhotoPath() != null) {
                File file = new File(currentContact.getPhotoPath());
                if(file.exists())
                    imageView.setImageURI(Uri.fromFile(new File(currentContact.getPhotoPath())));
            }
        }
    }
}