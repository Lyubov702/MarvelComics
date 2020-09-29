package com.marvel.developer.model;

import java.util.List;

public class ComicResult {

    public ComicResult(Comic comic, List<Character> characters) {
        this.comic = comic;
        this.characters = characters;
    }

    private Comic comic;
    private List<Character> characters;

    public Comic getComic() {
        return comic;
    }

    public void setComic(Comic comic) {
        this.comic = comic;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(List<Character> characters) {
        this.characters = characters;
    }
}
