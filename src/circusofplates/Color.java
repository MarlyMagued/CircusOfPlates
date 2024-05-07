/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package circusofplates;

/**
 *
 * @author marlymaged
 */
public enum Color {
    Red(0),
    Blue(1),
    Yellow(2);

    private int value;
    private final static Color[] values = values();

    Color(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Color valueOf(int num) {
        return values[num];
    }
}
