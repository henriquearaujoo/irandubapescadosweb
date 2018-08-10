package com.irandubamodulo01.dao;

import java.util.List;

import com.irandubamodulo01.model.Compra;
import com.irandubamodulo01.model.Lote;
import com.irandubamodulo01.util.Filtro;


public interface LoteDAO extends DAO<Lote, Long> {
	
	public int quantidadeFiltrados(Filtro filtro);
	
	public List<Lote> filtrados(Filtro filtro);
	
	public void salvarLotes(List<Lote> lotes);
	
	public List<Lote> buscarLotePorCompra(Compra compra);
	
	public List<Lote> buscarLotePorCompraa(Compra compra);

}
