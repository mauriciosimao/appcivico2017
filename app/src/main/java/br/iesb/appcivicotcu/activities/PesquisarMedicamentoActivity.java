package br.iesb.appcivicotcu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.iesb.appcivicotcu.R;
import br.iesb.appcivicotcu.authenticator.FirebaseConnection;

public class PesquisarMedicamentoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener authStateListener;
    private GoogleApiClient googleApiClient;
    private Button btnPesquisar;
    private TextView txtPequisaProduto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisar_medicamento);
        inicializaFirebase();
        conectarGoogleApi();
        inicializarComponentes();
        eventosClick();

    }

    private void eventosClick() {
        btnPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PesquisarMedicamentoActivity.this, CatalogoActivity.class);
                intent.putExtra("produto", txtPequisaProduto.getText().toString());
                startActivity(intent);


            }
        });
    }

    private void inicializarComponentes() {
        btnPesquisar = findViewById(R.id.pesquisar_medicamento_search_button);
        txtPequisaProduto = findViewById(R.id.pequisar_medicamento_produto);
        // Codigo gerado automaticamente quando criamos um drawer
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Mostrando os dados do user
        View header = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);


        ImageView mProfileAvatar = header.findViewById(R.id.nav_header_search_medicine_profile_picture);
        mProfileAvatar.setImageURI(user.getPhotoUrl());

        // TODO - Adicionar foto do usuario no header
        Glide.with(PesquisarMedicamentoActivity.this)
                .load(user.getPhotoUrl())
                .into(mProfileAvatar);

        TextView mProfileName = ((TextView) header.findViewById(R.id.nav_header_pesquisar_medicamento_profile_name));
        mProfileName.setText(user.getDisplayName());

        TextView mProfileEmail = ((TextView) header.findViewById(R.id.nav_header_search_medicine_profile_email));
        mProfileEmail.setText(user.getEmail());


    }

    private void inicializaFirebase() {
        auth = FirebaseConnection.getFirebaseAuth();
        user = FirebaseConnection.getFirebaseUser();
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

    private void conectarGoogleApi() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {
            startActivity(new Intent(PesquisarMedicamentoActivity.this, EditPerfilActivity.class));

        } else if (id == R.id.nav_logout) {
            FirebaseConnection.logout();

            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    alert("Conta Google desconectadada");
                    finish();
                }
            });

            LoginManager.getInstance().logOut();
            finish();

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        alert("Falha na conexão com Google");
    }

    private void alert(String msg) {
        Toast.makeText(PesquisarMedicamentoActivity.this, msg, Toast.LENGTH_LONG).show();
    }


    //    private void verificarUser() {
//        if (user == null) {
//            finish();
//        } else {
//            txtEmail.setText(user.getEmail());
//        }
//    }
}
