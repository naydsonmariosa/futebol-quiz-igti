package br.com.igti.android.futebolquiz;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

public abstract class SingleFragmentActivity extends Activity {

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, (fragment == null) ? createFragment() : fragment)
                    .commit();
        }
    }
}