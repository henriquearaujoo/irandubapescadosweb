package com.irandubamodulo01.service;

import com.irandubamodulo01.model.Armazenamento;

public class ArmazenamentoAutorizado implements ControladorArmazenamento{

	@Override
	public Armazenamento armazenar(Armazenamento armazenamento) {
		return armazenamento;
	}

}
