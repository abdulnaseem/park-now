package com.example.parknow;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class info extends Fragment {


    public info() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_info, container, false);

        final Button reserve = (Button) view.findViewById(R.id.reserve);
        reserve.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                reservation();
            }
        });
        return view;
    }
    private void reservation(){
        Intent res = new Intent(getActivity().getApplication(), Booking.class);
        startActivity(res);
    }


}
