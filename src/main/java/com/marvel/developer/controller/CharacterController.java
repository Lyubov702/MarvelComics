package com.marvel.developer.controller;


import com.marvel.developer.model.Character;
import com.marvel.developer.model.Comic;
import com.marvel.developer.service.CharacterService;
import com.marvel.developer.service.ComicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.List;

@RestController
@RequestMapping("/characters")
@Api(value = "character_resources", description = "CRUD ")
public class CharacterController {

    @Autowired
    private CharacterService characterService;

    @Autowired
    private ComicService comicService;

    //#TODO когда работает пейджинатор, не работает фильтр по имени

    @GetMapping("")
    @ApiOperation(value = "Fetches lists of characters")
    public ResponseEntity<List<Character>> findAll(@QueryParam("name") String name,
                                                   @QueryParam("start") Integer start,
                                                   @QueryParam("size") Integer size) {
        List<Character> allCharacter;
        if (name != null) {
            allCharacter = characterService.getAllCharactersForName(name);
        } else {
            allCharacter = characterService.findAllCharacter();
        }
        if (start !=null && size!=null) allCharacter = characterService.getAllCharactersPaginated(start, size);
        if (null != allCharacter) {
            return ResponseEntity.ok(allCharacter);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/{characterId}")
    @ApiOperation(value = "Fetches a single character by id.")
    public ResponseEntity<Character> findById(@RequestParam int id) {
        Character character = characterService.findById(id);
        if (character != null) {
            return ResponseEntity.ok(character);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/comic/{id}")
    @ApiOperation(value = "  Список героев по ид комикса")
    public ResponseEntity<List<Character>> findByComic(@RequestParam int id) {
        List<Character> result = characterService.findByComicId(id);
        return result != null ? ResponseEntity.ok(result) :
                new ResponseEntity(HttpStatus.NOT_FOUND);
    }


    @GetMapping("/{characterId}/comics")
    @ApiOperation(value = "Fetches lists of comics filtered by a character id.")
    public ResponseEntity<List<Comic>> findComicsByCharacterId(@RequestParam int id) {
        List<Comic> result = comicService.findByCharacterId(id);
        return result != null ? ResponseEntity.ok(result) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


 /*   @GetMapping("/images")
    public void showImage(@RequestParam("id") Integer id, HttpServletResponse response) throws IOException {
        response.setContentType("image/png");
        Character character = characterService.findById(id);

        InputStream is = new ByteArrayInputStream(character.getLogo());
        IOUtils.copy(is, response.getOutputStream());
    }*/

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseStatus(code = HttpStatus.CREATED)
    @ApiOperation(value = "Save new character.")
    public void saveCharacter(@RequestBody Character character) {
        characterService.save(character);
    }
}
