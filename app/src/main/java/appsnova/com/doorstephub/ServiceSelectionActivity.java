package appsnova.com.doorstephub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import appsnova.com.doorstephub.adapters.ServiceSelectionAdapter;
import appsnova.com.doorstephub.models.ServiceSelectionModel;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class ServiceSelectionActivity extends AppCompatActivity {
    List<ServiceSelectionModel> serviceSelectionModels;
    RecyclerView recyclerView;
    ServiceSelectionAdapter serviceSelectionAdapter;
    List<String> namelist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_selection);

        setTitle("ServiveSelectionActivity");

        recyclerView = findViewById(R.id.serviceselection_list);
        serviceSelectionModels = new ArrayList<>();
        serviceSelectionAdapter = new ServiceSelectionAdapter(serviceSelectionModels);


        LinearLayoutManager layoutManager = new LinearLayoutManager(ServiceSelectionActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(serviceSelectionAdapter);
        onpreparedata();

    }

    public void onpreparedata() {
        ServiceSelectionModel model = new ServiceSelectionModel("Networking Issues");
        serviceSelectionModels.add(model);
        model = new ServiceSelectionModel("Printer Repair");
        serviceSelectionModels.add(model);
        model = new ServiceSelectionModel("Hardware Issues");
        serviceSelectionModels.add(model);
        model = new ServiceSelectionModel("Software Installation");
        serviceSelectionModels.add(model);
        model = new ServiceSelectionModel("System Upgradation");
        serviceSelectionModels.add(model);
        model = new ServiceSelectionModel("Data Backup");
        serviceSelectionModels.add(model);
        model = new ServiceSelectionModel("System Slow");
        serviceSelectionModels.add(model);
        model = new ServiceSelectionModel("Power Problems");
        serviceSelectionModels.add(model);
        model = new ServiceSelectionModel("General Service");
        serviceSelectionModels.add(model);
        model = new ServiceSelectionModel("AMC Service");
        serviceSelectionModels.add(model);
        model = new ServiceSelectionModel("Other Troubleshooting");
        serviceSelectionModels.add(model);
        model = new ServiceSelectionModel("Sree");
        serviceSelectionModels.add(model);
        model = new ServiceSelectionModel("Divya");
        serviceSelectionModels.add(model);
        model = new ServiceSelectionModel("Teja");
        serviceSelectionModels.add(model);
        model = new ServiceSelectionModel("Neelu");
        serviceSelectionModels.add(model);
        model = new ServiceSelectionModel("Deepthi");
        serviceSelectionModels.add(model);

        serviceSelectionAdapter.notifyDataSetChanged();

    }

    public void serviceselection(View view) {
        namelist= new ArrayList<String>();
        for (ServiceSelectionModel model :serviceSelectionModels) {
            if (model.isSelected()) {
                String name = model.getName();
                namelist.add( name);
                Log.d("name","Output : " + name);
                Log.d("namelist", String.valueOf(namelist.size()));
            }

        }

    }
}
