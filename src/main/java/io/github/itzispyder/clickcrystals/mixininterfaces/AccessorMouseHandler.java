package io.github.itzispyder.clickcrystals.mixininterfaces;

public interface AccessorMouseHandler {

    void clickCrystals$leftClick();

    void clickCrystals$rightClick();

    void clickCrystals$middleClick();

    void clickCrystals$scroll(double amount);

    void clickCrystals$setCursorPos(double x, double y);

    void clickCrystals$toggleLeft(boolean toggle);

    void clickCrystals$toggleRight(boolean toggle);

    void clickCrystals$toggleMiddle(boolean toggle);
}
