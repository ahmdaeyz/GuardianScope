package dev.ahmdaeyz.guardianscope.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import dev.ahmdaeyz.guardianscope.data.db.daos.BookmarkedArticlesDao;
import dev.ahmdaeyz.guardianscope.data.model.theguardian.ArticleWithBody;

@Database(entities = ArticleWithBody.class, version = 1)
public abstract class GuardianScopeDatabase extends RoomDatabase {
    private static GuardianScopeDatabase INSTANCE;

    public static GuardianScopeDatabase init(Context context) {
        if (INSTANCE != null) {
            throw new RuntimeException("Database Already initialized use getInstance.");
        } else {
            INSTANCE = Room.databaseBuilder(context,
                    GuardianScopeDatabase.class,
                    "guardian_scope_database").build();
            return INSTANCE;
        }

    }

    public static GuardianScopeDatabase getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        } else {
            throw new RuntimeException("You can't getInstance before Initializing the Database with init.");
        }
    }

    public abstract BookmarkedArticlesDao bookmarkedArticlesDao();
}

