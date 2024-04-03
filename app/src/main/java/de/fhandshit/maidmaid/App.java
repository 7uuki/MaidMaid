package de.fhandshit.maidmaid;

import android.app.Application;

import androidx.room.Room;

import de.fhandshit.maidmaid.data.database.AppDatabase;
import de.fhandshit.maidmaid.data.repository.DummyRepo;
import de.fhandshit.maidmaid.data.repository.Repo;

public class App extends Application {
    private static AppDatabase database;
    private static Repo repo;

    public static Repo getRepo(){
        if(repo == null){
            repo = new DummyRepo();
            //uncomment later for database connection
            //AppDatabase b1 = getDatabase();
            //repo = new Repo(b1.productDao(), b1.categoryDao(), b1.productItemDao());
        }
        return repo;
    }


    private AppDatabase getDatabase(){
        if(database == null) database = Room.databaseBuilder(getApplicationContext(),AppDatabase.class, "database-name").build();
        return database;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //AppDatabase appDatabase = getDatabase();
    }
}
