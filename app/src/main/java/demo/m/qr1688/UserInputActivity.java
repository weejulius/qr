package demo.m.qr1688;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Created by jyu on 15-3-23.
 */
public class UserInputActivity extends Activity{

    EditText userIdET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_input);

        userIdET = (EditText)findViewById(R.id.et_user_id);

    }

    public void doGotoPhoto(View view) {

        String userId = userIdET.getText().toString();
        Intent intent = new Intent(this,PhotoActivity.class);

        intent.putExtra("USER_ID_INPUT",userId);
        startActivity(intent);
    }
}
