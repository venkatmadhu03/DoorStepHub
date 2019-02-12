package appsnova.com.doorstephub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import appsnova.com.doorstephub.adapters.ServiceSelectionAdapter;
import appsnova.com.doorstephub.models.ServiceSelectionModel;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class ServiceSelectionActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ServiceSelectionAdapter serviceSelectionAdapter;
    List<ServiceSelectionModel> serviceSelectionModels;
    List<ServiceSelectionModel> selecteditemlist;
    ActionMode mActionMode;
    boolean isMultiSelect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_selection);

        setTitle("ServiveSelectionActivity");

        recyclerView = findViewById(R.id.serviceselection_list);
        serviceSelectionModels = new ArrayList<>();
        selecteditemlist = new ArrayList<>();
        serviceSelectionAdapter = new ServiceSelectionAdapter(ServiceSelectionActivity.this, serviceSelectionModels, selecteditemlist);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(ServiceSelectionActivity.this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(serviceSelectionAdapter);
        serviceSelectionAdapter.notifyDataSetChanged();

        onpreparedata();


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (!isMultiSelect) {
                    isMultiSelect = true;
                    if (mActionMode == null) {
                        mActionMode = startActionMode(mActionModeCallback);
                    }
                }

                multi_select(position);

            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (!isMultiSelect) {
                    isMultiSelect = true;
                    if (mActionMode == null) {
                        mActionMode = startActionMode(mActionModeCallback);
                    }
                }

                multi_select(position);
            }
        }));
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

        serviceSelectionAdapter.notifyDataSetChanged();

    }


    public void serviceselection(View view) {
        /*selecteditemlist= new ArrayList<ServiceSelectionModel>();
        for (ServiceSelectionModel model :serviceSelectionModels) {
            if (model.isSelected()) {
                String name = model.getName();
                selecteditemlist.add(model);
                Log.d("name","Output : " + name);
                Log.d("namelist", String.valueOf(selecteditemlist.size()));
            }

        }*/
        startActivity(new Intent(this, ServiceScheduleActivity.class));

        }

    @Override
    protected void onPause() {
        super.onPause();
        selecteditemlist.clear();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        selecteditemlist.clear();
    }


    @Override
    protected void onStop() {
        super.onStop();
        selecteditemlist.clear();

    }

    public void multi_select(int position) {

        if (mActionMode != null) {
            if (selecteditemlist.contains(serviceSelectionModels.get(position))){
                selecteditemlist.remove(serviceSelectionModels.get(position));
            }else{
                selecteditemlist.add(serviceSelectionModels.get(position));
            }
            if (selecteditemlist.size() > 0){
                mActionMode.setTitle("Services : " + selecteditemlist.size());
            }else{
                mActionMode.setTitle("");
            }
            refreshAdapter();

        }
    }

    public void refreshAdapter() {
        serviceSelectionAdapter.selectedItemsList=selecteditemlist;
        serviceSelectionAdapter.serviceSelectionModelList = serviceSelectionModels;
        serviceSelectionAdapter.notifyDataSetChanged();
    }




    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
          /*  //    Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.send_sms_contacts_select, menu);
            context_menu = menu;*/
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

            return false; // Return false if nothing is done
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
           /* switch (item.getItemId()) {
                case R.id.action_select_contacts:

                    try {
                        convertSelectedContactsToJsonArray();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return true;

                case R.id.action_select_all_contacts_for_activities:

                    if (item.getTitle().toString().equalsIgnoreCase("Select all")){

                        selectedContactsList =  contactsListAdapter.getSelectAllItems();
                        Log.d("LeadList", "onActionItemClicked: "+selectedContactsList);

                        if (selectedContactsList.size() > 0){
                            mActionMode.setTitle("" + selectedContactsList.size());
                        }else{
                            mActionMode.setTitle("");
                        }
                        if (selectedContactsList.size() == contactsPojoList.size()){

                            if (item.getTitle().toString().equalsIgnoreCase("Select all")){
                                item.setTitle("Select None");
                            }else{
                                item.setTitle("Select all");
                            }
                        }
                        item.setTitle("Select None");

                    }else{
                        item.setTitle("Select all");
                        selectedContactsList = contactsListAdapter.deselectAll();

                        mActionMode.setTitle("");

                        if (mActionMode !=null){
                            mActionMode.finish();
                        }else{

                        }
                        isMultiSelect = false;
                        selectedContactsList = new ArrayList<ContactsPojo>();

                    }

                default:
                    return false;
            }*/
        return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            isMultiSelect = false;
            selecteditemlist = new ArrayList<ServiceSelectionModel>();
            refreshAdapter();
        }
    };

}
