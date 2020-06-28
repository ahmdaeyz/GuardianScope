package dev.ahmdaeyz.guardianscope.navigation;

public interface NavigateFrom {
    interface Reader {
        void onBackPressedFromFragment();
    }

    interface Browsers {
        void toReaderFromDiscover(String apiUrl);

        void toReaderFromBookmarks(String apiUrl);
    }
}
