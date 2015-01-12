package es.caib.gusite.lucene.model;

import java.util.List;

public class IndexResultados {

    private List lista;
    private int numEncontrados;
    private long duracionBusqueda;
    private String consultaOriginal;
    private String consultaSugerida;
    private String saltos;


    public IndexResultados(List lista, int numEncontrados, long duracionBusqueda, String consultaOriginal, String consultaSugerida, String saltos) {
        this.lista = lista;
        this.numEncontrados = numEncontrados;
        this.duracionBusqueda = duracionBusqueda;
        this.consultaOriginal = consultaOriginal;
        this.consultaSugerida = consultaSugerida;
        this.saltos = saltos;
    }

    public String getConsultaOriginal() {
        return consultaOriginal;
    }

    public void setConsultaOriginal(String consultaOriginal) {
        this.consultaOriginal = consultaOriginal;
    }

    public String getConsultaSugerida() {
        return consultaSugerida;
    }

    public void setConsultaSugerida(String consultaSugerida) {
        this.consultaSugerida = consultaSugerida;
    }

    public long getDuracionBusqueda() {
        return duracionBusqueda;
    }

    public void setDuracionBusqueda(long duracionBusqueda) {
        this.duracionBusqueda = duracionBusqueda;
    }

    public List getLista() {
        return lista;
    }

    public void setLista(List lista) {
        this.lista = lista;
    }

    public int getNumEncontrados() {
        return numEncontrados;
    }

    public void setNumEncontrados(int numEncontrados) {
        this.numEncontrados = numEncontrados;
    }

    public String getSaltos() {
        return saltos;
    }

    public void setSaltos(String saltos) {
        this.saltos = saltos;
    }

}
