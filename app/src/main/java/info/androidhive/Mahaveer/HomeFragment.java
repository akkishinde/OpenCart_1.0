package info.androidhive.Mahaveer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class HomeFragment extends Fragment {
	
	public HomeFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        int key=getArguments().getInt("key");
        Toast.makeText(getActivity(),"Key:"+key,Toast.LENGTH_SHORT).show();
        return rootView;
    }
}
