package com.irandubamodulo01.converter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

public class MoedaBigDecimalConverter implements Converter {

	public Object getAsObject(FacesContext arg0, UIComponent arg1, String value) throws ConverterException {

		if(value != null && value.trim().length() > 0){
			try {
				 
				return new BigDecimal(value.replaceAll("[.]","").replace(",","."));
				
			}
			catch (NumberFormatException e){
				throw new ConverterException (new FacesMessage ("Formato de número inválido!"));
			}
		}
		return null;
	}

	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) throws ConverterException {
			DecimalFormatSymbols formatadorSymbols = new DecimalFormatSymbols();
			DecimalFormat  formatador  = new DecimalFormat();
			
			formatador.setMinimumFractionDigits(2);
			formatadorSymbols.setDecimalSeparator(',');
			formatadorSymbols.setGroupingSeparator('.');
	        formatador.setDecimalFormatSymbols(formatadorSymbols); 
			
			if (value == null){ 
			   return null;
			}

			return String.valueOf(formatador.format(new BigDecimal(value.toString())));
			
   	}
}
