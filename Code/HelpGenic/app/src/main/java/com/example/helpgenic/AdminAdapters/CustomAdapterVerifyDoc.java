package com.example.helpgenic.AdminAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.Classes.Doctor;
import com.example.helpgenic.DisplayImage;
import com.example.helpgenic.R;
import java.util.ArrayList;


public class CustomAdapterVerifyDoc extends ArrayAdapter<Doctor> {


//    ArrayList<Doctor> docList;
//    Context context;

    public CustomAdapterVerifyDoc(Context context, int resource, @NonNull ArrayList<Doctor> objects) {
        super(context, resource, objects);
//        docList=objects;
//        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        Doctor doc = getItem(position);

        if (convertView == null){

            convertView = LayoutInflater.from(getContext().getApplicationContext()).inflate(R.layout.list_cell_custom_verify_doctors_design, parent, false);
//            ViewHolder view = new ViewHolder();
//            view.reject=(Button) convertView.findViewById(R.id.reject);
//            view.verify=(Button) convertView.findViewById(R.id.accept);
//            convertView.setTag(view);
//
//            //======================= Setting click listeners for both the buttons ===================== //
//
//            view.verify.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    System.out.println("The id is " + doc.getId());
//                    dbHandler.setVerified(getContext(),doc.getId());
//                    Toast.makeText(getContext(),"Verified" + position,Toast.LENGTH_SHORT).show();
//                    docList.remove(position);
//                    notifyDataSetChanged();
//                }
//            });
//
//            view.reject.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    System.out.println("The id is " + doc.getId());
//                    dbHandler.removeDoctor(getContext(), doc.getId());
//                    docList.remove(position);
//                    notifyDataSetChanged();
//                }
//            });
//
        }


        TextView docName = convertView.findViewById(R.id.DocName);
        TextView qualificationPlusProfession = convertView.findViewById(R.id.info);

        String Name ="Dr."+ doc.getName();

        docName.setText(Name);

        String info = doc.getSpecialization();

        qualificationPlusProfession.setText(info);

        return convertView;
    }


//    public class ViewHolder{
//        Button verify;
//        Button reject;
//        Button viewDegree;
//    }

}
