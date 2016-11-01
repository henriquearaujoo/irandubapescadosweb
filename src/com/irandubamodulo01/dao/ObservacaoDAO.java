package com.irandubamodulo01.dao;

import java.util.List;

import com.irandubamodulo01.model.Compra;
import com.irandubamodulo01.model.Observacao;

public interface ObservacaoDAO extends DAO<Observacao, Long>{

	public List<Observacao> getObservacaoPorCompra(Compra compra);
	
}
