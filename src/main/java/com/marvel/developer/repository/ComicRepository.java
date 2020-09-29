package com.marvel.developer.repository;

import com.marvel.developer.model.Character;
import com.marvel.developer.model.Comic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ComicRepository  extends JpaRepository<Comic, Integer> {

    @Query(value = "SELECT comic.id, comic.title, comic.description," +
            " comic.modified" +
            " FROM comic " +
            " JOIN character_comics on character_comics.comics_id = comic.id" +
            " where character_comics.character_id=:id " +
            ";", nativeQuery = true)
    List<Comic> findByCharacterId(@Param("id") int id);
}
