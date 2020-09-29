package com.marvel.developer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * class of Comics
 */
@Entity
public class Comic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String title;
    private String description;
    private Date modified;



    @ManyToMany(cascade = CascadeType.ALL)
    private List<Character> characters = new ArrayList<>();

/*
    @Lob
    @Column(name = "logo", nullable = false)
    private byte[] logo;
*/

    public Comic() {
    }

    public Comic(String title, String description, Date modified, byte[] logo, Character... characters) {
        this.title = title;
        this.description = description;
        this.modified = modified;
        this.characters = Stream.of(characters).collect(Collectors.toList());
       // this.logo = logo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(List<Character> characters) {
        this.characters = characters;
    }

 /*   public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }*/
}
