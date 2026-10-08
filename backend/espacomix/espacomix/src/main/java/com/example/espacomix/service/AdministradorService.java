package com.example.espacomix.service;

import com.example.espacomix.model.Administrador;
import com.example.espacomix.repository.AdministradorRepository;
import com.example.espacomix.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdministradorService {

    @Autowired
    private AdministradorRepository administradorRepository;

    private static final String EMAIL_REGEX = "^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$";
    private static final String SENHA_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";

    public Administrador salvar(Administrador administrador){
    if (administrador.getEmail()== null || administrador.getEmail().trim().isEmpty()){
        throw new RuntimeException("EMAIL NAO PODE SER VAZIO");
    }if(!administrador.getEmail().matches(EMAIL_REGEX)){
        throw new RuntimeException("EMAIL INVALIDO");
    }
    if(administrador.getSenhaAdmin()==null || administrador.getSenhaAdmin().trim().isEmpty()){
        throw new RuntimeException("SENHA NAO PODE SER VAZIO");
    }if(!administrador.getSenhaAdmin().matches(SENHA_REGEX)){
        throw new RuntimeException("SENHA INVALIDA");
    }
    return administradorRepository.save(administrador);
    }



public List<Administrador> listarTudo(){return administradorRepository.findAll();}



public Administrador buscaId(Long id){
    return administradorRepository.findById(id)
            .orElseThrow(()-> new RuntimeException("ADMINISTRADOR COM O ID"+ id + "NAO FOI ENCONTRADO"));
}



public void deletar (Long id){
    Administrador administrador = buscaId(id);
    administradorRepository.delete(administrador);
   }

}
