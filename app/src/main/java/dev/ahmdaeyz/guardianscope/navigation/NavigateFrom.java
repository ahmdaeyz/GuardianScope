package dev.ahmdaeyz.guardianscope.navigation;

public interface NavigateFrom {
    interface Browsers {
        void toReaderFromDiscover(String apiUrl);

        void toReaderFromBookmarks(String apiUrl);

        void toSettingsFromMain();
    }
}
