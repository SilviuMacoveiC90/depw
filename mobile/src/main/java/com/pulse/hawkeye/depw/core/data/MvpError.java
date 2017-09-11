package com.pulse.hawkeye.depw.core.data;

public class MvpError {
	
	public enum ErrorType {Http, Parser, System, Processing}

	private Object data = "";

	private ErrorType type;
	
	public MvpError(ErrorType type) 
	{
		this.type = type;
	}
	
	public MvpError(ErrorType type, Object data) 
	{
		this.type = type;
		this.data = data;
	}
	
	@Override
	public String toString() {
		
		return "Type: " + type + " data: " + data;
	}
	
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public ErrorType getType() {
		return type;
	}

	public void setType(ErrorType type) {
		this.type = type;
	}
	
	/**
	 * 
	 *
	 * @param  
	 * @return      
	 * @see         
	 */	
	public String displayError() 
	{
		if ( data != null ) {
			return data.toString();
		}else{
			return  "";
		}
	}
	
}
