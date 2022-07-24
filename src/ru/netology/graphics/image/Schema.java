package ru.netology.graphics.image;

public class Schema implements TextColorSchema {

    @Override
    public char convert(int color) {
        char[] charArrays = {'#', '$', '@', '%', '*', '+', '-', '\''};

        return charArrays[color / 32];

    }
}
