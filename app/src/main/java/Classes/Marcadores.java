package Classes;

/**
 * Created by erick on 02/09/2017.
 */
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

import com.ecity.alice.project.ecity.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Exclude;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by erick on 27/08/2017.
 */

public class Marcadores {

    public String nome;
    public String problema;
    public double latitude;
    public double longitude;
    public int endIcon;
    public String userUid;
    public String data;


    public Marcadores(){

    }

    public Marcadores(String nome, String problema, double latitude, double longitude, int endIcon,
                      String userUid, String data){
        this.nome = nome;
        this.problema = problema;
        this.latitude = latitude;
        this.longitude = longitude;
        this.endIcon = endIcon;
        this.userUid = userUid;
        this.data = data;

    }
    public  String getNome(){
        return nome;
    }
    public void setNome(String nome){
        this.nome = nome;
    }
    public String getProblema(){
        return problema;
    }
    public void setProblema(String problema){
        this.problema = problema;
    }
    public double getLatitude(){
        return latitude;
    }
    public void setLatitude(double latitude){
        this.latitude = latitude;
    }
    public double getLongitude(){
        return longitude;
    }
    public void setLongitude(double longitude){
        this.longitude = longitude;
    }
    public int getEndIcon(){return endIcon;}
    public void setEndIcon(int endIcon) {this.endIcon = endIcon;}
    public String getUserUid(){return userUid;}
    public void setUserUid(String userUid) {this.userUid = userUid;}
    public String getData() {return data;}
    public void setData(String data){this.data = data;}

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("nome", nome);
        result.put("problema", problema);
        result.put("latitude", latitude);
        result.put("longitude", longitude);
        result.put("endIcon", endIcon);
        result.put("user", userUid);
        result.put("data", data);

        return result;

    }

}