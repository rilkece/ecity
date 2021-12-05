package Classes;




import android.provider.ContactsContract;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by erick on 20/09/2017.
 */

public class Userinfo {
    public String nome;
    public String emailuser;
    public String tele;
    public String dianasci;
    public String mesnasci;
    public String anonasci;
    public String sexo;


    public Userinfo(){

    }

    public Userinfo(String nome, String emailuser, String tele, String dianasci,
                    String mesnasci, String anonasci, String sexo ){
        this.nome = nome;
        this.emailuser = emailuser;
        this.tele = tele;
        this.dianasci = dianasci;
        this.mesnasci = mesnasci;
        this.anonasci = anonasci;
        this.sexo = sexo;

    }

    public String getNome() {
        return nome;
    }

    public String getEmailuser() {
        return emailuser;
    }

    public String getTele() {
        return tele;
    }

    public String getDianasci(){return  dianasci;}

    public  String getMesnasci(){return mesnasci;}

    public String getAnonasci(){return anonasci;}

    public String getSexo() {
        return sexo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmailuser(String emailuser) {
        this.emailuser = emailuser;
    }

    public void setTele(String tele) {
        this.tele = tele;
    }

    public void setDianasci(String dianasci) {this.dianasci = dianasci;}

    public void setMesnasci(String mesnasci) {this.mesnasci = mesnasci;}

    public void setAnonasci(String anonasci) {this.anonasci = anonasci;}

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
}
