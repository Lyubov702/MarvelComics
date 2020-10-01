package com.marvel.developer.controller;

import com.marvel.developer.exceptions.NotFoundException;
import com.marvel.developer.model.Image;
import com.marvel.developer.repository.ImageRepository;
import com.marvel.developer.service.ImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;


@RestController
@RequestMapping(path = "image")
@Api(value = "image_resources")
public class ImageController {

    @Autowired
    ImageService imageService;

    @PostMapping("/upload")
    @ApiOperation(value = "upload image.")
    public BodyBuilder uploadImage(@RequestParam("imageFile") MultipartFile file) throws IOException {
        Image img = new Image(file.getOriginalFilename(), file.getContentType(),
                compressBytes(file.getBytes()));
        imageService.save(img);
        return ResponseEntity.status(HttpStatus.OK);
    }

    @GetMapping(path = {"/get/{imageName}"})
    @ApiOperation(value = "Get image for name.")
    public Image getImage(@PathVariable("imageName") String imageName) throws IOException {

        final Optional<Image> retrievedImage = imageService.findByName(imageName);
        Image img = new Image(retrievedImage.get().getName(), retrievedImage.get().getType(),
                decompressBytes(retrievedImage.get().getPicByte()));
        return img;
    }

    public static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException ignored) {
        }

        return outputStream.toByteArray();
    }

    public static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException ignored) {
        }
        return outputStream.toByteArray();
    }
}