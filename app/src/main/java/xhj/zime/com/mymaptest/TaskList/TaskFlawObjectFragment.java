package xhj.zime.com.mymaptest.TaskList;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import xhj.zime.com.mymaptest.R;
import xhj.zime.com.mymaptest.SUser.TaskPointStatusString;
import xhj.zime.com.mymaptest.SqliteDatabaseCollector.SQLdm;

public class TaskFlawObjectFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flaw_object, container, false);
        EditText flawJiLu = (EditText) view.findViewById(R.id.flaw_jilu);
        EditText flawLevel = (EditText) view.findViewById(R.id.flaw_level);
        EditText flawJiLuTime = (EditText) view.findViewById(R.id.flaw_jilu_time);
//        SQLiteDatabase db = new SQLdm().openDatabase(getActivity());
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int userId = preferences.getInt("userId",-1);
//        Cursor cursor = db.rawQuery("select hasflaw from taskpoint where user_id = ? ", new String[]{userId+""});

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}



