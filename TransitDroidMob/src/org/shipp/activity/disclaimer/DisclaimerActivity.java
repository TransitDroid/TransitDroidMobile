package org.shipp.activity.disclaimer;

import org.shipp.activity.PrincipalActivity;
import org.shipp.activity.R;

import com.coboltforge.domain.Setting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DisclaimerActivity extends Activity implements android.view.View.OnClickListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.disclaimer);
		
		((Button)findViewById(R.id.disclaim)).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Setting set = Setting.getSetting(PrincipalActivity.activity);
		set.setDisclaimer("Yes");
		startActivity(new Intent(this, PrincipalActivity.class));
	}
	
	
	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle("Closing Activity")
        .setMessage("Are you sure you want to exit?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            finish();    
        }

    })
    .setNegativeButton("No", null)
    .show();
	}

}
