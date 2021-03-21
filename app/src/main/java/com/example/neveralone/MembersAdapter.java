package com.example.neveralone;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MembersViewHolder>
{

    ArrayList<CreateUser> nameList;
    MyCircleActivity myCircleActivity;
    Context context;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mReference,mJoinedRef;


    public MembersAdapter(ArrayList<CreateUser> nameList, MyCircleActivity myCircleActivity, Context context)
    {
        this.nameList = nameList;
        this.context=context;
        this.myCircleActivity = myCircleActivity;
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid()).child("CircleMembers");
        mJoinedRef = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    @Override
    public int getItemCount()
    {
        return nameList.size();
    }

    @Override
    public MembersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout,parent,false);
        MembersViewHolder membersViewHolder = new MembersViewHolder(view);
        return membersViewHolder;

    }

    @Override
    public void onBindViewHolder(MembersViewHolder holder, int position) {

        CreateUser addCircle = nameList.get(position);
        holder.name_txt.setText(addCircle.name);

        if(addCircle.issharing.equals("false"))
        {
            holder.i1.setImageResource(R.drawable.redoffline);
        }
        else if(addCircle.issharing.equals("true"))
        {
            holder.i1.setImageResource(R.drawable.green);
        }

    }

    public  class MembersViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener,
            MenuItem.OnMenuItemClickListener

    {
        TextView name_txt = itemView.findViewById(R.id.item_title);
        ImageView i1 = itemView.findViewById(R.id.item_image);



        public MembersViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnCreateContextMenuListener(this);


            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();

                myCircleActivity.openLiveActivity(position,nameList);
            });
        }



        @Override
        public boolean onMenuItemClick(MenuItem item) {

            final CreateUser addCircle = nameList.get(getAdapterPosition());

            mReference.child(addCircle.userId).removeValue()
                    .addOnCompleteListener(task -> {
                            if(task.isSuccessful())
                            {
                                mJoinedRef.child(addCircle.userId).child("CircleMembers").child(mUser.getUid()).removeValue()
                                        .addOnCompleteListener(task1 -> {
                                                if(task1.isSuccessful())
                                                {
                                                    nameList.remove(addCircle);
                                                    notifyDataSetChanged();
                                                    notifyItemRemoved(getAdapterPosition());

                                                    Toast.makeText(context,"User removed from circle.", Toast.LENGTH_SHORT).show();

                                                }
                                        });
                            }
                    });


            return false;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem myActionItem = menu.add("Remove");
            myActionItem.setOnMenuItemClickListener(this);
        }
    }


}
