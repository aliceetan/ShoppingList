package hu.ait.android.shoppinglist.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import hu.ait.android.shoppinglist.MainActivity;
import hu.ait.android.shoppinglist.R;
import hu.ait.android.shoppinglist.Tobuy;


/**
 * Created by alicetan on 11/5/17.
 */

public class TobuyRecyclerAdapter extends RecyclerView.Adapter<TobuyRecyclerAdapter.ViewHolder> {

    private List<Tobuy> tobuyList;
    private Context context;
    private int lastPosition = -1;
    private static final int low = 10;
    private static final int mid = 20;

    public TobuyRecyclerAdapter(List<Tobuy> tobuyList, Context context) {
        this.context = context;
        this.tobuyList = tobuyList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View tobuyRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.tobuy_row, parent, false);
        return new ViewHolder(tobuyRow);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Tobuy tobuyData = tobuyList.get(position);
        holder.tvItemName.setText(tobuyData.getName());
        holder.tvAmt.setText(tobuyData.getPrice());
        holder.cbBought.setChecked(tobuyData.isBought());
        holder.tvDescription.setText(tobuyData.getDescription());
        if (tobuyData.getPrice() == "") {
            int price = Integer.parseInt(tobuyData.getPrice());
            if (price < low) {
                holder.itemView.setBackgroundColor(Color.GREEN);
            } else if (price < mid) {
                holder.itemView.setBackgroundColor(Color.YELLOW);
            } else if (price >= mid) {
                holder.itemView.setBackgroundColor(Color.RED);
            } else {
                holder.itemView.setBackgroundColor(Color.WHITE);
            }
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
        }
        int category = tobuyData.getCategory();
        if (category == 0) {
            holder.ivPic.setImageResource(R.drawable.food);
        } else if (category == 1) {
            holder.ivPic.setImageResource(R.drawable.clothing);
        } else if (category == 2) {
            holder.ivPic.setImageResource(R.drawable.electronics);
        } else if (category == 3) {
            holder.ivPic.setImageResource(R.drawable.books);
        }
        holder.cbBought.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                checkUpdate(holder.getAdapterPosition(), isChecked);
                holder.cbBought.setChecked(isChecked);
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                removeItem(holder.getAdapterPosition());
            }
        });
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) context).openEditActivity(
                        tobuyList.get(holder.getAdapterPosition()).getItemID(),
                        holder.getAdapterPosition());
            }
        });

        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return tobuyList.size();
    }


    public void addItem(Tobuy item) {
        tobuyList.add(item);
        notifyDataSetChanged();
    }

    public void updateItem(int positionToEdit, Tobuy item) {
        tobuyList.set(positionToEdit, item);
        notifyItemChanged(positionToEdit);
    }

    public void removeItem(int position) {
        ((MainActivity) context).deleteItem(tobuyList.get(position));
        tobuyList.remove(position);
        notifyItemRemoved(position);
    }

    public void checkUpdate(int position, boolean bought) {
        ((MainActivity) context).changeCheck(bought, tobuyList.get(position));
    }


    public void swapPlaces(int oldPosition, int newPosition) {
        if (oldPosition < newPosition) {
            for (int i = oldPosition; i < newPosition; i++) {
                Collections.swap(tobuyList, i, i + 1);
            }
        } else {
            for (int i = oldPosition; i > newPosition; i--) {
                Collections.swap(tobuyList, i, i - 1);
            }
        }
        notifyItemMoved(oldPosition, newPosition);
    }

    public Tobuy getItem(int i) {
        return tobuyList.get(i);
    }

    public void clearList() {
        ((MainActivity) context).deleteAll();
        tobuyList.clear();
        notifyDataSetChanged();
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivPic;
        private TextView tvItemName;
        private TextView tvAmt;
        private CheckBox cbBought;
        private Button btnDelete;
        private Button btnEdit;
        private TextView tvDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            ivPic = itemView.findViewById(R.id.ivPic);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvAmt = itemView.findViewById(R.id.tvAmt);
            cbBought = itemView.findViewById(R.id.cbBought);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }
    }

}
