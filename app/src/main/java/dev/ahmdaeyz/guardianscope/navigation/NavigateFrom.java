package dev.ahmdaeyz.guardianscope.navigation;

public interface NavigateFrom {
    interface Reader {
        void onBackPressedFromFragment();
    }

    interface Browsers {
        void toReader(String apiUrl);
    }
}
