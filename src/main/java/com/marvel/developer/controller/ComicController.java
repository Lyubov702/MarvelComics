package com.marvel.developer.controller;

import com.marvel.developer.model.Character;
import com.marvel.developer.model.Comic;
import com.marvel.developer.model.ComicResult;
import com.marvel.developer.service.CharacterService;
import com.marvel.developer.service.ComicService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.List;

@RestController
@RequestMapping("/comics")
public class ComicController {

    @Autowired
    private ComicService comicService;
    @Autowired
    private CharacterService characterService;

    @GetMapping("")
    @ApiOperation(value = "Fetches lists of comics.")
    public ResponseEntity<List<Comic>> findAll(@QueryParam("title") String title,
                                               @QueryParam("start") Integer start,
                                               @QueryParam("size") Integer size,
                                               @QueryParam("sortedBy") String sortedBy) {

        List<Comic> allComics = comicService.findAllComics();
        if (sortedBy != null) allComics = comicService.sortBy(sortedBy);

        if (title != null) allComics = comicService.getAllComicsForTitle(title, allComics);

        if (start != null && size != null) allComics = comicService.getAllComicsPaginated(start, size, allComics);

        if (allComics != null) {
            return ResponseEntity.ok(allComics);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Fetches a single comic by id.")
    public ResponseEntity<ComicResult> findById(@RequestParam int id) {
        Comic comic = comicService.findById(id);
        if (comic != null) {
            List<Character> characters = characterService.findByComicId(comic.getId());
            return ResponseEntity.ok(new ComicResult(comic, characters));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/{comicId}/characters")
    @ApiOperation(value = "Fetches lists of characters filtered by a comic i.")
    public ResponseEntity<List<Character>> findByComicId(@RequestParam int id) {
        List<Character> result = characterService.findByComicId(id);
        return result != null ? ResponseEntity.ok(result) :
                new ResponseEntity(HttpStatus.NOT_FOUND);
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseStatus(code = HttpStatus.CREATED)
    @ApiOperation(value = "Save new comics.")
    public void saveComic(@RequestBody Comic comic) {
        comicService.save(comic);
    }

}
