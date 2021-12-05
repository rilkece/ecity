package com.ecity.alice.project.ecity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

import Classes.Userinfo;

import static android.content.ContentValues.TAG;


public class AtualizarPerfil extends Fragment {

    private EditText editNome;
    private EditText ediTele;
    private Spinner diaNasc2;
    private Spinner mesNasc2;
    private Spinner anoNasc2;
    private RadioButton mRMasc2;
    private RadioButton mRFemi2;
    private TextView mEmail;
    private Button btnAtualizar;
    private String mSexo2;

    private ArrayAdapter adapterDia2;
    private ArrayAdapter adapterMes2;
    private ArrayAdapter adapterAno2;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private String nomeUser;
    public Userinfo perfil;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
// Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_atualizar_perfil, container, false);


    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {




        diaNasc2 =(Spinner) getView().findViewById(R.id.spinnerDia2);
        mesNasc2 =(Spinner) getView().findViewById(R.id.spinnerMes2);
        anoNasc2 =(Spinner) getView().findViewById(R.id.spinnerAno2);

        adapterDia2 = ArrayAdapter.createFromResource(getContext(), R.array.dia_nasc,
                R.layout.support_simple_spinner_dropdown_item);
        adapterDia2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        diaNasc2.setAdapter(adapterDia2);

        adapterMes2 = ArrayAdapter.createFromResource(getContext(), R.array.mes_nasc,
                R.layout.support_simple_spinner_dropdown_item);
        adapterMes2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mesNasc2.setAdapter(adapterMes2);

        adapterAno2 = ArrayAdapter.createFromResource(getContext(), R.array.ano_nasc,
                R.layout.support_simple_spinner_dropdown_item);
        adapterAno2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        anoNasc2.setAdapter(adapterAno2);

        mRFemi2 = (RadioButton) getView().findViewById(R.id.radioFemi2);
        mRMasc2 = (RadioButton)getView().findViewById(R.id.radioMasc2);
        mRFemi2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRMasc2.setChecked(false);
                mSexo2 = "feminino";
            }
        });
        mRMasc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRFemi2.setChecked(false);
                mSexo2 = "masculino";
            }
        });

        editNome = (EditText) getView().findViewById(R.id.input_nome2);
        ediTele = (EditText)getView().findViewById(R.id.input_tele2);
        mEmail = (TextView) getView().findViewById(R.id.email2);



        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        final String userID = user.getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getInstance().getReference("users/"+userID);
        DatabaseReference nomeRef = myRef.child("nome");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                perfil = dataSnapshot.getValue(Userinfo.class);
                //Toast.makeText(getContext(), perfil.nome, Toast.LENGTH_LONG).show();
                if (perfil == null){
                    perfil = new Userinfo(""
                            , mAuth.getCurrentUser().getEmail()
                            , ""
                            , "01"
                            , "01"
                            , "1950"
                            , "maculino");
                }


                editNome.setText(perfil.getNome());
                ediTele.setText(perfil.getTele());

                if (perfil.getSexo().equals("masculino")){
                    mRMasc2.setChecked(true);
                    mSexo2 = "masculino";

                }else if (perfil.getSexo().equals("feminino")){
                    mRFemi2.setChecked(true);
                    mSexo2 = "feminino";
                }

                int intDia = Integer.parseInt(perfil.getDianasci());
                diaNasc2.setSelection(intDia - 1);
                int intMes = Integer.parseInt(perfil.getMesnasci());
                mesNasc2.setSelection(intMes - 1);
                int intAno = Integer.parseInt(perfil.getAnonasci());
                anoNasc2.setSelection(intAno - 1950);

                mEmail.setText(user.getEmail());

                btnAtualizar = (Button)getView().findViewById(R.id.btn_email_sign_in2);
                btnAtualizar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Userinfo infosUsuario2 = new Userinfo(editNome.getText().toString(),
                                mEmail.getText().toString(), ediTele.getText().toString(),
                                diaNasc2.getSelectedItem().toString(),
                                mesNasc2.getSelectedItem().toString(),
                                anoNasc2.getSelectedItem().toString(), mSexo2);
                        myRef.setValue(infosUsuario2);

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(editNome.getText().toString())
                                .build();
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        TastyToast.makeText(getContext(), getResources().getString(R.string.perfil_atualizado),
                                                TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();

                                        Log.d(TAG, getResources().getString(R.string.perfil_atualizado));
                                        MainActivity.mapasInfra.setVisibility(View.VISIBLE);
                                        MainActivity.mapasBemEstar.setVisibility(View.VISIBLE);
                                        MainActivity.marcadores.setVisibility(View.VISIBLE);
                                        FragmentTransaction transaction2 = MainActivity.fragmentManager.beginTransaction();
                                        transaction2.replace(R.id.conteiner, new MapaEcity(), "MapsFragment");
                                        MainActivity.fabM1.setVisibility(View.GONE);
                                        MainActivity.fabM2.setVisibility(View.GONE);
                                        MainActivity.fabM3.setVisibility(View.GONE);
                                        MainActivity.fabM4.setVisibility(View.GONE);
                                        MainActivity.fabM5.setVisibility(View.GONE);
                                        MainActivity.fabM6.setVisibility(View.GONE);
                                        MainActivity.fabM7.setVisibility(View.GONE);
                                        transaction2.commitAllowingStateLoss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                TastyToast.makeText(getContext(), getResources().getString(R.string.falha_atualizar),
                                        TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });








    }
}
