package com.bvcoe.bvpconnect.ui.attendance;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bvcoe.bvpconnect.DatabaseHelper;
import com.bvcoe.bvpconnect.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AttendanceFragment extends Fragment {
    BottomNavigationView bottomNavigationView;
    DatabaseHelper mydb;
    TextView percentage, attendedText, missedText;
    ImageButton add, minus;
    int attend,missed;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance,null);

        if(getActivity()!=null)
            bottomNavigationView = getActivity().findViewById(R.id.bottomNav);
        percentage = view.findViewById(R.id.textView7);
        attendedText = view.findViewById(R.id.qtyTextview);
        missedText = view.findViewById(R.id.textView8);
        add = view.findViewById(R.id.increase);
        minus = view.findViewById(R.id.decrease);
        mydb= new DatabaseHelper(getContext());

        setText(0);

        /*boolean res= mydb.insetData("AM3","0","0");
        if (res==true)
            Toast.makeText(getContext(), "Data added successfully", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getContext(), "Data not added", Toast.LENGTH_SHORT).show();
        */
        attend = Integer.parseInt(attendedText.getText().toString());
        missed = Integer.parseInt(missedText.getText().toString());

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                attend++;
                boolean res = mydb.updateData("AM3",Integer.toString(attend),Integer.toString(missed));
                if (res==true) {
                    Toast.makeText(getContext(), "Data updated successfully", Toast.LENGTH_SHORT).show();
                    attendedText.setText(Integer.toString(attend));
                }
                else
                    Toast.makeText(getContext(), "Data updation unsuccessfull", Toast.LENGTH_SHORT).show();
                setText(1);
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                missed++;
                boolean res = mydb.updateData("AM3",attendedText.getText().toString(),Integer.toString(missed));
                if (res==true) {
                    Toast.makeText(getContext(), "Data updated successfully", Toast.LENGTH_SHORT).show();
                    missedText.setText(Integer.toString(missed));
                }
                else
                    Toast.makeText(getContext(), "Data updation unsuccessfull", Toast.LENGTH_SHORT).show();
                setText(1);
            }
        });


        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        bottomNavigationView.getMenu().findItem(R.id.nav_attendance).setChecked(true);
    }

    public void setText(int a)
    {
        Cursor res = mydb.viewAllData();
        if (res.getCount()==0){
            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            int total = Integer.parseInt(res.getString(2))+Integer.parseInt(res.getString(3));
            buffer.append(res.getString(2) +"/"+ Integer.toString(total));
            if (a==0){
                attendedText.setText(res.getString(2));
                missedText.setText(res.getString(3));
            }
        }
        StringBuffer text = new StringBuffer();
        text.append("Attendance:\n");
        text.append(buffer);
        percentage.setText(text);
    }
}