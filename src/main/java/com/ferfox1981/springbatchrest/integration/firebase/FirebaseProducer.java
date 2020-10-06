package com.ferfox1981.springbatchrest.integration.firebase;

import java.util.ArrayList;
import java.util.List;

import com.ferfox1981.springbatchrest.entity.CovidData;

public class FirebaseProducer {

    public List<CovidData> produceFoo(){
        System.out.println("XAU");
        List<CovidData> lista = new ArrayList<CovidData>(); 
        lista.add(new CovidData());
        lista.add(new CovidData());
        return lista;

    }
    
}
