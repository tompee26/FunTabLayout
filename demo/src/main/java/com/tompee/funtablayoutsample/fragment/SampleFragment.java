package com.tompee.funtablayoutsample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tompee.funtablayoutsample.R;

public class SampleFragment extends Fragment {
    private static final String TAG_NUMBER = "number";

    public static SampleFragment newInstance(int number) {
        SampleFragment fragment = new SampleFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TAG_NUMBER, number);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sample, container, false);
        TextView textView = (TextView) view.findViewById(R.id.tv_number);
        textView.setText("This is \nFragment " + String.valueOf(getArguments().getInt(TAG_NUMBER)));
        return view;
    }
}
