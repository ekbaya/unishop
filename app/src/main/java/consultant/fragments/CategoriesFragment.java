package consultant.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkError;
import com.android.volley.VolleyError;
import com.example.unishop.R;
import com.example.unishop.adapters.CategoriesAdapter;
import com.example.unishop.api.CategoriesAPI;
import com.example.unishop.models.Category;
import com.example.unishop.services.CategoriesListener;
import com.google.firebase.database.DatabaseError;

import org.json.JSONException;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class CategoriesFragment extends Fragment implements CategoriesListener.LoadCategoriesListener {
    @BindView(R.id.categories_recyclerview) RecyclerView categories_recyclerview;
    //@BindView(R.id.categories_dialog) ProgressBar categories_dialog;

    private CategoriesAdapter categoriesAdapter;
    private CategoriesAPI categoriesAPI;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        ButterKnife.bind(this, view);

        //setting its properties
        categories_recyclerview.setHasFixedSize(true);
        categories_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        categoriesAPI = new CategoriesAPI();
        categoriesAPI.setLoadCategoriesListener(this);
        loadCategories();

        return view;
    }

    private void loadCategories() {
        // get all categories
        categoriesAPI.getCategories();
    }

    @Override
    public void onCategoriesReceived(List<Category> categoryList) {
        //initialise adapter
        categoriesAdapter = new CategoriesAdapter(getActivity(), categoryList);
        //set adapter to recyclerView
        categories_recyclerview.setAdapter(categoriesAdapter);
        //categories_dialog.setVisibility(View.GONE);
        categories_recyclerview.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDatabaseCancelled(DatabaseError error) {
        Toasty.error(requireActivity(),""+error.getMessage(), Toasty.LENGTH_LONG).show();
    }
}
