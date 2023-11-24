package com.safetynet.safetynetalerts.repository;

import com.safetynet.safetynetalerts.model.Firestation;

import java.util.List;

public class FirestationRepository {

    public List<Firestation> firestationsList;

    public void setFirestationsList(List<Firestation> firestations) {
        firestationsList = firestations;
    }





    // retourner une liste de personnes en fonction du numéro de station donné
    // retourne précisément : prénom, nom, adresse, téléphone de chacun + le nombre d'adultes et d'enfants (18 ans ou moins)


    // retourner une liste d'enfants (18 ans ou moins) habitant à l'adresse donnée
    // retourne précisément : prénom, nom, âge, liste des autres membres du foyer pour chacun. Si pas d'enfant, url renvoie une chaîne vide


    // retourner une liste des numéros de téléphone des résidents désservis par le numéro de station donné


    // retourner la liste des habitants vivant à l'adresse donnée  et le numéro de la caserne de pompier la desservant
    // retourne précisément : nom, prénom, téléphone, âge, antécédents médicaux (médicaments, posologie et allergies) pour chacun

    // retourner la liste de tous les foyers desservis par la station
}
