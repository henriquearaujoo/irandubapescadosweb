package com.irandubamodulo01.converter;

import com.irandubamodulo01.dao.*;
import com.irandubamodulo01.daoimpl.ParcelaDaoImpl;
import com.irandubamodulo01.daoimpl.PedidoDaoImpl;
import com.irandubamodulo01.daoimpl.ProdutoDaoImpl;
import com.irandubamodulo01.daoimpl.VendedorDAOImpl;
import com.irandubamodulo01.model.Pedido;
import com.irandubamodulo01.model.Vendedor;
import com.irandubamodulo01.util.CDILocator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;


@FacesConverter(forClass = Pedido.class)
public class PedidoConverter implements Converter {

	private PedidoDAO pedidoDAO;
	private ProdutoDAO produtoDAO;
	private ParcelaDAO parcelaDAO;

	public PedidoConverter(){

		this.pedidoDAO = CDILocator.getBean(PedidoDaoImpl.class);
		this.produtoDAO = CDILocator.getBean(ProdutoDaoImpl.class);
		this.parcelaDAO = CDILocator.getBean(ParcelaDaoImpl.class);
	}
	
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent uicomponent, String string) {
		 if (string == null || string.isEmpty() ) {
			return null;
		}
		 
		Pedido pedido = pedidoDAO.getById(Pedido.class, Long.valueOf(string));
		pedido.setProdutos(produtoDAO.getProdutosPorPedido(pedido));
		pedido.setParcelas(parcelaDAO.getParcelasPorPedido(pedido));
		 
		return pedido;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent uicomponent, Object object) {
		Pedido pedido = (Pedido) object;
		 if (pedido == null || pedido.getId() == null) {
			return null;
		}
		 
	 return String.valueOf(pedido.getId());
	}

	
	
	
}
