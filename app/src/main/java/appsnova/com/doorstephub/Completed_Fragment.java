package appsnova.com.doorstephub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import appsnova.com.doorstephub.adapters.vendor.Completed_RecyclerView_Adapter;
import appsnova.com.doorstephub.models.vendor.MyLeadsPojo;


public class Completed_Fragment extends Fragment {
    List<MyLeadsPojo> mycompleted_pojolist = new ArrayList<>();
    Completed_RecyclerView_Adapter completed_recyclerView_adapter;
    MyLeadsPojo completed_leadpojo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_completed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView completed_recyclerview = view.findViewById(R.id.completed_recyclerview);
        completed_recyclerView_adapter =new Completed_RecyclerView_Adapter(getContext(),mycompleted_pojolist);
        completed_leadpojo = new MyLeadsPojo("Sai","hyderabad","Description about sai");
        mycompleted_pojolist.add(completed_leadpojo);
        completed_leadpojo = new MyLeadsPojo("sree","bangalore","Description about Sree");
        mycompleted_pojolist.add(completed_leadpojo);
        completed_leadpojo = new MyLeadsPojo("Rao","chennai","Description about Rao");
        mycompleted_pojolist.add(completed_leadpojo);
        completed_leadpojo = new MyLeadsPojo("Raj","mumbai","Description about Raj");
        mycompleted_pojolist.add(completed_leadpojo);
        completed_leadpojo = new MyLeadsPojo("Ram","vizag","Description about Ram");
        mycompleted_pojolist.add(completed_leadpojo);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        completed_recyclerview.setLayoutManager(linearLayoutManager);
        completed_recyclerview.setAdapter(completed_recyclerView_adapter);

    }
}
