package com.irandubamodulo01.dao;

import com.irandubamodulo01.model.Barco;
import com.irandubamodulo01.model.Fornecedor;
import com.irandubamodulo01.model.UltimoLogin;
import com.irandubamodulo01.model.Usuario;

import java.util.List;

public interface UltimoLoginDAO extends DAO<UltimoLogin, Long>{

	public UltimoLogin getUltimoLoginUsuario(Usuario usuario);
}
