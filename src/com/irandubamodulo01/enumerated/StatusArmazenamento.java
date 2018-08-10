package com.irandubamodulo01.enumerated;

import javax.enterprise.inject.Instance;

import com.irandubamodulo01.service.ArmazenamentoAutorizado;
import com.irandubamodulo01.service.ArmazenamentoEnviado;
import com.irandubamodulo01.service.ArmazenamentoRetornado;
import com.irandubamodulo01.service.ControladorArmazenamento;

public enum StatusArmazenamento{

	
	ENVIADO("ENVIADO") {
		@Override
		public ControladorArmazenamento obterControlador(Instance<ControladorArmazenamento> controladores) {
			return controladores.select(ArmazenamentoEnviado.class).get();
		}
	},
	AUTORIZADO("AUTORIZADO") {
		@Override
		public ControladorArmazenamento obterControlador(Instance<ControladorArmazenamento> controladores) {
			return controladores.select(ArmazenamentoAutorizado.class).get();
		}
	},
	TUNEL("TUNEL") {
		@Override
		public ControladorArmazenamento obterControlador(Instance<ControladorArmazenamento> controladores) {
			return controladores.select(ArmazenamentoRetornado.class).get();
		}
	},
	RETORNADO("RETORNADO") {
		@Override
		public ControladorArmazenamento obterControlador(Instance<ControladorArmazenamento> controladores) {
			return controladores.select(ArmazenamentoRetornado.class).get();
		}
	};
	
	
	private String status;
	
	private StatusArmazenamento(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}
	
	
	public abstract ControladorArmazenamento obterControlador(Instance<ControladorArmazenamento> controladores);
}
