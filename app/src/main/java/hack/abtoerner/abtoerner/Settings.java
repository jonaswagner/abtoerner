package hack.abtoerner.abtoerner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

/*    Switch simpleSwitch = (Switch) findViewById(R.id.switch1);
        simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                System.out.println("Toggle switch enabled!");
            } else {
                System.out.println("Toggle switch disabled!");
            }
        }
    });*/
}
