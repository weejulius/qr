package demo.m.qr1688;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.util.List;

import demo.m.qr1688.model.Spu;

/**
 * Created by jyu on 15-3-9.
 */
public class PageFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    private final Gson gson = new Gson();


    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ListView listView = (ListView)inflater.inflate(R.layout.page, container, false);
        //Construct the data source
        List<Spu> spus = gson.fromJson(new InputStreamReader(getResources().openRawResource(R.raw.offers)), new TypeToken<List<Spu>>() {
        }.getType());

        SpuArrayAdapter adapter = new SpuArrayAdapter(getActivity(), spus);

        listView.setAdapter(adapter);

// Attach the adapter to a ListView
        listView.setAdapter(adapter);
        return listView;
    }
}
