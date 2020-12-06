package com.abc.qwert.thescience;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static androidx.core.app.ActivityOptionsCompat.makeSceneTransitionAnimation;

public abstract class RVAdapter extends RecyclerView.Adapter<FormulaViewHolder> {

    private List<FormulaCardData> mFormulaList;
    private List<FormulaCardData> itemsCopy;
    private Activity physSelectActivity;
    private String key1;
    private TextView search_no_results;
    private boolean fullLength;

    RVAdapter(Activity physSelectActivity, List<FormulaCardData> mFormulaList, String key1, TextView search_no_results, boolean fullLength) {
        this.mFormulaList = mFormulaList;
        itemsCopy = new ArrayList<>();
        itemsCopy.clear();
        itemsCopy.addAll(mFormulaList);
        this.physSelectActivity = physSelectActivity;
        this.key1 = key1;
        this.search_no_results = search_no_results;
        this.fullLength = fullLength;

    }

    @NonNull
    public FormulaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView;
        if(fullLength){
            mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.formula_card_ml, parent, false);
        } else {
            mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.formula_card_sl, parent, false);
        }

        return new FormulaViewHolder(mView);

    }

    @Override
    public void onBindViewHolder(@NonNull final FormulaViewHolder holder, final int position) {
        holder.mFormulaImage.setImageResource(mFormulaList.get(position).getFormulaImage());
        holder.mFormulaName.setText(mFormulaList.get(position).getFormulaName());
        holder.mFormulaGroup.setText(mFormulaList.get(position).getFormulaGroup());

        holder.getCardView().setOnClickListener(view -> {
            Intent intent = new Intent(physSelectActivity, PhysTabActivity.class);
            intent.putExtra("title", mFormulaList.get(holder.getAdapterPosition()).getFormulaName());
            intent.putExtra("position", mFormulaList.get(holder.getAdapterPosition()).getPosition());//mFormulaList.get(holder.getAdapterPosition()).getPosition() //holder.getAdapterPosition()

            //Pair<View, String> pair1 = Pair.create(holder.getCardView(), key1);

            Bundle bundle = makeSceneTransitionAnimation(physSelectActivity, holder.getCardView(), key1).toBundle();//, pair2

            physSelectActivity.startActivity(intent, bundle);
        });

    }

    @Override
    public int getItemCount() {
        return mFormulaList.size();
    }

    void filter(String text) {
        mFormulaList.clear();
        if(text.isEmpty()){
            mFormulaList.addAll(itemsCopy);
        } else{
            text = text.toLowerCase();
            for(FormulaCardData item: itemsCopy){
                if(item.getFormulaName().toLowerCase().contains(text) || item.getFormulaGroup().toLowerCase().contains(text)){
                    mFormulaList.add(item);
                }
            }
        }

        search_no_results.setVisibility(mFormulaList.isEmpty()?View.VISIBLE:View.GONE);

        notifyDataSetChanged();
    }

}


class FormulaCardData implements Parcelable {
    private String formulaName;
    private String formulaGroup;
    private int formulaImage;
    private int position;

    FormulaCardData(String formulaName, String formulaGroup, int formulaImage, int position){//, int position
        this.formulaName = formulaName;
        this.formulaGroup = formulaGroup;
        this.formulaImage = formulaImage;
        this.position = position;//this.position = position;
    }

    private FormulaCardData(Parcel in) {
        formulaName = in.readString();
        formulaGroup = in.readString();
        formulaImage = in.readInt();
        position = in.readInt();
    }

    public static final Creator<FormulaCardData> CREATOR = new Creator<FormulaCardData>() {
        @Override
        public FormulaCardData createFromParcel(Parcel in) {
            return new FormulaCardData(in);
        }

        @Override
        public FormulaCardData[] newArray(int size) {
            return new FormulaCardData[size];
        }
    };

    String getFormulaName(){
        return formulaName;
    }

    String getFormulaGroup(){
        return formulaGroup;
    }

    int getFormulaImage(){
        return formulaImage;
    }

    int getPosition(){
        return position;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(formulaName);
        parcel.writeString(formulaGroup);
        parcel.writeInt(formulaImage);
        parcel.writeInt(position);
    }
}

class FormulaViewHolder extends RecyclerView.ViewHolder {

    ImageView mFormulaImage;
    TextView mFormulaName;
    TextView mFormulaGroup;
    private CardView cardView;

    FormulaViewHolder(View itemView) {
        super(itemView);

        cardView = itemView.findViewById(R.id.recycle_cardview);

        mFormulaImage = itemView.findViewById(R.id.formula_image);
        mFormulaName = itemView.findViewById(R.id.formula_name);
        mFormulaGroup = itemView.findViewById(R.id.formula_group);

    }

    CardView getCardView() {
        return cardView;
    }
}

