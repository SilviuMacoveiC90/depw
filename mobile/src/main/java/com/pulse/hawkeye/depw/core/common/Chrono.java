package com.pulse.hawkeye.depw.core.common;

public class Chrono {
	private long start;
	private	long end;

	
	/**
	 * 
	 *
	 * @param  
	 * @return      
	 * @see         
	 */	
	public Chrono()
	{
		start = System.currentTimeMillis();
	}
	
	/**
	 * 
	 *
	 * @param  
	 * @return      
	 * @see         
	 */		
	public String print()
	{
		end = System.currentTimeMillis();
		String print = ""; 
		long diff = end-start;
		long secs = diff/1000;
		if (secs > 0)
		{	
			diff = diff % 1000;
		}
		print += diff +"ms";
		long min = secs / 60;
		if (min >0)
		{
			secs = secs % 60;
		}
		print = secs +"s:" + print;
		print = min +"m " + print;

		return print;
	}
}
