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

import appsnova.com.doorstephub.adapters.vendor.FollowUp_RecyclerView_Adapter;
import appsnova.com.doorstephub.models.vendor.MyLeadsPojo;

public class FollowUp_Fragment extends Fragment {
List<MyLeadsPojo> myaccepeted_pojolist = new ArrayList<>();
FollowUp_RecyclerView_Adapter followUp_recyclerView_adapter;
MyLeadsPojo accepted_leadpojo;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Intent intent = getActivity().getIntent();
//        intent.getBundleExtra("bundle");
//        name =getArguments().getString("answered_name");
//        city = getArguments().getString("answered_city");
//        description =getArguments().getString("answered_description");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_followup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView accepeted_recyclerview = view.findViewById(R.id.accepeted_recycler_view);
        followUp_recyclerView_adapter =new FollowUp_RecyclerView_Adapter(getContext(),myaccepeted_pojolist);

        accepted_leadpojo = new MyLeadsPojo("sai","hyderabad","Desciption about sai");
        myaccepeted_pojolist.add(accepted_leadpojo);
        accepted_leadpojo = new MyLeadsPojo("sree","bangalore","Description about sree");
        myaccepeted_pojolist.add(accepted_leadpojo);
        accepted_leadpojo = new MyLeadsPojo("raj","chennai","Description about raj");
        myaccepeted_pojolist.add(accepted_leadpojo);
        accepted_leadpojo = new MyLeadsPojo("rao","mumbai","Description about rao");
        myaccepeted_pojolist.add(accepted_leadpojo);
        accepted_leadpojo = new MyLeadsPojo("ram","vizag","Description about ram");
        myaccepeted_pojolist.add(accepted_leadpojo);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        accepeted_recyclerview.setLayoutManager(linearLayoutManager);
        accepeted_recyclerview.setAdapter(followUp_recyclerView_adapter);

    }
}
