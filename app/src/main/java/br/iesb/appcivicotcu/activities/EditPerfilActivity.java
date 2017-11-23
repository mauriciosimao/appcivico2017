package br.iesb.appcivicotcu.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import br.iesb.appcivicotcu.R;

public class EditPerfilActivity extends AppCompatActivity {

    private static final int PICK_FROM_CAMERA = 1;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private ImageView ivPhoto;
    private EditText tvNome;
    private EditText tvTelefone;
    private Button btnEditar;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_perfil);

        inicializaComponentes();
        eventosClick();
        carregaForm();


    }


    public void inicializaComponentes() {
        tvNome = findViewById(R.id.profile_edit_nome);
        tvTelefone = findViewById(R.id.profile_edit_telefone);
        btnEditar = findViewById(R.id.profile_btn_editar);
        ivPhoto = findViewById(R.id.perfil_iv_photo);
    }

    public void eventosClick() {
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editarPerfil();
                finish();
            }
        });


        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getPhotoFromCamera();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void carregaForm() {
        tvNome.setText(user.getDisplayName());
        tvTelefone.setText(user.getPhoneNumber());
        ivPhoto.setImageURI(user.getPhotoUrl());

    }

    public void editarPerfil() {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(tvNome.getText().toString())
                .setPhotoUri(selectedImageUri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Teste", "User profile updated.");
                            Log.d("Teste", "teste" + selectedImageUri);
                            Toast.makeText(EditPerfilActivity.this, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Log.d("Teste", "Nao funfou.");
                        }
                    }
                });
    }


    public void getPhotoFromCamera() throws IOException {
        int counter = 1;
        counter++; //this is an int
        String imageFileName = "JPEG_" + counter; //make a better file name
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,
                ".jpg",
                storageDir
        );

        selectedImageUri = Uri.fromFile(image);

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri);
        startActivityForResult(takePhotoIntent, PICK_FROM_CAMERA);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case PICK_FROM_CAMERA:

                selectedImageUri = data.getData();

                InputStream imageStream = null;
                try {
                    imageStream = getContentResolver().openInputStream(selectedImageUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ivPhoto.setImageURI(selectedImageUri);

    }
}
