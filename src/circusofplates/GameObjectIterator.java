/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package circusofplates;

import circusofplates.Shape.*;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import eg.edu.alexu.csd.oop.game.GameObject;

/**
 *
 * @author marlymaged
 */
public class GameObjectIterator implements Iterator<GameObject> {

    private int cursor = 0;
    private GameObject[] gameObjects;

    public GameObjectIterator(List<GameObject> gameObjects) {
        this.gameObjects = gameObjects.toArray(new GameObject[gameObjects.size()]);
    }

    @Override
    public boolean hasNext() {
        if (cursor<gameObjects.length) {
            return true;
        }
        return false;
    }

    @Override
    public GameObject next() {
            return gameObjects[cursor++] ;
    }

}
