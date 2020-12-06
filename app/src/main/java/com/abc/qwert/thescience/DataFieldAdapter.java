package com.abc.qwert.thescience;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.List;

public abstract class DataFieldAdapter extends RecyclerSwipeAdapter<DataFieldViewHolder> {
    List<DataFieldData> mDataFieldList;


    DataFieldAdapter(List <DataFieldData> mDataFieldList){
        this.mDataFieldList = mDataFieldList;
/*
        this.inputs = new String[mDataFieldList.size()];
*/

    }

    @NonNull
    @Override
    public DataFieldViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_field, parent, false);

        //spinner = mView.findViewById(R.id.namings);

        //setupSpinner();

        return new DataFieldViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull DataFieldViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.mEditText.setHint(mDataFieldList.get(position).getHint());
        holder.mImage.setText(mDataFieldList.get(position).getImg());
        holder.mNaming.setText(mDataFieldList.get(position).getNaming());

        holder.dataField.setShowMode(SwipeLayout.ShowMode.PullOut);
        holder.dataField.addDrag(SwipeLayout.DragEdge.Left, holder.dataField.findViewById(R.id.background));
        holder.dataField.addDrag(SwipeLayout.DragEdge.Right, holder.dataField.findViewById(R.id.namings));
        holder.dataField.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {

            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onClose(SwipeLayout layout) {

            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

            }
        });
        holder.mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(holder.mEditText.getText().toString().equals("")){
                    mDataFieldList.get(position).setText("");
                } else {
                    mDataFieldList.get(position).setText(holder.mEditText.getText().toString());
                }

            }

        });

        mItemManger.bindView(holder.itemView, position);

    }

    @Override
    public int getItemCount() {
        return mDataFieldList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.dataField;
    }

}


class DataFieldData {
    private String hint;
    private String img;
    private String naming;

    private String text = "";

    DataFieldData(String hint, String img, String naming){
        this.hint = hint;
        this.img = img;
        this.naming = naming;
    }

    String getHint() {
        return hint;
    }

    String getImg() {
        return img;
    }

    String getNaming(){
        return naming;
    }

    String getText(){
        return text;
    }

    void setText(String text){
        this.text = text;
    }

}

class DataFieldViewHolder extends RecyclerView.ViewHolder {
    SwipeLayout dataField;

    EditText mEditText;
    TextView mImage;
    TextView mNaming;

    DataFieldViewHolder(View itemView){
        super(itemView);

        dataField = itemView.findViewById(R.id.dataField);

        mEditText = itemView.findViewById(R.id.editText);
        mImage = itemView.findViewById(R.id.imgHint);
        mNaming = itemView.findViewById(R.id.naming);



    }



}


