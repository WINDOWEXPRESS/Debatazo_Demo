package com.example.debatazo.debaterecycler;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.debatazo.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class debateFragment extends Fragment {
    public debateFragment(){}
    private List<debateItem> debateList;
    private RecyclerView debateRecyclerV;
    private debateAdapte debateAd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_debate,container,false);
        debateList = generaDebates();
        debateRecyclerV = layout.findViewById(R.id.fragmentD_recyclerV);
        debateRecyclerV.setLayoutManager(new LinearLayoutManager(getActivity()));
        debateAd = new debateAdapte(debateList);
        debateRecyclerV.setAdapter(debateAd);


        return layout;
    }
    private  List<debateItem> generaDebates(){
        List<debateItem> debateItems = new ArrayList<debateItem>();
        debateItems.add(new debateItem(1,1,"https://i.imgur.com/c4ujVR1.png", new Date(),
                "juan","Tomate es mas sano que Manzana", "https://i.imgur.com/Y7rr3sW.png"
                ));
        debateItems.add(new debateItem(1,1,"https://i.imgur.com/rC1asEd.png", new Date(),
                "Ana","Rock es la musica mas mejor del mundo entero!!!", "https://i.imgur.com/Y7rr3sW.png"
        ));
        return  debateItems;
    }
}