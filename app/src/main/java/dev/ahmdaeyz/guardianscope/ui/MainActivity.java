package dev.ahmdaeyz.guardianscope.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import dev.ahmdaeyz.guardianscope.R;
import dev.ahmdaeyz.guardianscope.databinding.ActivityMainBinding;
import dev.ahmdaeyz.guardianscope.navigation.NavigateFrom;
import dev.ahmdaeyz.guardianscope.ui.browser.BrowserFragment;
import dev.ahmdaeyz.guardianscope.ui.reader.ReaderFragment;

public class MainActivity extends AppCompatActivity implements NavigateFrom.Reader, NavigateFrom.Browsers {
    private ActivityMainBinding binding;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        fragmentManager = getSupportFragmentManager();
        BrowserFragment browserFragment;
        ReaderFragment readerFragment;
        if (savedInstanceState != null) {
            browserFragment = (BrowserFragment) fragmentManager.getFragment(savedInstanceState, "BrowserFragment");
            readerFragment = (ReaderFragment) fragmentManager.getFragment(savedInstanceState, "ReaderFragment");
        } else {
            browserFragment = new BrowserFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, browserFragment, "browser_fragment")
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fragmentManager.findFragmentByTag("browser_fragment") != null) {
            fragmentManager.putFragment(outState, "BrowserFragment", fragmentManager.findFragmentByTag("browser_fragment"));
        }
        if (fragmentManager.findFragmentByTag("reader_fragment") != null) {
            fragmentManager.putFragment(outState, "ReaderFragment", fragmentManager.findFragmentByTag("reader_fragment"));
        }
    }

    @Override
    public void onBackPressedFromFragment() {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void toReader(String apiUrl) {
        ReaderFragment readerFragment = ReaderFragment.newInstance(apiUrl);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, readerFragment, "reader_fragment")
                .addToBackStack("reader_fragment")
                .commit();
    }

    public void updateStatusBarColor(int colorResId) {// Color must be in hexadecimal fromat
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(colorResId));
    }
}
