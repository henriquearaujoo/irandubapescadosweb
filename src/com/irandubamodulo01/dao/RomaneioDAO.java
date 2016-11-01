package com.irandubamodulo01.dao;

import com.irandubamodulo01.model.Barco;
import com.irandubamodulo01.model.Fornecedor;
import com.irandubamodulo01.model.Produto;
import com.irandubamodulo01.model.Romaneio;

import java.util.List;

public interface RomaneioDAO extends DAO<Romaneio, Long>{

	public List<Romaneio> getRomaneiosPorProduto(Produto produto);
}
