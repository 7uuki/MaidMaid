package de.fhandshit.maidmaid;

import android.app.Application;

import de.fhandshit.maidmaid.data.database.AppDatabase;
import de.fhandshit.maidmaid.data.repository.Repo;

public class App extends Application {
    private static AppDatabase database;
    private static Repo repo;

    public Repo getRepo(){
        if(repo == null){
            repo = new Repo(getDatabase());
        }
        return repo;
    }


    private AppDatabase getDatabase(){
        if(database == null) database = AppDatabase.getDatabase(getApplicationContext());
        return database;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
