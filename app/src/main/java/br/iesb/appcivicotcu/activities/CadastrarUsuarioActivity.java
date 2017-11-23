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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.iesb.appcivicotcu.R;
import br.iesb.appcivicotcu.authenticator.FirebaseConnection;


public class CadastrarUsuarioActivity extends AppCompatActivity {


    private Button btnCadastrar;
    private Button btnVoltar;
    private EditText editNome;
    private EditText editEmail;
    private EditText editSenha;
    private FirebaseAuth auth;

    private ProgressDialog progressDoalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);

        inicializaComponentes();
        eventosClick();
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = FirebaseConnection.getFirebaseAuth();
    }

    public void spinner() {
        progressDoalog = new ProgressDialog(CadastrarUsuarioActivity.this);
        progressDoalog.setMessage("Aguarde....");
        progressDoalog.setTitle("Realizando Cadastro");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDoalog.show();
    }

    private void eventosClick() {
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editNome.getText().toString().trim();
                String email = editEmail.getText().toString().trim();
                String senha = editSenha.getText().toString().trim();

                if (email.isEmpty()) {
                    alert("Campo email obrigatório!");
                    editEmail.requestFocus();
                } else if (senha.isEmpty()) {
                    alert("Campo senha obrigatório!");
                    editSenha.requestFocus();
                } else if (!validateEmail(email)) {
                    alert("Email inválito!");
                    editEmail.requestFocus();
                } else if (!validatePassword(senha)) {
                    alert("Senha muito curta ou longa demais!");
                    editSenha.requestFocus();
                } else {
                    spinner();
                    criarUsuario(email, senha);
                }

            }
        });


        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

    private void criarUsuario(String email, String senha) {
        auth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(CadastrarUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            progressDoalog.dismiss();
                            alert("Usuário cadastrado com sucesso!");
                            startActivity(new Intent(CadastrarUsuarioActivity.this, PesquisarMedicamentoActivity.class));
                            finish();
                        } else {
                            alert("Erro de cadastro");
                        }

                    }
                });
    }

    private void alert(String msg) {
        Toast.makeText(CadastrarUsuarioActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    private void inicializaComponentes() {
        editNome = findViewById(R.id.cadastrar_edit_nome);
        editEmail = findViewById(R.id.cadastrar_edit_email);
        editSenha = findViewById(R.id.cadastrar_edit_senha);
        btnCadastrar = findViewById(R.id.cadastrar_btn);
        btnVoltar = findViewById(R.id.cadastrar_btn_voltar);
    }
}
