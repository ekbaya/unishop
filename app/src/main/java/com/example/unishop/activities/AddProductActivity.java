package com.example.unishop.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.unishop.R;
import com.example.unishop.data.SharedHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class AddProductActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
View.OnClickListener{
    //views
    @BindView(R.id.p_nameEt) EditText p_nameEt;
    @BindView(R.id.categoryRG) RadioGroup categoryRG;
    @BindView(R.id.nextBtn) Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ButterKnife.bind(this);

        categoryRG.setOnCheckedChangeListener(this);
        nextBtn.setOnClickListener(this);
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
            SharedHelper.putKey(AddProductActivity.this, "p_name", p_nameEt.getText().toString());
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
        Intent intent = new Intent(AddProductActivity.this, AdminHomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.healthy_and_Beauty:
                SharedHelper.putKey(AddProductActivity.this,"category", "Healthy and Beauty");
                break;
            case R.id.grocery:
                SharedHelper.putKey(AddProductActivity.this,"category", "Grocery");
                break;
            case R.id.women_fashion:
                SharedHelper.putKey(AddProductActivity.this,"category", "Women's Fashion");
                break;
            case R.id.men_fashion:
                SharedHelper.putKey(AddProductActivity.this,"category", "Men's Fashion");
                break;
            case R.id.baby_products:
                SharedHelper.putKey(AddProductActivity.this,"category", "Baby's Products");
                break;
            case R.id.phones_and_Tablets:
                SharedHelper.putKey(AddProductActivity.this,"category", "Phones and Tables");
                break;
            case R.id.electronics:
                SharedHelper.putKey(AddProductActivity.this,"category", "Electronics");
                break;
            case R.id.computing:
                SharedHelper.putKey(AddProductActivity.this,"category", "Computing");
                break;
            case R.id.appliance:
                SharedHelper.putKey(AddProductActivity.this,"category", "Appliance");
                break;
        }
    }

    @Override
    public void onClick(View v) {
       if (v==nextBtn){
           if (validate()){
               startActivity(new Intent(new Intent(AddProductActivity.this, AddProductActivity2.class)));
           }
       }
    }
}
