package com.example.espacomix.service;

import com.example.espacomix.model.Cliente;
import com.example.espacomix.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    private static final String TELEFONE_REGEX = "^\\d{10,11}$";
    private static final String CPF_REGEX = "^\\d{11}$";
    private static final String CEP_REGEX = "^\\d{8}$";
    private static final String EMAIL_REGEX = "^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$";
    private static final String SENHA_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";

    public Cliente salvar(Cliente cliente){
        if(cliente.getNome()== null || cliente.getNome().trim().isEmpty()){
            throw new RuntimeException("NOME NAO PODE SER VAZIO");
        }

        if(cliente.getEndereco()== null || cliente.getEndereco().trim().isEmpty()){
            throw new RuntimeException("ENDERECO NAO PODE SER VAZIO");
        }

        if(cliente.getTelefone()==null || cliente.getTelefone().trim().isEmpty()){
            throw new RuntimeException("TELEFONE NAO PODE SER VAZIO");
        }if(!cliente.getTelefone().matches(TELEFONE_REGEX)){
            throw new RuntimeException("NUMERO DE TELEFONE INVALIDO");
        }

        if(cliente.getCpf()== null || cliente.getCpf().trim().isEmpty()){
            throw new RuntimeException("CPF NAO PODE SER VAZIO");
        }if (!cliente.getCpf().matches(CPF_REGEX)){
            throw new RuntimeException("CPF INVALIDO");
        }

        if(cliente.getCep()== null || cliente.getCep().trim().isEmpty()){
            throw new RuntimeException("CEP NAO PODE SER VAZIO");
        }if(!cliente.getCep().matches(CEP_REGEX)){
            throw new RuntimeException("CEP INVALIDO");
        }

        if(cliente.getEmail()== null || cliente.getEmail().trim().isEmpty()){
            throw new RuntimeException("EMAIL NAO PODE SER VAZIO");
        }if(!cliente.getEmail().matches(EMAIL_REGEX)){
            throw new RuntimeException("EMAIL INVALIDO");
        }

        if(cliente.getSenha()== null || cliente.getSenha().trim().isEmpty()){
            throw new RuntimeException("SENHA NAO PODE SER VAZIA");
        }if(!cliente.getSenha().matches(SENHA_REGEX)){
            throw new RuntimeException("SENHA INVALIDA");
        }
        return clienteRepository.save(cliente);
    }



    public List<Cliente>listarTudo(){return clienteRepository.findAll();}



    public Cliente buscaId(Long id){
        return clienteRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("CLIENTE COM O ID"+ id + "NAO FOI ENCONTRADO"));
    }



    public void deletar (Long id){
        Cliente cliente= buscaId(id);
        clienteRepository.delete(cliente);
    }

}
