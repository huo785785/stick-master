package com.hth.stick.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.hth.stick.R;
import com.hth.stick.widget.NoScrollListView;

import java.util.ArrayList;
import java.util.List;

public class StickFragment extends Fragment
{
    private String fragTag ;
    private List<String> mDatas = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            fragTag = getArguments().getString("key");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.list, container, false);
        NoScrollListView listView= (NoScrollListView) view.findViewById(R.id.list);

        for (int i = 0; i < 50; i++)
        {
            mDatas.add(fragTag + " -> " + i);
        }
        listView.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_expandable_list_item_1,mDatas));
        return view;

    }
    public static StickFragment newInstance(String title)
    {
        StickFragment tabFragment = new StickFragment();
        Bundle bundle = new Bundle();
        bundle.putString("key", title);
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden)
            ((MainActivity)getActivity()).showRemenber(fragTag);
    }
}
