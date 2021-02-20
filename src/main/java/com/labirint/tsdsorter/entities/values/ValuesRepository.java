package com.labirint.tsdsorter.entities.values;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import ru.labirint.core.entities.Barcode;
import com.labirint.tsdsorter.entities.PersonInfo;
import com.labirint.tsdsorter.entities.PlaceResponse;
import com.labirint.tsdsorter.entities.values.Values;
import com.labirint.tsdsorter.entities.values.ValuesDao;

public class ValuesRepository {

    BehaviorSubject<String> personText = BehaviorSubject.createDefault("");
    Values values;
    ValuesDao valuesDao;


    public ValuesRepository(ValuesDao valuesDao) {
        this.valuesDao = valuesDao;
        values = valuesDao.getValues();
    }

    public Observable<String> getPersonText(){return personText;}

    private void setPersonText(){
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
}
