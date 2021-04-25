package com.nickmonks.contactroom.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.nickmonks.contactroom.R;
import com.nickmonks.contactroom.model.Contact;

import java.util.List;

// For the adapter of the RecyclerView, we need an adapter to adapt the layout format
// we need to extend the adapter, which passes a generic of the ViewHolder. We create that class
// inside this class.

// By extending the adapter, we can create the viewHolder, binding it.

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private onContactClickListener contactClickListener;
    private List<Contact> contactList;
    private Context context;

    public RecyclerViewAdapter(List<Contact> contactList, Context context, onContactClickListener contactClickListener) {
        this.contactClickListener = contactClickListener;
        this.contactList = contactList;
        this.context = context;
    }

    public RecyclerViewAdapter() {
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Layout inflator converts the XML into a view.
        // parent is the ViewGroup
        // inflate(layout XML, where to render, ...)

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_row, parent, false);

        // we need to return view; we use our ViewHolder class to do so, since it receives a view in the constructor super:
        return new ViewHolder(view, contactClickListener);

        // Once this is passed to the viewHolder, we can do anything!
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        // Retrieve data from one position directly, where "positon" is where the focus is
        Contact contact = contactList.get(position);

        // knowing the contact elements from the adapter, we can bind/set them view items
        holder.name.setText(contact.getName());
        holder.occupation.setText(contact.getOccupation());
    }

    @Override
    public int getItemCount() {
        // this method is implemented so the recyclerview knows exactly how much data is getting, thus
        // improving peformance of the application

        // to do so, we need to know the size of our data. In our case, it's just a LiveData container of
        // Contacts.
        return contactList.size();
    }

    // viewholder class contains all methods necessary to recycle the views!

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        onContactClickListener onContactClickListener;
        public TextView name;
        public TextView occupation;

        // item view is anything that we hold inside our contact row XML (the textViews and buttons)
        public ViewHolder(@NonNull View itemView, onContactClickListener onContactClickListener) {

            super(itemView);

            // we can acces to our widgets in the contact_row.xml here:

            name = itemView.findViewById(R.id.rowname_textview);
            occupation = itemView.findViewById(R.id.rowoccupation_textview);
            this.onContactClickListener = onContactClickListener;

            // once this is set up, we bind it automatically in the onBindViewHolder

            //...

            // in order to interact with the view Item, we implement the onclicklistener
            // inside the onClick, we call the onContactClick interface, where we can do any operation
            // and any activity that implements this onContactClickListener can be used.
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("TAG", "onClick: inside");
            // thanks to the getAdapterPosition, in the MainActivity we dont need to specify an int.
            onContactClickListener.onContactClick(getAdapterPosition());
        }
    }



    //
    public interface onContactClickListener {
        void onContactClick(int Position);
    }
}
