package com.example.unishop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.unishop.data.SharedHelper;

public class AddProductActivity extends AppCompatActivity {
    //views
    private EditText p_nameEt;
    private RadioGroup categoryRG;
    private Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //init views
        p_nameEt = findViewById(R.id.p_nameEt);
        categoryRG = findViewById(R.id.categoryRG);
        nextBtn = findViewById(R.id.nextBtn);

        categoryRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    startActivity(new Intent(new Intent(AddProductActivity.this, AddProductActivity2.class)));
                }
            }
        });
    }

    private boolean validate() {
        if (TextUtils.isEmpty(p_nameEt.getText().toString())){
            Toast.makeText(this, "Invalid product name", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (categoryRG.getCheckedRadioButtonId() == -1){
            Toast.makeText(this, "Invalid product category", Toast.LENGTH_SHORT).show();
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
}
