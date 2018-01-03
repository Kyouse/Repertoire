package com.reclycer.repertoire;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kyouse on 03/12/17.
 */

public class DeleteAdapter extends RecyclerView.Adapter<DeleteAdapter.MyViewHolder>{

    private List<Contact> contact = new ArrayList<>();
    private ArrayList<Integer> list_idContact = new ArrayList<>();

    public void deleteContact(Context context){
        DatabaseManager databaseManager = new DatabaseManager(context);
        for(Integer i : list_idContact) {
            for(Contact c : contact){
                if(i == c.getIdContact() && c.getisPhotoFromApp()) {
                        File mydir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                        File myImage = new File(mydir, c.getPhotoPath());
                        myImage.delete();
                        Log.i("DeleteAdapter","Image : " + c.getPhotoPath());
                }
            }
            databaseManager.deleteContact(i);
        }
        databaseManager.close();
    }

    public void refreshContact(Context context){
        DatabaseManager databaseManager = new DatabaseManager(context);
        contact = databaseManager.readContact();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return contact.size();
    }

    @Override
    public DeleteAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.todelete, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DeleteAdapter.MyViewHolder holder, int position) {
        Contact fouet = contact.get(position);
        holder.display(fouet);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView description;
        private Contact currentContact;
        private CheckBox checkBox;
        private final ImageView imageView;

        public MyViewHolder(final View itemView) {
            super(itemView);

            name = ((TextView) itemView.findViewById(R.id.name_del));
            description = ((TextView) itemView.findViewById(R.id.description_del));
            checkBox = ((CheckBox) itemView.findViewById(R.id.chkSelected));
            imageView = (ImageView) itemView.findViewById(R.id.imageView_del);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(checkBox.isChecked()) {
                        checkBox.setChecked(false);
                        list_idContact.remove(Integer.valueOf(currentContact.getIdContact()));
                    }
                    else {
                        checkBox.setChecked(true);
                        list_idContact.add(currentContact.getIdContact());
                        }
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
