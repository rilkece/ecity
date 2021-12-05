package com.ecity.alice.project.ecity;


import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionMenu;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.sdsmdg.tastytoast.TastyToast;



import static com.ecity.alice.project.ecity.MapaEcity.mMap;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    public static String tipoMapa;
    public static int tipoMarcador = 0;
    public static String descri;
    public static String descriMarker;
    private static final String TAG = "MapaECITY";
    public static Uri uri;
    public static StorageReference filePath;
    public static FragmentManager fragmentManager;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;

    private ImageView fotoUser;


    private static final int GALLERY_INTENT = 2;
    public static int galeria = 0;


    public static FloatingActionMenu mapasInfra;
    public static FloatingActionMenu mapasBemEstar;
    public static FloatingActionMenu marcadores;

    public com.github.clans.fab.FloatingActionButton fabSau;
    public com.github.clans.fab.FloatingActionButton fabEdu;
    public com.github.clans.fab.FloatingActionButton fabSeg;
    public com.github.clans.fab.FloatingActionButton fabLaz;
    public com.github.clans.fab.FloatingActionButton fabAmb;

    public com.github.clans.fab.FloatingActionButton fabEle;
    public com.github.clans.fab.FloatingActionButton fabAgu;
    public com.github.clans.fab.FloatingActionButton fabLix;
    public com.github.clans.fab.FloatingActionButton fabMob;
    public com.github.clans.fab.FloatingActionButton fabInf;

    public static com.github.clans.fab.FloatingActionButton fabM1;
    public static com.github.clans.fab.FloatingActionButton fabM2;
    public static com.github.clans.fab.FloatingActionButton fabM3;
    public static com.github.clans.fab.FloatingActionButton fabM4;
    public static com.github.clans.fab.FloatingActionButton fabM5;
    public static com.github.clans.fab.FloatingActionButton fabM6;
    public static com.github.clans.fab.FloatingActionButton fabM7;

    private GoogleApiClient googleApiClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        //Conectar Api Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        //setup gps
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);



        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.conteiner, new MapaEcity(), "MapsFragment");

        transaction.commitAllowingStateLoss();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    // Name, email address, and profile photo Url
                    String name = user.getDisplayName();
                    String email = user.getEmail();
                    Uri photoUrl = user.getPhotoUrl();

                    fotoUser = (ImageView)findViewById(R.id.photoUser);

                    // The user's ID, unique to the Firebase project. Do NOT use this value to
                    // authenticate with your backend server, if you have one. Use
                    // FirebaseUser.getToken() instead.
                    String uid = user.getUid();

                    TextView usuario = (TextView) findViewById(R.id.txt_user);
                    usuario.setText(name);
                    TextView email_user = (TextView) findViewById(R.id.txt_desc_user);
                    email_user.setText(email);
                    Glide.with(MainActivity.this)
                            .load(photoUrl)
                            .into(fotoUser);
                } else {
                    Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            finish();
                        }
                    });
                    startActivity(new Intent(MainActivity.this, LoginECITY.class));
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };


        mapasInfra = (FloatingActionMenu) findViewById(R.id.menu_infra);
        mapasBemEstar = (FloatingActionMenu) findViewById(R.id.menu_bem_estar);
        marcadores = (FloatingActionMenu) findViewById(R.id.marcadores);

        fabSau = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_saude);
        fabEdu = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_educ);
        fabSeg = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_seguranca);
        fabLaz = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_lazer);
        fabAmb = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_meio_ambiente);
        fabEle = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_ele);
        fabAgu = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_agua);
        fabLix = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_lixo);
        fabMob = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_mob);
        fabInf = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_geral);
        fabM1 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.mark1);
        fabM2 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.mark2);
        fabM3 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.mark3);
        fabM4 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.mark4);
        fabM5 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.mark5);
        fabM6 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.mark6);
        fabM7 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.mark7);


        mapasInfra.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapasInfra.isOpened()) {
                    mapasBemEstar.close(true);
                    marcadores.close(true);
                    mapasInfra.close(true);
                } else {
                    mapasBemEstar.close(true);
                    marcadores.close(true);
                    mapasInfra.open(true);

                }

            }
        });

        mapasBemEstar.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapasBemEstar.isOpened()) {
                    mapasBemEstar.close(true);
                    marcadores.close(true);
                    mapasInfra.close(true);
                } else {
                    mapasInfra.close(true);
                    marcadores.close(true);
                    mapasBemEstar.open(true);

                }
            }
        });
        marcadores.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (marcadores.isOpened()) {
                    mapasBemEstar.close(true);
                    marcadores.close(true);
                    mapasInfra.close(true);
                } else {
                    mapasBemEstar.close(true);
                    mapasInfra.close(true);
                    marcadores.open(true);
                }
            }
        });


        fabEle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int yellowPressed = Color.parseColor("#FFF59D");
                int yellowNormal = Color.parseColor("#FFCA28");
                marcadores.setMenuButtonColorNormal(yellowNormal);
                marcadores.setMenuButtonColorPressed(yellowPressed);

                fabM1.setColorNormal(yellowNormal);
                fabM1.setColorPressed(yellowPressed);
                fabM1.setLabelText(getResources().getString(R.string.edu_fab1));
                fabM1.setImageResource(R.mipmap.ic_poste);
                fabM1.setTag(R.mipmap.ic_poste);
                fabM1.setVisibility(View.VISIBLE);

                fabM2.setColorNormal(yellowNormal);
                fabM2.setColorPressed(yellowPressed);
                fabM2.setImageResource(R.mipmap.ic_luzoff);
                fabM2.setLabelText(getResources().getString(R.string.ele_fab2));
                fabM2.setVisibility(View.VISIBLE);

                fabM3.setColorNormal(yellowNormal);
                fabM3.setColorPressed(yellowPressed);
                fabM3.setImageResource(R.mipmap.ic_public_ilumi);
                fabM3.setLabelText(getResources().getString(R.string.edu_fab3));
                fabM3.setVisibility(View.VISIBLE);

                fabM4.setColorNormal(yellowNormal);
                fabM4.setColorPressed(yellowPressed);
                fabM4.setImageResource(R.mipmap.ic_fio_solto);
                fabM4.setLabelText(getResources().getString(R.string.ele_fab4));
                fabM4.setVisibility(View.VISIBLE);

                fabM5.setVisibility(View.GONE);

                fabM6.setColorNormal(yellowNormal);
                fabM6.setColorPressed(yellowPressed);
                fabM6.setImageResource(R.mipmap.ic_inter);
                fabM6.setLabelText(getResources().getString(R.string.ele_fab6));
                fabM6.setVisibility(View.VISIBLE);

                fabM7.setVisibility(View.GONE);

                tipoMapa = "ele";

                MapaEcity mapa = (MapaEcity) fragmentManager.findFragmentById(R.id.conteiner);

                mapa.AdicionarMarcador(mMap);

                fabM1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 1;
                        setAlert(fabM1.getLabelText());
                        descriMarker = fabM1.getLabelText();

                    }
                });

                fabM2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 2;
                        setAlert(fabM2.getLabelText());
                        descriMarker = fabM2.getLabelText();
                    }
                });

                fabM3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 3;
                        setAlert(fabM3.getLabelText());
                        descriMarker = fabM3.getLabelText();

                    }
                });

                fabM4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 4;
                        setAlert(fabM4.getLabelText());
                        descriMarker = fabM4.getLabelText();

                    }
                });

                fabM5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 5;
                        setAlert(fabM5.getLabelText());
                        descriMarker = fabM5.getLabelText();

                    }
                });

                fabM6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 6;
                        setAlert(fabM6.getLabelText());
                        descriMarker = fabM6.getLabelText();

                    }
                });

                fabM7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 7;
                        setAlert(fabM7.getLabelText());
                        descriMarker = fabM7.getLabelText();

                    }
                });

            }
        });

        fabAgu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int colorPressed = Color.parseColor("#E1F5FE");
                int colorNormal = Color.parseColor("#4FC3F7");
                marcadores.setMenuButtonColorNormal(colorNormal);
                marcadores.setMenuButtonColorPressed(colorPressed);

                fabM1.setColorNormal(colorNormal);
                fabM1.setColorPressed(colorPressed);
                fabM1.setLabelText(getResources().getString(R.string.agufab1));
                fabM1.setImageResource(R.mipmap.ic_agua_suja);
                fabM1.setVisibility(View.VISIBLE);

                fabM2.setColorNormal(colorNormal);
                fabM2.setColorPressed(colorPressed);
                fabM2.setImageResource(R.mipmap.ic_falta_dagua);
                fabM2.setLabelText(getResources().getString(R.string.agufab2));
                fabM2.setVisibility(View.VISIBLE);

                fabM3.setColorNormal(colorNormal);
                fabM3.setColorPressed(colorPressed);
                fabM3.setImageResource(R.mipmap.ic_esgoto);
                fabM3.setLabelText(getResources().getString(R.string.agufab3));
                fabM3.setVisibility(View.VISIBLE);

                fabM4.setColorNormal(colorNormal);
                fabM4.setColorPressed(colorPressed);
                fabM4.setImageResource(R.mipmap.ic_inund);
                fabM4.setLabelText(getResources().getString(R.string.agufab4));
                fabM4.setVisibility(View.VISIBLE);

                fabM5.setColorNormal(colorNormal);
                fabM5.setColorPressed(colorPressed);
                fabM5.setImageResource(R.mipmap.ic_vaza);
                fabM5.setLabelText(getResources().getString(R.string.agufab5));
                fabM5.setVisibility(View.VISIBLE);

                fabM6.setColorNormal(colorNormal);
                fabM6.setColorPressed(colorPressed);
                fabM6.setImageResource(R.mipmap.ic_inter);
                fabM6.setLabelText(getResources().getString(R.string.agufab6));
                fabM6.setVisibility(View.VISIBLE);

                fabM7.setVisibility(View.GONE);

                tipoMapa = "agu";

                MapaEcity mapa = (MapaEcity) fragmentManager.findFragmentById(R.id.conteiner);
                mapa.AdicionarMarcador(mMap);


                fabM1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 1;
                        setAlert(fabM1.getLabelText());
                        descriMarker = fabM1.getLabelText();

                    }
                });

                fabM2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 2;
                        setAlert(fabM2.getLabelText());
                        descriMarker = fabM2.getLabelText();
                    }
                });

                fabM3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 3;
                        setAlert(fabM3.getLabelText());
                        descriMarker = fabM3.getLabelText();

                    }
                });

                fabM4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 4;
                        setAlert(fabM4.getLabelText());
                        descriMarker = fabM4.getLabelText();

                    }
                });

                fabM5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 5;
                        setAlert(fabM5.getLabelText());
                        descriMarker = fabM5.getLabelText();

                    }
                });

                fabM6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 6;
                        setAlert(fabM6.getLabelText());
                        descriMarker = fabM6.getLabelText();

                    }
                });

                fabM7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 7;
                        setAlert(fabM7.getLabelText());
                        descriMarker = fabM7.getLabelText();

                    }
                });

            }
        });

        fabLix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int colorPressed = Color.parseColor("#D7CCC8");
                int colorNormal = Color.parseColor("#8D6E63");
                marcadores.setMenuButtonColorNormal(colorNormal);
                marcadores.setMenuButtonColorPressed(colorPressed);

                fabM1.setColorNormal(colorNormal);
                fabM1.setColorPressed(colorPressed);
                fabM1.setLabelText(getResources().getString(R.string.lix_fab1));
                fabM1.setImageResource(R.mipmap.ic_lixeira);
                fabM1.setVisibility(View.VISIBLE);

                fabM2.setColorNormal(colorNormal);
                fabM2.setColorPressed(colorPressed);
                fabM2.setImageResource(R.mipmap.ic_amb_lixo);
                fabM2.setLabelText(getResources().getString(R.string.lix_fab2));
                fabM2.setVisibility(View.VISIBLE);

                fabM3.setColorNormal(colorNormal);
                fabM3.setColorPressed(colorPressed);
                fabM3.setImageResource(R.mipmap.ic_publico_lixo);
                fabM3.setLabelText(getResources().getString(R.string.lix_fab3));
                fabM3.setVisibility(View.VISIBLE);

                fabM4.setColorNormal(colorNormal);
                fabM4.setColorPressed(colorPressed);
                fabM4.setImageResource(R.mipmap.ic_baldio);
                fabM4.setLabelText(getResources().getString(R.string.lix_fab4));
                fabM4.setVisibility(View.VISIBLE);


                fabM5.setVisibility(View.GONE);

                fabM6.setColorNormal(colorNormal);
                fabM6.setColorPressed(colorPressed);
                fabM6.setImageResource(R.mipmap.ic_inter);
                fabM6.setLabelText(getResources().getString(R.string.lix_fab6));
                fabM6.setVisibility(View.VISIBLE);

                fabM7.setVisibility(View.GONE);

                tipoMapa = "lix";

                MapaEcity mapa = (MapaEcity) fragmentManager.findFragmentById(R.id.conteiner);
                mapa.AdicionarMarcador(mMap);

                fabM1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 1;
                        setAlert(fabM1.getLabelText());
                        descriMarker = fabM1.getLabelText();

                    }
                });

                fabM2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 2;
                        setAlert(fabM2.getLabelText());
                        descriMarker = fabM2.getLabelText();
                    }
                });

                fabM3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 3;
                        setAlert(fabM3.getLabelText());
                        descriMarker = fabM3.getLabelText();

                    }
                });

                fabM4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 4;
                        setAlert(fabM4.getLabelText());
                        descriMarker = fabM4.getLabelText();

                    }
                });

                fabM5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 5;
                        setAlert(fabM5.getLabelText());
                        descriMarker = fabM5.getLabelText();

                    }
                });

                fabM6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 6;
                        setAlert(fabM6.getLabelText());
                        descriMarker = fabM6.getLabelText();

                    }
                });

                fabM7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 7;
                        setAlert(fabM7.getLabelText());
                        descriMarker = fabM7.getLabelText();

                    }
                });


            }
        });

        fabMob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int colorPressed = Color.parseColor("#F5F5F5");
                int colorNormal = Color.parseColor("#E0E0E0");
                marcadores.setMenuButtonColorNormal(colorNormal);
                marcadores.setMenuButtonColorPressed(colorPressed);

                fabM1.setColorNormal(colorNormal);
                fabM1.setColorPressed(colorPressed);
                fabM1.setLabelText(getResources().getString(R.string.mob_fab1));
                fabM1.setImageResource(R.mipmap.ic_semaforo);
                fabM1.setVisibility(View.VISIBLE);

                fabM2.setColorNormal(colorNormal);
                fabM2.setColorPressed(colorPressed);
                fabM2.setImageResource(R.mipmap.ic_faixa);
                fabM2.setLabelText(getResources().getString(R.string.mob_fab2));
                fabM2.setVisibility(View.VISIBLE);

                fabM3.setColorNormal(colorNormal);
                fabM3.setColorPressed(colorPressed);
                fabM3.setImageResource(R.mipmap.ic_buraco);
                fabM3.setLabelText(getResources().getString(R.string.mob_fab3));
                fabM3.setVisibility(View.VISIBLE);

                fabM4.setColorNormal(colorNormal);
                fabM4.setColorPressed(colorPressed);
                fabM4.setImageResource(R.mipmap.ic_bueiro);
                fabM4.setLabelText(getResources().getString(R.string.mob_fab4));
                fabM4.setVisibility(View.VISIBLE);


                fabM5.setColorNormal(colorNormal);
                fabM5.setColorPressed(colorPressed);
                fabM5.setImageResource(R.mipmap.ic_placa);
                fabM5.setLabelText(getResources().getString(R.string.mob_fab5));
                fabM5.setVisibility(View.VISIBLE);

                fabM6.setColorNormal(colorNormal);
                fabM6.setColorPressed(colorPressed);
                fabM6.setImageResource(R.mipmap.ic_parada);
                fabM6.setLabelText(getResources().getString(R.string.mob_fab6));
                fabM6.setVisibility(View.VISIBLE);

                fabM7.setColorNormal(colorNormal);
                fabM7.setColorPressed(colorPressed);
                fabM7.setImageResource(R.mipmap.ic_inter);
                fabM7.setLabelText(getResources().getString(R.string.mob_fab7));
                fabM7.setVisibility(View.VISIBLE);

                tipoMapa = "mob";

                MapaEcity mapa = (MapaEcity) fragmentManager.findFragmentById(R.id.conteiner);
                mapa.AdicionarMarcador(mMap);

                fabM1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 1;
                        setAlert(fabM1.getLabelText());
                        descriMarker = fabM1.getLabelText();

                    }
                });

                fabM2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 2;
                        setAlert(fabM2.getLabelText());
                        descriMarker = fabM2.getLabelText();
                    }
                });

                fabM3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 3;
                        setAlert(fabM3.getLabelText());
                        descriMarker = fabM3.getLabelText();

                    }
                });

                fabM4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 4;
                        setAlert(fabM4.getLabelText());
                        descriMarker = fabM4.getLabelText();

                    }
                });

                fabM5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 5;
                        setAlert(fabM5.getLabelText());
                        descriMarker = fabM5.getLabelText();

                    }
                });

                fabM6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 6;
                        setAlert(fabM6.getLabelText());
                        descriMarker = fabM6.getLabelText();

                    }
                });

                fabM7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 7;
                        setAlert(fabM7.getLabelText());
                        descriMarker = fabM7.getLabelText();

                    }
                });


            }
        });

        fabInf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int colorPressed = Color.parseColor("#F8BBD0");
                int colorNormal = Color.parseColor("#EC407A");
                marcadores.setMenuButtonColorNormal(colorNormal);
                marcadores.setMenuButtonColorPressed(colorPressed);

                fabM1.setColorNormal(colorNormal);
                fabM1.setColorPressed(colorPressed);
                fabM1.setLabelText(getResources().getString(R.string.ger_fab1));
                fabM1.setImageResource(R.mipmap.ic_degradado);
                fabM1.setVisibility(View.VISIBLE);

                fabM2.setColorNormal(colorNormal);
                fabM2.setColorPressed(colorPressed);
                fabM2.setImageResource(R.mipmap.ic_cal_acess);
                fabM2.setLabelText(getResources().getString(R.string.ger_fab2));
                fabM2.setVisibility(View.VISIBLE);

                fabM3.setVisibility(View.GONE);

                fabM4.setVisibility(View.GONE);

                fabM5.setVisibility(View.GONE);

                fabM6.setColorNormal(colorNormal);
                fabM6.setColorPressed(colorPressed);
                fabM6.setImageResource(R.mipmap.ic_inter);
                fabM6.setLabelText(getResources().getString(R.string.ger_fab6));
                fabM6.setVisibility(View.VISIBLE);

                fabM7.setVisibility(View.GONE);

                tipoMapa = "ger";

                MapaEcity mapa = (MapaEcity) fragmentManager.findFragmentById(R.id.conteiner);
                mapa.AdicionarMarcador(mMap);

                fabM1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 1;
                        setAlert(fabM1.getLabelText());
                        descriMarker = fabM1.getLabelText();

                    }
                });

                fabM2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 2;
                        setAlert(fabM2.getLabelText());
                        descriMarker = fabM2.getLabelText();
                    }
                });

                fabM3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 3;
                        setAlert(fabM3.getLabelText());
                        descriMarker = fabM3.getLabelText();

                    }
                });

                fabM4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 4;
                        setAlert(fabM4.getLabelText());
                        descriMarker = fabM4.getLabelText();

                    }
                });

                fabM5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 5;
                        setAlert(fabM5.getLabelText());
                        descriMarker = fabM5.getLabelText();

                    }
                });

                fabM6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 6;
                        setAlert(fabM6.getLabelText());
                        descriMarker = fabM6.getLabelText();

                    }
                });

                fabM7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 7;
                        setAlert(fabM7.getLabelText());
                        descriMarker = fabM7.getLabelText();

                    }
                });

            }
        });

        fabSau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int colorPressed = Color.parseColor("#FFEBEE");
                int colorNormal = Color.parseColor("#FF8A80");
                marcadores.setMenuButtonColorNormal(colorNormal);
                marcadores.setMenuButtonColorPressed(colorPressed);

                fabM1.setColorNormal(colorNormal);
                fabM1.setColorPressed(colorPressed);
                fabM1.setLabelText(getResources().getString(R.string.sau_fab1));
                fabM1.setImageResource(R.mipmap.ic_saude_sem_acesso);
                fabM1.setVisibility(View.VISIBLE);

                fabM2.setColorNormal(colorNormal);
                fabM2.setColorPressed(colorPressed);
                fabM2.setImageResource(R.mipmap.ic_sem_medico);
                fabM2.setLabelText(getResources().getString(R.string.sau_fab2));
                fabM2.setVisibility(View.VISIBLE);

                fabM3.setColorNormal(colorNormal);
                fabM3.setColorPressed(colorPressed);
                fabM3.setImageResource(R.mipmap.ic_sem_medicamentos);
                fabM3.setLabelText(getResources().getString(R.string.sau_fab3));
                fabM3.setVisibility(View.VISIBLE);

                fabM4.setColorNormal(colorNormal);
                fabM4.setColorPressed(colorPressed);
                fabM4.setImageResource(R.mipmap.ic_lotado);
                fabM4.setLabelText(getResources().getString(R.string.sau_fab4));
                fabM4.setVisibility(View.VISIBLE);


                fabM5.setVisibility(View.GONE);

                fabM6.setColorNormal(colorNormal);
                fabM6.setColorPressed(colorPressed);
                fabM6.setImageResource(R.mipmap.ic_inter);
                fabM6.setLabelText(getResources().getString(R.string.sau_fab6));
                fabM6.setVisibility(View.VISIBLE);

                fabM7.setVisibility(View.GONE);

                tipoMapa = "sau";

                MapaEcity mapa = (MapaEcity) fragmentManager.findFragmentById(R.id.conteiner);
                mapa.AdicionarMarcador(mMap);

                fabM1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 1;
                        setAlert(fabM1.getLabelText());
                        descriMarker = fabM1.getLabelText();

                    }
                });

                fabM2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 2;
                        setAlert(fabM2.getLabelText());
                        descriMarker = fabM2.getLabelText();
                    }
                });

                fabM3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 3;
                        setAlert(fabM3.getLabelText());
                        descriMarker = fabM3.getLabelText();

                    }
                });

                fabM4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 4;
                        setAlert(fabM4.getLabelText());
                        descriMarker = fabM4.getLabelText();

                    }
                });

                fabM5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 5;
                        setAlert(fabM5.getLabelText());
                        descriMarker = fabM5.getLabelText();

                    }
                });

                fabM6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 6;
                        setAlert(fabM6.getLabelText());
                        descriMarker = fabM6.getLabelText();

                    }
                });

                fabM7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 7;
                        setAlert(fabM7.getLabelText());
                        descriMarker = fabM7.getLabelText();

                    }
                });

            }
        });

        fabEdu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int colorPressed = Color.parseColor("#E3F2FD");
                int colorNormal = Color.parseColor("#42A5F5");
                marcadores.setMenuButtonColorNormal(colorNormal);
                marcadores.setMenuButtonColorPressed(colorPressed);

                fabM1.setColorNormal(colorNormal);
                fabM1.setColorPressed(colorPressed);
                fabM1.setLabelText(getResources().getString(R.string.edu_fab1));
                fabM1.setImageResource(R.mipmap.ic_escola_acess);
                fabM1.setVisibility(View.VISIBLE);

                fabM2.setColorNormal(colorNormal);
                fabM2.setColorPressed(colorPressed);
                fabM2.setImageResource(R.mipmap.ic_sem_comida);
                fabM2.setLabelText(getResources().getString(R.string.edu_fab2));
                fabM2.setVisibility(View.VISIBLE);

                fabM3.setColorNormal(colorNormal);
                fabM3.setColorPressed(colorPressed);
                fabM3.setImageResource(R.mipmap.ic_sem_aula);
                fabM3.setLabelText(getResources().getString(R.string.edu_fab3));
                fabM3.setVisibility(View.VISIBLE);

                fabM4.setColorNormal(colorNormal);
                fabM4.setColorPressed(colorPressed);
                fabM4.setImageResource(R.mipmap.ic_sem_prof);
                fabM4.setLabelText(getResources().getString(R.string.edu_fab4));
                fabM4.setVisibility(View.VISIBLE);

                fabM5.setVisibility(View.GONE);

                fabM6.setColorNormal(colorNormal);
                fabM6.setColorPressed(colorPressed);
                fabM6.setImageResource(R.mipmap.ic_inter);
                fabM6.setLabelText(getResources().getString(R.string.edu_fab6));
                fabM6.setVisibility(View.VISIBLE);

                fabM7.setVisibility(View.GONE);

                tipoMapa = "edu";

                MapaEcity mapa = (MapaEcity) fragmentManager.findFragmentById(R.id.conteiner);
                mapa.AdicionarMarcador(mMap);

                fabM1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 1;
                        setAlert(fabM1.getLabelText());
                        descriMarker = fabM1.getLabelText();

                    }
                });

                fabM2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 2;
                        setAlert(fabM2.getLabelText());
                        descriMarker = fabM2.getLabelText();
                    }
                });

                fabM3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 3;
                        setAlert(fabM3.getLabelText());
                        descriMarker = fabM3.getLabelText();

                    }
                });

                fabM4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 4;
                        setAlert(fabM4.getLabelText());
                        descriMarker = fabM4.getLabelText();

                    }
                });

                fabM5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 5;
                        setAlert(fabM5.getLabelText());
                        descriMarker = fabM5.getLabelText();

                    }
                });

                fabM6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 6;
                        setAlert(fabM6.getLabelText());
                        descriMarker = fabM6.getLabelText();

                    }
                });

                fabM7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 7;
                        setAlert(fabM7.getLabelText());
                        descriMarker = fabM7.getLabelText();

                    }
                });

            }
        });

        fabSeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int colorPressed = Color.parseColor("#FFE0B2");
                int colorNormal = Color.parseColor("#FB8C00");
                marcadores.setMenuButtonColorNormal(colorNormal);
                marcadores.setMenuButtonColorPressed(colorPressed);

                fabM1.setColorNormal(colorNormal);
                fabM1.setColorPressed(colorPressed);
                fabM1.setLabelText(getResources().getString(R.string.seg_fab1));
                fabM1.setImageResource(R.mipmap.ic_assalto);
                fabM1.setVisibility(View.VISIBLE);

                fabM2.setColorNormal(colorNormal);
                fabM2.setColorPressed(colorPressed);
                fabM2.setImageResource(R.mipmap.ic_ronda);
                fabM2.setLabelText(getResources().getString(R.string.seg_fab2));
                fabM2.setVisibility(View.VISIBLE);


                fabM3.setColorNormal(colorNormal);
                fabM3.setColorPressed(colorPressed);
                fabM3.setImageResource(R.mipmap.ic_rua_perigosa);
                fabM3.setLabelText(getResources().getString(R.string.seg_fab3));
                fabM3.setVisibility(View.VISIBLE);

                fabM4.setVisibility(View.GONE);

                fabM5.setVisibility(View.GONE);

                fabM6.setColorNormal(colorNormal);
                fabM6.setColorPressed(colorPressed);
                fabM6.setImageResource(R.mipmap.ic_inter);
                fabM6.setLabelText(getResources().getString(R.string.seg_fab6));
                fabM6.setVisibility(View.VISIBLE);

                fabM7.setVisibility(View.GONE);

                tipoMapa = "seg";

                MapaEcity mapa = (MapaEcity) fragmentManager.findFragmentById(R.id.conteiner);
                mapa.AdicionarMarcador(mMap);

                fabM1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 1;
                        setAlert(fabM1.getLabelText());
                        descriMarker = fabM1.getLabelText();

                    }
                });

                fabM2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 2;
                        setAlert(fabM2.getLabelText());
                        descriMarker = fabM2.getLabelText();
                    }
                });

                fabM3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 3;
                        setAlert(fabM3.getLabelText());
                        descriMarker = fabM3.getLabelText();

                    }
                });

                fabM4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 4;
                        setAlert(fabM4.getLabelText());
                        descriMarker = fabM4.getLabelText();

                    }
                });

                fabM5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 5;
                        setAlert(fabM5.getLabelText());
                        descriMarker = fabM5.getLabelText();

                    }
                });

                fabM6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 6;
                        setAlert(fabM6.getLabelText());
                        descriMarker = fabM6.getLabelText();

                    }
                });

                fabM7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 7;
                        setAlert(fabM7.getLabelText());
                        descriMarker = fabM7.getLabelText();

                    }
                });

            }
        });

        fabLaz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int colorPressed = Color.parseColor("#F5F5F5");
                int colorNormal = Color.parseColor("#9E9E9E");
                marcadores.setMenuButtonColorNormal(colorNormal);
                marcadores.setMenuButtonColorPressed(colorPressed);

                fabM1.setColorNormal(colorNormal);
                fabM1.setColorPressed(colorPressed);
                fabM1.setLabelText(getResources().getString(R.string.laz_fab1));
                fabM1.setImageResource(R.mipmap.ic_praia_suja);
                fabM1.setVisibility(View.VISIBLE);

                fabM2.setColorNormal(colorNormal);
                fabM2.setColorPressed(colorPressed);
                fabM2.setImageResource(R.mipmap.ic_praca_degradada);
                fabM2.setLabelText(getResources().getString(R.string.laz_fab2));
                fabM2.setVisibility(View.VISIBLE);

                fabM3.setColorNormal(colorNormal);
                fabM3.setColorPressed(colorPressed);
                fabM3.setImageResource(R.mipmap.ic_sem_praca);
                fabM3.setLabelText(getResources().getString(R.string.laz_fab3));
                fabM3.setVisibility(View.VISIBLE);


                fabM4.setVisibility(View.GONE);

                fabM5.setVisibility(View.GONE);

                fabM6.setColorNormal(colorNormal);
                fabM6.setColorPressed(colorPressed);
                fabM6.setImageResource(R.mipmap.ic_inter);
                fabM6.setLabelText(getResources().getString(R.string.laz_fab6));
                fabM6.setVisibility(View.VISIBLE);

                fabM7.setVisibility(View.GONE);

                tipoMapa = "laz";

                MapaEcity mapa = (MapaEcity) fragmentManager.findFragmentById(R.id.conteiner);
                mapa.AdicionarMarcador(mMap);

                fabM1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 1;
                        setAlert(fabM1.getLabelText());
                        descriMarker = fabM1.getLabelText();

                    }
                });

                fabM2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 2;
                        setAlert(fabM2.getLabelText());
                        descriMarker = fabM2.getLabelText();
                    }
                });

                fabM3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 3;
                        setAlert(fabM3.getLabelText());
                        descriMarker = fabM3.getLabelText();

                    }
                });

                fabM4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 4;
                        setAlert(fabM4.getLabelText());
                        descriMarker = fabM4.getLabelText();

                    }
                });

                fabM5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 5;
                        setAlert(fabM5.getLabelText());
                        descriMarker = fabM5.getLabelText();

                    }
                });

                fabM6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 6;
                        setAlert(fabM6.getLabelText());
                        descriMarker = fabM6.getLabelText();

                    }
                });

                fabM7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 7;
                        setAlert(fabM7.getLabelText());
                        descriMarker = fabM7.getLabelText();

                    }
                });

            }
        });

        fabAmb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int colorPressed = Color.parseColor("#C5E1A5");
                int colorNormal = Color.parseColor("#7CB342");
                marcadores.setMenuButtonColorNormal(colorNormal);
                marcadores.setMenuButtonColorPressed(colorPressed);

                fabM1.setColorNormal(colorNormal);
                fabM1.setColorPressed(colorPressed);
                fabM1.setLabelText(getResources().getString(R.string.amb_fab1));
                fabM1.setImageResource(R.mipmap.ic_queimadas);
                fabM1.setVisibility(View.VISIBLE);

                fabM2.setColorNormal(colorNormal);
                fabM2.setColorPressed(colorPressed);
                fabM2.setImageResource(R.mipmap.ic_desmatamento);
                fabM2.setLabelText(getResources().getString(R.string.amb_fab2));
                fabM2.setVisibility(View.VISIBLE);

                fabM3.setColorNormal(colorNormal);
                fabM3.setColorPressed(colorPressed);
                fabM3.setImageResource(R.mipmap.ic_invasion);
                fabM3.setLabelText(getResources().getString(R.string.amb_fab3));
                fabM3.setVisibility(View.VISIBLE);

                fabM4.setColorNormal(colorNormal);
                fabM4.setColorPressed(colorPressed);
                fabM4.setImageResource(R.mipmap.ic_captura);
                fabM4.setLabelText(getResources().getString(R.string.amb_fab4));
                fabM4.setVisibility(View.VISIBLE);

                fabM5.setColorNormal(colorNormal);
                fabM5.setColorPressed(colorPressed);
                fabM5.setImageResource(R.mipmap.ic_praga);
                fabM5.setLabelText(getResources().getString(R.string.amb_fab5));
                fabM5.setVisibility(View.VISIBLE);

                fabM6.setColorNormal(colorNormal);
                fabM6.setColorPressed(colorPressed);
                fabM6.setImageResource(R.mipmap.ic_inter);
                fabM6.setLabelText(getResources().getString(R.string.amb_fab6));
                fabM6.setVisibility(View.VISIBLE);

                fabM7.setVisibility(View.GONE);

                tipoMapa = "amb";

                MapaEcity mapa = (MapaEcity) fragmentManager.findFragmentById(R.id.conteiner);
                mapa.AdicionarMarcador(mMap);

                fabM1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 1;
                        setAlert(fabM1.getLabelText());
                        descriMarker = fabM1.getLabelText();

                    }
                });

                fabM2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 2;
                        setAlert(fabM2.getLabelText());
                        descriMarker = fabM2.getLabelText();
                    }
                });

                fabM3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 3;
                        setAlert(fabM3.getLabelText());
                        descriMarker = fabM3.getLabelText();

                    }
                });

                fabM4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 4;
                        setAlert(fabM4.getLabelText());
                        descriMarker = fabM4.getLabelText();

                    }
                });

                fabM5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 5;
                        setAlert(fabM5.getLabelText());
                        descriMarker = fabM5.getLabelText();

                    }
                });

                fabM6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 6;
                        setAlert(fabM6.getLabelText());
                        descriMarker = fabM6.getLabelText();

                    }
                });

                fabM7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoMarcador = 7;
                        setAlert(fabM7.getLabelText());
                        descriMarker = fabM7.getLabelText();

                    }
                });

            }
        });


    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.mapaPrincial) {
            // Handle the camera action
            mapasInfra.setVisibility(View.VISIBLE);
            mapasBemEstar.setVisibility(View.VISIBLE);
            marcadores.setVisibility(View.VISIBLE);
            fabM1.setVisibility(View.GONE);
            fabM2.setVisibility(View.GONE);
            fabM3.setVisibility(View.GONE);
            fabM4.setVisibility(View.GONE);
            fabM5.setVisibility(View.GONE);
            fabM6.setVisibility(View.GONE);
            fabM7.setVisibility(View.GONE);
            tipoMapa = "problema";

            FragmentTransaction transaction2 = fragmentManager.beginTransaction();
            transaction2.replace(R.id.conteiner, new MapaEcity(), "MapsFragment");

            transaction2.commitAllowingStateLoss();
        } else if (id == R.id.mapaSoluções){
            mapasInfra.setVisibility(View.GONE);
            mapasBemEstar.setVisibility(View.GONE);
            marcadores.setVisibility(View.GONE);
            tipoMapa="resolvido";

            FragmentTransaction transaction2 = fragmentManager.beginTransaction();
            transaction2.replace(R.id.conteiner, new MapaEcity(), "MapsFragment");

            transaction2.commitAllowingStateLoss();

        }else if(id == R.id.FaleConosco){

            final EditText editAssunto = new EditText(this);
            final EditText editMensagem = new EditText(this);
            editAssunto.setHint(getResources().getString(R.string.assunto));
            editAssunto.setHint(getResources().getString(R.string.mensagem));
            editMensagem.setTextColor(getResources().getColor(R.color.lightBlue));
            final NiftyDialogBuilder faleConosco = NiftyDialogBuilder.getInstance(this);
            faleConosco.withTitle(getResources().getString(R.string.posso_ajudar))                                  //.withTitle(null)  no title
                    .withTitleColor(getResources().getColor(R.color.lightBlue))                                  //def
                    .withDividerColor(getResources().getColor(R.color.darkBlue))                              //def
                    .withDialogColor(getResources().getColor(R.color.darkLightBlue))                               //def  | withDialogColor(int resid)
                    .withIcon(getResources().getDrawable(R.drawable.ic_mail_outline_white_24dp))
                    .withDuration(700)                                          //def
                    .withEffect(Effectstype.Fadein)                                         //def Effectstype.Slidetop
                    .setCustomView(editAssunto, this)
                    .setCustomView(editMensagem, this)
                    .withButton1Text(getResources().getString(R.string.cancelar))                                      //def gone
                    .withButton2Text(getResources().getString(R.string.enviar))                                  //def gone
                    .isCancelableOnTouchOutside(false)                           //def    | isCancelable(true)
                    //.setCustomView(R.layout.custom_view,v.getContext())         //.setCustomView(View or ResId,context)
                    .setButton1Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            faleConosco.dismiss();
                        }
                    })
                    .setButton2Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           faleConosco.dismiss();
                           enviarEmail(editMensagem.getText().toString(), editAssunto.getText().toString());
                        }
                    })
                    .show();

        }else if (id == R.id.atualizarPerfil) {
            mapasInfra.setVisibility(View.GONE);
            mapasBemEstar.setVisibility(View.GONE);
            marcadores.setVisibility(View.GONE);
            FragmentTransaction transaction3 = fragmentManager.beginTransaction()
                    .replace(R.id.conteiner, new AtualizarPerfil());

            //transaction3.add(R.id.conteiner, new AtualizarPerfil(), "AtualizarPerfil");

            transaction3.commitAllowingStateLoss();

        }  else if (id == R.id.logOut) {

            mAuth.signOut();
            getSupportActionBar().hide();
            //startActivity(new Intent(MainActivity.this, LoginECITY.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void setAlert(String problema) {

        //final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        //final AlertDialog.Builder upFoto = new AlertDialog.Builder(this);


        final NiftyDialogBuilder alert =NiftyDialogBuilder.getInstance(this);
        final NiftyDialogBuilder upFoto =NiftyDialogBuilder.getInstance(this);

        alert.withTitle(problema)
                .withMessage(getResources().getString(R.string.descri_problema))
                .withTitleColor("#E0F2F1")                                  //def
                .withDividerColor("#009688")                              //def
                .withMessageColor("#E0F2F1")                              //def  | withMessageColor(int resid)
                .withDialogColor("#00BFA5");                               //def  | withDialogColor(int resid)

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setTextColor(getResources().getColor(R.color.greenLight));
        alert.setCustomView(input, this)
                .withButton1Text("OK")                                      //def gone
                .withButton2Text("Cancel")
                .isCancelableOnTouchOutside(false)   //def gone
                .setButton1Click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                descri = input.getText().toString();
                mapasInfra.close(true);
                mapasBemEstar.close(true);
                marcadores.close(true);

                upFoto.withTitle(getResources().getString(R.string.enviar_foto))
                        .withTitleColor("#E0F2F1")                                  //def
                        .withDividerColor("#009688")                              //def
                        .withMessageColor("#E0F2F1")                              //def  | withMessageColor(int resid)
                        .withDialogColor("#00BFA5")
                        .withButton1Text(getResources().getString(R.string.not))                                      //def gone
                        .withButton2Text(getResources().getString(R.string.yes))
                        .isCancelableOnTouchOutside(false)   //def gone//def  | withDialogColor(int resid)
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                galeria = 0;
                                upFoto.dismiss();

                                TastyToast.makeText(getApplication(), getResources().getString(R.string.clique_para_marcar),
                                        TastyToast.LENGTH_LONG, TastyToast.INFO).show();
                            }
                        })

                        .setButton2Click(new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {
                              galeria = 1;
                              upFoto.dismiss();
                              Intent intent = new Intent(Intent.ACTION_PICK);
                              intent.setType("image/*");
                              startActivityForResult(intent, GALLERY_INTENT);

                           }
                          });

                upFoto.withIcon(R.drawable.ic_camera)
                        .withEffect(Effectstype.SlideBottom)
                        .show();

            }
        })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                        TastyToast.makeText(getApplication(), getResources().getString(R.string.cancelado)
                                , TastyToast.LENGTH_LONG, TastyToast.INFO).show();
                    }
                });
        alert.withEffect(Effectstype.SlideBottom)                                         //def Effectstype.Slidetop
             .show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {

            uri = data.getData();
            TastyToast.makeText(getApplication(), getResources().getString(R.string.clique_para_marcar),
                    TastyToast.LENGTH_LONG, TastyToast.INFO).show();

        }
    }
    private void enviarEmail(String mensagem, String assunto){
        sendEmail enviarEmail = new sendEmail(this, "projetoalice.ecit@gmail.com"
                , "Fale Conosco: "+assunto
                , mensagem+"\n\n"
                +"Autor: "
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
    @Override
    public void onResume() {
        super.onResume();
        googleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }
}