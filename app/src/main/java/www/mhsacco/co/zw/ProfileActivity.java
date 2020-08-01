package www.mhsacco.co.zw;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import SharedPreferences.SharedPrefManager;

public class ProfileActivity extends AppCompatActivity {


    private TextView textViewUsername, textViewUserEmail;
    private EditText edexperiment;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);

        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }







       // edexperiment = (EditText) findViewById(R.id.experiment);

        textViewUsername = (TextView) findViewById(R.id.paying);
        textViewUserEmail = (TextView) findViewById(R.id.loanInfo);

        textViewUserEmail.setText(SharedPrefManager.getInstance(this).getKeyUserConfirm());
        textViewUsername.setText(SharedPrefManager.getInstance(this).getpaynumber());
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menuLogout:
                SharedPrefManager.getInstance(this).logout();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.menuSettings:
                Toast.makeText(this, "You clicked settings", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }
    @Override
    public void onBackPressed() {
       startActivity(new Intent(this, variousitems.class));
       finish();

    }
}
