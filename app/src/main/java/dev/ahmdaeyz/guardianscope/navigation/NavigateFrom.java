package dev.ahmdaeyz.guardianscope.navigation;

public interface NavigateFrom {
    interface Reader {
        void onBackPressedFromFragment();
    }

    interface Browsers {
        interface Discover {
            void toReader(String apiUrl);
        }
    }
}
