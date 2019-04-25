package appsnova.com.doorstephub;

import android.os.Bundle;;
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
import appsnova.com.doorstephub.adapters.vendor.Answer_Recyclerview_Adapter;
import appsnova.com.doorstephub.models.vendor.MyLeadsPojo;


public class Answered_Fragment extends Fragment {
    List<MyLeadsPojo> myLeadsPojoList  =new ArrayList<>();
    MyLeadsPojo myLeadsPojo;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_answer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.answered_recycler_view);
        Answer_Recyclerview_Adapter answer_recyclerview_adapter = new Answer_Recyclerview_Adapter(myLeadsPojoList,getContext());
        myLeadsPojo = new MyLeadsPojo("Sai","Hyderabad","Description about sai");
        myLeadsPojoList.add(myLeadsPojo);
        myLeadsPojo = new MyLeadsPojo("Sree","Bangalore","Description about sree");
        myLeadsPojoList.add(myLeadsPojo);
        myLeadsPojo = new MyLeadsPojo("Teja","Chennai","Description about teja");
        myLeadsPojoList.add(myLeadsPojo);
        myLeadsPojo = new MyLeadsPojo("Rao","Mumbai","Description about rao");
        myLeadsPojoList.add(myLeadsPojo);
        myLeadsPojo = new MyLeadsPojo("Ram","Vizag","Description about ram");
        myLeadsPojoList.add(myLeadsPojo);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(answer_recyclerview_adapter);

    }
}
