package com.tbd.twitter.Models;

import javax.persistence.*;

@Entity
@Table(name="listapalabras")
public class ListaPalabra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, name = "`palabra`")
    private String palabra;

    public ListaPalabra(){

    }

    public ListaPalabra(String palabra){
        this.palabra = palabra;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPalabra() {
        return palabra;
    }

    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }
}
