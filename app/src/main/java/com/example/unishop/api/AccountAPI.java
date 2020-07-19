package com.example.unishop.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.unishop.R;
import com.example.unishop.models.Product;
import com.example.unishop.models.User;
import com.example.unishop.utilities.Server;
import com.example.unishop.utilities.SharedHelper;
import com.example.unishop.services.AccountListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public class AccountAPI {
    //Declare an instance of FirebaseAuth
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private AccountListener.RegistrationListener registrationListener;
    private AccountListener.LoginListener loginListener;
    private AccountListener.AccountInstantListener accountInstantListener;
    private AccountListener.AccountRecoveryListener accountRecoveryListener;
    private AccountListener.LoadAccountsListener loadAccountsListener;
    private User user;
    private Context context;
    private List<User> userList;

    public AccountAPI(Context context) {
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");
        user = new User();
        userList = new ArrayList<>();
    }

    public void loginUser(final String email, final String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        loginListener.onSuccessLogin();
                    } else {
                        loginListener.onLoginFailure();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               loginListener.onFailureResponse(e);
            }
        });
    }

    public void registerNewUser(final String first_name, final String last_name, final String u_email,
                                 final String u_phone, final String u_id_number, final String password, final String user_role){
        mAuth.createUserWithEmailAndPassword(u_email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();

                            //get user email and user id from auth
                            String email = user.getEmail();
                            String uid = user.getUid();
                            // when user is registered store user info in firebase realtime database too
                            //using HashMap
                            HashMap<Object, String> hashMap = new HashMap<>();
                            //put info in hashmap
                            hashMap.put("uid",uid);
                            hashMap.put("email",email);
                            hashMap.put("firstname", first_name);
                            hashMap.put("lastname", last_name);
                            hashMap.put("id_number", u_id_number);
                            hashMap.put("phone", u_phone);
                            hashMap.put("role", user_role);
                            hashMap.put("enrolledBy", SharedHelper.getKey(context,"user_id"));

                            //put data within hashMap in database
                            reference.child(uid).setValue(hashMap);
                            registrationListener.onAccountCreated();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       registrationListener.onFailureResponse(e);
                    }
                });

    }

    public void getUserAccount(String email){
        Query query = reference.orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // check until required data get
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    // get data
                    String email = "" + ds.child("email").getValue();
                    String enrolledBy = "" + ds.child("enrolledBy").getValue();
                    String firstname = "" + ds.child("firstname").getValue();
                    String id_number = "" + ds.child("id_number").getValue();
                    String lastname = "" + ds.child("lastname").getValue();
                    String phone = "" + ds.child("phone").getValue();
                    String role = "" + ds.child("role").getValue();
                    String uid = "" + ds.child("uid").getValue();

                    user.setEmail(email);
                    user.setEnrolledBy(enrolledBy);
                    user.setFirstname(firstname);
                    user.setId_number(id_number);
                    user.setLastname(lastname);
                    user.setPhone(phone);
                    user.setRole(role);
                    user.setUid(uid);
                }
                accountInstantListener.onAccountReceived(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                accountInstantListener.onRequestCancelled(error);

            }
        });
    }

    public void recoverAccount(String email){
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                          accountRecoveryListener.onEmailSent();
                        }
                        else {
                           accountRecoveryListener.onFailureSendingEmail();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                accountRecoveryListener.onFailureResponse(e);
            }
        });
    }

    public void getAllAccounts(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    userList.add(user);
                }
                loadAccountsListener.onAccountsReceived(userList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadAccountsListener.onDatabaseCancelled(error);

            }
        });
    }
    public AccountListener.RegistrationListener getRegistrationListener() {
        return registrationListener;
    }

    public void setRegistrationListener(AccountListener.RegistrationListener registrationListener) {
        this.registrationListener = registrationListener;
    }

    public AccountListener.LoginListener getLoginListener() {
        return loginListener;
    }

    public void setLoginListener(AccountListener.LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    public AccountListener.AccountInstantListener getAccountInstantListener() {
        return accountInstantListener;
    }

    public void setAccountInstantListener(AccountListener.AccountInstantListener accountInstantListener) {
        this.accountInstantListener = accountInstantListener;
    }

    public AccountListener.AccountRecoveryListener getAccountRecoveryListener() {
        return accountRecoveryListener;
    }

    public void setAccountRecoveryListener(AccountListener.AccountRecoveryListener accountRecoveryListener) {
        this.accountRecoveryListener = accountRecoveryListener;
    }

    public AccountListener.LoadAccountsListener getLoadAccountsListener() {
        return loadAccountsListener;
    }

    public void setLoadAccountsListener(AccountListener.LoadAccountsListener loadAccountsListener) {
        this.loadAccountsListener = loadAccountsListener;
    }
}
