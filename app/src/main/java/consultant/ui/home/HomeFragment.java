package consultant.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.unishop.activities.LoginActivity;
import com.example.unishop.R;
import com.example.unishop.utilities.SharedHelper;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true); // to show menu options in fragment
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //inflating menu
        inflater.inflate(R.menu.menu_main_dashboard,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout){
            showLogoutDialogue();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLogoutDialogue() {
        //AlertDialog
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        //set Layout Linear Layout
        LinearLayout linearLayout = new LinearLayout(getActivity());
        // Views to set in dialog
        final TextView textView = new TextView(getActivity());
        textView.setText("Are you sure you want to exit?");
        textView.setTextSize(20);
        linearLayout.addView(textView);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);


        //cancel button
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dismiss dialog
                dialog.dismiss();
            }
        });

        //Reset pin button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedHelper.clearSharedPreferences(getActivity());
                startActivity(new Intent(new Intent(getActivity(), LoginActivity.class)));
            }
        });

        //create and show dialog
        builder.create().show();
    }
}
