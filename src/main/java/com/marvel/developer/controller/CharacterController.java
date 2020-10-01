package com.marvel.developer.controller;


import com.marvel.developer.exceptions.NotFoundException;
import com.marvel.developer.model.Character;
import com.marvel.developer.model.Comic;
import com.marvel.developer.service.CharacterService;
import com.marvel.developer.service.ComicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.QueryParam;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/characters")
@Api(value = "character_resources", description = "CRUD character")
public class CharacterController {

    @Autowired
    private CharacterService characterService;

    @Autowired
    private ComicService comicService;

    @GetMapping("")
    @ApiOperation(value = "Fetches lists of characters")
    public ResponseEntity<List<Character>> findAll(@QueryParam("name") String name,
                                                   @QueryParam("start") Integer start,
                                                   @QueryParam("size") Integer size,
                                                   @QueryParam("sortedBy") String sortedBy) {

        List<Character> allCharacter = characterService.findAllCharacter();
        if (sortedBy != null) allCharacter = characterService.sortBy(sortedBy);

        if (name != null) allCharacter = characterService.getAllCharactersForName(name, allCharacter);

        if (start != null && size != null)
            allCharacter = characterService.getAllCharactersPaginated(start, size, allCharacter);

        if (null != allCharacter) {
            return ResponseEntity.ok(allCharacter);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Fetches a single character by id.")
    public ResponseEntity<Character> findById(@RequestParam int id) {
        Character character = characterService.findById(id);
        if (character != null) {
            return ResponseEntity.ok(character);
        } else {
            throw new NotFoundException();
        }
    }


    @GetMapping("/{characterId}/comics")
    @ApiOperation(value = "Fetches lists of comics filtered by a character id.")
    public ResponseEntity<List<Comic>> findComicsByCharacterId(@RequestParam int id) {
        List<Comic> result = comicService.findByCharacterId(id);
        if (result != null)
           return ResponseEntity.ok(result);
        else throw new NotFoundException();
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseStatus(code = HttpStatus.CREATED)
    @ApiOperation(value = "Save new character.")
    public void saveCharacter(@RequestBody @Valid Character character) {
        characterService.save(character);
    }


    @GetMapping(value = "/", produces = MediaType.IMAGE_JPEG_VALUE)
    public void getImage(HttpServletResponse response) throws IOException {
        ClassPathResource imgFile = new ClassPathResource("/img/character_id=1.png");
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(imgFile.getInputStream(), response.getOutputStream());
    }


    @PostMapping("/upload")
    public void singleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get("D://img/" + file.getOriginalFilename());
            Files.write(path, bytes);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}