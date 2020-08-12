package com.example.unishop.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.NetworkError;
import com.android.volley.VolleyError;
import com.example.unishop.R;
import com.example.unishop.api.CategoriesAPI;
import com.example.unishop.models.Category;
import com.example.unishop.services.CategoriesListener;
import com.example.unishop.utilities.SharedHelper;
import com.google.firebase.database.DatabaseError;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class CategoriesActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
View.OnClickListener, CategoriesListener.LoadCategoriesListener {
    //views
    @BindView(R.id.p_nameEt) EditText p_nameEt;
    @BindView(R.id.categoryRG) RadioGroup categoryRG;
    @BindView(R.id.nextBtn) Button nextBtn;

    private CategoriesAPI categoriesAPI;
    private List<String> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ButterKnife.bind(this);

        categoryRG.removeAllViewsInLayout();
        categoryRG.setOnCheckedChangeListener(this);
        nextBtn.setOnClickListener(this);

        categoriesAPI = new CategoriesAPI();
        categoriesAPI.setLoadCategoriesListener(this);
        categories = new ArrayList<>();
        loadCategories();
    }

    private void loadCategories() {
        categoriesAPI.getCategories();
    }

    private boolean validate() {
        if (TextUtils.isEmpty(p_nameEt.getText().toString())){
            Toasty.warning(this, "Invalid product name", Toasty.LENGTH_LONG).show();
            return false;
        }
        else if (categoryRG.getCheckedRadioButtonId() == -1){
            Toasty.warning(this, "Invalid product category", Toasty.LENGTH_LONG).show();
            return false;

        }
        else {
            SharedHelper.putKey(CategoriesActivity.this, "p_name", p_nameEt.getText().toString());
            return true;
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CategoriesActivity.this, AdminHomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case 0:
                SharedHelper.putKey(CategoriesActivity.this,"category", categories.get(0));
                break;
            case 1:
                SharedHelper.putKey(CategoriesActivity.this,"category", categories.get(1));
                break;
            case 2:
                SharedHelper.putKey(CategoriesActivity.this,"category", categories.get(2));
                break;
            case 3:
                SharedHelper.putKey(CategoriesActivity.this,"category", categories.get(3));
                break;
            case 4:
                SharedHelper.putKey(CategoriesActivity.this,"category", categories.get(4));
                break;
            case 5:
                SharedHelper.putKey(CategoriesActivity.this,"category", categories.get(5));
                break;
            case 6:
                SharedHelper.putKey(CategoriesActivity.this,"category", categories.get(6));
                break;
            case 7:
                SharedHelper.putKey(CategoriesActivity.this,"category", categories.get(7));
                break;
            case 8:
                SharedHelper.putKey(CategoriesActivity.this,"category", categories.get(8));
                break;
        }
    }

    @Override
    public void onClick(View v) {
       if (v==nextBtn){
           if (validate()){
               startActivity(new Intent(new Intent(CategoriesActivity.this, AddProductActivity.class)));
           }
       }
    }

    @Override
    public void onCategoriesReceived(List<Category> categoryList) {
        for (int i= 0; i < categoryList.size(); i++){
            String name = categoryList.get(i).getName();
            RadioButton rb = new RadioButton(CategoriesActivity.this);
            rb.setText(name);
            rb.setId(i);
            categoryRG.addView(rb);
            categories.add(name);
        }
    }

    @Override
    public void onDatabaseCancelled(DatabaseError error) {
        Toasty.error(this,""+error.getMessage(), Toasty.LENGTH_LONG).show();

    }
}
