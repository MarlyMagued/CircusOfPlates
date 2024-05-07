/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package circusofplates;

import java.util.ArrayList;

/**
 *
 * @author marlymaged
 */
public class GameStatusSubject implements Subject {

    private ArrayList<Observer> observers = new ArrayList<>();

    @Override
    public void register(Observer newObserver) {
        observers.add(newObserver);
    }

    @Override
    public void unregister(Observer deletedObserver) {
        observers.remove(deletedObserver);
    }

    @Override
    public void notifyObserver() {
        for(Observer observer : observers)
        {
            observer.update();
        }
    }

}
