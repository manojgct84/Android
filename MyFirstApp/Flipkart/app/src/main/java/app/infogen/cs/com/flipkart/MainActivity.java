package app.infogen.cs.com.flipkart;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import app.infogen.cs.com.flipkart.Fragment.ItemListFragment;

public class MainActivity extends AppCompatActivity {

    private Button bntClick;
    private FragmentTransaction fragmentTransaction;
    private ItemListFragment itemListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bntClick = (Button) findViewById(R.id.click);

        bntClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListFragment = new ItemListFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.itemsFrame, itemListFragment, "ItemsFragmentLayout");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                bntClick.setVisibility(v.GONE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent moveback = new Intent(this, MainActivity.class);
        startActivity(moveback);
        finish();
    }
}
