package br.iesb.appcivicotcu.entities;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by luan on 22/11/17.
 */
public interface TCUMedicamentos {
    @GET("rest/remedios")
    Call<List<Medicamento>> listarRemedios(@Query("produto") String produto, @Query("campos") String campos);

//    Call<List<Medicamento>> listarRemedios();
}

