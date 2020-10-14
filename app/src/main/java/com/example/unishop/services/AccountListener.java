package com.example.unishop.services;

import com.example.unishop.models.User;
import com.google.firebase.database.DatabaseError;

import java.util.List;

public interface AccountListener {
    interface RegistrationListener{
        void onAccountCreated();
        void onFailureResponse(Exception e);
    }

    interface LoginListener{
        void onSuccessLogin();
        void onLoginFailure();
        void onFailureResponse(Exception e);
    }

    interface AccountRecoveryListener{
        void onEmailSent();
        void onFailureSendingEmail();
        void onFailureResponse(Exception e);
    }


    interface AccountInstantListener{
        void onAccountReceived(User user);
        void onRequestCancelled(DatabaseError error);
    }

    interface LoadAccountsListener{
        void onAccountsReceived(List<User> userList);
        void onDatabaseCancelled(DatabaseError error);
    }

    interface UpdateAccountListener{
        void onAccountUpdated();
        void onFailureResponse(Exception e);
    }

    interface AccountsCountListener{
        void onAccountCountReceived(int count);
        void onDatabaseCancelled(DatabaseError error);
    }
}
