package nullvoid.shaketorch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

   // boolean isServiceActive;
    ImageView btn_switch;
    TextView txtv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_switch = (ImageView) findViewById(R.id.btnswitch);
        txtv = (TextView) findViewById(R.id.textView);
        if(FlashService.isServiceActive == 1){
            btn_switch.setImageResource(R.drawable.btn_switch_on);
            txtv.setText("Torch Status: On");
        }

        btn_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,FlashService.class);
                if(FlashService.isServiceActive == 1){
                    stopService(intent);
                    btn_switch.setImageResource(R.drawable.btn_switch_off);
                    txtv.setText("Torch Status: Off");

                }
                else {
                    startService(intent);
                    btn_switch.setImageResource(R.drawable.btn_switch_on);
                    txtv.setText("Torch Status: On");
                }
            }

        });
    }


    private void toggle_button(){


        if(FlashService.isServiceActive == 0  || FlashService.isServiceActive == 2) {

            btn_switch.setImageResource(R.drawable.btn_switch_on);

            txtv.setText("ShakeTorch Status: On");
        }


        else{
            btn_switch.setImageResource(R.drawable.btn_switch_off);
            txtv.setText("ShakeTorch Status: Off");
        }
    }



}
