package com.irandubamodulo01.dao;

import com.irandubamodulo01.model.Barco;
import com.irandubamodulo01.model.Fornecedor;
import com.irandubamodulo01.model.Pagamento;
import com.irandubamodulo01.model.Pedido;
import com.irandubamodulo01.util.Filtro;

import java.util.List;

public interface PedidoDAO extends DAO<Pedido, Long>{

	public List<Pedido> filtrados(Filtro filtro);

	public int quantidadeFiltrados(Filtro filtro);

	public void salvarPedido(Pedido pedido);

	public void deletaParcelasPedido(Pedido pedido);
}
