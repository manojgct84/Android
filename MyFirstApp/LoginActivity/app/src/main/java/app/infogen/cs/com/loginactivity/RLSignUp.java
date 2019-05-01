package app.infogen.cs.com.loginactivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RLSignUp extends AppCompatActivity implements View.OnClickListener {

    private Button submit;
    private EditText dobText;
    private DatePickerDialog dob;
    private SimpleDateFormat dateFormatter;
    private Map<String, EditText> input = new HashMap<>();
    private AppCompatSpinner country;
    private View childCounty;
    private List<String> countryLst;
    private List<String> states;
    private ArrayAdapter adapter;

    private ListView lstView;
    private ArrayAdapter adapterState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rlsign_up);
        dateFormatter = new SimpleDateFormat("dd/MM/yyy", Locale.UK);

        submit = (Button) findViewById(R.id.submit);
        input.put("name", (EditText) findViewById(R.id.name));
        input.put("email", (EditText) findViewById(R.id.email));
        input.put("mobile", (EditText) findViewById(R.id.mobile));
        input.put("address", (EditText) findViewById(R.id.address));


        dobText = (EditText) findViewById(R.id.dob);

        country = (AppCompatSpinner) findViewById(R.id.country);
        lstView = (ListView) findViewById(R.id.state);

        states = new ArrayList<>();
        states.add("KA");
        states.add("TN");
        states.add("AP");

        countryLst = new ArrayList<>();
        countryLst.add("India");
        countryLst.add("US");
        countryLst.add("UK");


        adapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, countryLst);
        country.setAdapter(adapter);

        adapterState = new ArrayAdapter(this, android.R.layout.simple_list_item_1, states);

        lstView.setAdapter(adapterState);

        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = (String) parent.getItemAtPosition(position);
                Toast.makeText(RLSignUp.this, "The value selected -" + value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(RLSignUp.this, "Value not selected", Toast.LENGTH_SHORT).show();
            }
        });


        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = (String) parent.getItemAtPosition(position);
                Toast.makeText(RLSignUp.this, "The Item Selected - " + value, Toast.LENGTH_SHORT).show();
            }
        });


        setDateTimeField();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInputs();
            }
        });
    }

    private void setDateTimeField() {
        dobText.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();

        dob = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dobText.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }


    private void validateInputs() {

        String vName = null, vEmail = null, vMobile = null, vAddress = null;
        EditText name = null;
        View focusView;

        for (Map.Entry<String, EditText> entry : input.entrySet()) {
            String key = entry.getKey();
            if (key.equals("name")) {
                name = entry.getValue();
                name.setError(null);
                vName = name.getText().toString();
            }

            if (TextUtils.isEmpty(vName)) {
                name.setError(getString(R.string.error_field_required));
                focusView = name;
            }
            if (key.equals("email")) {
                EditText email = entry.getValue();
                email.setError(null);
                vEmail = email.getText().toString();
            }

            if (key.equals("mobile")) {
                EditText mobile = entry.getValue();
                mobile.setError(null);
                vMobile = mobile.getText().toString();
            }

            if (key.equals("address")) {
                EditText address = entry.getValue();
                address.setError(null);
                vAddress = address.getText().toString();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == dobText) {
            dob.show();
        }
    }
}