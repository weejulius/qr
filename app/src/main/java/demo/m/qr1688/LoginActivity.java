package demo.m.qr1688;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.inject.Key;

import java.util.HashMap;
import java.util.Map;

import roboguice.RoboGuice;
import roboguice.inject.RoboInjector;
import roboguice.util.RoboAsyncTask;
import roboguice.util.RoboContext;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends ActionBarActivity implements LoaderCallbacks<Cursor> ,RoboContext{


    private EditText nameEditText;
    private EditText passwordEditText;
    private MenuItem loginMenuItem;
    private String IS_LOGINED;

    protected HashMap<Key<?>, Object> scopedObjects = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        final RoboInjector injector = RoboGuice.getInjector(this);
        injector.injectMembersWithoutViews(this);

        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        nameEditText = (EditText) findViewById(R.id.etv_login_name);
        passwordEditText = (EditText) findViewById(R.id.etv_login_password);

        TextWatcher watcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable gitDirEditText) {
                updateEnablement();
            }
        };
        nameEditText.addTextChangedListener(watcher);
        passwordEditText.addTextChangedListener(watcher);
    }

    private void updateEnablement() {
        if (loginMenuItem != null)
            loginMenuItem.setEnabled(loginEnabled());
    }

    private boolean loginEnabled() {
        return !TextUtils.isEmpty(nameEditText.getText())
                && !TextUtils.isEmpty(passwordEditText.getText());
    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    /**
     * 登陆方法
     *
     * @param
     */
    public void login() {

        final String password = passwordEditText.getText().toString();

        new RoboAsyncTask<Long>(this) {

            @Override
            public Long call() throws Exception {

                if ("111".equals(password)) {
                    return 1l;
                }
                return -1l;
            }


            public void onSuccess(Long rs) {
                if (rs > 0) {
                    Intent intent = new Intent();
                    intent.putExtra("IS_LOGINED",true);
                    setResult(RESULT_OK,intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "password error", Toast.LENGTH_SHORT).show();
                }
            }

            public void onException(Exception e) {

            }
        }.execute();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.m_login:
                login();
                return true;
            default:
                return false;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu optionMenu) {
        getMenuInflater().inflate(R.menu.m_login, optionMenu);
        loginMenuItem = optionMenu.findItem(R.id.m_login);
        return true;
    }

    @Override
    public Map<Key<?>, Object> getScopedObjectMap() {
        return scopedObjects;
    }
}



