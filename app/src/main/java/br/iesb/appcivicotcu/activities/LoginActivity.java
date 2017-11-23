package br.iesb.appcivicotcu.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.iesb.appcivicotcu.R;
import br.iesb.appcivicotcu.authenticator.FirebaseConnection;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private Button btnLogin;
    private SignInButton btnLoginGoogle;
    private LoginButton btnLoginFacebook;

    private EditText editEmail;
    private EditText editSenha;

    private FirebaseAuth auth;
    private GoogleApiClient mGoogleApiClient;

    private CallbackManager callbackManager;
    private ProgressDialog progressDoalog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inicializarFirebase();
        conectarGoogleApi();

        inicializaComponentes();

        eventosClick();

    }

    private void inicializaComponentes() {
        editEmail = findViewById(R.id.login_edit_email);
        editSenha = findViewById(R.id.login_edit_senha);

        btnLogin = findViewById(R.id.login_btn_entrar);
        btnLoginGoogle = findViewById(R.id.login_btn_google_entrar);
        btnLoginFacebook = findViewById(R.id.login_btn_facebook_entrar);
        btnLoginFacebook.setReadPermissions("email", "public_profile");
    }

    public void spinner() {
        progressDoalog = new ProgressDialog(LoginActivity.this);
        progressDoalog.setMessage("Aguarde....");
        progressDoalog.setTitle("Realizando Login");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDoalog.show();
    }

    private void eventosClick() {


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editEmail.getText().toString();
                String senha = editSenha.getText().toString();

                if(!email.isEmpty() && !senha.isEmpty()){
                    if (validateEmail(email) && validatePassword(senha)) {
                        spinner();
                        loginFirebaseEmailSenha(email, senha);
                    } else {
                        alert("Erro ao logar, email ou senha inválito");
                    }
                } else {
                    alert("Erro ao logar, email ou senha inválito");
                }
            }
        });

        btnLoginGoogle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                logarComGoogle();

            }
        });

        btnLoginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                firebaseLoginFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                alert("Operação cancelada");
            }

            @Override
            public void onError(FacebookException error) {
                alert("Erro ao logar com o Facebook");
            }
        });
    }

    private boolean validateEmail(String email) {
        final String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern patternEmail = Pattern.compile(emailPattern);
        Matcher matcherEmail = patternEmail.matcher(email);

        return matcherEmail.matches();
    }

    private boolean validatePassword(String senha) {
        if (senha.length() >5 && senha.length() <13){
            return true;
        } else {
            return false;
        }
    }

    private void firebaseLoginFacebook(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            btnLoginFacebook.setVisibility(View.INVISIBLE); //<- IMPORTANT
                            Intent intent = new Intent(LoginActivity.this, PesquisarMedicamentoActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            alert("Erro de autenticação com firebase");
                        }
                    }
                });
    }

    private void inicializarFirebase() {
        auth = FirebaseConnection.getFirebaseAuth();
        callbackManager = CallbackManager.Factory.create();
    }


    private void conectarGoogleApi() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void loginFirebaseEmailSenha(String email, String senha) {

        auth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDoalog.dismiss();
                            alert("Bem vindo!");
                            startActivity(new Intent(LoginActivity.this, PesquisarMedicamentoActivity.class));
                            finish();
                        } else {
                            alert("Erro ao logar, verifique seu email e senha");
                        }
                    }
                });
    }

    private void logarComGoogle() {
        Intent i = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();

                firebaseLoginGoogle(account);
            }
        }


    }

    private void firebaseLoginGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(LoginActivity.this, PesquisarMedicamentoActivity.class));
                        } else {
                            alert("Falha no login com Google!");
                        }
                    }
                });
    }

    private void alert(String msg) {
        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        alert("Falha na conexão com Google");
    }
}


