package com.hotel.model;

import com.hotel.model.enumeration.TypeService;

public class ServiceSupplementaire {
    private int idService;
    private String nomService;
    private TypeService typeService;
    private double prixService;

    public ServiceSupplementaire() {
    }

    public ServiceSupplementaire(int idService, String nomService, TypeService typeService, double prixService) {
        this.idService = idService;
        this.nomService = nomService;
        this.typeService = typeService;
        this.prixService = prixService;
    }

    public ServiceSupplementaire(String nomService, TypeService typeService, double prixService) {
        this.nomService = nomService;
        this.typeService = typeService;
        this.prixService = prixService;
    }

    public int getIdService() {
        return idService;
    }

    public void setIdService(int idService) {
        this.idService = idService;
    }

    public String getNomService() {
        return nomService;
    }

    public void setNomService(String nomService) {
        this.nomService = nomService;
    }

    public TypeService getTypeService() {
        return typeService;
    }

    public void setTypeService(TypeService typeService) {
        this.typeService = typeService;
    }

    public double getPrixService() {
        return prixService;
    }

    public void setPrixService(double prixService) {
        this.prixService = prixService;
    }

    public String toString() {
        return nomService + " (" + prixService + " MAD)";
    }
}
