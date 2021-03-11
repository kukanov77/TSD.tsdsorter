package com.labirint.tsdsorter.entities.values;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import ru.labirint.core.entities.Barcode;
import com.labirint.tsdsorter.entities.PersonInfo;
import com.labirint.tsdsorter.entities.Place;
import com.labirint.tsdsorter.entities.PlaceResponse;
import com.labirint.tsdsorter.entities.values.Values;
import com.labirint.tsdsorter.entities.values.ValuesDao;

public class ValuesRepository extends ru.labirint.core_tsd.entities.values.ValuesRepository {

    BehaviorSubject<String> personText = BehaviorSubject.createDefault("");
    Values values;
    ValuesDao valuesDao;
    Place place;


    public ValuesRepository(ValuesDao valuesDao) {
        this.valuesDao = valuesDao;
        values = valuesDao.getValues();
    }

    public Observable<String> getPersonText(){return personText;}

    public void setPersonText(){
        personText.onNext(values.getName());
    }

    public void setIdPerson(int idPerson) {
        values.setIdPerson(idPerson);
    }

    public int getIdPerson() {
        return values.getIdPerson();
    }

    public void setPersonName(String name){
        values.setName(name);
        valuesDao.insert(values);
        setPersonText();
    }

    public void setPersonInfo(PersonInfo personInfo) {
        values.setName(personInfo.getName());
        valuesDao.insert(values);
    }

    public void setPlace(PlaceResponse place) {
        values.setIdBox(place.getIdBox());
        values.setIdPlace(place.getIdPlace());
        values.setPlace(place.getTXT());
        valuesDao.insert(values);
    }

    public int getIdPlace() {
        return values.getIdPlace();
    }

    public int getIdBox(){
        return values.getIdBox();
    }

    public void clear() {
        values = new Values();
        valuesDao.insert(values);
    }

    @Override
    public String getProjectName() {
        return "TSDSorter";
    }

    public void setPlace(Place place) {
        values.setIdPlace(place.getIdPlace());
        values.setPlace(place.getPlace());
        valuesDao.insert(values);
    }

    public void setIdSales(Barcode barcode) {
        int i = -1;
        try {
            i = Integer.parseInt(barcode.toString().substring(2, 10));
        } catch(Exception e) {
            System.out.println("Could not parse " + e);
        }
        values.setIdSales(i);
        valuesDao.insert(values);
    }

    public void setStretch(Barcode barcode) {
        int i = -1;
        try {
            i = Integer.parseInt(barcode.toString().substring(10, 12));
        } catch(Exception e) {
            System.out.println("Could not parse " + e);
        }
        values.setStretch(i);
        valuesDao.insert(values);
    }

    public int getIdSales() {
        return values.getIdSales();
    }

    public int getStretch() {
        return values.getStretch();
    }

    public void setIdPlace(int id_place) {
        values.setIdPlace(id_place);
        valuesDao.insert(values);
    }

    public void setIdSales(int idSales) {
        values.setIdSales(idSales);
        valuesDao.insert(values);
    }

    public void setStretch(int stretch) {
        values.setStretch(stretch);
        valuesDao.insert(values);
    }

    public String getPlace() {
        return values.getPlace();
    }

    public void clearPlace() {
        values.setIdPlace(0);
        values.setPlace("");
        valuesDao.insert(values);
    }

    public void setPlace(String place) {
        values.setPlace(place);
        valuesDao.insert(values);
    }
}
