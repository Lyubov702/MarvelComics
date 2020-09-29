package com.marvel.developer.service;

import com.marvel.developer.model.Character;
import com.marvel.developer.model.Comic;
import com.marvel.developer.repository.ComicRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComicService {

    private  final ComicRepository comicRepository;

    public ComicService(ComicRepository comicRepository) {
        this.comicRepository = comicRepository;
    }


    public List<Comic> findAllComics(){
        return comicRepository.findAll();
    }

    public Comic findById(int id){
        Optional<Comic> optionalComic = comicRepository.findById(id);
        return optionalComic.orElse(null);
    }

    public List<Comic> findByCharacterId(int characterId){
        List<Comic> comics = comicRepository.findByCharacterId(characterId);
        return comics;
    }


    public Comic save(Comic comic){
        return comicRepository.save(comic);
    }
}
