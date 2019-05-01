package count.message.cs.com.message;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    private Button scanSMSMessage;
    private TextView textView;
    private String permission = "android.permission.READ_SMS";
    private final int permissionRequestCode = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scanSMSMessage.findViewById(R.id.scanSMS);
        textView.findViewById(R.id.textview);

        scanSMSMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smsCount(permission,permissionRequestCode);
            }
        });
    }

    private void smsCount(String permission, int permissionRequestCode) {

        if (ContextCompat.checkSelfPermission(this,permission ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{permission} , permissionRequestCode);

        }else {
            Toast.makeText(this,"Permission is already give",Toast.LENGTH_LONG).show();
        }
        final Uri SMS_INBOX = Uri.parse("content://sms/inbox");

        Cursor c = getContentResolver().query(SMS_INBOX, null, "read = 0", null, null);

        int unreadMessagesCount = c.getCount();

        Log.i("SMS COUNT", " " + unreadMessagesCount);

        c.deactivate();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case permissionRequestCode:
                    if (grantResults.length > 0 && grantResults[0] == requestCode){
                        Toast.makeText(this,"Permission is granted", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this,"Permission is not granted", Toast.LENGTH_SHORT).show();
                    }
        }
    }
}
