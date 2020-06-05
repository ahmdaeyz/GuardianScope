package dev.ahmdaeyz.guardianscope.navigation;

import dev.ahmdaeyz.guardianscope.data.model.theguardian.Article;

public interface NavigateFrom {
    interface Reader {
        void onBackPressedFromFragment();
    }

    interface Browsers {
        interface Discover {
            void toReader(Article article);
        }
    }
}
