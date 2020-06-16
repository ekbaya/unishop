package consultant.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkError;
import com.android.volley.VolleyError;
import com.example.unishop.R;
import com.example.unishop.activities.ConsultantsActivity;
import com.example.unishop.adapters.CategoriesAdapter;
import com.example.unishop.adapters.ConsultantAdapter;
import com.example.unishop.api.CategoriesAPI;
import com.example.unishop.api.ConsultantsAPI;
import com.example.unishop.models.ModelCategory;
import com.example.unishop.services.CategoriesListener;
import com.example.unishop.utilities.NetworkConnection;

import org.json.JSONException;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class CategoriesFragment extends Fragment implements CategoriesListener {
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

        categoriesAPI = new CategoriesAPI(getActivity());
        categoriesAPI.setCategoriesListener(this);
        loadCategories();

        return view;
    }

    private void loadCategories() {
        if (new NetworkConnection().get().isConnected(Objects.requireNonNull(getActivity()))){
            // get all categories
            categoriesAPI.getCategories();

        }else {
            Toasty.warning(getActivity(), getText(R.string.network_text), Toasty.LENGTH_LONG).show();
        }
    }

    @Override
    public void onVolleyErrorResponse(VolleyError error) {
        if (error instanceof NetworkError){
            Toasty.error(Objects.requireNonNull(getActivity()), "Check your connection and try again", Toasty.LENGTH_LONG).show();
        }
        else Toasty.error(Objects.requireNonNull(getActivity()), error.toString(), Toasty.LENGTH_LONG).show();
    }

    @Override
    public void onJSONObjectException(JSONException e) {
        Toasty.error(Objects.requireNonNull(getActivity()), e.toString(), Toasty.LENGTH_LONG).show();
    }

    @Override
    public void onCategoriesReceived(List<ModelCategory> categoryList) {
        //initialise adapter
        categoriesAdapter = new CategoriesAdapter(getActivity(), categoryList);
        //set adapter to recyclerView
        categories_recyclerview.setAdapter(categoriesAdapter);
        //categories_dialog.setVisibility(View.GONE);
        categories_recyclerview.setVisibility(View.VISIBLE);
    }
}
