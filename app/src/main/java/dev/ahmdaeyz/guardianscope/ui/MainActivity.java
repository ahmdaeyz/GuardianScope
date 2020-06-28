package dev.ahmdaeyz.guardianscope.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import dev.ahmdaeyz.guardianscope.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
//        fragmentManager = getSupportFragmentManager();
//        BrowserFragment browserFragment;
//        ReaderFragment readerFragment;
//        if (savedInstanceState != null) {
//            browserFragment = (BrowserFragment) fragmentManager.getFragment(savedInstanceState, "BrowserFragment");
//            readerFragment = (ReaderFragment) fragmentManager.getFragment(savedInstanceState, "ReaderFragment");
//        } else {
//            browserFragment = new BrowserFragment();
//            fragmentManager.beginTransaction()
//                    .replace(R.id.fragment_container, browserFragment, "browser_fragment")
//                    .commit();
//        }
    }

//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        if (fragmentManager.findFragmentByTag("browser_fragment") != null) {
//            fragmentManager.putFragment(outState, "BrowserFragment", fragmentManager.findFragmentByTag("browser_fragment"));
//        }
//        if (fragmentManager.findFragmentByTag("reader_fragment") != null) {
//            fragmentManager.putFragment(outState, "ReaderFragment", fragmentManager.findFragmentByTag("reader_fragment"));
//        }
//    }

    //    @Override
//    public void onBackPressedFromFragment() {
//        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//    }
//
    public void updateStatusBarColor(int colorResId) {// Color must be in hexadecimal fromat
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(colorResId));
    }

//    @Override
//    public void toReaderFromDiscover(String apiUrl) {
//        Navigation.findNavController(this,R.id.nav_host_fragment).navigate();
//    }
//
//    @Override
//    public void toReaderFromBookmarks(String apiUrl) {
//        Navigation.findNavController(this,R.id.nav_host_fragment).navigate(BookmarksFragmentDirections.actionBookmarksFragmentToReaderFragment(apiUrl));
//    }
}
