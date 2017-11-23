package br.iesb.appcivicotcu.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import br.iesb.appcivicotcu.R;

/**
 * Created by luan on 21/11/17.
 */

public class CatalogoViewHolder extends RecyclerView.ViewHolder {
    final TextView produto;
    final TextView apresentacao;
    final TextView pmc0;

    public CatalogoViewHolder(View view) {
        super(view);
        produto = (TextView) view.findViewById(R.id.item_remedio_produto);
        apresentacao = (TextView) view.findViewById(R.id.item_remedio_apresentacao);
        pmc0 = (TextView) view.findViewById(R.id.item_remedio_preco_maximo);

    }


}
