package consultant.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.unishop.R;
import com.example.unishop.api.AccountAPI;
import com.example.unishop.models.User;
import com.example.unishop.services.AccountListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class AccountFragment extends Fragment implements AccountListener.AccountInstantListener {
    @BindView(R.id.nameTv)
    TextView nameTv;
    @BindView(R.id.phoneTv)
    TextView phoneTv;
    @BindView(R.id.emailTv)
    TextView emailTv;

    private AccountAPI accountAPI;
    private FirebaseAuth auth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        ButterKnife.bind(this, view);

        accountAPI = new AccountAPI(getActivity());
        accountAPI.setAccountInstantListener(this);
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        assert user != null;
        String email = user.getEmail();
        accountAPI.getUserAccount(email);
        return view;
    }

    @Override
    public void onAccountReceived(User user) {
        nameTv.setText(user.getFirstname() + " "+user.getLastname());
        phoneTv.setText(user.getPhone());
        emailTv.setText(user.getEmail());
    }

    @Override
    public void onRequestCancelled(DatabaseError error) {
        Toasty.error(getActivity(), "Error fetching user profile, "+error.getMessage(), Toasty.LENGTH_LONG).show();

    }
}
