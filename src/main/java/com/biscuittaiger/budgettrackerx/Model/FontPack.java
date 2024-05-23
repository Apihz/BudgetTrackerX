package com.biscuittaiger.budgettrackerx.Model;

import javafx.scene.text.Font;

public class FontPack {
    private String fontPackPath = "/com/biscuittaiger/budgettrackerx/FontPack/";//path to FOntPack


    private String jetBrainFontPath = fontPackPath + "JetbrainFont/Static/JetBrainsMono-Regular.ttf";

    public FontPack() {
        // Constructor is not needed for this class
    }

    public Font getFontJetBrain() {
        return Font.loadFont(getClass().getResourceAsStream(jetBrainFontPath), 12);
    }
}
