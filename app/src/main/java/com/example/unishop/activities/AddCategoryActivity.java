package com.example.unishop.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.unishop.R;
import com.example.unishop.api.CategoriesAPI;
import com.example.unishop.services.CategoriesListener;
import com.example.unishop.utilities.Loader;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class AddCategoryActivity extends AppCompatActivity implements CategoriesListener.AddCategoryListener,
        View.OnClickListener {
    //views
    @BindView(R.id.nameEdit) EditText nameEdit;
    @BindView(R.id.descriptionEdit) EditText descriptionEdit;
    @BindView(R.id.sendBtn) Button sendBtn;

    private CategoriesAPI categoriesAPI;
    private Loader loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ButterKnife.bind(this);
        sendBtn.setOnClickListener(this);
        categoriesAPI = new CategoriesAPI();
        categoriesAPI.setAddCategoryListener(this);
        loader = new Loader(this);
    }

    @Override
    public void onClick(View view) {
        if (view.equals(sendBtn)){
            if (validated()){
                categoriesAPI.addCategory(nameEdit.getText().toString(), descriptionEdit.getText().toString());
                loader.showDialogue();
            }
        }

    }

    private boolean validated() {
        if (TextUtils.isEmpty(nameEdit.getText().toString())){
            Toasty.warning(this, "Category name is required", Toasty.LENGTH_LONG).show();
            return false;
        }
        else if (TextUtils.isEmpty(descriptionEdit.getText().toString())){
            Toasty.warning(this, "Category description is required", Toasty.LENGTH_LONG).show();
            return false;
        }else return true;
    }

    @Override
    public void onCategoryAdded() {
        loader.hideDialogue();
        Toasty.success(this, "New Category added successfully", Toasty.LENGTH_LONG).show();
        nameEdit.setText("");
        descriptionEdit.setText("");
    }

    @Override
    public void onFailure(Exception e) {
        loader.hideDialogue();
        Toasty.error(this, ""+e.getMessage(), Toasty.LENGTH_LONG).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}