package br.iesb.appcivicotcu.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.iesb.appcivicotcu.R;
import br.iesb.appcivicotcu.entities.Medicamento;

/**
 * Created by luan on 21/11/17.
 */

public class CatalogoAdapter extends RecyclerView.Adapter {
    private List<Medicamento> remedios;
    private Context context;

    public CatalogoAdapter(List<Medicamento> remedios, Context context) {
        this.remedios = remedios;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_medicamento, parent, false);

        CatalogoViewHolder holder = new CatalogoViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CatalogoViewHolder viewHolder = (CatalogoViewHolder) holder;

        Medicamento remedio = remedios.get(position);


        viewHolder.produto.setText(remedio.getProduto());
        viewHolder.apresentacao.setText(remedio.getApresentacao());
        viewHolder.pmc0.setText("Preço máximo: " + remedio.getPmc20().toString());
//        viewHolder.precoMercado.setText(remedio.getPf0().toString());
//        viewHolder.precoFabrica.setText(remedio.getPf17().toString());
//        viewHolder.principioAtivo.setText(remedio.getPrincipioAtivo());
    }

    @Override
    public int getItemCount() {
        return remedios.size();
    }

}
