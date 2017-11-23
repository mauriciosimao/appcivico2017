package br.iesb.appcivicotcu.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.iesb.appcivicotcu.R;
import br.iesb.appcivicotcu.adapters.CatalogoAdapter;
import br.iesb.appcivicotcu.entities.Medicamento;
import br.iesb.appcivicotcu.entities.TCUMedicamentos;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CatalogoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Medicamento> medicamentosList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);

        inicializaComponentes();

        buscarDados();


    }

    private void buscarDados() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://mobile-aceite.tcu.gov.br/mapa-da-saude/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TCUMedicamentos service = retrofit.create(TCUMedicamentos.class);


        Intent intent = getIntent();
        String produto = intent.getStringExtra("produto");

        Call<List<Medicamento>> remedios = service.listarRemedios(produto, "produto,apresentacao,pmc20");

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(CatalogoActivity.this);
        progressDoalog.setMessage("Aguarde....");
        progressDoalog.setTitle("Buscando medicamentos");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDoalog.show();

        remedios.enqueue(new Callback<List<Medicamento>>() {
            @Override
            public void onResponse(Call<List<Medicamento>> call,
                                   Response<List<Medicamento>> response) {
                List<Medicamento> lista = response.body();

                for (Medicamento m : lista) {
                    Log.d("RETROFIT", m.getProduto() + " " + m.getApresentacao());
                    medicamentosList.add(new Medicamento(m.getProduto(), m.getApresentacao(), m.getPmc20()));

                    recyclerView.setAdapter(new CatalogoAdapter(medicamentosList, CatalogoActivity.this));

                    RecyclerView.LayoutManager layout = new LinearLayoutManager(CatalogoActivity.this, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(layout);

                    progressDoalog.dismiss();

                }
            }

            @Override
            public void onFailure(Call<List<Medicamento>> call,
                                  Throwable t) {
                Toast.makeText(CatalogoActivity.this, "Erro ao buscar", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void inicializaComponentes() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
    }
}
