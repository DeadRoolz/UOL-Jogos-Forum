/* 
* Classe que representa o banco de dados  
* que armazena o login salvo do usuário
*/

package com.gpsoft.uoljogosforum;


//android imports
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;


public class BancoDeDados extends SQLiteOpenHelper {
    
    public static final int DATABASE_VERSION = 6;
    public static final String DATABASE_NAME = "ForumUolJogosApp.db";
    

    BancoDeDados(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE usuarios (_ID INTEGER PRIMARY KEY, nickname TEXT, email TEXT, senha TEXT, avatarurl TEXT); ");
        db.execSQL("CREATE UNIQUE INDEX idx_usuarios_email ON usuarios (email);");
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        
        db.execSQL("DROP TABLE IF EXISTS usuarios; ");
        onCreate(db);
    }
}