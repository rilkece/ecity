package com.ecity.alice.project.ecity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Build;
import android.os.Parcel;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.sdsmdg.tastytoast.TastyToast;


import java.lang.annotation.Target;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



import Classes.Marcadores;
import Classes.ObtainGPS;

public class MapaEcity extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener,
        LocationListener, ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.InfoWindowAdapter,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private String string44;
    private static final String TAG = "MapaECITY";
    private static final int REQUEST_PERMISSION = 1;
    private static String[] PERMISSIONS = {android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private SimpleDateFormat formatodata = new SimpleDateFormat("dd-MM-yyyy");
    private ProgressDialog mProgressDialog;
    private int endIcon;
    private Marcadores marcadores;
    public static GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener myLocation;
    private DatabaseReference mDatabase;
    ChildEventListener mChildEventListener;
    ValueEventListener mValue;
    DatabaseReference mMarcaRef;
    private String mUid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

    public String keyChild;
    private String ReferenciaMapa = "inicio";

    public String tt;
    public String ss;
    public String userId;
    public String idUnico = "000000";

    public DatabaseReference infosGeral;

    public TextView tit;
    private TextView snep;

    public Marcadores mm;
    public LinearLayout jota;


    private StorageReference storageReference;
    public Task stringdoida;
    public Glide glideMarker;

    private String tipodeMapa;
    private String refExcluir;
    private DatabaseReference dataMarkerSolved;

    private String txtCriado;
    private String txtData;

    // public GoogleApiClient mapGoogleApiClient;
    //public ObtainGPS gps;
    //public LatLng mlatLng;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMapAsync(this);

/*
        if (mapGoogleApiClient == null) {
            mapGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        getLocalization();

*/

    }

    /*
    public void getLocalization() {
        gps = new ObtainGPS(getContext());


        if (GetLocalization(getContext())) {
            // check if GPS enabled
            if (gps.canGetLocation()) {

                AlertDialog erroLocation = new AlertDialog.Builder(getContext()).create();
                erroLocation.setTitle("Localização");
                erroLocation.setMessage("Lat:" + gps.getLatitude() + " Lng:" + gps.getLongitude());
                erroLocation.show();
                mlatLng = new LatLng(gps.getLatitude(), gps.getLongitude());

            } else {

                AlertDialog erroLocation = new AlertDialog.Builder(getContext()).create();
                erroLocation.setTitle("Localização não encontrada");
                erroLocation.setMessage("Sua Localização não foi encontrada!! Tente novamente!");
                erroLocation.show();
                gps.showSettingsAlert();
            }

        }
    }

    public boolean GetLocalization(Context context) {
        int REQUEST_PERMISSION_LOCALIZATION = 221;
        boolean res = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.

                res = false;
                ActivityCompat.requestPermissions((Activity) context, new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_PERMISSION_LOCALIZATION);

            }
        }
        return res;
    }
*/

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     * */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

        // Setting Dialog Title
        alertDialog.setTitle(getResources().getString(R.string.gps));

        // Setting Dialog Message
        alertDialog.setMessage(getResources().getString(R.string.gps_n_habilitado));

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                getContext().startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            //Passando o googleMap para variável mMap
            mMap = googleMap;

            //Setando o que acontece quando se clica na Janela do marcador
            //No caso está apagando o arquivo no firebase
            //E apagando o marcador no mapa

            /*mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    DatabaseReference dataMarker = FirebaseDatabase.getInstance()
                            .getReference(ReferenciaMapa);
                    keyChild = marker.getTag().toString();
                    DatabaseReference Marca = dataMarker.child(keyChild) ;
                    Marca.removeValue();
                    marker.remove();
                }
            });*/

            storageReference = FirebaseStorage.getInstance().getReference();
            mProgressDialog = new ProgressDialog(getContext());
            mMap.setOnMapClickListener(this);
            // Add a marker in Sydney and move the camera
            LatLng bayeux = new LatLng(-7.124000
                    , -34.920297 );
            if (LoginECITY.mlatLng !=null){
                mMap.moveCamera(CameraUpdateFactory.newLatLng(LoginECITY.mlatLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);

            }else{
                mMap.moveCamera(CameraUpdateFactory.newLatLng(bayeux));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(4));
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            }

            mMap.getUiSettings().setMapToolbarEnabled(true);

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.INTERNET,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    REQUEST_PERMISSION);
            if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.INTERNET)
                            != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                TastyToast.makeText(getContext(), "Permissões não concedidas", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.INTERNET,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                        REQUEST_PERMISSION);
            }



            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);



        } catch (SecurityException ex) {
            Log.e(TAG, "error", ex);
        }
        AdicionarTodosMarcadores(mMap);


        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {


                return null;
            }

            @Override
            public View getInfoContents(final Marker markerInfo) {
                if (ReferenciaMapa == "inicio") {

                    jota = null;
                    idUnico = markerInfo.getTag().toString();
                    String marcOUres = "Marcadores";
                    if (MainActivity.tipoMapa == "resolvido"){
                        marcOUres="Resolvidos";
                    }


                        if (markerInfo.getTitle().equals(getResources().getString(R.string.ele_fab1))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.ele_fab2))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.ele_fab3))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.ele_fab4))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.ele_fab5))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.ele_fab6))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.ele_fab7))) {

                            tipodeMapa = "ele";
                            criarLinearlayout(marcOUres+"/eletrico", markerInfo);

                        } else if (markerInfo.getTitle().equals(getResources().getString(R.string.agufab1))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.agufab2))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.agufab3))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.agufab4))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.agufab5))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.agufab6))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.agufab7))) {

                            tipodeMapa = "agu";
                            criarLinearlayout(marcOUres+"/agua", markerInfo);
                        } else if (markerInfo.getTitle().equals(getResources().getString(R.string.lix_fab1))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.lix_fab2))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.lix_fab3))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.lix_fab4))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.lix_fab5))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.lix_fab6))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.lix_fab7))) {

                            tipodeMapa = "lix";
                            criarLinearlayout(marcOUres+"/lixo", markerInfo);
                        } else if (markerInfo.getTitle().equals(getResources().getString(R.string.mob_fab1))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.mob_fab2))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.mob_fab3))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.mob_fab4))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.mob_fab5))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.mob_fab6))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.mob_fab7))) {

                            tipodeMapa = "mob";
                            criarLinearlayout(marcOUres+"/mobilidade", markerInfo);
                        } else if (markerInfo.getTitle().equals(getResources().getString(R.string.ger_fab1))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.ger_fab2))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.ger_fab3))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.ger_fab4))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.ger_fab5))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.ger_fab6))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.ger_fab7))){

                            tipodeMapa = "ger";
                            criarLinearlayout(marcOUres+"/infraGeral", markerInfo);
                        } else if (markerInfo.getTitle().equals(getResources().getString(R.string.sau_fab1))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.sau_fab2))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.sau_fab3))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.sau_fab4))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.sau_fab5))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.sau_fab6))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.sau_fab7))) {

                            tipodeMapa = "sau";
                            criarLinearlayout(marcOUres+"/saude", markerInfo);
                        } else if (markerInfo.getTitle().equals(getResources().getString(R.string.edu_fab1))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.edu_fab2))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.edu_fab3))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.edu_fab4))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.edu_fab5))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.edu_fab6))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.edu_fab7))) {

                            tipodeMapa = "edu";
                            criarLinearlayout(marcOUres+"/educacao", markerInfo);
                        } else if (markerInfo.getTitle().equals(getResources().getString(R.string.seg_fab1))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.seg_fab2))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.seg_fab3))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.seg_fab4))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.seg_fab5))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.seg_fab6))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.seg_fab7))) {

                            tipodeMapa = "seg";
                            criarLinearlayout(marcOUres+"/seguranca", markerInfo);
                        } else if (markerInfo.getTitle().equals(getResources().getString(R.string.laz_fab1))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.laz_fab2))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.laz_fab3))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.laz_fab4))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.laz_fab5))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.laz_fab6))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.laz_fab7))) {

                            tipodeMapa = "laz";
                            criarLinearlayout(marcOUres+"/lazer", markerInfo);
                        } else if (markerInfo.getTitle().equals(getResources().getString(R.string.amb_fab1))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.amb_fab2))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.amb_fab3))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.amb_fab4))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.amb_fab5))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.amb_fab6))
                                || markerInfo.getTitle().equals(getResources().getString(R.string.amb_fab7))) {

                            tipodeMapa = "amb";
                            criarLinearlayout(marcOUres+"/meioAmbiente", markerInfo);
                        }

                    return jota;

                } else {

                    jota = null;
                    idUnico = markerInfo.getTag().toString();

                    infosGeral = FirebaseDatabase.getInstance()
                            .getReference(ReferenciaMapa + "/" + idUnico);

                    infosGeral.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            mm = dataSnapshot.getValue(Marcadores.class);
                            tt = mm.getNome();
                            ss = mm.getProblema();
                            userId = mm.getUserUid();

                            String user2 = FirebaseAuth.getInstance().getCurrentUser()
                                    .getUid();
                            if (userId.equals(user2)) {


                            }
                            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                @Override
                                public void onInfoWindowClick(final Marker marker) {


                                    storageReference.child("Mapas")
                                            .child(MainActivity.tipoMapa)
                                            .child(markerInfo.getPosition().latitude + "_" + markerInfo.getPosition().longitude)
                                            .getDownloadUrl()
                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    string44 = uri.toString();
                                                    showPop(markerInfo);
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            string44 = "https://firebasestorage.googleapis.com/v0/b/ecity-030886.appspot.com/o/sem_foto.jpg?alt=media&token=48e522d5-7b57-4fe0-b5f4-dc43e9f2e6e5";
                                            showPop(markerInfo);
                                        }
                                    });

                                }
                            });

                            jota = new LinearLayout(getContext());

                            jota.setOrientation(LinearLayout.VERTICAL);

                            tit = new TextView(getContext());
                            snep = new TextView(getContext());

                            snep.setText(ss);

                            tit.setText(tt);
                            tit.setTextColor(Color.BLACK);
                            tit.setTypeface(null, Typeface.BOLD);
                            tit.setGravity(Gravity.CENTER);


                            jota.addView(tit);
                            jota.addView(snep);


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            TastyToast.makeText(getContext(), getResources().getString(R.string.cancelado), TastyToast.LENGTH_SHORT, TastyToast.WARNING).show();
                        }
                    });

                    return jota;
                }
            }
        });


    }

    private void criarLinearlayout(String refMapa, final Marker markerTodos) {
        infosGeral = FirebaseDatabase.getInstance()
                .getReference(refMapa + "/" + idUnico);

        infosGeral.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mm = dataSnapshot.getValue(Marcadores.class);
                tt = mm.getNome();
                ss = mm.getProblema();
                userId = mm.getUserUid();

                String user2 = FirebaseAuth.getInstance().getCurrentUser()
                        .getUid();
                if (userId.equals(user2)) {
                    // TastyToast.makeText(getContext(), "Pressione e segure para excluir",
                    //    TastyToast.LENGTH_SHORT).show();

                }
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(final Marker marker) {
                        String mapaStorage = "Mapas";
                        if (MainActivity.tipoMapa == "resolvido"){
                            mapaStorage = "Resolvidos";
                        }


                        storageReference.child(mapaStorage)
                                .child(tipodeMapa)
                                .child(markerTodos.getPosition().latitude + "_" + markerTodos.getPosition().longitude)
                                .getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        string44 = uri.toString();
                                        showPop(markerTodos);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                string44 = "https://firebasestorage.googleapis.com/v0/b/ecity-030886.appspot.com/o/sem_foto.jpg?alt=media&token=48e522d5-7b57-4fe0-b5f4-dc43e9f2e6e5";
                                showPop(markerTodos);
                            }
                        });

                    }
                });

                jota = new LinearLayout(getContext());

                jota.setOrientation(LinearLayout.VERTICAL);

                tit = new TextView(getContext());
                snep = new TextView(getContext());

                snep.setText(ss);

                tit.setText(tt);
                tit.setTextColor(Color.BLACK);
                tit.setTypeface(null, Typeface.BOLD);
                tit.setGravity(Gravity.CENTER);


                jota.addView(tit);
                jota.addView(snep);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                TastyToast.makeText(getContext(), getResources().getString(R.string.cancelado), TastyToast.LENGTH_SHORT, TastyToast.WARNING).show();
            }
        });
    }


    @Override
    public void onMapClick(LatLng latLng) {
        Date dataGeral = new Date();
        String data = formatodata.format(dataGeral);


        switch (MainActivity.tipoMapa) {
            case "ele":

                switch (MainActivity.tipoMarcador) {
                    case 0:
                        break;
                    case 1:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });

                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/eletrico");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                1, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 2:

                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/eletrico");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                2, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 3:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/eletrico");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                3, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 4:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/eletrico");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                4, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 5:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/eletrico");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                5, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 6:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/eletrico");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                6, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 7:
                        break;

                }

                break;


            case "agu":

                switch (MainActivity.tipoMarcador) {
                    case 0:
                        break;
                    case 1:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/agua");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                1, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 2:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/agua");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                2, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 3:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/agua");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                3, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 4:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/agua");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                4, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 5:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/agua");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                5, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;

                        break;
                    case 6:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/agua");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                6, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 7:
                        break;

                }

                break;

            case "lix":

                switch (MainActivity.tipoMarcador) {
                    case 0:
                        break;
                    case 1:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/lixo");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                1, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 2:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/lixo");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                2, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 3:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/lixo");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                3, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 4:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/lixo");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                4, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 5:

                        break;
                    case 6:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/lixo");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                6, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 7:
                        break;

                }

                break;

            case "mob":

                switch (MainActivity.tipoMarcador) {
                    case 0:
                        break;
                    case 1:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/mobilidade");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                1, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 2:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/mobilidade");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                2, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 3:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/mobilidade");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                3, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 4:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/mobilidade");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                4, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 5:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/mobilidade");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                5, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;

                        break;
                    case 6:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/mobilidade");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                6, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 7:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/mobilidade");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                7, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;

                        break;

                }

                break;

            case "ger":

                switch (MainActivity.tipoMarcador) {
                    case 0:
                        break;
                    case 1:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/infraGeral");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                1, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 2:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/infraGeral");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                2, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/infraGeral");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                6, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 7:
                        break;
                }

            case "sau":

                switch (MainActivity.tipoMarcador) {
                    case 0:
                        break;
                    case 1:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/saude");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                1, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 2:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/saude");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                2, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 3:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/saude");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                3, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 4:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/saude");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                4, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 5:

                        break;
                    case 6:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/saude");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                6, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 7:
                        break;
                }

            case "edu":

                switch (MainActivity.tipoMarcador) {
                    case 0:
                        break;
                    case 1:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/educacao");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                1, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 2:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/educacao");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                2, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 3:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/educacao");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                3, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 4:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/educacao");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                4, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 5:

                        break;
                    case 6:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/educacao");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                6, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 7:
                        break;
                }

            case "seg":

                switch (MainActivity.tipoMarcador) {
                    case 0:
                        break;
                    case 1:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/seguranca");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                1, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 2:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/seguranca");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                2, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 3:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/seguranca");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                3, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 4:
                        break;
                    case 5:

                        break;
                    case 6:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/seguranca");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                6, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 7:
                        break;
                }

            case "laz":

                switch (MainActivity.tipoMarcador) {
                    case 0:
                        break;
                    case 1:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/lazer");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                1, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 2:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/lazer");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                2, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 3:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/lazer");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                3, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 4:
                        break;
                    case 5:

                        break;
                    case 6:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/lazer");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                6, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 7:
                        break;
                }

            case "amb":

                switch (MainActivity.tipoMarcador) {
                    case 0:
                        break;
                    case 1:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/meioAmbiente");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                1, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 2:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/meioAmbiente");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                2, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 3:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/meioAmbiente");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                3, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 4:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/meioAmbiente");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                4, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 5:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/meioAmbiente");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                5, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;
                        break;
                    case 6:
                        if (MainActivity.galeria == 1) {
                            mProgressDialog.setMessage(getResources().getString(R.string.salvando_foto));
                            mProgressDialog.show();
                            MainActivity.filePath = storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //Toast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG).show();
                                           // mProgressDialog.dismiss();

                                        }
                                    });
                            MainActivity.filePath = storageReference.child("Resolvidos")
                                    .child(MainActivity.tipoMapa).child(latLng.latitude + "_" + latLng.longitude);
                            MainActivity.filePath.putFile(MainActivity.uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();
                                            mProgressDialog.dismiss();

                                        }
                                    });
                        }
                        MainActivity.galeria = 0;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Marcadores/meioAmbiente");
                        marcadores = new Marcadores(MainActivity.descriMarker,
                                MainActivity.descri,
                                latLng.latitude,
                                latLng.longitude,
                                6, mUid, data);
                        mDatabase.push().setValue(marcadores);
                        MainActivity.tipoMarcador = 0;


                        break;
                    case 7:
                        break;
                }

        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int grantResults[]) {
        switch (requestCode) {
            case REQUEST_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    TastyToast.makeText(getContext(), getResources().getString(R.string.autorizado), TastyToast.LENGTH_SHORT, TastyToast.INFO).show();


                } else {
                    TastyToast.makeText(getContext(), getResources().getString(R.string.permi_negada), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                }
                return;
            }
        }
    }


    public void AdicionarTodosMarcadores(GoogleMap map) {

      txtCriado = new String(getResources().getString(R.string.criado));



        mMap.clear();

        String probOUsolu = "Marcadores";
        if (MainActivity.tipoMapa == "resolvido"){
            probOUsolu = "Resolvidos";
            txtCriado = "";
        }

        ReferenciaMapa = probOUsolu+"/eletrico";
        mMarcaRef = FirebaseDatabase.getInstance().getReference(ReferenciaMapa);

        mChildEventListener = mMarcaRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                keyChild = dataSnapshot.getKey();



                Marcadores marcadores = dataSnapshot.getValue(Marcadores.class);
                txtData = marcadores.data;
                if(MainActivity.tipoMapa=="resolvido"){
                    txtData="";
                }

                String nome = marcadores.getNome();
                String problemas = marcadores.getProblema()+"\n"+txtCriado+txtData;
                double lat = marcadores.getLatitude();
                double lon = marcadores.getLongitude();
                LatLng latLng = new LatLng(lat, lon);
                int icon = marcadores.getEndIcon();


                switch (icon) {

                    case 1:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_poste))


                        ).setTag(keyChild);
                               /* storageReference = FirebaseStorage.getInstance().getReference()
                                        .child("Mapas").child("ele").child(latLng.toString());

                                MainActivity.filePath = storageReference.child("Mapas").child("ele").child(latLng.toString());
                                MainActivity.filePath.putFile(MainActivity.uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        TastyToast.makeText(getContext(), getResources().getString(R.string.foto_salva), TastyToast.LENGTH_LONG,
                                                    TastyToast.SUCCESS).show();


                                    }
                                });
*/
                        break;
                    case 2:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_falta_energia))

                        ).setTag(keyChild);
                        break;
                    case 3:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_ilumi_publica))

                        ).setTag(keyChild);
                        break;
                    case 4:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_fio_solto))

                        ).setTag(keyChild);
                        break;
                    case 5:
                        break;
                    case 6:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_outros_ele))

                        ).setTag(keyChild);
                        break;
                    case 7:
                        break;

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {


            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        ReferenciaMapa = probOUsolu+"/agua";
        mMarcaRef = FirebaseDatabase.getInstance().getReference(ReferenciaMapa);

        mChildEventListener = mMarcaRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                keyChild = dataSnapshot.getKey();

                Marcadores marcadores = dataSnapshot.getValue(Marcadores.class);
                txtData = marcadores.data;
                if(MainActivity.tipoMapa=="resolvido"){
                    txtData="";
                }

                String nome = marcadores.getNome();
                String problemas = marcadores.getProblema()+"\n"+txtCriado+txtData;
                double lat = marcadores.getLatitude();
                double lon = marcadores.getLongitude();
                LatLng latLng = new LatLng(lat, lon);
                int icon = marcadores.getEndIcon();

                switch (icon) {

                    case 1:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_agua_suja))


                        ).setTag(keyChild);
                        break;
                    case 2:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_falta_dagua))

                        ).setTag(keyChild);
                        break;
                    case 3:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_esgoto))

                        );
                        break;
                    case 4:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_inund))

                        ).setTag(keyChild);
                        break;
                    case 5:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_vaza))

                        ).setTag(keyChild);
                        break;
                    case 6:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_outros_agua))

                        ).setTag(keyChild);
                        break;
                    case 7:
                        break;

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {


            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        ReferenciaMapa = probOUsolu+"/lixo";
        mMarcaRef = FirebaseDatabase.getInstance().getReference(ReferenciaMapa);

        mChildEventListener = mMarcaRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                keyChild = dataSnapshot.getKey();

                Marcadores marcadores = dataSnapshot.getValue(Marcadores.class);
                txtData = marcadores.data;
                if(MainActivity.tipoMapa=="resolvido"){
                    txtData="";
                }

                String nome = marcadores.getNome();
                String problemas = marcadores.getProblema()+"\n"+txtCriado+txtData;
                double lat = marcadores.getLatitude();
                double lon = marcadores.getLongitude();
                LatLng latLng = new LatLng(lat, lon);
                int icon = marcadores.getEndIcon();

                switch (icon) {

                    case 1:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_lixeira))


                        ).setTag(keyChild);
                        break;
                    case 2:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_amb_lixo))

                        ).setTag(keyChild);
                        break;
                    case 3:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_publico_higiene))

                        ).setTag(keyChild);
                        break;
                    case 4:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_baldio))

                        ).setTag(keyChild);
                        break;
                    case 5:
                        break;
                    case 6:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_outros_lix))

                        ).setTag(keyChild);
                        break;
                    case 7:
                        break;

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        ReferenciaMapa = probOUsolu+"/mobilidade";
        mMarcaRef = FirebaseDatabase.getInstance().getReference(ReferenciaMapa);

        mChildEventListener = mMarcaRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                keyChild = dataSnapshot.getKey();

                Marcadores marcadores = dataSnapshot.getValue(Marcadores.class);
                txtData = marcadores.data;
                if(MainActivity.tipoMapa=="resolvido"){
                    txtData="";
                }

                String nome = marcadores.getNome();
                String problemas = marcadores.getProblema()+"\n"+txtCriado+txtData;
                double lat = marcadores.getLatitude();
                double lon = marcadores.getLongitude();
                LatLng latLng = new LatLng(lat, lon);
                int icon = marcadores.getEndIcon();

                switch (icon) {

                    case 1:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_semaforo))


                        ).setTag(keyChild);
                        break;
                    case 2:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_faixa))

                        ).setTag(keyChild);
                        break;
                    case 3:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_buraco_pista))

                        ).setTag(keyChild);
                        break;
                    case 4:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_bueiro))

                        ).setTag(keyChild);
                        break;
                    case 5:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_placa))

                        ).setTag(keyChild);
                        break;
                    case 6:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_parada))

                        ).setTag(keyChild);
                        break;
                    case 7:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_outros_mob))
                        ).setTag(keyChild);
                        break;

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        ReferenciaMapa = probOUsolu+"/infraGeral";
        mMarcaRef = FirebaseDatabase.getInstance().getReference(ReferenciaMapa);

        mChildEventListener = mMarcaRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                keyChild = dataSnapshot.getKey();

                Marcadores marcadores = dataSnapshot.getValue(Marcadores.class);
                txtData = marcadores.data;
                if(MainActivity.tipoMapa=="resolvido"){
                    txtData="";
                }

                String nome = marcadores.getNome();
                String problemas = marcadores.getProblema()+"\n"+txtCriado+txtData;
                double lat = marcadores.getLatitude();
                double lon = marcadores.getLongitude();
                LatLng latLng = new LatLng(lat, lon);
                int icon = marcadores.getEndIcon();

                switch (icon) {

                    case 1:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_patrimonio))


                        ).setTag(keyChild);
                        break;
                    case 2:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_calcada))

                        ).setTag(keyChild);
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_outros_geral))

                        ).setTag(keyChild);
                        break;
                    case 7:
                        break;

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        ReferenciaMapa = probOUsolu+"/saude";
        mMarcaRef = FirebaseDatabase.getInstance().getReference(ReferenciaMapa);

        mChildEventListener = mMarcaRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                keyChild = dataSnapshot.getKey();

                Marcadores marcadores = dataSnapshot.getValue(Marcadores.class);
                txtData = marcadores.data;
                if(MainActivity.tipoMapa=="resolvido"){
                    txtData="";
                }

                String nome = marcadores.getNome();
                String problemas = marcadores.getProblema()+"\n"+txtCriado+txtData;
                double lat = marcadores.getLatitude();
                double lon = marcadores.getLongitude();
                LatLng latLng = new LatLng(lat, lon);
                int icon = marcadores.getEndIcon();

                switch (icon) {

                    case 1:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_acess_saude))


                        ).setTag(keyChild);
                        break;
                    case 2:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_sem_medicos))

                        ).setTag(keyChild);
                        break;
                    case 3:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_sem_medicamentos))

                        ).setTag(keyChild);
                        break;
                    case 4:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_lotado))

                        ).setTag(keyChild);
                        break;
                    case 5:
                        break;
                    case 6:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_outros_sau))

                        ).setTag(keyChild);
                        break;
                    case 7:
                        break;

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        ReferenciaMapa = probOUsolu+"/educacao";
        mMarcaRef = FirebaseDatabase.getInstance().getReference(ReferenciaMapa);

        mChildEventListener = mMarcaRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                keyChild = dataSnapshot.getKey();

                Marcadores marcadores = dataSnapshot.getValue(Marcadores.class);
                txtData = marcadores.data;
                if(MainActivity.tipoMapa=="resolvido"){
                    txtData="";
                }

                String nome = marcadores.getNome();
                String problemas = marcadores.getProblema()+"\n"+txtCriado+txtData;
                double lat = marcadores.getLatitude();
                double lon = marcadores.getLongitude();
                LatLng latLng = new LatLng(lat, lon);
                int icon = marcadores.getEndIcon();

                switch (icon) {

                    case 1:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_escola_acess))


                        ).setTag(keyChild);
                        break;
                    case 2:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_sem_merenda))

                        ).setTag(keyChild);
                        break;
                    case 3:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_sem_aulas))

                        ).setTag(keyChild);
                        break;
                    case 4:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_sem_prof))

                        ).setTag(keyChild);
                        break;
                    case 5:
                        break;
                    case 6:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_outros_edu))

                        ).setTag(keyChild);
                        break;
                    case 7:
                        break;

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ReferenciaMapa = probOUsolu+"/seguranca";
        mMarcaRef = FirebaseDatabase.getInstance().getReference(ReferenciaMapa);

        mChildEventListener = mMarcaRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                keyChild = dataSnapshot.getKey();

                Marcadores marcadores = dataSnapshot.getValue(Marcadores.class);
                txtData = marcadores.data;
                if(MainActivity.tipoMapa=="resolvido"){
                    txtData="";
                }

                String nome = marcadores.getNome();
                String problemas = marcadores.getProblema()+"\n"+txtCriado+txtData;
                double lat = marcadores.getLatitude();
                double lon = marcadores.getLongitude();
                LatLng latLng = new LatLng(lat, lon);
                int icon = marcadores.getEndIcon();

                switch (icon) {

                    case 1:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_assalto))


                        ).setTag(keyChild);
                        break;
                    case 2:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_ronda))

                        ).setTag(keyChild);
                        break;
                    case 3:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_rua_perigosa))

                        ).setTag(keyChild);
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_outros_seg))

                        ).setTag(keyChild);
                        break;
                    case 7:
                        break;

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        ReferenciaMapa = probOUsolu+"/lazer";
        mMarcaRef = FirebaseDatabase.getInstance().getReference(ReferenciaMapa);

        mChildEventListener = mMarcaRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                keyChild = dataSnapshot.getKey();

                Marcadores marcadores = dataSnapshot.getValue(Marcadores.class);
                txtData = marcadores.data;
                if(MainActivity.tipoMapa=="resolvido"){
                    txtData="";
                }

                String nome = marcadores.getNome();
                String problemas = marcadores.getProblema()+"\n"+txtCriado+txtData;
                double lat = marcadores.getLatitude();
                double lon = marcadores.getLongitude();
                LatLng latLng = new LatLng(lat, lon);
                int icon = marcadores.getEndIcon();

                switch (icon) {

                    case 1:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_praia_suja))


                        ).setTag(keyChild);
                        break;
                    case 2:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_praca_degradada))

                        ).setTag(keyChild);
                        break;
                    case 3:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_sem_praca))

                        ).setTag(keyChild);
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_outros_laz))

                        ).setTag(keyChild);
                        break;
                    case 7:
                        break;

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        ReferenciaMapa = probOUsolu+"/meioAmbiente";
        mMarcaRef = FirebaseDatabase.getInstance().getReference(ReferenciaMapa);

        mChildEventListener = mMarcaRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                keyChild = dataSnapshot.getKey();

                Marcadores marcadores = dataSnapshot.getValue(Marcadores.class);
                txtData = marcadores.data;
                if(MainActivity.tipoMapa=="resolvido"){
                    txtData="";
                }

                String nome = marcadores.getNome();
                String problemas = marcadores.getProblema()+"\n"+txtCriado+txtData;
                double lat = marcadores.getLatitude();
                double lon = marcadores.getLongitude();
                LatLng latLng = new LatLng(lat, lon);
                int icon = marcadores.getEndIcon();

                switch (icon) {

                    case 1:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_queimadas))


                        ).setTag(keyChild);
                        break;
                    case 2:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_desmatamento))

                        ).setTag(keyChild);
                        break;
                    case 3:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_invasao))

                        ).setTag(keyChild);
                        break;
                    case 4:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_captura))

                        ).setTag(keyChild);
                        break;
                    case 5:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_praga))

                        ).setTag(keyChild);
                        break;
                    case 6:
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(nome)
                                .snippet(problemas)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_outros_meioambi))

                        ).setTag(keyChild);
                        break;
                    case 7:
                        break;

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ReferenciaMapa = "inicio";


    }

    public void AdicionarMarcador(GoogleMap map) {


        switch (MainActivity.tipoMapa) {
            case "ele":
                mMap.clear();
                ReferenciaMapa = "Marcadores/eletrico";
                mMarcaRef = FirebaseDatabase.getInstance().getReference(ReferenciaMapa);

                mChildEventListener = mMarcaRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        keyChild = dataSnapshot.getKey();

                        Marcadores marcadores = dataSnapshot.getValue(Marcadores.class);

                        String nome = marcadores.getNome();
                        String problemas = marcadores.getProblema()+"\n"+getResources().getString(R.string.criado)+marcadores.data;
                        double lat = marcadores.getLatitude();
                        double lon = marcadores.getLongitude();
                        LatLng latLng = new LatLng(lat, lon);
                        int icon = marcadores.getEndIcon();

                        switch (icon) {

                            case 1:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_poste))


                                ).setTag(keyChild);
                                break;
                            case 2:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_falta_energia))

                                ).setTag(keyChild);
                                break;
                            case 3:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_ilumi_publica))

                                ).setTag(keyChild);
                                break;
                            case 4:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_fio_solto))

                                ).setTag(keyChild);
                                break;
                            case 5:

                                break;
                            case 6:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_outros_ele))

                                ).setTag(keyChild);
                                break;
                            case 7:
                                break;

                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {


                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;

            case "agu":
                mMap.clear();
                ReferenciaMapa = "Marcadores/agua";
                mMarcaRef = FirebaseDatabase.getInstance().getReference(ReferenciaMapa);

                mChildEventListener = mMarcaRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        keyChild = dataSnapshot.getKey();
                        Marcadores marcadores = dataSnapshot.getValue(Marcadores.class);

                        String nome = marcadores.getNome();
                        String problemas = marcadores.getProblema()+"\n"+getResources().getString(R.string.criado)+marcadores.data;
                        double lat = marcadores.getLatitude();
                        double lon = marcadores.getLongitude();
                        LatLng latLng = new LatLng(lat, lon);
                        int icon = marcadores.getEndIcon();

                        switch (icon) {

                            case 1:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_agua_suja))

                                ).setTag(keyChild);
                                break;
                            case 2:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_falta_dagua))

                                ).setTag(keyChild);
                                break;
                            case 3:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_esgoto))

                                ).setTag(keyChild);
                                break;
                            case 4:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_inund))

                                ).setTag(keyChild);
                                break;
                            case 5:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_vaza))

                                ).setTag(keyChild);
                                break;
                            case 6:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_outros_agua))

                                ).setTag(keyChild);
                                break;
                            case 7:
                                break;

                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {


                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;

            case "lix":
                mMap.clear();
                ReferenciaMapa = "Marcadores/lixo";
                mMarcaRef = FirebaseDatabase.getInstance().getReference(ReferenciaMapa);

                mChildEventListener = mMarcaRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        keyChild = dataSnapshot.getKey();

                        Marcadores marcadores = dataSnapshot.getValue(Marcadores.class);

                        String nome = marcadores.getNome();
                        String problemas = marcadores.getProblema()+"\n"+getResources().getString(R.string.criado)+marcadores.data;
                        double lat = marcadores.getLatitude();
                        double lon = marcadores.getLongitude();
                        LatLng latLng = new LatLng(lat, lon);
                        int icon = marcadores.getEndIcon();

                        switch (icon) {

                            case 1:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_lixeira))


                                ).setTag(keyChild);
                                break;
                            case 2:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_amb_lixo))

                                ).setTag(keyChild);
                                break;
                            case 3:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_publico_higiene))

                                ).setTag(keyChild);
                                break;
                            case 4:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_baldio))

                                ).setTag(keyChild);
                                break;
                            case 5:
                                break;
                            case 6:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_outros_lix))

                                ).setTag(keyChild);
                                break;
                            case 7:
                                break;

                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;

            case "mob":
                mMap.clear();
                ReferenciaMapa = "Marcadores/mobilidade";
                mMarcaRef = FirebaseDatabase.getInstance().getReference(ReferenciaMapa);

                mChildEventListener = mMarcaRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        keyChild = dataSnapshot.getKey();

                        Marcadores marcadores = dataSnapshot.getValue(Marcadores.class);

                        String nome = marcadores.getNome();
                        String problemas = marcadores.getProblema()+"\n"+getResources().getString(R.string.criado)+marcadores.data;
                        double lat = marcadores.getLatitude();
                        double lon = marcadores.getLongitude();
                        LatLng latLng = new LatLng(lat, lon);
                        int icon = marcadores.getEndIcon();

                        switch (icon) {

                            case 1:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_semaforo))


                                ).setTag(keyChild);
                                break;
                            case 2:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_faixa))

                                ).setTag(keyChild);
                                break;
                            case 3:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_buraco_pista))

                                ).setTag(keyChild);
                                break;
                            case 4:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_bueiro))

                                ).setTag(keyChild);
                                break;
                            case 5:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_placa))

                                ).setTag(keyChild);
                                break;
                            case 6:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_parada))

                                ).setTag(keyChild);
                                break;
                            case 7:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_outros_mob))
                                ).setTag(keyChild);
                                ;
                                break;

                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;

            case "ger":
                mMap.clear();
                ReferenciaMapa = "Marcadores/infraGeral";
                mMarcaRef = FirebaseDatabase.getInstance().getReference(ReferenciaMapa);

                mChildEventListener = mMarcaRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        keyChild = dataSnapshot.getKey();

                        Marcadores marcadores = dataSnapshot.getValue(Marcadores.class);

                        String nome = marcadores.getNome();
                        String problemas = marcadores.getProblema()+"\n"+getResources().getString(R.string.criado)+marcadores.data;
                        double lat = marcadores.getLatitude();
                        double lon = marcadores.getLongitude();
                        LatLng latLng = new LatLng(lat, lon);
                        int icon = marcadores.getEndIcon();

                        switch (icon) {

                            case 1:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_patrimonio))


                                ).setTag(keyChild);
                                break;
                            case 2:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_calcada))

                                ).setTag(keyChild);
                                break;
                            case 3:
                                break;
                            case 4:
                                break;
                            case 5:
                                break;
                            case 6:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_outros_geral))

                                ).setTag(keyChild);
                                break;
                            case 7:
                                break;

                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;

            case "sau":
                mMap.clear();
                ReferenciaMapa = "Marcadores/saude";
                mMarcaRef = FirebaseDatabase.getInstance().getReference(ReferenciaMapa);

                mChildEventListener = mMarcaRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        keyChild = dataSnapshot.getKey();

                        Marcadores marcadores = dataSnapshot.getValue(Marcadores.class);

                        String nome = marcadores.getNome();
                        String problemas = marcadores.getProblema()+"\n"+getResources().getString(R.string.criado)+marcadores.data;
                        double lat = marcadores.getLatitude();
                        double lon = marcadores.getLongitude();
                        LatLng latLng = new LatLng(lat, lon);
                        int icon = marcadores.getEndIcon();

                        switch (icon) {

                            case 1:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_acess_saude))


                                ).setTag(keyChild);
                                break;
                            case 2:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_sem_medicos))

                                ).setTag(keyChild);
                                break;
                            case 3:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_sem_medicamentos))

                                ).setTag(keyChild);
                                break;
                            case 4:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_lotado))

                                ).setTag(keyChild);
                                break;
                            case 5:
                                break;
                            case 6:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_outros_sau))

                                ).setTag(keyChild);
                                break;
                            case 7:
                                break;

                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;

            case "edu":
                mMap.clear();
                ReferenciaMapa = "Marcadores/educacao";
                mMarcaRef = FirebaseDatabase.getInstance().getReference(ReferenciaMapa);

                mChildEventListener = mMarcaRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        keyChild = dataSnapshot.getKey();

                        Marcadores marcadores = dataSnapshot.getValue(Marcadores.class);

                        String nome = marcadores.getNome();
                        String problemas = marcadores.getProblema()+"\n"+getResources().getString(R.string.criado)+marcadores.data;
                        double lat = marcadores.getLatitude();
                        double lon = marcadores.getLongitude();
                        LatLng latLng = new LatLng(lat, lon);
                        int icon = marcadores.getEndIcon();

                        switch (icon) {

                            case 1:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_escola_acess))


                                ).setTag(keyChild);
                                break;
                            case 2:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_sem_merenda))

                                ).setTag(keyChild);
                                break;
                            case 3:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_sem_aulas))

                                ).setTag(keyChild);
                                break;
                            case 4:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_sem_prof))

                                ).setTag(keyChild);
                                break;
                            case 5:
                                break;
                            case 6:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_outros_edu))

                                ).setTag(keyChild);
                                break;
                            case 7:
                                break;

                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;

            case "seg":
                mMap.clear();
                ReferenciaMapa = "Marcadores/seguranca";
                mMarcaRef = FirebaseDatabase.getInstance().getReference(ReferenciaMapa);

                mChildEventListener = mMarcaRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        keyChild = dataSnapshot.getKey();

                        Marcadores marcadores = dataSnapshot.getValue(Marcadores.class);

                        String nome = marcadores.getNome();
                        String problemas = marcadores.getProblema()+"\n"+getResources().getString(R.string.criado)+marcadores.data;
                        double lat = marcadores.getLatitude();
                        double lon = marcadores.getLongitude();
                        LatLng latLng = new LatLng(lat, lon);
                        int icon = marcadores.getEndIcon();

                        switch (icon) {

                            case 1:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_assalto))


                                ).setTag(keyChild);
                                break;
                            case 2:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_ronda))

                                ).setTag(keyChild);
                                break;
                            case 3:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_rua_perigosa))

                                ).setTag(keyChild);
                                break;
                            case 4:
                                break;
                            case 5:
                                break;
                            case 6:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_outros_seg))

                                ).setTag(keyChild);
                                break;
                            case 7:
                                break;

                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;

            case "laz":
                mMap.clear();
                ReferenciaMapa = "Marcadores/lazer";
                mMarcaRef = FirebaseDatabase.getInstance().getReference(ReferenciaMapa);

                mChildEventListener = mMarcaRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        keyChild = dataSnapshot.getKey();

                        Marcadores marcadores = dataSnapshot.getValue(Marcadores.class);

                        String nome = marcadores.getNome();
                        String problemas = marcadores.getProblema()+"\n"+getResources().getString(R.string.criado)+marcadores.data;
                        double lat = marcadores.getLatitude();
                        double lon = marcadores.getLongitude();
                        LatLng latLng = new LatLng(lat, lon);
                        int icon = marcadores.getEndIcon();

                        switch (icon) {

                            case 1:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_praia_suja))


                                ).setTag(keyChild);
                                break;
                            case 2:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_praca_degradada))

                                ).setTag(keyChild);
                                break;
                            case 3:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_sem_praca))

                                ).setTag(keyChild);
                                break;
                            case 4:
                                break;
                            case 5:
                                break;
                            case 6:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_outros_laz))

                                ).setTag(keyChild);
                                break;
                            case 7:
                                break;

                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;

            case "amb":
                mMap.clear();
                ReferenciaMapa = "Marcadores/meioAmbiente";
                mMarcaRef = FirebaseDatabase.getInstance().getReference(ReferenciaMapa);

                mChildEventListener = mMarcaRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        keyChild = dataSnapshot.getKey();

                        Marcadores marcadores = dataSnapshot.getValue(Marcadores.class);

                        String nome = marcadores.getNome();
                        String problemas = marcadores.getProblema()+"\n"+getResources().getString(R.string.criado)+marcadores.data;
                        double lat = marcadores.getLatitude();
                        double lon = marcadores.getLongitude();
                        LatLng latLng = new LatLng(lat, lon);
                        int icon = marcadores.getEndIcon();

                        switch (icon) {

                            case 1:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_queimadas))


                                ).setTag(keyChild);
                                break;
                            case 2:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_desmatamento))

                                ).setTag(keyChild);
                                break;
                            case 3:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_invasao))

                                ).setTag(keyChild);
                                break;
                            case 4:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_captura))

                                ).setTag(keyChild);
                                break;
                            case 5:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_praga))

                                ).setTag(keyChild);
                                break;
                            case 6:
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(nome)
                                        .snippet(problemas)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.m_outros_meioambi))

                                ).setTag(keyChild);
                                break;
                            case 7:
                                break;

                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;

            case "nada":
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        return null;
    }

    public void showPop(final Marker marker) {


        //   TastyToast.makeText(getContext(), string44, TastyToast.LENGTH_LONG).show();


        View vv = View.inflate(getContext(), R.layout.pop_dados_marker, null);
        final ImageView imageMarker = (ImageView) vv.findViewById(R.id.fotoMarker);
        TextView textTitulo = (TextView) vv.findViewById(R.id.tituloMarker);
        TextView descriMarker = (TextView) vv.findViewById(R.id.descriMaker);
        Button btnMarker = (Button) vv.findViewById(R.id.buttonMarker);
        Button btnDeletar = (Button) vv.findViewById(R.id.deletar);
        TextView latTxt = (TextView) vv.findViewById(R.id.latTxt);
        TextView lonTxt = (TextView) vv.findViewById(R.id.longTxt);
        Button btnSolved = (Button) vv.findViewById(R.id.btnResolvido);
        Button btnDenunciar = (Button)vv.findViewById(R.id.btnDenucia);
        LinearLayout linearDados =(LinearLayout) vv.findViewById(R.id.linearDados);

        switch (MainActivity.tipoMapa) {
            case "ele":
                linearDados.setBackgroundColor(getResources().getColor(R.color.elePressed));
                break;
            case "agu":
                linearDados.setBackgroundColor(getResources().getColor(R.color.aguPressed));
                break;
            case "lix":
                linearDados.setBackgroundColor(getResources().getColor(R.color.lixPressed));
                break;
            case "mob":
                linearDados.setBackgroundColor(getResources().getColor(R.color.mobPressed));
                break;
            case "ger":
                linearDados.setBackgroundColor(getResources().getColor(R.color.gerPressed));
                break;
            case "sau":
                linearDados.setBackgroundColor(getResources().getColor(R.color.sauPressed));
                break;
            case "edu":
                linearDados.setBackgroundColor(getResources().getColor(R.color.eduPressed));
                break;
            case "seg":
                linearDados.setBackgroundColor(getResources().getColor(R.color.segPressed));
                break;
            case "laz":
                linearDados.setBackgroundColor(getResources().getColor(R.color.lazPressed));
                break;
            case "amb":
                linearDados.setBackgroundColor(getResources().getColor(R.color.ambPressed));
                break;

        }


        latTxt.setText("Lat: " + marker.getPosition().latitude);
        lonTxt.setText("Lon: " + marker.getPosition().longitude);
        textTitulo.setText(marker.getTitle());
        descriMarker.setText(marker.getSnippet());
        Glide.with(getContext())
                .load(string44)
                .into(imageMarker);
        final PopupWindow dadosMarker = new PopupWindow(vv
                , RelativeLayout.LayoutParams.WRAP_CONTENT
                , RelativeLayout.LayoutParams.WRAP_CONTENT);
        //dadosMarker.setContentView(imageMarker);

        dadosMarker.showAtLocation(vv, Gravity.CENTER, 0, 0);


        btnMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dadosMarker.dismiss();
                //    TastyToast.makeText(getContext(), string44, TastyToast.LENGTH_LONG).show();
            }
        });
        final String user = FirebaseAuth.getInstance().getCurrentUser()
                .getUid();
        if (userId.equals(user)) {
            btnDeletar.setVisibility(View.VISIBLE);
            btnSolved.setVisibility(View.VISIBLE);
        }
        if (MainActivity.tipoMapa=="resolvido"){
            btnDeletar.setVisibility(View.GONE);
            //btnDenunciar.setVisibility(View.GONE);
            btnSolved.setVisibility(View.GONE);
        }

        btnSolved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dadosMarker.dismiss();
                if (MainActivity.tipoMapa == "problema") {
                    String refMarcadores = "Marcadores/";
                    switch (tipodeMapa) {
                        case "ele":
                            refExcluir = refMarcadores + "eletrico";
                            break;
                        case "agu":
                            refExcluir = refMarcadores + "agua";
                            break;
                        case "lix":
                            refExcluir = refMarcadores + "lixo";
                            break;
                        case "mob":
                            refExcluir = refMarcadores + "mobilidade";
                            break;
                        case "ger":
                            refExcluir = refMarcadores + "nfraGeral";
                            break;
                        case "sau":
                            refExcluir = refMarcadores + "saude";
                            break;
                        case "edu":
                            refExcluir = refMarcadores + "educacao";
                            break;
                        case "seg":
                            refExcluir = refMarcadores + "seguranca";
                            break;
                        case "laz":
                            refExcluir = refMarcadores + "lazer";
                            break;
                        case "amb":
                            refExcluir = refMarcadores + "meioAmbiente";
                            break;
                    }
                }

                final EditText editSolved = new EditText(getContext());
                editSolved.setHint(getResources().getString(R.string.hint_problem_solved));
                final AlertDialog.Builder excMarker = new AlertDialog.Builder(getContext());
                excMarker.setTitle(getResources().getString(R.string.title_problem_solved))
                        .setMessage(getResources().getString(R.string.msg_problem_solved))
                        .setView(editSolved);

                excMarker.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        if(MainActivity.tipoMapa == "problema") {
                            dataMarkerSolved = FirebaseDatabase
                                    .getInstance()
                                    .getReference(refExcluir);
                        }else {
                            dataMarkerSolved = FirebaseDatabase
                                    .getInstance()
                                    .getReference(ReferenciaMapa);
                        }

                        keyChild = infosGeral.getKey();
                        DatabaseReference Marca = dataMarkerSolved.child(keyChild);
                        makerSolvedSave(marker, editSolved.getText().toString());
                        Marca.removeValue();
                        marker.remove();
                        dadosMarker.dismiss();

                        AdicionarMarcador(mMap);
                    }
                });
                excMarker.setNegativeButton(getResources().getString(R.string.not), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                excMarker.show();
            }
        });

        btnDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dadosMarker.dismiss();

                final AlertDialog.Builder excMarker = new AlertDialog.Builder(getContext());
                excMarker.setTitle(getResources().getString(R.string.title_maker_delete))
                        .setMessage(getResources().getString(R.string.msg_marker_delete));
                excMarker.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DatabaseReference dataMarker = FirebaseDatabase
                                .getInstance()
                                .getReference(ReferenciaMapa);

                        keyChild = infosGeral.getKey();


                        if (MainActivity.tipoMapa=="problema"){

                            switch (tipodeMapa){
                                case "ele":
                                    dataMarker = FirebaseDatabase.getInstance().getReference("Marcadores/eletrico");
                                    break;
                                case "agu":
                                    dataMarker = FirebaseDatabase.getInstance().getReference("Marcadoresagua");
                                    break;
                                case "lix":
                                    dataMarker = FirebaseDatabase.getInstance().getReference("Marcadores/lixo");
                                    break;
                                case "mob":
                                    dataMarker = FirebaseDatabase.getInstance().getReference("Marcadores/mobilidade");
                                    break;
                                case "ger":
                                    dataMarker = FirebaseDatabase.getInstance().getReference("Marcadores/infraGeral");
                                    break;
                                case "sau":
                                    dataMarker = FirebaseDatabase.getInstance().getReference("Marcadores/saude");
                                    break;
                                case "edu":
                                    dataMarker = FirebaseDatabase.getInstance().getReference("Marcadores/educacao");
                                    break;
                                case "seg":
                                    dataMarker = FirebaseDatabase.getInstance().getReference("Marcadores/seguranca");
                                    break;
                                case "laz":
                                    dataMarker = FirebaseDatabase.getInstance().getReference("Marcadores/lazer");
                                    break;
                                case "amb":
                                    dataMarker = FirebaseDatabase.getInstance().getReference("Marcadores/meioAmbiente");
                                    break;
                            }
                        }
                        DatabaseReference Marca = dataMarker.child(keyChild);
                        Marca.removeValue();

                        if (MainActivity.tipoMapa == "problema"){
                            storageReference.child("Mapas")
                                    .child(tipodeMapa)
                                    .child(marker.getPosition().latitude + "_" + marker.getPosition().longitude)
                                    .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    mProgressDialog.dismiss();
                                    TastyToast.makeText(getContext(), getResources().getString(R.string.no_image)
                                            , TastyToast.LENGTH_LONG, TastyToast.WARNING).show();

                                }
                            });
                            AdicionarTodosMarcadores(mMap);

                        }else{
                            storageReference.child("Mapas")
                                    .child(MainActivity.tipoMapa)
                                    .child(marker.getPosition().latitude + "_" + marker.getPosition().longitude)
                                    .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    mProgressDialog.dismiss();
                                    TastyToast.makeText(getContext(), getResources().getString(R.string.no_image)
                                            , TastyToast.LENGTH_LONG, TastyToast.WARNING).show();

                                }
                            });
                            AdicionarMarcador(mMap);

                        }

                        dadosMarker.dismiss();

                        TastyToast.makeText(getContext(), getResources().getString(R.string.marker_deleted),
                                TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                    }
                });
                excMarker.setNegativeButton(getResources().getString(R.string.not), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                excMarker.show();

            }
        });

        btnDenunciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText emailText = new EditText(getContext());
                emailText.setTextColor(getResources().getColor(R.color.lightOrange));
                final NiftyDialogBuilder dialogEmail = new NiftyDialogBuilder(getContext());
                dialogEmail.withTitle(getResources().getString(R.string.title_marker_denucia))
                        .withTitleColor(getResources().getColor(R.color.lightOrange))                                  //def
                        .withDividerColor(getResources().getColor(R.color.darkOrange))                              //def
                        .withMessageColor(getResources().getColor(R.color.lightOrange) )                             //def  | withMessageColor(int resid)
                        .withDialogColor(getResources().getColor(R.color.darkLightOrange))
                        .withButton1Text(getResources().getString(R.string.cancelar))                                      //def gone
                        .withButton2Text(getResources().getString(R.string.enviar))
                        .setCustomView(emailText, getContext())
                        .isCancelableOnTouchOutside(false)   //def gone//def  | withDialogColor(int resid)
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogEmail.dismiss();


                            }
                        })

                        .setButton2Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogEmail.dismiss();
                                enviarEmail(emailText.getText().toString(), marker);


                            }
                        })
                       // .withIcon(R.drawable.ic_camera)
                        .withEffect(Effectstype.Sidefill)
                        .show();


            }
        });
    }

    public void makerSolvedSave(Marker marker, String descriSolved) {

                    final Marker marker2 = marker;
                    mProgressDialog.setMessage(getResources().getString(R.string.reg_solveds));
                    mProgressDialog.show();
                    storageReference.child("Mapas")
                                .child(MainActivity.tipoMapa)
                                .child(marker.getPosition().latitude + "_" + marker.getPosition().longitude)
                                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mProgressDialog.dismiss();
                            TastyToast.makeText(getContext(), getResources().getString(R.string.registrado_solved)
                                    , TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mProgressDialog.dismiss();
                            TastyToast.makeText(getContext(), getResources().getString(R.string.no_image)
                                    , TastyToast.LENGTH_LONG, TastyToast.WARNING).show();
                            TastyToast.makeText(getContext(), getResources().getString(R.string.como_resolvido)
                                    , TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();
                        }
                    });




            switch (MainActivity.tipoMapa) {
                case "ele":
                    mDatabase = FirebaseDatabase.getInstance().getReference("Resolvidos/eletrico");
                    break;
                case "agu":
                    mDatabase = FirebaseDatabase.getInstance().getReference("Resolvidos/agua");
                    break;
                case "lix":
                    mDatabase = FirebaseDatabase.getInstance().getReference("Resolvidos/lixo");
                    break;
                case "mob":
                    mDatabase = FirebaseDatabase.getInstance().getReference("Resolvidos/mobilidade");
                    break;
                case "ger":
                    mDatabase = FirebaseDatabase.getInstance().getReference("Resolvidos/infraGeral");
                    break;
                case "sau":
                    mDatabase = FirebaseDatabase.getInstance().getReference("Resolvidos/saude");
                    break;
                case "edu":
                    mDatabase = FirebaseDatabase.getInstance().getReference("Resolvidos/educacao");
                    break;
                case "seg":
                    mDatabase = FirebaseDatabase.getInstance().getReference("Resolvidos/seguranca");
                    break;
                case "laz":
                    mDatabase = FirebaseDatabase.getInstance().getReference("Resolvidos/lazer");
                    break;
                case "amb":
                    mDatabase = FirebaseDatabase.getInstance().getReference("Resolvidos/meioAmbiente");
                    break;
                case "problema":
                    switch (tipodeMapa){
                        case "ele":
                            mDatabase = FirebaseDatabase.getInstance().getReference("Resolvidos/eletrico");
                            break;
                        case "agu":
                            mDatabase = FirebaseDatabase.getInstance().getReference("Resolvidos/agua");
                            break;
                        case "lix":
                            mDatabase = FirebaseDatabase.getInstance().getReference("Resolvidos/lixo");
                            break;
                        case "mob":
                            mDatabase = FirebaseDatabase.getInstance().getReference("Resolvidos/mobilidade");
                            break;
                        case "ger":
                            mDatabase = FirebaseDatabase.getInstance().getReference("Resolvidos/infraGeral");
                            break;
                        case "sau":
                            mDatabase = FirebaseDatabase.getInstance().getReference("Resolvidos/saude");
                            break;
                        case "edu":
                            mDatabase = FirebaseDatabase.getInstance().getReference("Resolvidos/educacao");
                            break;
                        case "seg":
                            mDatabase = FirebaseDatabase.getInstance().getReference("Resolvidos/seguranca");
                            break;
                        case "laz":
                            mDatabase = FirebaseDatabase.getInstance().getReference("Resolvidos/lazer");
                            break;
                        case "amb":
                            mDatabase = FirebaseDatabase.getInstance().getReference("Resolvidos/meioAmbiente");
                            break;
                    }
            }

        Date dataGeral = new Date();
        String data = formatodata.format(dataGeral);


        Marcadores marcadores = new Marcadores(marker.getTitle(),
                        marker.getSnippet()+"\n\n"+getResources().getString(R.string.solucao)+descriSolved,
                        marker.getPosition().latitude,
                        marker.getPosition().longitude,
                        1, mUid, data);
                        mDatabase.push().setValue(marcadores);
                MainActivity.tipoMarcador = 0;






    }

    private void enviarEmail(String mensagem, Marker marker){
        sendEmail enviarEmail = new sendEmail(getContext(), "projetoalice.ecit@gmail.com"
                , "denúncia de marcador"
                , mensagem+"\n\n"
                +"Dados do marcador: "+"\n"+"key: "+marker.getTag()+"\n"
                +"Tipo de mapa: "+MainActivity.tipoMapa+"\n"
                +"Problema: "+marker.getTitle()+"\n"
                +"Descrição: "+marker.getSnippet()+"\n"
                +"Imagem: "+string44+"\n"
                +"Latitude: "+marker.getPosition().latitude+"\n"
                +"Longitude: "+marker.getPosition().longitude+"\n\n"
                +"Autor da denúncia: "
                + FirebaseAuth.getInstance().getCurrentUser().getDisplayName()+"\n"
                + FirebaseAuth.getInstance().getCurrentUser().getEmail()+"\n"
                + "Uid: "+FirebaseAuth.getInstance().getCurrentUser().getUid());
        enviarEmail.execute();

    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
