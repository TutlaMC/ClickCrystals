package io.github.itzispyder.clickcrystals.mixininterfaces;

public interface AccessorKeyboardHandler {

    void clickCrystals$pressKey(int key, int scan);

    void clickCrystals$toggleKey(int key, int scan, boolean toggle);
}
