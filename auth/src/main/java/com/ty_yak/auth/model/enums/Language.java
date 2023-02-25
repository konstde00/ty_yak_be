package com.ty_yak.auth.model.enums;

public enum Language {

    DE("German", "Deutsch", "\uD83C\uDDE9\uD83C\uDDEA"),
    EN("English", "English", "\uD83C\uDDFA\uD83C\uDDF8"),
    FR("French", "Français", "\uD83C\uDDEB\uD83C\uDDF7"),
    ZH("Chinese", "中文", "\uD83C\uDDE8\uD83C\uDDF3"),
    UK("Ukrainian", "Українська", "\uD83C\uDDFA\uD83C\uDDE6");

    private final String name;
    private final String nativeName;
    private final String emoji;

    Language(String name, String nativeName, String emoji) {
        this.name = name;
        this.nativeName = nativeName;
        this.emoji = emoji;
    }

    public String getName() {
        return name;
    }

    public String getNativeName() {
        return nativeName;
    }

    public String getEmoji() {
        return emoji;
    }

    public static Language valueOfOrDefault(String enumName){
        if(enumName != null) {
            for (Language language : values()) {
                if (language.toString().equals(enumName)) {
                    return language;
                }
            }
        }
        return EN;
    }
}
