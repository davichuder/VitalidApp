package com.example.healthserviceapp.service;

import com.example.healthserviceapp.Exceptions.MiException;
import com.example.healthserviceapp.entity.Imagen;
import com.example.healthserviceapp.repository.ImagenRepository;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImagenService {
    @Autowired
    private ImagenRepository imagenRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    public Imagen getOne(String id) {
        return imagenRepository.getById(id);
    }

    public Imagen guardar(MultipartFile archivo) throws MiException {
        Imagen imagen = new Imagen();
        if (archivo != null && !archivo.getContentType().equals("application/octet-stream")) {
            try {
                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getOriginalFilename());
                imagen.setContenido(archivo.getBytes());
                imagenRepository.save(imagen);
                return imagen;
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        try {
            Resource resource = resourceLoader.getResource("classpath:/static/img/defaultProfile.jpg");
            File imagenPredeterminada = resource.getFile();
            byte[] bytes = Files.readAllBytes(imagenPredeterminada.toPath());
            imagen.setMime("image/jpg");
            imagen.setNombre("defaultProfile");
            imagen.setContenido(bytes);
            imagenRepository.save(imagen);
            return imagen;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public Imagen actualizar(String idImagen, MultipartFile archivo) throws MiException {
        Optional<Imagen> respuesta = imagenRepository.findById(idImagen);
        if (respuesta.isPresent()){
            Imagen imagen = respuesta.get();
            if (archivo != null && !archivo.getContentType().equals("application/octet-stream")) {
                try {
                    imagen.setMime(archivo.getContentType());
                    imagen.setNombre(archivo.getOriginalFilename());
                    imagen.setContenido(archivo.getBytes());
                    return imagenRepository.save(imagen);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
                return imagen;
        }
        return null;
    }

    @Transactional(readOnly = true)
    public List<Imagen> listarTodos() {
        return imagenRepository.findAll();
    }

    public Imagen defaultImagen(){
        try {
            Imagen imagen = new Imagen();
            Resource resource = resourceLoader.getResource("classpath:/static/img/defaultProfile.jpg");
            File imagenPredeterminada = resource.getFile();
            byte[] bytes = Files.readAllBytes(imagenPredeterminada.toPath());
            imagen.setMime("image/jpg");
            imagen.setNombre("defaultProfile");
            imagen.setContenido(bytes);
            imagenRepository.save(imagen);
            return imagen;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

}