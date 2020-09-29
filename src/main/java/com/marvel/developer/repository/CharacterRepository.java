package com.marvel.developer.repository;

import com.marvel.developer.model.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CharacterRepository extends JpaRepository<Character, Integer> {

/*    @Query(value = "SELECT character.id, character.name, character.description, character.modified, comic.comics_id" +
            "FROM character_comics " +
            " JOIN character ON character.id=character_comics.character_id " +
            " JOIN comic  ON comic.id=character_comics.comics_id;", nativeQuery = true)*/
    List<Character> findAll();

    @Query(value = "SELECT character.id, character.name, character.description," +
            " character.modified" +
            " FROM character" +
            " JOIN character_comics on character_comics.character_id = character.id" +
            " where character_comics.comics_id=:id " +
            ";", nativeQuery = true)
    List<Character> findByComicId(@Param("id") int id);
}