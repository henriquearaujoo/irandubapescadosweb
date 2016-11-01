package com.irandubamodulo01.dao;

import java.util.List;

import com.irandubamodulo01.model.Usuario;
import com.irandubamodulo01.util.Filtro;

public interface UsuarioDAO extends DAO<Usuario, Long> {
	
	public Usuario logar(String login, String senha);
	
	public List<Usuario> filtrar(String s);
	
	public List<Usuario> filtrados(Filtro filtro);
	
	public int quantidadeFiltrados(Filtro filtro);

}
