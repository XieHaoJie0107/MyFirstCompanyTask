package xhj.zime.com.mymaptest.TaskList;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import xhj.zime.com.mymaptest.R;

public class TaskFlawObjectFragment2 extends Fragment implements BaseSpinnerAdapter.OnItemClickListener{
    SpinnerChooseAdapter adapter1,adapter2;
    List<String> list1 = new ArrayList<>();
    List<String> list2 = new ArrayList<>();
    TextView mTextView1,mTextView2;
    SpinnerUtils mSpinnerUtils1,mSpinnerUtils2;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flaw_object2,container,false);
        initData();
        adapter1 = new SpinnerChooseAdapter(getContext(),list1,this);
        adapter2 = new SpinnerChooseAdapter(getContext(),list2,this);
        mTextView1 = (TextView)view.findViewById(R.id.flaw_level);
        mTextView2 = (TextView)view.findViewById(R.id.flaw_leixing);
        mSpinnerUtils1 = new SpinnerUtils(getContext(),mTextView1,adapter1);
        mSpinnerUtils2 = new SpinnerUtils(getContext(),mTextView2,adapter2);
        mSpinnerUtils1.init();
        mSpinnerUtils2.init();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initData() {
        list1.add("一般");
        list1.add("紧急");
        list1.add("特急");
        list1.add("严重");
        list2.add("隧道渗水问题模板");
        list2.add("隧道通风问题模板");
        list2.add("隧道照明问题模板");
    }

    @Override
    public void onItemClick(View view, int position) {
        if (list1.contains(list1.get(position))) {
            Toast.makeText(getActivity(), "点击" + list1.get(position), Toast.LENGTH_SHORT).show();
            mTextView1.setText(list1.get(position));
        }else {
            Toast.makeText(getActivity(), "点击" + list2.get(position), Toast.LENGTH_SHORT).show();
            mTextView2.setText(list2.get(position));
        }

        if (mSpinnerUtils1 != null) {
            mSpinnerUtils1.closeSpinner();
        }
        if (mSpinnerUtils2 != null) {
            mSpinnerUtils2.closeSpinner();
        }

    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}
