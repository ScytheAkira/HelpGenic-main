
package com.example.helpgenic.CommonAdapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.helpgenic.Doctor.ProfileDoc;
import com.example.helpgenic.Patient.PatientViewingDocProfile;
import com.example.helpgenic.R;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private final Map<String,List<String>> physicalSchedule;
    private final List<String> groupList;

    public ExpandableListViewAdapter(Context context, List<String> grouplist, Map<String,List<String>> physicalSchedule)
    {
        this.context=context;
        this.physicalSchedule=physicalSchedule;
        this.groupList=grouplist;
    }
    @Override
    public int getGroupCount() {
        return physicalSchedule.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return Objects.requireNonNull(physicalSchedule.get(groupList.get(i))).size();
    }

    @Override
    public Object getGroup(int i) {
        return groupList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return Objects.requireNonNull(physicalSchedule.get(groupList.get(i))).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String clinic=getGroup(i).toString();
        if(view==null)
        {
            LayoutInflater inflater = (LayoutInflater)  context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.expandable_listview_group_item_patient_views_doc_profile,null);
        }
        TextView item = view.findViewById(R.id.group_item);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(clinic);

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        String Details = getChild(i,i1).toString();
        if(view==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.expandable_listview_child_item_patient_views_doc_prof,null);
        }
        TextView item = view.findViewById(R.id.child_item);
        item.setText(Details);


        // ================================ remove service for phy schedule =============================\




//        Button button = view.findViewById(R.id.deletebutton);
//        boolean flag = PatientViewingDocProfile.flag;
//
//        if (flag!=false)
//            button.setVisibility(View.GONE);
//
//        if (flag==false)
//        {
//            button.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View view) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                    builder.setMessage("Do you want to remove?");
//                    builder.setCancelable(true);
//                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int id) {
//                            List<String> child = physicalSchedule.get(groupList.get(i));
//                            String s=child.toString();
//                            s=s+",";
//                            StringTokenizer st = new StringTokenizer(s,",");
//                            String clocation = st.nextToken();//[Not decided yet
//                            String phoneNum = st.nextToken();//[ 032
//                            clocation=clocation.substring(1);
//                            phoneNum=phoneNum.substring(1,phoneNum.length()-1);
//                            String cName=getGroup(i).toString();
//                            child.clear();
////                            dbHandler.connectToDb(context.getApplicationContext());
////                            int pid = dbHandler.getPid(cName,clocation,phoneNum);
////                            dbHandler.removePhysicalSchedule(pid);
////                            dbHandler.removeAppSchedule(pid);
//                            notifyDataSetChanged();
//                        }
//                    });
//                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.cancel();
//                        }
//                    });
//                    AlertDialog alertDialog = builder.create();
//                    alertDialog.show();
//
//                }
//            });
//        }


        // ===================================================================================
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true ;
    }
}
