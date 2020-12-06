package com.abc.qwert.thescience;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class PhysTab1 extends Fragment {

    private PhysValues values;

    int position;
    List<DataFieldData> mDataFieldList;
    DataFieldAdapter adapter;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.phys_tab_1, container, false);

        if(getResources().getConfiguration().locale.getLanguage().equals("ru")){
            values = new PhysValuesRU();
        } else {
            values = new PhysValuesEN();
        }

        recyclerView = view.findViewById(R.id.phys_tab_recycle_view);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.setHasFixedSize(true);

        //recyclerView.setPadding(0, 0, 0, (int) getResources().getDimension(R.dimen.adHeight));

        position = PhysTabActivity.position;

        mDataFieldList = new ArrayList<>();

        return view;


    }

    void setupResyclerView(){
        String[] hints = values.getHints(position);
        String[] images = values.getHintImage(position);
        String[] measure = values.getMeasure(position);

        int N = values.getFieldsLength(position);

        for(int i = 0; i<N; i++) {
            mDataFieldList.add(new DataFieldData(hints[i], images[i], measure[i]));
        }
        setAdapter();


        recyclerView.setItemViewCacheSize(mDataFieldList.size());
        recyclerView.setHasFixedSize(true);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        recyclerView.animate().alpha(1f).setDuration(200).start();

    }

    void setAdapter(){
        adapter = new DataFieldAdapter(mDataFieldList) {
            @NonNull
            @Override
            public DataFieldViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return super.onCreateViewHolder(parent, viewType);
            }
        };

        this.recyclerView.setAdapter(adapter);
    }

}
