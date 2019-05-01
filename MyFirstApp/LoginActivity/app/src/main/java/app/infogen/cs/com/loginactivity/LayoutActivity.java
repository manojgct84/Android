package app.infogen.cs.com.loginactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);
        Button RL = (Button) findViewById(R.id.RL);
        Button LL = (Button) findViewById(R.id.LL);

        RL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RL = new Intent(LayoutActivity.this, RL.class);
                startActivity(RL);
            }
        });

        LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent LL = new Intent(LayoutActivity.this, LL.class);
                startActivity(LL);
            }
        });
    }
}
