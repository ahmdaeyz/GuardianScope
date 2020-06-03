package dev.ahmdaeyz.guardianscope.ui;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import dev.ahmdaeyz.guardianscope.R;
import dev.ahmdaeyz.guardianscope.databinding.ActivityMainBinding;
import dev.ahmdaeyz.guardianscope.ui.browser.BrowserFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    FragmentManager fragmentManager;
    Fragment browserFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null) {
            browserFragment = (BrowserFragment) fragmentManager.getFragment(savedInstanceState, "BrowserFragment");
        } else {
            BrowserFragment browserFragment = new BrowserFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, browserFragment, "browser_fragment")
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        fragmentManager.putFragment(outState, "BrowserFragment", fragmentManager.findFragmentByTag("browser_fragment"));
    }

}
