package com.ferfox1981.springbatchrest.integration.firebase;

import java.util.ArrayList;
import java.util.List;

import com.ferfox1981.springbatchrest.entity.CovidData;


public class FirebaseConsumer {


    public List<CovidData> processFoo(){
        System.out.println("OOOOOOI");
        List<CovidData> lista = new ArrayList<>(); 
        lista.add(new CovidData());
        lista.add(new CovidData());
        return lista;

    }
    
}
